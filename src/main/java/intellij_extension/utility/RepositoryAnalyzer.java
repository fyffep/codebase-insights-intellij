package intellij_extension.utility;

import intellij_extension.Constants;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.models.redesign.HeatObject;
import intellij_extension.utility.commithistory.JGitHelper;
import intellij_extension.utility.filesize.FileSizeCalculator;
import org.apache.commons.io.output.NullOutputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;


/**
 * This class gathers all data from JGit and stores it in a Codebase model.
 * The attachCodebaseData(CodebaseV2 codebase) method is meant to do everything at once, including
 * file size computation and the number of commits per file for all commits in a Git repository.
 */
public class RepositoryAnalyzer {

    // The class needs to have a Git repos open to analyze
//    private final Repository repository;
    private final Git git;

    /**
     * Automatically locates the Git project according to what project
     * the user has open in IntelliJ.
     */
    public RepositoryAnalyzer() throws IOException {
        // Open the repos
//        this.repository = JGitHelper.openLocalRepository();
        this.git = new Git(JGitHelper.openLocalRepository());
    }

    public RepositoryAnalyzer(File projectPath) throws IOException {
        // Open the repos
//        this.repository = JGitHelper.openLocalRepository(projectPath);
        this.git = new Git(JGitHelper.openLocalRepository(projectPath));
    }

    /**
     * Adds 1 to the number of commits associated with a file.
     *
     * @param filePath   the file's path or name (can be either)
     * @param commitHash the Git commit hash where the file changed at.
     */
    private static void incrementNumberOfTimesChanged(Codebase codebase, String filePath, String commitHash) {
        FileObject fileObject = codebase.createOrGetFileObjectFromPath(filePath);

        int oldNumberOfCommits = 0;
        String prevCommit = fileObject.getLatestCommit();
        if (prevCommit != null) {
            oldNumberOfCommits = fileObject.getHeatObjectAtCommit(prevCommit).getNumberOfCommits();
        }
        fileObject.setLatestCommit(commitHash);


        //Retrieve the HeatObject that holds the number of commits for the target file
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(commitHash);
        //Increment the HeatObject's number of commits
        heatObject.setNumberOfCommits(oldNumberOfCommits + 1);

        //TEMP
//        if (new File(filePath).getName().equals("CodebaseInsightsToolWindowFactory.java"))
//        {
//            System.out.println("File CodebaseInsightsToolWindowFactory has "+heatObject.getNumberOfCommits()+" commits as of "+commitHash);
//        }

        if (new File(filePath).getName().equals("TestData.java"))
        {
            System.out.println("File TestData has "+heatObject.getNumberOfCommits()+" commits as of "+commitHash);
        }

//        if (new File(filePath).getName().equals("RepositoryAnalyzer.java"))
//        {
//            System.out.println("File RepositoryAnalyzer has "+heatObject.getNumberOfCommits()+" commits as of "+commitHash);
//        }

//        if (new File(filePath).getName().equals("CodeBaseObservable.java")) {
//            System.out.println("File CodeBaseObservable has " + heatObject.getNumberOfCommits() + " commits as of " + commitHash);
//        }
    }

    private static void trackAuthors(Codebase codebase, String filePath, RevCommit commit) {
        // Get FileObject at path filePath
        FileObject fileObject = codebase.createOrGetFileObjectFromPath(filePath);

        // Extract and add author info to FileObject
        PersonIdent authorInfo = commit.getAuthorIdent();
        fileObject.getUniqueAuthors().add(authorInfo.getName());
        fileObject.getUniqueAuthors().add(authorInfo.getEmailAddress());

        // Retrieve the HeatObject associated with this commit
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(commit.getName());
        // Attach author count
        heatObject.setNumberOfAuthors(fileObject.getUniqueAuthors().size());
    }

    /**
     * Returns an InputStream with the old version of the file open.
     * This is method is expensive because it has to search through the entire
     * old version of the repository to find the file.
     *
     * @param filePath   the path of the file to open relative to the project root dir (ex: "my-project/my-package/my-file.java")
     * @param commitHash a commit hash, such as "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a" that the file is present on.
     * @throws IOException           when there are problems opening the commit
     * @throws IllegalStateException if the file could not be found at that commit
     */
    public InputStream obtainFileContents(String filePath, String commitHash) throws IOException {
        //Create a commit object from the commit hash
        ObjectId commitId = git.getRepository().resolve(commitHash);
        assert commitId != null;
        RevWalk revWalk = new RevWalk(git.getRepository());
        RevCommit commit = revWalk.parseCommit(commitId);

        //Prepare a TreeWalk that can walk through the version of the repos at that commit
        RevTree tree = commit.getTree();
        TreeWalk treeWalk = new TreeWalk(git.getRepository());
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);

        //Traverse through the old version of the project until the target file is found.
        //I couldn't get `treeWalk.setFilter(PathFilter.create(filePath));` to work, so this is an alternative approach.
        while (treeWalk.next()) {
            String path = treeWalk.getPathString();
            if (path.endsWith(filePath)) {
                //Return an input stream that has the old version of the file open
                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = git.getRepository().open(objectId);
                return loader.openStream();
            }
        }

        //Could not find the file on that commit
        throw new IllegalStateException(String.format("The file `%s` could not be found in the commit `%s`.", filePath, commitHash));
    }

    /**
     * Given a CodeBase, attaches all the file sizes (in bytes) and line counts of each file present
     * at a particular commit of the Git repository. Each FileObject in the Codebase is given a new
     * HeatObject that holds this file size and line count data. If a HeatObject already exists for the
     * file at the given commit, this updates the existing HeatObject.
     *
     * @param codeBase   represents the entire repository and history
     * @param commitHash the Git commit hash representing the version of the repository to examine
     * @throws IOException when there are problems opening the commit
     */
    public void attachLineCountToCodebase(Codebase codeBase, String commitHash) throws IOException {
        //Create a commit object from the commit hash
        ObjectId commitId = git.getRepository().resolve(commitHash);
        assert commitId != null;
        RevWalk revWalk = new RevWalk(git.getRepository());
        RevCommit revCommit = revWalk.parseCommit(commitId);

        attachLineCountToCodebase(codeBase, revCommit);
    }

    /**
     * Given a CodeBase, attaches all the file sizes (in bytes) and line counts of each file present
     * at a particular commit of the Git repository. Each FileObject in the Codebase is given a new
     * HeatObject that holds this file size and line count data. If a HeatObject already exists for the
     * file at the given commit, this updates the existing HeatObject.
     *
     * @param codeBase  represents the entire repository and history
     * @param revCommit the state of the Git repository to examine
     * @throws IOException when there are problems opening the commit
     */
    public void attachLineCountToCodebase(Codebase codeBase, RevCommit revCommit) throws IOException {
        //Prepare a TreeWalk that can walk through the version of the repos at that commit
        RevTree tree = revCommit.getTree();
        TreeWalk treeWalk = new TreeWalk(git.getRepository());
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);

        //Traverse through the old version of the project until the target file is found.
        //I couldn't get `treeWalk.setFilter(PathFilter.create(filePath));` to work, so this is an alternative approach.
        while (treeWalk.next()) {
            String path = treeWalk.getPathString();
            if (path.endsWith(".java")) {
                //Create an input stream that has the old version of the file open
                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = git.getRepository().open(objectId);
                InputStream inputStream = loader.openStream();

                //Get number of lines and file size
                long lineCount = FileSizeCalculator.computeLineCount(inputStream);
                long fileSize = loader.getSize();

                //Attach data to the HeatObject associated with this version of the file
                FileObject fileObject = codeBase.createOrGetFileObjectFromPath(path);
                HeatObject heatObject = fileObject.getHeatObjectAtCommit(revCommit.getName());
                heatObject.setLineCount(lineCount);
                heatObject.setFileSize(fileSize);
            }
        }
    }

    /**
     * Computes line count, file size, and number of commits for every file at every commit.
     * This places FileObjects in the given Codebase, and each of these FileObjects has its
     * map of commit hashes to HeatObjects filled out.
     * These new HeatObjects contain the newly computed metrics.
     *
     * @param codebase the Codebase to modify. It should have its active branch set.
     */
    public void attachCodebaseData(Codebase codebase) {
        try {

            // Get all commits in the repos for one branch
            Iterable<RevCommit> commitIterable = getCommitsByBranch(codebase.getActiveBranch());

            // Convert the commitIterable (a RevWalk) to a List so that it can be sorted
            List<RevCommit> commitList = new LinkedList<>();
            Iterator<RevCommit> commitIterator = commitIterable.iterator();
            while (commitIterator.hasNext()) {
                commitList.add(0, commitIterator.next());
            }
            commitIterable = null; // we're done with this now

            // Sort the commits by date with the oldest commits first
            Comparator<RevCommit> TIME = Comparator.comparingInt(RevCommit::getCommitTime);
            commitList.sort(TIME);
            commitIterator = commitList.iterator();

            //Iterate through the commits two-at-a-time
            while (commitIterator.hasNext()) {

                RevCommit processCommit = commitIterator.next();
                Commit commitExtract = new Commit(processCommit);
                codebase.getActiveCommits().add(commitExtract);
                attachLineCountToCodebase(codebase, processCommit);
                System.out.println("\nProcessing commit: " + processCommit.getName());

                // Find the difference between the processCommit and the previous commit
                final List<DiffEntry> diffs = diffCommit(processCommit.getName());
                commitExtract.addDiffEntriesToDiffList(diffs);

                if (processCommit.getName().equals("5720bf903e26d5da5ae09d40888a4b539b2ed534")) {
                    diffCommit("5720bf903e26d5da5ae09d40888a4b539b2ed534");
                }
                if (processCommit.getName().equals("723a3eae7a8524b06733e9568f1b2240a0537b0b")) {
                    diffCommit("723a3eae7a8524b06733e9568f1b2240a0537b0b");
                }
                if (processCommit.getName().equals("748e142e937b064f7df97cd6e22869cd20707d29")) {
                    diffCommit("748e142e937b064f7df97cd6e22869cd20707d29");
                }

                // For each file modified in the commit...
                System.out.println("DiffEntry size: " + diffs.size());
                for (DiffEntry diffEntry : diffs) {
                    String newFilePath = diffEntry.getNewPath();
                    if (newFilePath.endsWith(".java")) {
                        String fileName = new File(newFilePath).getName(); //convert file path to file name


                        if (fileName.equals("TestData.java")) {
                            System.out.println(fileName + " found in commit " + processCommit.getName());
                        }

                        // Count the number of times the file was changed
                        incrementNumberOfTimesChanged(codebase, newFilePath, processCommit.getName());
                        trackAuthors(codebase, newFilePath, processCommit);
                        commitExtract.getFileSet().add(fileName);
                    }
                }
            }
        } catch (IOException | GitAPIException e) {
            Constants.LOG.error(e);
            Constants.LOG.error(e.getMessage());
        }
    }

    /**
     * Finds the list of all LOCAL branch names and adds them
     * to the list inside the provided Codebase.
     */
    public void attachBranchNameList(Codebase codebase) throws GitAPIException {
        // Get the list of all LOCAL branches
        List<Ref> call = git.branchList().call();
        // Alternatively: Get the list of all branches, both local and REMOTE --> call = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

        // Add all branch names to the Codebase
        for (Ref ref : call) {
            String branchName = new File(ref.getName()).getName(); //quick-and-dirty way to convert a branch name from format "refs/heads/retire-old-model" to "retire-old-model"
            codebase.getBranchNameList().add(branchName.toLowerCase());
        }
    }

    public Iterable<RevCommit> getCommitsByBranch(String branchName) throws IOException, GitAPIException {
        // Choose the branch
        ObjectId branchId = git.getRepository().resolve(branchName);
        return git.log().add(branchId).call();
    }

    private List<DiffEntry> diffCommit(String hashID) throws IOException {
        // Get the commit you are looking for.
        RevCommit newCommit;
        try (RevWalk walk = new RevWalk(git.getRepository())) {
            newCommit = walk.parseCommit(git.getRepository().resolve(hashID));
        }

        System.out.println("LogCommit: " + newCommit);
        System.out.println("LogMessage: " + newCommit.getFullMessage());

        // Compute diff and return
        try {
            return getDiffOfCommit(newCommit);
        } catch (GitAPIException e) {
            // Proper exception handling in this case
            e.printStackTrace();
        }

        // Proper null handling in this case
        return new ArrayList<>();
    }

    // Helper gets the DiffEntry list
    private List<DiffEntry> getDiffOfCommit(RevCommit newCommit) throws IOException, GitAPIException {

        // Get commit that is previous to the current one.
        RevCommit oldCommit = getPrevHash(newCommit);
        if (oldCommit == null) {
            return new ArrayList<>();
        }

        // Use treeIterator to diff.
        AbstractTreeIterator oldTreeIterator = getCanonicalTreeParser(oldCommit);
        AbstractTreeIterator newTreeIterator = getCanonicalTreeParser(newCommit);

        // Find the difference between the oldTree and newTree
        List<DiffEntry> diffs = git.diff()
                .setOldTree(oldTreeIterator)
                .setNewTree(newTreeIterator)
                .call();

//        System.out.println("\n~~~~~!!!PREFORMAT!!!~~~~~");
//        for (DiffEntry diffEntry : diffs) {
//            System.out.println("~~~~~NEW FILE~~~~~");
//            System.out.println("DiffEntry ChangeType: " + diffEntry.getChangeType());
//            System.out.println("DiffEntry OldPath: " + diffEntry.getOldPath());
//            System.out.println("DiffEntry NewPath: " + diffEntry.getNewPath());
//        }

        OutputStream outputStream = NullOutputStream.NULL_OUTPUT_STREAM;
        try (DiffFormatter formatter = new DiffFormatter(outputStream)) {
            formatter.setRepository(git.getRepository());
            formatter.setDetectRenames(true);
            formatter.getRenameDetector().addAll(diffs);
            diffs = formatter.getRenameDetector().compute();
        }

//        System.out.println("\n~~~~~!!!POSTFORMAT!!!~~~~~");
//        for (DiffEntry diffEntry : diffs) {
//            System.out.println("~~~~~NEW FILE~~~~~");
//            System.out.println("DiffEntry ChangeType: " + diffEntry.getChangeType());
//            System.out.println("DiffEntry OldPath: " + diffEntry.getOldPath());
//            System.out.println("DiffEntry NewPath: " + diffEntry.getNewPath());
//        }

        return diffs;
    }

    // Helper function to get the previous commit. Written by Whitecat
    public RevCommit getPrevHash(RevCommit commit) throws IOException {

        try (RevWalk walk = new RevWalk(git.getRepository())) {
            // Starting point
            walk.markStart(commit);
            int count = 0;
            for (RevCommit rev : walk) {
                // got the previous commit.
                if (count == 1) {
                    return rev;
                }
                count++;
            }
            walk.dispose();
        }
        //Reached end and no previous commits.
        return null;
    }

    // Helper function to get the tree of the changes in a commit. Written by Rüdiger Herrmann
    private AbstractTreeIterator getCanonicalTreeParser(ObjectId commitId) throws IOException {
        try (RevWalk walk = new RevWalk(git.getRepository())) {
            RevCommit commit = walk.parseCommit(commitId);
            ObjectId treeId = commit.getTree().getId();
            try (ObjectReader reader = git.getRepository().newObjectReader()) {
                return new CanonicalTreeParser(null, reader, treeId);
            }
        }
    }


    /**UNUSED METHODS!!
     * UNUSED METHODS!!
     * UNUSED METHODS!!
     * UNUSED METHODS!!
     * UNUSED METHODS!!*/
    /**
     * A helper method from the JGit Cookbook...I actually have no idea what it does.
     * However, it's necessary for finding diffs.
     */
    private AbstractTreeIterator prepareTreeParser(String commitHash) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        // noinspection Duplicates
        try (RevWalk walk = new RevWalk(git.getRepository())) {
            RevCommit commit = walk.parseCommit(git.getRepository().resolve(commitHash));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = git.getRepository().newObjectReader()) {
                // Maybe this reset was doing us wrong??
                // Not exactly sure what it does nor am I looking into it
                // - Ethan
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }

    public Iterable<RevCommit> getAllCommits() throws IOException, GitAPIException {
        return git.log().all().call();
    }

}
