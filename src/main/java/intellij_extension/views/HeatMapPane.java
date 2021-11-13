package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.models.CodeBase;
import intellij_extension.models.Commit;
import intellij_extension.models.FileObject;
import intellij_extension.models.redesign.CodebaseV2;
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
        CodeBase model = CodeBase.getInstance();
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
    public void refresh(CodebaseV2 codeBase) {
        //TODO
    }
}