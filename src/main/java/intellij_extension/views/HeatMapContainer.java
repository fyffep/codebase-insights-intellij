package intellij_extension.views;

import intellij_extension.models.FileObject;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class HeatMapContainer extends FlowPane
{
    public HeatMapContainer()
    {
        //Set margin around the heat boxes.
        //The value 10 is arbitrary.
        this.setVgap(10);
        this.setHgap(10);
        this.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Clears the heat container, removing the HeatFileComponents.
     */
    public void clear()
    {
        getChildren().clear();
    }

    public void addNode(Node node)
    {
        getChildren().add(node);
    }

    public void populate(List<FileObject> heatList)
    {
        for (FileObject fileObject : heatList)
        {
            HeatFileComponent heatFileComponent = new HeatFileComponent();
            heatFileComponent.setStyle("-fx-background-color: #AA0066");
            addNode(heatFileComponent);
        }
    }
}
