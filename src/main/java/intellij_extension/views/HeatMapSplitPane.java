package intellij_extension.views;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;

public class HeatMapSplitPane extends SplitPane {

    // Top is Heat Map for a single commit + the history since the commit
    private HeatMapPane heatMapPane;

    // Bottom is for a selected file
    private SelectedFileTitledPane selectedFileView;

    public HeatMapSplitPane() {
        super();

        setOrientation(Orientation.VERTICAL);
        this.setWidth(250);

        // Top half
        heatMapPane = new HeatMapPane();
        this.getItems().add(heatMapPane);

        //Bottom half
        selectedFileView = new SelectedFileTitledPane();
        this.getItems().add(selectedFileView);
    }
}