package intellij_extension.controllers;

import intellij_extension.Constants;
import intellij_extension.models.CodeBase;
import intellij_extension.models.Commit;
import intellij_extension.models.Directory;
import intellij_extension.models.FileObject;
import intellij_extension.utility.HeatCalculationUtility;
import intellij_extension.utility.commithistory.CommitCountCalculator;
import intellij_extension.views.HeatMapPane;
import intellij_extension.views.unused.HeatFileComponent;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class HeatMapController implements IHeatMapController
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
        //Order the file metrics calculators to analyze the code base
        try
        {
            //Compute file size
            //TODO We may need to have the user select the project root
            Directory rootDirectory = new Directory("C:\\Users\\Pete\\Desktop\\team3-project\\src\\main");
            rootDirectory.parseDirectory();
            //Add the file size data to the map
            Commit activeCommit = codeBase.getActiveCommit();
            HashMap<String, FileObject> fileMetricMap = activeCommit.getFileMetricMap();
            rootDirectory.editFileMetricMap(fileMetricMap);

            //Add number of commits data to the map
            CommitCountCalculator commitCountCalculator = new CommitCountCalculator();
            commitCountCalculator.editFileMetricMap(fileMetricMap);

            //Now the activeCommit's fileMetricMap can be used to display the data
        }
        catch (IOException e)
        {
            Constants.LOG.error(e);
            Constants.LOG.error(e.getMessage());
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
            FileObject fileObject = fileMetricMap.get(fileName);
            int heatLevel = fileObject.computeHeatLevel();

            //Generate color
            String color = HeatCalculationUtility.colorOfHeat(heatLevel);

            //Add a pane (rectangle) to the screen
            HeatFileComponent heatFileComponent = new HeatFileComponent();
            heatFileComponent.setStyle("-fx-background-color: #" + color);
            heatMapPane.addNode(heatFileComponent);

            System.out.println("Added a file pane for "+fileName +" with heat level "+heatLevel+" and color "+color);
            //System.out.println("file has name=`"+fileName+"` filepath=`"+fileObject.getFilePath()+"`");
        }
    }

    public Node getView()
    {
        return this.heatMapPane;
    }
}
