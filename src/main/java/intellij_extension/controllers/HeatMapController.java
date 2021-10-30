package intellij_extension.controllers;

import intellij_extension.models.FileObject;
import intellij_extension.views.unused.HeatFileComponent;
import intellij_extension.views.unused.HeatMapContainer;
import javafx.scene.Node;

import java.util.List;

public class HeatMapController
{
    private HeatMapContainer heatMapContainer;
    private List<FileObject> heatList;

    public HeatMapController(List<FileObject> heatList)
    {
        this.heatList = heatList; //model

        //Create the view
        heatMapContainer = new HeatMapContainer();

        populateHeatMap();
    }

    public void clearHeatContainer()
    {
        heatMapContainer.clear();
    }

    public void populateHeatMap()
    {
        for (FileObject fileObject : heatList)
        {
            HeatFileComponent heatFileComponent = new HeatFileComponent();
            heatFileComponent.setStyle("-fx-background-color: #AA0066");
            heatMapContainer.addNode(heatFileComponent);
        }
    }

    public Node getView()
    {
        return this.heatMapContainer;
    }
}
