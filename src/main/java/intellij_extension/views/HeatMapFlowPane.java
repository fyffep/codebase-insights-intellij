package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.Constants.FilterMode;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.Constants.HeatMetricOptions;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.models.redesign.HeatObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.utility.HeatCalculationUtility;
import intellij_extension.views.interfaces.IContainerView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import static intellij_extension.Constants.TOOLTIP_FORMAT;
import static intellij_extension.Constants.TOP_FILE_WARNING;
import static intellij_extension.Constants.BLANK;

import java.util.*;

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
    private FilterMode filteringMode;
    private int filterMax = Constants.MAX_NUMBER_OF_FILTERING_FILES;
    private GroupingMode groupingMode;
    // For filtering
    private PriorityQueue<HeatFileComponent> topHeatFileComponents;
    //endregion

    //region Constructors
    public HeatMapFlowPane() {
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

    public void setGroupingMode(GroupingMode groupingMode) {
        this.groupingMode = groupingMode;
    }

    public void setFilteringModeDefaults() {
        this.filteringMode = Constants.DEFAULT_FILTERING;
        this.filterMax = Constants.MAX_NUMBER_OF_FILTERING_FILES;
    }

    public void setFilteringMode(FilterMode filterMode, int numberOfFiles) {
        this.filteringMode = filterMode;
        this.filterMax = numberOfFiles;
        // Radio buttons are weird and this is needed to guard against them
        if(topHeatFileComponents != null) {
            filterHeatMap();
        }
    }

    private void filterHeatMap() {
        // Clear the flowPane because we only want the top X files in it, but currently it has all files
        flowPane.getChildren().clear();

        // Iterator over topHeatFiles collected from refreshHeatMap(...)
        // (not sorted/organized by package anymore, we lost that)
        int index = 0;
        for(HeatFileComponent heatFile: topHeatFileComponents) {
            // Get the heatFile's container
            HeatFileContainer currentContainer = heatFile.getContainer();
//            System.out.printf("currentContainer %s%n", currentContainer.hashCode());

            // Check if already added
            if(!flowPane.getChildren().contains(currentContainer)) {
                // Clear container
                currentContainer.getChildren().clear();
                // Add container to flowPane
                flowPane.getChildren().add(heatFile.getContainer());
            }

            // Re-add the heatFile back to the parent
            currentContainer.getChildren().add(heatFile);

            // Break if we reached the max
            if(index + 1 == filterMax)
                break;

            // Continue otherwise
            index++;
        }
        System.out.printf("Index: %s%n", index);

        flowPane.layout();
    }

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
    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode, HeatMetricOptions heatMetricOption) {
        // If not our grouping mode, then don't do anything
        if (!this.groupingMode.equals(groupingMode)) return;

        // Set up for X top files
        topHeatFileComponents = new PriorityQueue<>(Comparator.comparing(HeatFileComponent::getFileHeatLevel).reversed());

        Platform.runLater(() -> {
            flowPane.getChildren().clear();
            int maxFileCounter = 0;

            for (Map.Entry<String, TreeSet<FileObject>> entry : setOfFiles.entrySet()) {
                // Get package name
                String groupingKey = entry.getKey();
                // Create a container for it
                HeatFileContainer heatFileContainer = new HeatFileContainer(groupingKey);
                heatFileContainer.setStyle("-fx-background-color: #BBBBBB");

                // Add files to the package container
                for (FileObject fileObject : entry.getValue()) {
                    // Get HeatObject/Level for targetCommit
                    HeatObject heatObject = fileObject.getHeatObjectAtCommit(targetCommit);
                    if (heatObject == null) continue;
                    int heatLevel = heatObject.getHeatLevel();

                    // Generate color
                    Color fileHeatColor = HeatCalculationUtility.colorOfHeat(heatLevel);

                    // Convert color to hex
                    String colorString = fileHeatColor.toString();
                    String colorFormat = String.format("-fx-background-color: #%s", colorString.substring(colorString.indexOf("x") + 1));

                    // Add a pane (rectangle) package container
                    HeatFileComponent heatFileComponent = new HeatFileComponent(fileObject, heatLevel, heatFileContainer);
                    heatFileComponent.setStyle(colorFormat);
                    heatFileContainer.addNode(heatFileComponent);
                    topHeatFileComponents.add(heatFileComponent);

                    // Adds the glowing behaviour to the file component iff the file is in the top 20
                    if (maxFileCounter < Constants.MAX_NUMBER_OF_FILTERING_FILES) {
                        heatFileComponent.setFadeTransition();
                        maxFileCounter++;
                    }

                    setFileToolTip(fileObject, heatLevel, groupingKey, fileObject.getHeatMetricString(heatObject, heatMetricOption), heatFileComponent);
                }

                // Only add if we actually made children for it.
                if (heatFileContainer.getChildren().size() > 0) {
                    flowPane.getChildren().add(heatFileContainer);
                }
            }

//            int index = 0;
//            PriorityQueue<HeatFileComponent> temp = new PriorityQueue<>(Comparator.comparing(HeatFileComponent::getFileHeatLevel).reversed());
//            Iterator<HeatFileComponent> topHeatFileComponentIterator = topHeatFileComponents.iterator();
//            while(topHeatFileComponentIterator.hasNext() && index < Constants.MAX_NUMBER_OF_FILTERING_FILES) {
//                HeatFileComponent component = topHeatFileComponentIterator.next();
//                temp.add(component);
//                index++;
//            }
//            topHeatFileComponents = temp;
            if(filteringMode == FilterMode.X_FILES) {
                filterHeatMap();
            }

            System.out.println("Finished adding panes to the heat map.");
        });
    }

    private void setFileToolTip(@NotNull FileObject fileObject, int heatLevel, String groupName, String heatMetricString, HeatFileComponent heatFileComponent) {
        // Add a tooltip to the file pane
        String fileName = fileObject.getFilename();
        Tooltip tooltip = new Tooltip(getToolTipMessage(fileName, heatLevel, groupName, heatMetricString, heatFileComponent));
        tooltip.setFont(Constants.TOOLTIP_FONT);
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.seconds(10));
        Tooltip.install(heatFileComponent, tooltip);
    }

    private String getToolTipMessage(String fileName, int heatLevel, String groupName, String heatMetricString, HeatFileComponent heatFileComponent) {
        return String.format(TOOLTIP_FORMAT, getWarningMessage(heatFileComponent), fileName, heatLevel, heatMetricString, groupName);
    }

    // Sets a top 20 warning message in the tool tip if file is one of the top 20 files
    private String getWarningMessage(HeatFileComponent heatFileComponent) {
        if (heatFileComponent.hasFadeTransition()) return TOP_FILE_WARNING;
        else return BLANK;
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