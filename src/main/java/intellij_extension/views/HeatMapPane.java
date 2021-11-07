package intellij_extension.views;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class HeatMapPane extends FlowPane {

    public HeatMapPane() {
        super();
        //Set margin around the heat boxes.
        //The value 10 is arbitrary.
        this.setVgap(10);
        this.setHgap(10);
        this.setPadding(new Insets(10, 10, 10, 10));
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
}