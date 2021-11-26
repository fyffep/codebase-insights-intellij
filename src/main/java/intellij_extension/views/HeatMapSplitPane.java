package intellij_extension.views;

import intellij_extension.views.interfaces.IContainerView;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

public class HeatMapSplitPane implements IContainerView {

    //region Vars
    // Basically this class' main node
    private SplitPane parent;
    private MainScene mainScene;

    // Top is Heat Map for a single commit + the history since the commit
    private HeatMapFlowPane heatMapFlowPane;

    // Bottom is for a selected file
    private SelectedFileTitledPane selectedFileView;
    //endregion

    //region Constructors
    private HeatMapSplitPane() {
    }

    public HeatMapSplitPane(MainScene mainScene) {
        this.mainScene = mainScene;

        parent = new SplitPane();
        parent.setOrientation(Orientation.VERTICAL);

        // Top half: insert HeatMapPane inside an AnchorPane inside a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.prefWidthProperty().bind(parent.widthProperty());
        parent.getItems().add(scrollPane);

        // Create ScrollPane and the AnchorPane inside it
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());
        scrollPane.setContent(anchorPane);
        scrollPane.prefWidthProperty().bind(parent.widthProperty());
        scrollPane.maxWidthProperty().bind(parent.widthProperty());

        // Create HeatMapPane
        heatMapFlowPane = new HeatMapFlowPane();
        heatMapFlowPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.getChildren().add(heatMapFlowPane);


        //Bottom half: SelectedFileTitledPane
        selectedFileView = new SelectedFileTitledPane();
        parent.getItems().add(selectedFileView);
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return parent;
    }

    @Override
    public void setProperties() {
        parent.prefHeightProperty().bind(mainScene.heightProperty());
    }
    //endregion
}