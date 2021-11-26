package intellij_extension.views;

import intellij_extension.views.interfaces.IContainerView;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class HeatMapPane implements IContainerView {

    //region Vars
    // Basically this class' main node
    private Pane parent;

    // Holds HeatMapPane and CommitGroupingPane
    // private HeatMapTabbedPane heatMapTabbedPane;

    // Heat Map for a single commit + the history up to the commit
    // TODO move out when HeatMapTabbedPane is ready
    private HeatMapFlowPane heatMapFlowPane;
    //endregion

    //region Constructors
    public HeatMapPane() {
        parent = new Pane();

        // Top half: insert HeatMapPane inside an AnchorPane inside a ScrollPane
        // TODO this will eventually become a TabView
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.prefWidthProperty().bind(parent.widthProperty());
        scrollPane.maxWidthProperty().bind(parent.widthProperty());
        parent.getChildren().add(scrollPane);

        // Create ScrollPane and the AnchorPane inside it
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());
        scrollPane.setContent(anchorPane);

        // Create HeatMapFlowPane
        heatMapFlowPane = new HeatMapFlowPane();
        FlowPane flowPane = (FlowPane)heatMapFlowPane.getNode();
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.getChildren().add(heatMapFlowPane.getNode());
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return parent;
    }
    //endregion
}
