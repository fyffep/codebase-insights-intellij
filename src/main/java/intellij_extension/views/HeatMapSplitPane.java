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

    // Top is banner plus tab view.
    private HeatMapPane heatMapPane;

    // Bottom is for a selected file
    private SelectedFileTitledPane selectedFileView;
    //endregion

    //region Constructors
    private HeatMapSplitPane() {
    }

    public HeatMapSplitPane(MainScene mainScene) {
        parent = new SplitPane();
        parent.setOrientation(Orientation.VERTICAL);
        parent.prefHeightProperty().bind(mainScene.heightProperty());

        // Top Half: heatMapPane (holds banner and tabbed view)
        heatMapPane = new HeatMapPane();
        parent.getItems().add(heatMapPane.getNode());

        // Bottom half: SelectedFileTitledPane
        selectedFileView = new SelectedFileTitledPane();
        parent.getItems().add(selectedFileView);
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return parent;
    }
    //endregion
}