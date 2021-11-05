package intellij_extension.controllers;

import intellij_extension.filesize.RootDirectory;
import intellij_extension.models.CodeBase;
import intellij_extension.models.Commit;
import intellij_extension.models.FileObject;
import intellij_extension.views.HeatMapPane;
import intellij_extension.views.unused.HeatFileComponent;
import intellij_extension.views.unused.HeatMapContainer;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HeatMapController
{
    private HeatMapPane heatMapPane;
    private CodeBase codeBase;

    public HeatMapController(CodeBase codeBase)
    {
        this.codeBase = codeBase;

        //Create the view
        heatMapPane = new HeatMapPane();

        recalculateHeat();
        populateHeatMap();
    }

    public void clearHeatContainer()
    {
        heatMapPane.clear();
    }

    public void recalculateHeat()
    {
        //todo: order the file size measurer to create a list of file objects that will replace the current model

        try
        {
            //Compute file size
            //TODO We may need to have the user select the project root
            RootDirectory rootDirectory =new RootDirectory("C:\\Users\\Pete\\Desktop\\team3-project\\src\\main");
            rootDirectory.parsedirectory();
            rootDirectory.displayDetails();//TEMP

            //Add the file size data to the map
            Commit activeCommit = codeBase.getActiveCommit();
            HashMap<String, FileObject> fileMetricMap = activeCommit.getFileMetricMap();
            rootDirectory.editFileMetricMap(fileMetricMap);
            System.out.println("Map size: "+fileMetricMap.size());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Heat calculations complete.");
    }

    public void populateHeatMap()
    {
        Commit activeCommit = codeBase.getActiveCommit();
        if (activeCommit == null)
        {
            System.out.println("Cannot populate the heat map since no commit is selected.");
        }
        HashMap<String, FileObject> fileMetricMap = codeBase.getActiveCommit().getFileMetricMap();

        //Iterate through the files and add them to the screen
        Iterator<String> keyIterator = fileMetricMap.keySet().iterator();
        while (keyIterator.hasNext())
        {
            String fileName = keyIterator.next();
            FileObject fileObject = fileMetricMap.get(fileName); //unused

            //Generate color (TODO -- this is just a placeholder version that arbitrarily chooses a red color)
            fileObject.computeHeatLevel(); //TEMP should be moved?
            int redValue = (int) ((fileObject.computeHeatLevel() / 10.0) * 255);
            String color = String.format("%02x", redValue) + "0000";

            //Add a pane (rectangle) to the screen
            HeatFileComponent heatFileComponent = new HeatFileComponent();
            heatFileComponent.setStyle("-fx-background-color: #" + color);
            heatMapPane.addNode(heatFileComponent);

            System.out.println("Added a file pane for "+fileName +" with heat level "+fileObject.getHeatLevel());
        }
    }

    public Node getView()
    {
        return this.heatMapPane;
    }
}
