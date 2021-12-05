package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.Constants.HeatMetricOptions;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.DashboardModel;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.views.interfaces.IContainerView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;


public class DashboardPane implements IContainerView, CodeBaseObserver {

    //region Vars
    // Basically this class' main nodes
    // ScrollPane -> AnchorPane -> VBox
    private final ScrollPane scrollPane;
    private final AnchorPane anchorPane;
    private final VBox vbox; //holds all the content for the dashboard
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

        //Create VBox to hold everything else
        vbox = new VBox();
        vbox.setPadding(Constants.HEATMAP_PADDING);
        anchorPane.getChildren().add(vbox);

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
        vbox.getChildren().clear();
    }
    //endregion

    //region CodeBaseObserver methods

    private void setupDashboard()
    {
        Platform.runLater(() -> {
            System.out.println("Creating the dashboard...");
            clearPane();

            //Set a heading to explain the average scores below it
            Label scoreTitleLabel = new Label("Average Heat Scores Out of 10 From Each Metric");
            scoreTitleLabel.setFont(Font.font(Constants.HEADER_FONT, Constants.SF_TEXT_FONT_WEIGHT, 18));
            scoreTitleLabel.wrapTextProperty().set(true);
            vbox.getChildren().add(scoreTitleLabel);

            //Add the average heat scores to the dashboard
            FlowPane scoreFlowPane = createScoreFlowPane();
            vbox.getChildren().add(scoreFlowPane);

            //Set a heading to explain the hottest files below it
            Label hottestFilesTitleLabel = new Label("The #1 Hottest Files From Each Metric");
            hottestFilesTitleLabel.setFont(Font.font(Constants.HEADER_FONT, Constants.SF_TEXT_FONT_WEIGHT, 18));
            hottestFilesTitleLabel.wrapTextProperty().set(true);
            vbox.getChildren().add(hottestFilesTitleLabel);

            //Add hottest files
            addHottestFileHyperlinks();

            System.out.println("Finished creating the dashboard.");
        });
    }

    /**
     * Creates a FlowPane that holds ScoreContainers for every heat metric.
     * Inside each ScoreContainer is the average score for that metric across all files
     * present at the latest commit and a caption to indicate the metric name.
     */
    private FlowPane createScoreFlowPane()
    {
        FlowPane scoreFlowPane = new FlowPane();
        anchorPane.getChildren().add(scoreFlowPane);
        // Set Properties
        scoreFlowPane.setMinWidth(Constants.ZERO_WIDTH);
        scoreFlowPane.prefWidthProperty().bind(scrollPane.widthProperty());
        scoreFlowPane.setVgap(Constants.HEATMAP_VERTICAL_SPACING);
        scoreFlowPane.setHgap(Constants.HEATMAP_HORIZONTAL_SPACING);
        scoreFlowPane.setPadding(Constants.HEATMAP_PADDING);

        //Get the list of all average scores
        DashboardModel dashboardModel = DashboardModel.getInstance();
        ArrayList<Double> averageScoreList = dashboardModel.getAverageHeatScoreList();
        assert averageScoreList.size() == HeatMetricOptions.values().length;

        //For every metric, add a ScoreContainer to represent the average of that metric
        int i = 0;
        for (String heatMetricText : Constants.HEAT_METRIC_OPTIONS)
        {
            //Get the score and heat metric name (caption)
            double averageScore = averageScoreList.get(i);
            String captionText = String.format("%s Score", heatMetricText); //displays text such as "Overall Score" in each label

            //Place above data into a ScoreContainer view
            ScoreContainer scoreContainerNumberOfAuthors = new ScoreContainer(averageScore, captionText);
            scoreFlowPane.getChildren().add(scoreContainerNumberOfAuthors.getNode());

            i++;
        }

        return scoreFlowPane;
    }


    /**
     * Places a Hyperlink for the #1 hottest file from each metric onto the dashboard
     */
    private void addHottestFileHyperlinks()
    {
        DashboardModel dashboardModel = DashboardModel.getInstance();
        ArrayList<String> namesOfHottestFilesList = dashboardModel.getNamesOfHottestFileList();
        assert namesOfHottestFilesList.size() == HeatMetricOptions.values().length;

        //For every metric, add a Hyperlink containing the heat metric name and the file name
        int i = 0;
        for (String heatMetricText : Constants.HEAT_METRIC_OPTIONS)
        {
            String fileName = namesOfHottestFilesList.get(i);

            //Add the name of the file as a Hyperlink to the Dashboard
            Hyperlink hyperlink = createFileHyperlink(heatMetricText, fileName);
            vbox.getChildren().add(hyperlink);

            i++;
        }
    }

    /**
     * Creates a Hyperlink so that, when clicking on a file name, it is as if that file
     * was selected in the heat map. Other views are populated with that file's data accordingly.
     */
    private Hyperlink createFileHyperlink(String heatMetricName, String fileName)
    {
        Hyperlink hyperlink = new Hyperlink(String.format("Hottest file from %s: %s", heatMetricName, fileName));
        hyperlink.setFont(Font.font(Constants.SF_TEXT_FONT, Constants.SF_TEXT_FONT_WEIGHT, Constants.SF_TEXT_SIZE));
        hyperlink.wrapTextProperty().set(true);
        //On clicking the hyperlink, populate the plugin panes with the file data
        if (!fileName.equals(Constants.NO_FILES_EXIST))
        {
            hyperlink.setOnAction(event -> HeatMapController.getInstance().heatMapComponentSelected(fileName));
        }

        return hyperlink;
    }


    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode currentGroupingMode, HeatMetricOptions currentHeatMetricOption) {
        setupDashboard();
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void newBranchSelected(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode, HeatMetricOptions heatMetricOption) {
        setupDashboard();
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