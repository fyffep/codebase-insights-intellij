package intellij_extension.views;

import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.CodeBase;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;

public class HeatMapSplitPane extends SplitPane {

    // Top is Heat Map for a single commit + the history since the commit
    private HeatMapPane heatMapPane;

    // Bottom is for a selected file
    private SelectedFilePane selectedFileView;

    public HeatMapSplitPane() {
        super();

        setOrientation(Orientation.VERTICAL);

        // Top half
        heatMapPane = new HeatMapPane();
        this.getItems().add(heatMapPane);

        //Bottom half
        selectedFileView = new SelectedFilePane();
        this.getItems().add(selectedFileView);
    }
}