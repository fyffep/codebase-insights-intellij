package intellij_extension.utility;

import intellij_extension.Constants;
import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.CommitV2;
import intellij_extension.models.redesign.FileObjectV2;
import intellij_extension.models.redesign.HeatObject;
import intellij_extension.utility.commithistory.JGitHelper;
import intellij_extension.utility.filesize.FileSizeCalculator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.*;
import java.util.Iterator;
import java.util.List;


/**
 * This class gathers all data from JGit and stores it in a Codebase model.
 * The attachCodebaseData(CodebaseV2 codebase) method is meant to do everything at once, including
 * file size computation and the number of commits per file for all commits in a Git repository.
 */
public class RepositoryAnalyzer
{
    //The class needs to have a Git repos open to analyze
    private Repository repository;
    private Git git;

    /**
     * Automatically locates the Git project according to what project
     * the user has open in IntelliJ.
     */
    public RepositoryAnalyzer() throws IOException {
        //Open the repos
        this.repository = JGitHelper.openLocalRepository();
        this.git = new Git(repository);
    }

    public RepositoryAnalyzer(File projectPath) throws IOException {
        //Open the repos
        this.repository = JGitHelper.openLocalRepository(projectPath);
        this.git = new Git(repository);
    }


    /**
     * Returns an InputStream with the old version of the file open.
     * This is method is expensive because it has to search through the entire
     * old version of the repository to find the file.
     * @param filePath the path of the file to open relative to the project root dir (ex: "my-project/my-package/my-file.java")
     * @param commitHash a commit hash, such as "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a" that the file is present on.
     * @throws IOException when there are problems opening the commit
     * @throws IllegalStateException if the file could not be found at that commit
     */
    public InputStream obtainFileContents(String filePath, String commitHash) throws IOException
    {
        //Create a commit object from the commit hash
        ObjectId commitId = repository.resolve(commitHash);
        assert commitId != null;
        RevWalk revWalk = new RevWalk(repository);
        RevCommit commit = revWalk.parseCommit(commitId);

        //Prepare a TreeWalk that can walk through the version of the repos at that commit
        RevTree tree = commit.getTree();
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);

        //Traverse through the old version of the project until the target file is found.
        //I couldn't get `treeWalk.setFilter(PathFilter.create(filePath));` to work, so this is an alternative approach.
        while (treeWalk.next()) {
            String path = treeWalk.getPathString();
            if (path.endsWith(filePath))
            {
                //Return an input stream that has the old version of the file open
                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = repository.open(objectId);
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
     * @param codeBase represents the entire repository and history
     * @param commitHash the Git commit hash representing the version of the repository to examine
     * @throws IOException when there are problems opening the commit
     */
    public void attachLineCountToCodebase(CodebaseV2 codeBase, String commitHash) throws IOException
    {
        //Create a commit object from the commit hash
        ObjectId commitId = repository.resolve(commitHash);
        assert commitId != null;
        RevWalk revWalk = new RevWalk(repository);
        RevCommit revCommit = revWalk.parseCommit(commitId);

        attachLineCountToCodebase(codeBase, revCommit);
    }

    /**
     * Given a CodeBase, attaches all the file sizes (in bytes) and line counts of each file present
     * at a particular commit of the Git repository. Each FileObject in the Codebase is given a new
     * HeatObject that holds this file size and line count data. If a HeatObject already exists for the
     * file at the given commit, this updates the existing HeatObject.
     * @param codeBase represents the entire repository and history
     * @param revCommit the state of the Git repository to examine
     * @throws IOException when there are problems opening the commit
     */
    public void attachLineCountToCodebase(CodebaseV2 codeBase, RevCommit revCommit) throws IOException
    {
        //Prepare a TreeWalk that can walk through the version of the repos at that commit
        RevTree tree = revCommit.getTree();
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);

        //Traverse through the old version of the project until the target file is found.
        //I couldn't get `treeWalk.setFilter(PathFilter.create(filePath));` to work, so this is an alternative approach.
        while (treeWalk.next()) {
            String path = treeWalk.getPathString();

            //Create an input stream that has the old version of the file open
            ObjectId objectId = treeWalk.getObjectId(0);
            ObjectLoader loader = repository.open(objectId);
            InputStream inputStream = loader.openStream();

            //Get number of lines and file size
            long lineCount = FileSizeCalculator.computeLineCount(inputStream);
            long fileSize = loader.getSize();

            //Attach data to the HeatObject associated with this version of the file
            FileObjectV2 fileObject = codeBase.createOrGetFileObjectFromPath(path);
            HeatObject heatObject = fileObject.getHeatObjectAtCommit(revCommit.getName());
            heatObject.setLineCount(lineCount);
            heatObject.setFileSize(fileSize);
        }
    }


    /**
     * Computes line count, file size, and number of commits for every file at every commit.
     * This places FileObjects in the given Codebase, and each of these FielObjects has its
     * map of commit hashes to HeatObjects filled out.
     * These new HeatObjects contain the newly computed metrics.
     * @param codebase the Codebase to modify. It should have its active branch set.
     */
    public void attachCodebaseData(CodebaseV2 codebase)
    {
        try
        {
            //Get all commits in the repos for one branch
            Iterable<RevCommit> commitIterable = getCommitsByBranch(codebase.getActiveBranch());

            //Extract the first commit
            Iterator<RevCommit> commitIterator = commitIterable.iterator();
            RevCommit newerRevCommit;
            if (commitIterator.hasNext())
                newerRevCommit = commitIterator.next();
            else {
                Constants.LOG.info("There were not enough commits to compute the number of times each file was changed.");
                return;
            }
            //Process the first commit (can be moved to another method?)
            attachLineCountToCodebase(codebase, newerRevCommit); //computes line count and file size data!
            CommitV2 newerCommit = new CommitV2(newerRevCommit);
            codebase.getActiveCommits().add(newerCommit); //extracts data from the RevCommit and stores it in our codebase model


            //Iterate through the commits two-at-a-time
            while (commitIterator.hasNext())
            {
                RevCommit olderRevCommit = commitIterator.next();
                //Find the difference between the olderRevCommit and newerRevCommit
                final List<DiffEntry> diffs = git.diff()
                        .setOldTree(prepareTreeParser(olderRevCommit.getName()))
                        .setNewTree(prepareTreeParser(newerRevCommit.getName()))
                        .call();

                //For each file modified in the commit...
                for (DiffEntry diffEntry : diffs)
                {
                    String filePath = diffEntry.getNewPath(); //arbitrarily choose the newer path of the file since its name may have changed
                    String fileName = new File(filePath).getName(); //convert file path to file name

                    //Count the number of times the file was changed
                    String newCommitHash = newerRevCommit.getName();
                    incrementNumberOfTimesChanged(codebase, filePath, newCommitHash);
                    newerCommit.getFileSet().add(fileName);
                }

				//Process the olderRevCommit (can be moved to another method?)
				attachLineCountToCodebase(codebase, olderRevCommit);
                newerCommit = new CommitV2(olderRevCommit);
                codebase.getActiveCommits().add(newerCommit);

                newerRevCommit = olderRevCommit;
            }
        }
        catch (IOException | GitAPIException e) {
            Constants.LOG.error(e);
            Constants.LOG.error(e.getMessage());
        }
    }

    /**
     * Adds 1 to the number of commits associated with a file.
     * @param filePath the file's path or name (can be either)
     * @param commitHash the state of the Git repos that the number should be recorded at.
    *  Example: On commit #3, a file has a commit count of 2 because it was modified in the previous two commits.
     */
    private static void incrementNumberOfTimesChanged(CodebaseV2 codebase, String filePath, String commitHash)
    {
        //Retrieve the HeatObject that holds the number of commits for the target file
        FileObjectV2 fileObjectV2 = codebase.createOrGetFileObjectFromPath(filePath);
        HeatObject heatObject = fileObjectV2.getHeatObjectAtCommit(commitHash);
        //Increment the HeatObject's number of commits
        heatObject.setNumberOfCommits(heatObject.getNumberOfCommits() + 1);
    }


    /**
     * Finds the list of all LOCAL branch names and adds them
     * to the list inside the provided Codebase.
     */
    public void attachBranchNameList(CodebaseV2 codebase) throws GitAPIException
    {
        //Get the list of all LOCAL branches
        List<Ref> call = git.branchList().call();
        //Alternatively: Get the list of all branches, both local and REMOTE --> call = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

        //Add all branch names to the Codebase
        for (Ref ref : call)
        {
            String branchName = new File(ref.getName()).getName(); //quick-and-dirty way to convert a branch name from format "refs/heads/retire-old-model" to "retire-old-model"
            codebase.getBranchNameList().add(branchName);
        }
    }

















    /**
     * A helper method from the JGit Cookbook...I actually have no idea what it does.
     * However, it's necessary for finding diffs.
     */
    private AbstractTreeIterator prepareTreeParser(String commitHash) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(commitHash));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }

    public Iterable<RevCommit> getAllCommits() throws IOException, GitAPIException {
        return git.log().all().call();
    }

    public Iterable<RevCommit> getCommitsByBranch(String branchName) throws IOException, GitAPIException {
        //Choose the branch
        ObjectId branchId = repository.resolve(branchName);

        return git.log().add(branchId).call();
    }
}
