package intellij_extension.utility;

import intellij_extension.Constants;
import intellij_extension.models.CodeBase;
import intellij_extension.models.FileObject;
import intellij_extension.utility.commithistory.JGitHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * WIP from Pete -- trying to populate the model with file/commit/diff data.
 * Can be deleted since it is unused.
 */
public class RepositoryAnalyzer
{
    private Repository repository;
    private Git git;

    public RepositoryAnalyzer() throws IOException {
        //Open the repos
        this.repository = JGitHelper.openLocalRepository();
        this.git = new Git(repository);
    }


    public void populateModel(CodeBase codeBase)
    {
        String branchName = codeBase.getActiveBranch();
        try
        {
            Iterable<RevCommit> revCommitsIterable = getCommitsByBranch(branchName);
            //HashBasedTable<String, String, Integer> table = codeBase.getCommitToFileAssociation();


            //Iterate through the commits two-at-a-time
            Iterator<RevCommit> commitIterator = revCommitsIterable.iterator();
            RevCommit newerCommit;
            if (commitIterator.hasNext())
                newerCommit = commitIterator.next();
            else {
                Constants.LOG.error("There were not enough commits to compute the number of times each file was changed.");
                return; //TODO needs changed?
            }
            while (commitIterator.hasNext()) {
                RevCommit olderCommit = commitIterator.next();

                final List<DiffEntry> diffs = git.diff()
                        .setOldTree(prepareTreeParser(olderCommit.getName()))
                        .setNewTree(prepareTreeParser(newerCommit.getName()))
                        .call();




                //Count the number of times each file was changed
                /*for (DiffEntry diffEntry : diffs) {
                    String filePath = diffEntry.getOldPath(); //arbitrarily choose the older name of the file even if its name changed in the commit
                    if (filePathToChangeCountMap.containsKey(filePath)) {
                        //Increment hash map value since it exists
                        int timesChanged = filePathToChangeCountMap.get(filePath);
                        filePathToChangeCountMap.put(filePath, timesChanged + 1);
                    } else {
                        filePathToChangeCountMap.put(filePath, 1);
                    }
                }*/

                newerCommit = olderCommit;
            }

        }
        catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void populateBranchNameList(CodeBase codeBase)
    {
        //TODO
    }

    public void populateCommitToFileAssociationTable(CodeBase codeBase) throws IOException, GitAPIException
    {
        String branchName = codeBase.getActiveBranch();
        Iterable<RevCommit> revCommitsIterable = getCommitsByBranch(branchName);
        //HashBasedTable<String, String, Integer> table = codeBase.getCommitToFileAssociation();

        for (RevCommit revCommit : revCommitsIterable)
        {
            List<String> filesPresentInCommit = getFilePathsFromCommit(revCommit);
            for (String filePath : filesPresentInCommit)
            {
                String commitHash = revCommit.getName();
                //table.put(filePath, commitHash, PRESENT_IN_TABLE);
                //TODO waiting on Abhishek's code
            }
        }
    }

    public List<String> getFilePathsFromCommit(RevCommit revCommit)
    {
        LinkedList<String> pathLinkedList = new LinkedList<>();

        // a RevWalk allows to walk over commits based on some filtering that is defined
        try (RevWalk walk = new RevWalk(repository))
        {
            RevTree tree = revCommit.getTree();
            // now use a TreeWalk to iterate over all files in the Tree
            // you can set Filters to narrow down the results if needed
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                pathLinkedList.add(treeWalk.getPathString());
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return pathLinkedList;
    }









    public HashMap<String, Integer> calculateNumberOfCommitsPerFile(Iterable<RevCommit> commitList) {
        HashMap<String, Integer> filePathToChangeCountMap = new HashMap<>();
        try {
            //Iterate through the commits two-at-a-time
            Iterator<RevCommit> commitIterator = commitList.iterator();
            RevCommit newerCommit;
            if (commitIterator.hasNext())
                newerCommit = commitIterator.next();
            else {
                Constants.LOG.error("There were not enough commits to compute the number of times each file was changed.");
                return new HashMap<>();
            }
            while (commitIterator.hasNext()) {
                RevCommit olderCommit = commitIterator.next();

                final List<DiffEntry> diffs = git.diff()
                        .setOldTree(prepareTreeParser(olderCommit.getName()))
                        .setNewTree(prepareTreeParser(newerCommit.getName()))
                        .call();


                //Count the number of times each file was changed
                for (DiffEntry diffEntry : diffs) {
                    String filePath = diffEntry.getOldPath(); //arbitrarily choose the older name of the file even if its name changed in the commit
                    if (filePathToChangeCountMap.containsKey(filePath)) {
                        //Increment hash map value since it exists
                        int timesChanged = filePathToChangeCountMap.get(filePath);
                        filePathToChangeCountMap.put(filePath, timesChanged + 1);
                    } else {
                        filePathToChangeCountMap.put(filePath, 1);
                    }
                }

                newerCommit = olderCommit;
            }
        } catch (IOException | GitAPIException e) {
            Constants.LOG.error(e);
            Constants.LOG.error(e.getMessage());
        }
        return filePathToChangeCountMap;
    }

    private AbstractTreeIterator prepareTreeParser(String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(objectId));
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

    public HashMap<String, FileObject> editFileMetricMap(HashMap<String, FileObject> existingFileMetricMap) {
        try {
            //Calculate the number of times each file was changed in the entire commit history
            HashMap<String, Integer> numberOfCommitsPerFile = this.calculateNumberOfCommitsPerFile(
                    this.getAllCommits());
            Iterator<String> timesUpdatedIterator = numberOfCommitsPerFile.keySet().iterator();

            //Put the data onto the hash map
            while (timesUpdatedIterator.hasNext()) {
                String filePath = timesUpdatedIterator.next();

                //Merge the existing data (if it exists) with the newly computed data
                FileObject existingData = existingFileMetricMap.get(filePath); //what was passed in as a param
                int numberOfCommits = numberOfCommitsPerFile.get(filePath); //what this class computed
                if (existingData == null)
                    existingData = new FileObject(filePath, filePath, -1);
                existingData.setNumberOfCommits(numberOfCommits);

                existingFileMetricMap.put(filePath, existingData);
            }

            return existingFileMetricMap;
        } catch (IOException | GitAPIException e) {
            Constants.LOG.error(e);
            Constants.LOG.error(e.getMessage());
            return new HashMap<>();
        }
    }
}
