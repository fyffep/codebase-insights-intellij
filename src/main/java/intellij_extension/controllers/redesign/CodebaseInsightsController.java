package intellij_extension.controllers.redesign;

import intellij_extension.models.redesign.CodebaseV2;

public class CodebaseInsightsController {

    private CodebaseV2 model;

    public CodebaseInsightsController() {
        model = new CodebaseV2();
    }

    // Where did this action occur in the View?
        // A user click on one of the view objects that make up the heat map.
    // What do we do now?
        // We need to tell model about this so the model can send the CommitHistory of that associated file to the view.
        // I.e. Update the FileHistoryPane and Update SelectedFileTerminalPane
    public void heatMapViewObjectSelected(String id) {
        model.heatMapObjectSelected(id);
    }

    // Where did this action occur in the View?
        // A user changed the branch in the FileHistroyPane->ComboBox
        // FileHistoryPane lines 207-214
    // What do we do now?
        // We need to tell model about this so the model can rebuild all the data with this new branch.
        // This causes HeatMapPane to change, FileCommitHistoryPane to clear, CommitDetails to clear, and SelectedFileTerminalPane to hide.
    public void branchSelected(String branchName) {
        model.branchSelected(branchName);
    }

    // Where did this action occur in the View?
        // A user clicked on one of the rows in the FileHistoryPane->TableView
        // FileHistoryPane lines 163-166
    // What do we do now?
        // We need to tell model about this so the model can send the CommitDetails the info of the selected commit
        // I.e. Update ComitDetailsPane
    public void commitSelected(String commitHash) {
        model.commitSelected(commitHash);
    }

    // Where did this action occur in the View?
        // A user clicked on the button "Update HeatMap to this Commit" in the CommitDetails Pane.
        // CommitDetailsPane lines 83-88
    // What do we do now?
        // We need to tell model about this so the model can send HeatMap data associated with the selected CommitHash
        // I.e. Update HeatMapPane with a new heat map
            // Might make sense to have the selected file highlighted.

    public void changeHeatMapToCommit(String commitHash) {
        model.changeHeatMapToCommit(commitHash);
    }

    // Where did this action occur in the View?
        // A user clicked on the button "Open File" in the SelectedFileTerminalPane
    // What do we do now?
        // This is a good question... I guess this still needs to go to the model, so we can get the full path.
        // But after that I'd imagine we are doing some sort of Intellij call and nothing else.
    public void openFile(String filename) {
        model.openFile(filename);
    }
}