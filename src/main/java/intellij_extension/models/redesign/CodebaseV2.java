package intellij_extension.models.redesign;

import com.google.common.collect.HashBasedTable;
import intellij_extension.models.Commit;
import intellij_extension.utility.commithistory.CommitCountCalculator;
import javafx.util.Pair;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class CodebaseV2 {

    private String activeBranch;
    private LinkedHashSet<String> branchNameList;
    private LinkedHashSet<Commit> activeCommits;
    private LinkedHashSet<FileObjectV2> activeFileObjects;

    // TODO
    // I think this is the exact sort of DataStructure we want for this.
    // Here's more info on it.
    // The stack overflow post isn't really related to our problem but it led me to the discovery of this class.
    // https://stackoverflow.com/questions/7906123/java-matrix-data-type-for-inserting-values-based-on-their-coordinates
    // https://guava.dev/releases/snapshot/api/docs/com/google/common/collect/Table.html
    // https://guava.dev/releases/snapshot/api/docs/com/google/common/collect/HashBasedTable.html
    // https://github.com/google/guava/wiki/NewCollectionTypesExplained#table
    private HashBasedTable<String, String, Integer> commitToFileAssociation;

    public CodebaseV2() {
        activeBranch = "master";
        branchNameList = new LinkedHashSet<>();
        activeCommits = new LinkedHashSet<>();
        activeFileObjects = new LinkedHashSet<>();
        commitToFileAssociation = HashBasedTable.create();
    }

    // TODO
    // Is this the JGit object?
    public void buildBranchData(String branch) throws IOException {
        Pair<Iterable<RevCommit>, TreeWalk> treeWalkCommitsPair = CommitCountCalculator.getCommitsAndTreeByBranch(branch);
        for (RevCommit revCommit : treeWalkCommitsPair.getKey()) {
            TreeWalk treeWalk = treeWalkCommitsPair.getValue();
            treeWalk.addTree(revCommit.getTree());
            CommitV2 newCommit = new CommitV2(revCommit);

            while (treeWalk.next()) {
                //This can be used if there's a need for the File object to be stored
                //treeWalk.getObjectId(0).copyTo(System.out);
                String fileName = treeWalk.getNameString();
                String filePath = treeWalk.getPathString();
                ObjectLoader loader = CommitCountCalculator.getObjectLoader(treeWalk.getObjectId(0));

                FileObjectV2 fileObject = new FileObjectV2(Paths.get(filePath), fileName);

                // Nice, love me a good stream implementation
                // For those unfamiliar
                // Stream() = loop over all elements
                // Filter() = while looping over all elements only give me objects that match my filter
                    // In this case if the above fileObject = a fileObject in the list already
                // FindAny() = Give me any object that satisfies the filter
                // OrElse() = If FindAny fails return the above fileObject (meaning there is no already created fileObject for this file)
                FileObjectV2 existingFileObject = activeFileObjects.stream()
                        .filter(fileObjectV2 -> fileObject.equals(fileObjectV2)).findAny().orElse(fileObject);

                LinkedHashMap<String, HeatObject> commitHashMap = existingFileObject.getCommitHashToHeatObjectMap();
                HeatObject heatObject;
                if (commitHashMap.isEmpty()) {
                    // TODO - A question from Ethan
                    //  Why aren't we calculating the heat here?
                    heatObject = new HeatObject(1, fileName, getLineCount(filePath),
                            loader.getSize(), 1);
                } else {
                    heatObject = computeHeatObjectFromHistory(commitHashMap, existingFileObject,
                            fileName, filePath, loader);
                }
                existingFileObject.setHeatForCommit(newCommit.getHash(), heatObject);
                activeFileObjects.add(existingFileObject);


                /** I gave the 2D array/HashBasedTable idea another thought and came to the conclusion that either way is
                 * a very memory-intensive approach.
                 * If we go via the 2D array approach, iterating through a huge one for instance 1000 indexes of primitives
                 * can cause a bottleneck. On the other way round, if we discard are to get away from the idea of 2D array,
                 * there is no need to maintain 0 as a value for broken links (i.e. files that were never a part of a commit)
                 * It would be too much to hold for a drop of blood.
                 * This leads us to see HashMaps v/s HashBasedTable v/s Set of fileNames in every CommitV2 object
                 * In this comparison, HashMaps would win for sizes ranging from 1 to 10000 and it is only beyond that value
                 * is when using HashBasedTable would actually make sense. If we are no longer maintaining the broken links (0)
                 * as a value, a simple HashMap would have done the job for us, However, it's better to just have a simple
                 * Set of fileName string per CommitV2 object.
                 * If the above justification sounds OK, we can remove the HashBasedTable init.
                **/
                // TODO - Ethan's Comment - I prefer simplicity and to worry about size/slow down problems when that actually happens.
                //  Ethan's Comment - Like Prof. Rawlins said during the Command Pattern talk, do worry about it being too much data until it becomes a problem.
                //  Ethan's Comment - Another approach is to limit how far we go back in the history of a branch - Just an idea - Don't have to act on this.
                newCommit.addFileToSet(fileName);
            }
        }
    }

    private HeatObject computeHeatObjectFromHistory(LinkedHashMap<String, HeatObject> commitHashMap,
                                                    FileObjectV2 existingFileObject, String fileName, String filePath, ObjectLoader loader) {
        HeatObject previousHeat = commitHashMap.get(existingFileObject.getLatestCommit());
        float latestHeatLevel = previousHeat.getHeatLevel();
        int prevNumOfCommits = previousHeat.getNumberOfCommits();
        return new HeatObject(++latestHeatLevel, fileName, getLineCount(filePath), loader.getSize(), ++prevNumOfCommits);
    }

    // TODO: Need to find a way to get the number of lines through commit history
    //  The main question here is, is it required/worth to maintain the history of file per commit?
    private long getLineCount(String filePath) {
        try {
            // This technique would return the lines of the file in the latest version of the repository
            return Files.lines(Paths.get(filePath)).count();
        } catch (IOException e) {
            return 0;
        }
    }
}
