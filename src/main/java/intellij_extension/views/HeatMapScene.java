package intellij_extension.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HeatMapScene extends Scene implements Initializable
{
    @FXML
    public FlowPane flowPnHeatFileContainer;


    public HeatMapScene(Parent root) {
        super(root);
    }


    /**
     * Clears the heat container, removing the HeatFileComponents.
     */
    public void clear()
    {
        flowPnHeatFileContainer.getChildren().clear();
    }

    /**
     * Clears the heat container, removing the HeatFileComponents.
     */
    public void addNodeToHeatFileContainer(Node node)
    {
        flowPnHeatFileContainer.getChildren().add(node);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Calling init");
        //flowPnHeatFileContainer.setStyle("-fx-background-color: #00AA00"); //TEMP
    }
}
