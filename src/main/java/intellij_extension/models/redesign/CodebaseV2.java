package intellij_extension.models.redesign;

import com.google.common.collect.HashBasedTable;
import intellij_extension.models.Commit;

import java.util.ArrayList;
import java.util.HashSet;

public class CodebaseV2 {

    private String activeBranch;
    private ArrayList<String> branchNameList;
    private HashSet<CommitV2> activeCommits;
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

    public CodebaseV2() {
        activeBranch = "master";
        branchNameList = new ArrayList<>();
        activeCommits = new HashSet<>();
        activeFileObjects = new HashSet<>();
        commitToFileAssociation = HashBasedTable.create();
    }

    public HashSet<FileObjectV2> getActiveFileObjects() {
        return activeFileObjects;
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
        // }
    }

    // Just made the definitions to remove errors
    // These can change.
    public void heatMapObjectSelected(String id) {
    }

    public void branchSelected(String branchName) {
    }

    public void commitSelected(String commitHash) {
    }

    public void changeHeatMapToCommit(String commitHash) {
    }

    public void openFile(String filename) {
    }

    /**
     * @param id the file's path
     * @return a FileObject corresponding to the target path
     */
    public FileObjectV2 getFileObjectFromId(String id) {
        FileObjectV2 selectedFile = activeFileObjects.stream()
                .filter(file -> file.getFilename().equals(id)).findAny().orElse(null);

        // Failed to find file associated with param id
        if (selectedFile == null) {
            throw new NullPointerException(String.format("Failed to find the proper file associated with the selected HeatMapObject. ID = %s", id));
        }

        return selectedFile;
    }

    public CommitV2 getCommitFromId(String id) {
        CommitV2 selectedCommit = activeCommits.stream()
                .filter(commit -> commit.getHash().equals(id)).findAny().orElse(null);

        // Failed to find file associated with param id
        if (selectedCommit == null) {
            throw new NullPointerException(String.format("Failed to find the proper commit associated with the selected commit in the TableView. Hash = %s", id));
        }

        return selectedCommit;
    }
}
