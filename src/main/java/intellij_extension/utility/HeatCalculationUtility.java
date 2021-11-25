package intellij_extension.utility;

import intellij_extension.Constants;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.models.redesign.HeatObject;
import intellij_extension.utility.filesize.FileSizeCalculator;
import javafx.scene.paint.Color;
import org.eclipse.jgit.lib.ObjectLoader;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HeatCalculationUtility
{
    private HeatCalculationUtility() {
        //This is a utility class
    }


    /**
     * Converts the input heat level to a color.
     * Higher heat levels are indicated by higher intensities of red.
     * @param heatLevel a number from 1 to 10
     * @return a hexadecimal String of the form "FFFFFF" representing a color
     */
    public static Color colorOfHeat(int heatLevel) {
        // Get percentage
        float heatPercentage = heatLevel / ((float) Constants.HEAT_MAX);

        // Get color based on percentage 0 = completely BLUE 1 = Completely RED
        Color heatColor = Constants.HEAT_MIN_COLOR.interpolate(Constants.HEAT_MAX_COLOR, heatPercentage);

        return heatColor;
    }


    /**
     * Returns the level of heat caused by the HeatObject's file size.
     *
     * @param heatObject this should have its lineCount already assigned
     */
    public static int calculateHeatForFileSize(@NotNull HeatObject heatObject) {
        int heatLevel;

        long lineCount = heatObject.getLineCount();
        if (lineCount < 100) {
            heatLevel = Constants.HEAT_MIN;
        }
        //Give 1 point of heat for every hundred lines
        else if (lineCount > 100 && lineCount < 1000) {
            heatLevel = (int) Math.round(lineCount / 100.0);
        } else {
            heatLevel = Constants.HEAT_MAX;
        }

        if (heatLevel > Constants.HEAT_MAX)
            heatLevel = Constants.HEAT_MAX;

        return heatLevel;
    }


    public static void assignHeatLevelsFileSize(Codebase codebase)
    {
        final int REQUIRED_NUM_COMMITS_WITHOUT_CHANGING = 5; //the number of consecutive commits where no increase in a file's size is recorded needed in order to reduce the accumulated heat level.
        final int REQUIRED_SIZE_CHANGE = 200;
        final int SIZE_INCREASE_HEAT_CONSEQUENCE = 2; //how much the heat increases when the file size increases
        final int SIZE_DECREASE_HEAT_CONSEQUENCE = -1; //how much the heat decreases when the file size decreases
        final int SIZE_NO_CHANGE_HEAT_CONSEQUENCE = -1; //how much the heat decreases if the file size stays the same for long enough

        Set<FileObject> fileObjectSet = codebase.getActiveFileObjects();
        for (FileObject fileObject : fileObjectSet)
        {
            //The oldest commits are at the front of the LinkedHashMap
            LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap = fileObject.getCommitHashToHeatObjectMap();

            HeatObject lastHeatObject = null;
            int numberOfConsecutiveCommitsWithNoSizeIncrease = 0;

            for (Map.Entry<String, HeatObject> commitToHeatObjectEntry : commitHashToHeatObjectMap.entrySet())
            {
                HeatObject newerHeatObject = commitToHeatObjectEntry.getValue();
                if (lastHeatObject != null)
                {
                    newerHeatObject.setHeatLevel(lastHeatObject.getHeatLevel()); //use previous heat, then modify

                    //If the file size increased at all, incur 2 heat
                    long oldFileSize = lastHeatObject.getFileSize();
                    long newFileSize = newerHeatObject.getFileSize();
                    if (fileObject.getFilename().equals("HeatMapPane.java"))
                    {
                        System.out.println("New Hash: "+commitToHeatObjectEntry.getKey());
                        System.out.println("oldFileSize: "+oldFileSize);
                        System.out.println("newFileSize: "+newFileSize);
                    }
                    if (newFileSize > oldFileSize)
                    {
                        newerHeatObject.setHeatLevel(newerHeatObject.getHeatLevel() + SIZE_INCREASE_HEAT_CONSEQUENCE);
                        numberOfConsecutiveCommitsWithNoSizeIncrease = 0;
                    }
                    //File size decrease -> lose 1 heat
                    else if (oldFileSize - newFileSize >= REQUIRED_SIZE_CHANGE)
                    {
                        newerHeatObject.setHeatLevel(newerHeatObject.getHeatLevel() + SIZE_DECREASE_HEAT_CONSEQUENCE);
                        numberOfConsecutiveCommitsWithNoSizeIncrease++;
                    }
                    //File size stayed equal ↓
                    else
                    {
                        numberOfConsecutiveCommitsWithNoSizeIncrease++;

                        //If file went unchanged for long enough, the heat improved
                        if (numberOfConsecutiveCommitsWithNoSizeIncrease >= REQUIRED_NUM_COMMITS_WITHOUT_CHANGING)
                        {
                            newerHeatObject.setHeatLevel(newerHeatObject.getHeatLevel() + SIZE_NO_CHANGE_HEAT_CONSEQUENCE);
                            numberOfConsecutiveCommitsWithNoSizeIncrease = 0;
                        }
                    }
                }
                else
                {
                    newerHeatObject.setHeatLevel(Constants.HEAT_MIN); //No in/decreases in file size yet
                }
                if (fileObject.getFilename().equals("HeatMapPane.java"))
                    System.out.println("Heat: "+newerHeatObject.getHeatLevel()+"\n");

                lastHeatObject = newerHeatObject;
            }
        }
    }
}
