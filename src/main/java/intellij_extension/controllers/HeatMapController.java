package intellij_extension.controllers;

import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.progress.ProgressIndicator;
import intellij_extension.Constants;
import intellij_extension.models.CodeBase;
import intellij_extension.models.Commit;
import intellij_extension.models.Directory;
import intellij_extension.models.FileObject;
import intellij_extension.utility.commithistory.CommitCountCalculator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public class HeatMapController extends PreloadingActivity implements IHeatMapController
{
    private CodeBase codeBase;

    public HeatMapController()
    {
        this.codeBase = CodeBase.getInstance();
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
        Constants.LOG.info("Heat calculations complete. Number of files: " + codeBase.getActiveCommit().getFileMetricMap().size());
    }


    @Override
    public void preload(@NotNull ProgressIndicator indicator)
    {
        System.out.println("Preloading HeatMapController..."); //logger fails here
        recalculateHeat();
        codeBase.notifyObservers();
        System.out.println("Finished preloading HeatMapController.");
    }
}
