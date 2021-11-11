package intellij_extension.models.redesign;

import com.google.common.collect.HashBasedTable;
import intellij_extension.models.Commit;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.HashSet;

public class Codebase {

    private String activeBranch;
    private ArrayList<String> branchNameList;
    private HashSet<Commit> activeCommits;
    private HashSet<FileObjectV2> activeFileObjects;

    // TODO
    // I think this is the exact sort of DataStructure we want for this.
    // Here's more info on it.
    // The stack overflow post isn't really related to our problem but it led me to the discovery of this class.
    // https://stackoverflow.com/questions/7906123/java-matrix-data-type-for-inserting-values-based-on-their-coordinates
    // https://guava.dev/releases/snapshot/api/docs/com/google/common/collect/Table.html
    // https://guava.dev/releases/snapshot/api/docs/com/google/common/collect/HashBasedTable.html
    // https://github.com/google/guava/wiki/NewCollectionTypesExplained#table
    private HashBasedTable<String, String, Integer> commitToFileAssociation;

    public Codebase() {
        activeBranch = "master";
        branchNameList = new ArrayList<>();
        activeCommits = new HashSet<>();
        activeFileObjects = new HashSet<>();
        commitToFileAssociation = HashBasedTable.create();
    }

    // TODO
    // Is this the JGit object?
    public void buildBranchData(String branch) {
        // for (RevCommit rCommit : CommitCountCalculator.getCommitsByBranch(branch)) {
            // CommitV2 newCommit = new CommitV2(rCommit);
            // TODO - This does not exist - Might be looping over diff entry?
            // for (File file: newCommit.files) {
                // If File already exists in commitToFileAssocation
                    // CalculateHeat with history taken into account
                // Else
                    // CalculateHeat without history
                // BuildFileObject
                // FileObjectV2 newFile = new FileObjectV2();
                // activeFileOjbects.add(newFile);
                // commitToFileAssociation.put(newFile.getFilename(), newCommit.getHash(), 1);
            // }

            // Fill out table with 0s for files that are not in commit.
            // Foreach file NOT in new Commit
                // commitToFileAssociation.put(newFile.getFilename(), newCommit.getHash(), 0);
        }
    }
}
