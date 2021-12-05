package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.Constants.HeatMetricOptions;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
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
        Codebase codebase = Codebase.getInstance();

        Platform.runLater(() -> {
            System.out.println("Creating the dashboard...");
            clearPane();

            //FIXME adding this label causes the view to not render...
            Label scoreTitleLabel = new Label("Average Heat Scores out of 10 for Each Metric");
            scoreTitleLabel.setFont(Font.font(Constants.HEADER_FONT, Constants.SF_TEXT_FONT_WEIGHT, 18));
            scoreTitleLabel.wrapTextProperty().set(true);
            vbox.getChildren().add(scoreTitleLabel);

            //Add the average heat scores to the dashboard
            FlowPane scoreFlowPane = createScoreFlowPane(codebase);
            vbox.getChildren().add(scoreFlowPane);

            //begin adding of hottest files

            //end adding of hottest files

            System.out.println("Finished creating the dashboard.");
        });
    }

    private FlowPane createScoreFlowPane(Codebase codebase)
    {
        FlowPane scoreFlowPane = new FlowPane();
        anchorPane.getChildren().add(scoreFlowPane);
        // Set Properties
        scoreFlowPane.setMinWidth(Constants.ZERO_WIDTH);
        scoreFlowPane.prefWidthProperty().bind(scrollPane.widthProperty());
        scoreFlowPane.setVgap(Constants.HEATMAP_VERTICAL_SPACING);
        scoreFlowPane.setHgap(Constants.HEATMAP_HORIZONTAL_SPACING);
        scoreFlowPane.setPadding(Constants.HEATMAP_PADDING);

        //TODO use Iterator Pattern here
        String scoreFormat = "%s Score"; //displays text such as "Overall Score" in each label
        double scoreOverall = codebase.getAverageHeatOverall();
        ScoreContainer scoreContainerOverall = new ScoreContainer(scoreOverall, String.format(scoreFormat, Constants.OVERALL_TEXT));
        scoreFlowPane.getChildren().add(scoreContainerOverall.getNode());

        double scoreFileSize = codebase.getAverageHeatFileSize();
        ScoreContainer scoreContainerFileSize = new ScoreContainer(scoreFileSize, String.format(scoreFormat, Constants.OVERALL_TEXT));
        scoreFlowPane.getChildren().add(scoreContainerFileSize.getNode());

        double scoreNumberOfCommits = codebase.getAverageHeatNumberOfCommits();
        ScoreContainer scoreContainerNumberOfCommits = new ScoreContainer(scoreNumberOfCommits, String.format(scoreFormat, Constants.OVERALL_TEXT));
        scoreFlowPane.getChildren().add(scoreContainerNumberOfCommits.getNode());

        double scoreNumberOfAuthors = codebase.getAverageHeatNumberOfAuthors();
        ScoreContainer scoreContainerNumberOfAuthors = new ScoreContainer(scoreNumberOfAuthors, String.format(scoreFormat, Constants.OVERALL_TEXT));
        scoreFlowPane.getChildren().add(scoreContainerNumberOfAuthors.getNode());

        return scoreFlowPane;
    }

    private Hyperlink createFileHyperlink(String fileName)
    {
        Hyperlink hyperlink = new Hyperlink(fileName);
        //hyperlink.setAlignment(Pos.TOP_CENTER);
        //On clicking the hyperlink, populate the plugin panes with the file data
        hyperlink.setOnAction(event -> HeatMapController.getInstance().heatMapComponentSelected(fileName));

        return hyperlink;
    }


    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode currentGroupingMode, HeatMetricOptions currentHeatMetricOption) {
        // Nothing to do for this action
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