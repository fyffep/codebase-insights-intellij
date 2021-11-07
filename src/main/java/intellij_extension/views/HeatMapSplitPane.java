package intellij_extension.views;

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
        SelectedFileVBox selectedFileVBox = new SelectedFileVBox();
        selectedFileView = new SelectedFilePane(selectedFileVBox);
        this.getItems().add(selectedFileView);
    }
}