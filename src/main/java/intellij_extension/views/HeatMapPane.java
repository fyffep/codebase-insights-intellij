package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.utility.HeatCalculationUtility;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * A view that holds rectangles to represent files for a particular commit.
 * Each rectangle is colored based on the heat values assigned to its corresponding file.
 */
public class HeatMapPane extends FlowPane implements CodeBaseObserver {

    public HeatMapPane() {
        super();
        //Set margin around the heat boxes.
        this.setVgap(Constants.HEATMAP_VERTICAL_SPACING);
        this.setHgap(Constants.HEATMAP_HORIZONTAL_SPACING);
        this.setPadding(Constants.HEATMAP_PADDING);
        //Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
        //refreshHeatMap(model); //use latest appearance
    }

    /**
     * Clears the heat container, removing all child components.
     */
    public void clear() {
        getChildren().clear();
    }

    public void addNode(Node node) {
        getChildren().add(node);
    }


    /**
     * Clears the pane, then displays all files present in the latest commit.
     * Each file is represented by a rectangular pane.
     *
     * @param codebase the model whose fileMetricMap will be read to extract
     *                 file-to-heat data.
     */
    @Override
    public void refreshHeatMap(Codebase codebase) {
        System.out.println("Called refresh");
        Platform.runLater(() -> {
            clear();

            //Iterate through the files and add them to the screen
            Iterator<FileObject> fileObjectIterator = codebase.getActiveFileObjects().iterator();
            System.out.println("Updating the heatmap view");
            while (fileObjectIterator.hasNext()) {
                FileObject fileObject = fileObjectIterator.next();
                //String commitHash = fileObject.getCo(); // TODO maybe add a "current commit" field to the Codebase?
                int heatLevel = fileObject.getHeatObjectAtCommit("df24464eb2394991112ed60f5252ccf8c59da455").computeHeatLevel(); //retrieve or calculate heat level

                // Get commits associated with file
                ArrayList<Commit> associatedCommits = (ArrayList<Commit>) codebase.getActiveCommits().stream()
                        .filter(commit -> commit.getFileSet().contains(fileObject.getFilename()))
                        .collect(Collectors.toList());
                heatLevel = associatedCommits.size();

                //Generate color
                Color color = HeatCalculationUtility.colorOfHeat(heatLevel);

                //Convert color to hex
                String colorString = String.format("%02x%02x%02x", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));

                //Add a pane (rectangle) to the screen
                HeatFileComponent heatFileComponent = new HeatFileComponent(fileObject);
                heatFileComponent.setStyle("-fx-background-color: #" + colorString);
                this.addNode(heatFileComponent);

                //Add a tooltip to the file pane
                String fileName = fileObject.getFilename();
                Tooltip tooltip = new Tooltip(String.format("%s\nHeat Level = %d", fileName, heatLevel));
                tooltip.setFont(Constants.TOOLTIP_FONT);
                Tooltip.install(heatFileComponent, tooltip);

                System.out.println("Added a file pane for " + fileName + " with heat level " + heatLevel); //logger only works sometimes here
            }
        });
        System.out.println("Finished adding panes to the heat map.");
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void branchSelected() {
        // TODO
        //  Update heat map with latest commit in branch
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Nothing to do for this action
    }

    public void commitSelected(Commit commit) {
        // Nothing to do for this action
    }
}