package intellij_extension.controllers;

import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.progress.ProgressIndicator;
import intellij_extension.Constants;
import intellij_extension.models.Commit;
import intellij_extension.models.Directory;
import intellij_extension.models.FileObject;
import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.CommitV2;
import intellij_extension.utility.RepositoryAnalyzer;
import intellij_extension.utility.commithistory.CommitCountCalculator;
import intellij_extension.utility.commithistory.JGitHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public class HeatMapController extends PreloadingActivity implements IHeatMapController
{
    private CodebaseV2 codeBase;

    public HeatMapController()
    {
        this.codeBase = CodebaseV2.getInstance();
    }

    public void recalculateHeat()
    {
        /*/Order the file metrics calculators to analyze the code base
        try
        {
            //Locate project
            final String projectRootPath = JGitHelper.locateProjectRoot();
            assert projectRootPath != null;

            //Compute file size
            Directory rootDirectory = new Directory(projectRootPath);
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
        Constants.LOG.info("Heat calculations complete. Number of files: " + codeBase.getActiveCommit().getFileMetricMap().size());*/


        //Order the file metrics calculators to analyze the code base
        try
        {
            //Locate project
            final String projectRootPath = JGitHelper.locateProjectRoot();
            assert projectRootPath != null;

            //Retrieve all commits

            //Calculate file sizes for every commit
            RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer();
            repositoryAnalyzer.attachCodebaseData(codeBase);
        }
        catch (IOException e)
        {
            Constants.LOG.error(e);
            Constants.LOG.error(e.getMessage());
        }
        Constants.LOG.info("Heat calculations complete. Number of files: "); //TODO
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
