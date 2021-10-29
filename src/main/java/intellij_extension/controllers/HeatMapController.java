package intellij_extension.controllers;

import intellij_extension.models.FileToHeatMap;
import intellij_extension.models.HeatObject;
import intellij_extension.views.HeatFileComponent;
import intellij_extension.views.HeatMapScene;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HeatMapController
{
    private HeatMapScene heatMapScene;
    private FileToHeatMap model;

    public HeatMapController(HeatMapScene heatMapScene, FileToHeatMap model)
    {
        this.model = model;
        this.heatMapScene = heatMapScene;

        populateWithHeat();
    }

    public void clearHeatContainer()
    {
        heatMapScene.clear();
    }

    public void populateWithHeat()
    {
        //Should this be in a presenter class?

        HashMap<File, List<HeatObject>> heatMap = model.getMap();
        Iterator<File> heatMapIterator = heatMap.keySet().iterator();
        while (heatMapIterator.hasNext())
        {
            File heatFile = heatMapIterator.next();
            double heatAverage = model.getHeatAverage(heatFile);

            HeatFileComponent heatFileComponent = new HeatFileComponent();
            heatFileComponent.setStyle("-fx-background-color: #BB0011");
            heatMapScene.addNodeToHeatFileContainer(heatFileComponent);
            System.out.println("Added 1 component");
        }
    }
}
