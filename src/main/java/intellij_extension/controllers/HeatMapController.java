package intellij_extension.controllers;

import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.progress.ProgressIndicator;
import intellij_extension.Constants;
import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.utility.RepositoryAnalyzer;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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
            repositoryAnalyzer.attachBranchNameList(codeBase); //FIXME bad place to obtain the list of branches for a Codebase?
            codeBase.branchSelected("development"); //FIXME this should select the first branch it finds, whether "master" or "main"
            repositoryAnalyzer.attachCodebaseData(codeBase);

            //Now the Codebase contains all the data it needs
        }
        catch (IOException | GitAPIException e)
        {
            System.out.println("There was an error: ");
            e.printStackTrace();
            System.out.println(e); //logger fails here
            System.out.println(e.getMessage());
        }
        System.out.println("Heat calculations complete. Number of files: " + codeBase.getActiveFileObjects().size());
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
