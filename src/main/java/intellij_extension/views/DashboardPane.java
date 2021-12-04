package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.Constants.HeatMetricOptions;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.utility.HeatCalculationUtility;
import intellij_extension.views.interfaces.IContainerView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A view that holds rectangles to represent files for a particular commit.
 * Each rectangle is colored based on the heat values assigned to its corresponding file.
 */
public class DashboardPane implements IContainerView, CodeBaseObserver {

    //region Vars
    // Basically this class' main nodes
    // ScrollPane -> AnchorPane -> FlowPane
    private ScrollPane scrollPane;
    private AnchorPane anchorPane;
    private FlowPane flowPane;
    //endregion

    //region Constructors
    public DashboardPane() {
        // Create ScrollPane
        scrollPane = new ScrollPane();
        // Set Properties
        scrollPane.setMinWidth(Constants.ZERO_WIDTH);

        // Create the AnchorPane inside the ScrollPane
        anchorPane = new AnchorPane();
        scrollPane.setContent(anchorPane);
        // Set Properties
        anchorPane.setMinWidth(Constants.ZERO_WIDTH);
        anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());

        // Create HeatMapFlowPane inside the AnchorPane
        flowPane = new FlowPane();
        anchorPane.getChildren().add(flowPane);
        // Set Properties
        flowPane.setMinWidth(Constants.ZERO_WIDTH);
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());
        flowPane.setVgap(Constants.HEATMAP_VERTICAL_SPACING);
        flowPane.setHgap(Constants.HEATMAP_HORIZONTAL_SPACING);
        flowPane.setPadding(Constants.HEATMAP_PADDING);

        // Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return scrollPane;
    }

    @Override
    public void clearPane() {
        flowPane.getChildren().clear();
    }
    //endregion

    //region CodeBaseObserver methods

    /**
     * Clears the pane, then displays all files present in the target commit.
     * Each file is represented by a rectangular pane.
     *
     * @param setOfFiles a sorted grouping of files in a Codebase. Each HeatObject
     *                   inside the FileObject must already have its heatLevel according to the current
     *                   heat metric(s).
     */
    //TODO make a separate thread
    //TODO edit method signature & Javadoc
    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode, HeatMetricOptions heatMetricOption) {

        Codebase codebase = Codebase.getInstance();

        Platform.runLater(() -> {
            System.out.println("Creating the dashboard...");
            flowPane.getChildren().clear();

            double scoreOverall = codebase.getAverageHeatOverall();
            Label scoreOverallText = new Label(String.format("Overall Score: %s", scoreOverall));

            double scoreFileSize = codebase.getAverageHeatFileSize();
            Label scoreFileSizeText = new Label(String.format("File Size Score: %s", scoreFileSize));

            double scoreNumberOfCommits = codebase.getAverageHeatNumberOfCommits();
            Label scoreNumberOfCommitsText = new Label(String.format("Number of Commits Score: %s", scoreNumberOfCommits));

            double scoreNumberOfAuthors = codebase.getAverageHeatNumberOfAuthors();
            Label scoreNumberOfAuthorsText = new Label(String.format("Number of Authors Score: %s", scoreNumberOfAuthors));

            //Add all labels to the view
            flowPane.getChildren().addAll(scoreOverallText, scoreFileSizeText, scoreNumberOfCommitsText, scoreNumberOfAuthorsText);

            System.out.println("Finished creating the dashboard.");
        });
    }


    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void newBranchSelected(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode, HeatMetricOptions heatMetricOption) {
        refreshHeatMap(setOfFiles, targetCommit, groupingMode, heatMetricOption);
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Nothing to do for this action
    }

    public void commitSelected(Commit commit) {
        // Nothing to do for this action
    }
    //endregion
}