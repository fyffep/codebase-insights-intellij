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
        //Obtain file metrics by analyzing the code base
        try
        {
            //Calculate file sizes for every commit
            RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer();
            repositoryAnalyzer.attachCodebaseData(codeBase);

            //Now the Codebase contains all the data it needs
        }
        catch (IOException e)
        {
            Constants.LOG.error(e);
            Constants.LOG.error(e.getMessage());
        }
        Constants.LOG.info("Heat calculations complete. Number of files: " + codeBase.getActiveFileObjects().size());
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
