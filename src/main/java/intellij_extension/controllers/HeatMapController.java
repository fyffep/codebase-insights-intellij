package intellij_extension.controllers;

import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.progress.ProgressIndicator;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.utility.RepositoryAnalyzer;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HeatMapController extends PreloadingActivity implements IHeatMapController {

    private static HeatMapController instance;

    private Codebase codeBase;

    private HeatMapController() {
        this.codeBase = Codebase.getInstance();
    }

    public static HeatMapController getInstance() {
        if (instance == null) {
            instance = new HeatMapController();
        }

        return instance;
    }

    public void recalculateHeat() {
        //Obtain file metrics by analyzing the code base
        try {
            //Calculate file sizes for every commit
            RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer();
            repositoryAnalyzer.attachBranchNameList(codeBase); // FIXME bad place to obtain the list of branches for a Codebase?
            codeBase.selectDefaultBranch();
            repositoryAnalyzer.attachCodebaseData(codeBase);

            //Now the Codebase contains all the data it needs
        } catch (IOException | GitAPIException e) {
            System.out.println("There was an error: ");
            e.printStackTrace();
            System.out.println(e); //logger fails here
            System.out.println(e.getMessage());
        }
        System.out.println("Heat calculations complete. Number of files: " + codeBase.getActiveFileObjects().size());
    }

    @Override
    public void preload(@NotNull ProgressIndicator indicator) {
        System.out.println("Preloading HeatMapController..."); //logger fails here
        recalculateHeat();
        codeBase.notifyObserversOfRefreshHeatMap();
        System.out.println("Finished preloading HeatMapController.");
    }

    // Where did this action occur in the View?
    // A user click on one of the view objects that make up the heat map.
    // What do we do now?
    // We need to tell model about this so the model can send the CommitHistory of that associated file to the view.
    // I.e. Update the FileHistoryPane and Update SelectedFileTerminalPane
    public void heatMapComponentSelected(String id) {
        codeBase.heatMapComponentSelected(id);
    }

    // A way for FileHistoryDetails to get the branch list.
    public void branchListRequested() {
        codeBase.branchListRequested();
    }

    // Where did this action occur in the View?
    // A user changed the branch in the FileHistroyPane->ComboBox
    // FileHistoryPane lines 207-214
    // What do we do now?
    // We need to tell model about this so the model can rebuild all the data with this new branch.
    // This causes HeatMapPane to change, FileCommitHistoryPane to clear, CommitDetails to clear, and SelectedFileTerminalPane to hide.
    public void branchSelected(String branchName) {
        codeBase.branchSelected(branchName);
    }

    // Where did this action occur in the View?
    // A user clicked on one of the rows in the FileHistoryPane->TableView
    // FileHistoryPane lines 163-166
    // What do we do now?
    // We need to tell model about this so the model can send the CommitDetails the info of the selected commit
    // I.e. Update CommitDetailsPane
    public void commitSelected(String commitHash) {
        codeBase.commitSelected(commitHash);
    }

    // Where did this action occur in the View?
    // A user clicked on the button "Update HeatMap to this Commit" in the CommitDetails Pane.
    // CommitDetailsPane lines 83-88
    // What do we do now?
    // We need to tell model about this so the model can send HeatMap data associated with the selected CommitHash
    // I.e. Update HeatMapPane with a new heat map
    // Might make sense to have the selected file highlighted.
    public void changeHeatMapToCommit(String commitHash) {
        codeBase.changeHeatMapToCommit(commitHash);
    }

    // Where did this action occur in the View?
    // A user clicked on the button "Open File" in the SelectedFileTerminalPane
    // What do we do now?
    // This is a good question... I guess this still needs to go to the model, so we can get the full path.
    // But after that I'd imagine we are doing some sort of Intellij call and nothing else.
    public void openFile(String filename) {
        codeBase.openFile(filename);
    }
}
