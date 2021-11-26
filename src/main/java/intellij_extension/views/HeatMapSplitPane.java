package intellij_extension.views;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

public class HeatMapSplitPane extends SplitPane {

    // Top is Heat Map for a single commit + the history since the commit
    private HeatMapPane heatMapPane;

    // Bottom is for a selected file
    private SelectedFileTitledPane selectedFileView;

    public HeatMapSplitPane() {
        super();

        setOrientation(Orientation.VERTICAL);

        // Top half: insert HeatMapPane inside an AnchorPane inside a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.prefWidthProperty().bind(this.widthProperty());
        this.getItems().add(scrollPane);

        //Create ScrollPane and the AnchorPane inside it
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());
        scrollPane.setContent(anchorPane);
        scrollPane.prefWidthProperty().bind(this.widthProperty());
        scrollPane.maxWidthProperty().bind(this.widthProperty());

        //Create HeatMapPane
        heatMapPane = new HeatMapPane();
        heatMapPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.getChildren().add(heatMapPane);


        //Bottom half: SelectedFileTitledPane
        selectedFileView = new SelectedFileTitledPane();
        this.getItems().add(selectedFileView);
    }
}