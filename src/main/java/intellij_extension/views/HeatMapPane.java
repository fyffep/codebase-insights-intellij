package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.models.CodeBase;
import intellij_extension.models.Commit;
import intellij_extension.models.FileObject;
import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.FileObjectV2;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.utility.HeatCalculationUtility;
import intellij_extension.views.unused.HeatFileComponent;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A view that holds rectangles to represent files for a particular commit.
 * Each rectangle is colored based on the heat values assigned to its corresponding file.
 */
public class HeatMapPane extends FlowPane implements CodeBaseObserver
{

    public HeatMapPane() {
        super();
        //Set margin around the heat boxes.
        //The value 10 is arbitrary.
        this.setVgap(10);
        this.setHgap(10);
        this.setPadding(new Insets(10, 10, 10, 10));

        //Register self as an observer of the model
        CodebaseV2 model = CodebaseV2.getInstance();
        model.registerObserver(this);
        refresh(model); //use latest appearance
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
     * @param codeBase the model whose fileMetricMap will be read to extract
     *                 file-to-heat data.
     */
    public void refresh(CodeBase codeBase)
    {
        Platform.runLater(() -> {
            clear();
            Commit activeCommit = codeBase.getActiveCommit();
            if (activeCommit == null) {
                Constants.LOG.info("Cannot populate the heat map since no commit is selected.");
            }
            HashMap<String, FileObject> fileMetricMap = codeBase.getActiveCommit().getFileMetricMap();
            Constants.LOG.info("Placing " + fileMetricMap.size() + " file panes");

            //Iterate through the files and add them to the screen
            Iterator<String> keyIterator = fileMetricMap.keySet().iterator();
            while (keyIterator.hasNext()) {
                String fileName = keyIterator.next();
                FileObject fileObject = fileMetricMap.get(fileName);
                int heatLevel = fileObject.computeHeatLevel();

                //Generate color
                Color color = HeatCalculationUtility.colorOfHeat(heatLevel);

                //Add a pane (rectangle) to the screen
                HeatFileComponent heatFileComponent = new HeatFileComponent();
                heatFileComponent.setStyle("-fx-background-color: #" + color);
                this.addNode(heatFileComponent);

                //Add a tooltip to the file pane
                Tooltip tooltip = new Tooltip(String.format("%s\nHeat Level = %d", fileName, heatLevel));
                tooltip.setFont(Constants.TOOLTIP_FONT);
                Tooltip.install(heatFileComponent, tooltip);

                System.out.println("Added a file pane for " + fileName + " with heat level " + heatLevel); //logger only works sometimes here
            }
        });
    }

    @Override
    public void refresh(CodebaseV2 codebase)
    {
        System.out.println("Called refresh");
        Platform.runLater(() -> {
            clear();

            //Iterate through the files and add them to the screen
            Iterator<FileObjectV2> fileObjectIterator = codebase.getActiveFileObjects().iterator();
            System.out.println("Updating the heatmap view");
            while (fileObjectIterator.hasNext()) {
                FileObjectV2 fileObject = fileObjectIterator.next();
                //String commitHash = fileObject.getCo(); //TODO maybe add a "current commit" field to the Codebase?
                int heatLevel = fileObject.getHeatObjectAtCommit("0d124558bb1000395288d12299d7d290aec61521").computeHeatLevel(); //retrieve or calculate heat level

                //Generate color
                Color color = HeatCalculationUtility.colorOfHeat(heatLevel);
                //Convert color to hex
                String colorString = String.format("%02x%02x%02x", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));

                //Add a pane (rectangle) to the screen
                HeatFileComponent heatFileComponent = new HeatFileComponent();
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
    }
}