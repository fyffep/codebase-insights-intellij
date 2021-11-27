package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.models.redesign.HeatObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.utility.HeatCalculationUtility;
import intellij_extension.views.interfaces.IContainerView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A view that holds rectangles to represent files for a particular commit.
 * Each rectangle is colored based on the heat values assigned to its corresponding file.
 */
public class HeatMapFlowPane implements IContainerView, CodeBaseObserver {

    //region Vars
    // Basically this class' main nodes
    // ScrollPane -> AnchorPane -> FlowPane
    private ScrollPane scrollPane;
    private AnchorPane anchorPane;
    private FlowPane flowPane;
    private GroupingMode groupingMode;
    //endregion

    //region Constructors
    public HeatMapFlowPane(@NotNull Control grandParent) {
        // Create ScrollPane
        scrollPane = new ScrollPane();
        // Set Properties
        scrollPane.prefWidthProperty().bind(grandParent.widthProperty());
        scrollPane.maxWidthProperty().bind(grandParent.widthProperty());
        scrollPane.maxHeightProperty().bind(grandParent.heightProperty());

        // Create the AnchorPane inside the ScrollPane
        anchorPane = new AnchorPane();
        scrollPane.setContent(anchorPane);
        // Set Properties
        anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());

        // Create HeatMapFlowPane inside the AnchorPane
        flowPane = new FlowPane();
        anchorPane.getChildren().add(flowPane);
        // Set Properties
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());
        flowPane.setVgap(Constants.HEATMAP_VERTICAL_SPACING);
        flowPane.setHgap(Constants.HEATMAP_HORIZONTAL_SPACING);
        flowPane.setPadding(Constants.HEATMAP_PADDING);

        // Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
    }
    //endregion

    public void setGroupingMode(GroupingMode groupingMode) {
        this.groupingMode = groupingMode;
    }

    //region IContainerView methods
    @Override
    public Node getNode() {
        return scrollPane;
    }
    //endregion

    //region CodeBaseObserver methods

    /**
     * Clears the pane, then displays all files present in the target commit.
     * Each file is represented by a rectangular pane.
     */
    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode) {
        // If not our grouping mode, then don't do anything
        if(!this.groupingMode.equals(groupingMode)) return;

        //Calculate heat based on file size (SHOULD BE MOVED)
        //HeatCalculationUtility.assignHeatLevelsFileSize(codebase);
        HeatCalculationUtility.assignHeatLevelsNumberOfCommits(codebase);

        Map<String, ArrayList<FileObject>> packageToFileMap = GroupFileObjectUtility.groupByPackage(codebase);
        Platform.runLater(() -> {
            flowPane.getChildren().clear();

            for (Map.Entry<String, TreeSet<FileObject>> entry : setOfFiles.entrySet()) {
                // Get package name
                String groupingKey = entry.getKey();
                // Create a container for it
                HeatFileContainer heatFileContainer = new HeatFileContainer(groupingKey);
                heatFileContainer.maxWidthProperty().bind(flowPane.widthProperty());
                setContainerToolTip(heatFileContainer, groupingKey);

                // Add files to the package container
                for (FileObject fileObject : entry.getValue()) {
                    // Get HeatObject for targetCommit
                    HeatObject heatObject = fileObject.getHeatObjectAtCommit(targetCommit);
                    if (heatObject == null) continue;

                    // TODO Need to consider heat metric here?
                    int heatLevel = heatObject.getHeatLevel();

                    // Generate color
                    Color fileHeatColor = HeatCalculationUtility.colorOfHeat(heatLevel);

                    // Convert color to hex
                    String colorString = fileHeatColor.toString();
                    String colorFormat = String.format("-fx-background-color: #%s", colorString.substring(colorString.indexOf("x") + 1));

                    // Add a pane (rectangle) package container
                    HeatFileComponent heatFileComponent = new HeatFileComponent(fileObject);
                    heatFileComponent.setStyle(colorFormat);
                    heatFileContainer.addNode(heatFileComponent);

                    setFileToolTip(fileObject, heatLevel, heatFileComponent);
                }

                heatFileContainer.setStyle("-fx-background-color: #BBBBBB");
                // Only add if we actually made children for it.
                if (heatFileContainer.getChildren().size() > 0) {
                    flowPane.getChildren().add(heatFileContainer);
                }
            }
        });

        System.out.println("Finished adding panes to the heat map.");
    }

    private void setContainerToolTip(@NotNull HeatFileContainer container, String info) {
        // Add a tooltip to the file pane
        Tooltip tooltip = new Tooltip(info);
        tooltip.setFont(Constants.TOOLTIP_FONT);
        tooltip.setShowDelay(Duration.seconds(0.5f));
        Tooltip.install(container, tooltip);
    }

    private void setFileToolTip(@NotNull FileObject fileObject, int heatLevel, HeatFileComponent heatFileComponent) {
        // Add a tooltip to the file pane
        String fileName = fileObject.getFilename();
        Tooltip tooltip = new Tooltip(String.format("%s\nHeat Level = %d", fileName, heatLevel));
        tooltip.setFont(Constants.TOOLTIP_FONT);
        tooltip.setShowDelay(Duration.seconds(0));
        Tooltip.install(heatFileComponent, tooltip);
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void newBranchSelected(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode) {
        refreshHeatMap(setOfFiles, targetCommit, groupingMode);
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