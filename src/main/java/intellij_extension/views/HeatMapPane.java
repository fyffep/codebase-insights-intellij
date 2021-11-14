package intellij_extension.views;

import intellij_extension.Constants;
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

import java.util.Iterator;

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
     * @param codebase the model whose HeatObjects will be read at a particular commit
     */
    @Override
    public void refresh(CodebaseV2 codebase)
    {
        Platform.runLater(() -> {
            clear();

            //Iterate through the files and add them to the screen
            Iterator<FileObjectV2> fileObjectIterator = codebase.getActiveFileObjects().iterator();
            while (fileObjectIterator.hasNext()) {
                FileObjectV2 fileObject = fileObjectIterator.next();
                //String commitHash = fileObject.getCurrentCommit...(); //TODO maybe add a "current commit" field to the Codebase?
                int heatLevel = fileObject.getHeatObjectAtCommit("0b68f5637eb1edeb18adbe5d275f3d26ff380bad").computeHeatLevel(); //retrieve or calculate heat level

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
        System.out.println("Finished populating the HeatMapPane.");


        /*final String HASH_TO_DISPLAY = "201408af066ef1f6b2515c473ddfa9aae822698c"; //temporary
        System.out.println("Called refresh");
        Platform.runLater(() -> {
            clear();

            //Iterate through the files and add them to the screen
            Iterator<FileObjectV2> fileIterator = codebase.getActiveFileObjects().iterator();
            while (fileIterator.hasNext())
            {
                FileObjectV2 fileObject = fileIterator.next();
                for (String hash : fileObject.getCommitHashToHeatObjectMap().keySet())
                {
                    if (hash.equals(HASH_TO_DISPLAY))
                    {
                        int heatLevel = fileObject.getHeatObjectAtCommit(HASH_TO_DISPLAY).computeHeatLevel(); //calculate heat level

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
                }
            }
        });*/
    }
}