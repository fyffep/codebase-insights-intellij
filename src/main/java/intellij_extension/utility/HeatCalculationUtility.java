package intellij_extension.utility;

import intellij_extension.Constants;
import intellij_extension.models.FileObject;
import javafx.scene.paint.Color;

import static intellij_extension.Constants.HEAT_MAX;
import static intellij_extension.Constants.HEAT_MIN;

public class HeatCalculationUtility //can be renamed if adding more methods
{
    /**
     * Converts the input heat level to a color.
     * Higher heat levels are indicated by higher intensities of red.
     * @param heatLevel a number from 1 to 10
     * @return a hexadecimal String of the form "FFFFFF" representing a color
     */
    public static String colorOfHeat(int heatLevel)
    {
        Color choice;
        switch (heatLevel)
        {
            case 1:
                choice = Constants.HEAT_COLOR_1;
                break;
            case 2:
                choice = Constants.HEAT_COLOR_2;
                break;
            case 3:
                choice = Constants.HEAT_COLOR_3;
                break;
            case 4:
                choice = Constants.HEAT_COLOR_4;
                break;
            case 5:
                choice = Constants.HEAT_COLOR_5;
                break;
            case 6:
                choice = Constants.HEAT_COLOR_6;
                break;
            case 7:
                choice = Constants.HEAT_COLOR_7;
                break;
            case 8:
                choice = Constants.HEAT_COLOR_8;
                break;
            case 9:
                choice = Constants.HEAT_COLOR_9;
                break;
            case 10:
                choice = Constants.HEAT_COLOR_10;
                break;
            default:
                choice = Color.BLACK;
        }
        //Convert color to hex
        return String.format("%02x%02x%02x", (int)(choice.getRed() * 255), (int)(choice.getGreen() * 255), (int)(choice.getBlue() * 255));
    }


    /**
     * Returns the level of heat caused by the fileObject's file size.
     * @param fileObject this should have its lineCount already assigned
     */
    public static int calculateHeatForFileSize(FileObject fileObject)
    {
        int heatLevel;

        long lineCount = fileObject.getLineCount();
        if (lineCount < 100)
        {
            heatLevel = HEAT_MIN;
        }
        //Give 1 point of heat for every hundred lines
        else if (lineCount > 100 && lineCount < 1000)
        {
            heatLevel = (int) Math.round(lineCount / 100.0);
        }
        else
        {
            heatLevel = HEAT_MAX;
        }

        if (heatLevel > HEAT_MAX)
            heatLevel = HEAT_MAX;

        return heatLevel;
    }


    /**
     * Returns the level of heat caused by the fileObject's number of commits
     * @param fileObject this should have its lineCount already assigned
     */
    public static int calculateHeatForNumberOfCommits(FileObject fileObject)
    {
        int heatLevel;

        //TODO this does not take commit **history** into account. It needs to consider how a file's heat should decrease as its commit frequency decreases

        int commitCount = fileObject.getNumberOfCommits();
        heatLevel = commitCount; //TEMP

        if (heatLevel > HEAT_MAX)
            heatLevel = HEAT_MAX;

        return heatLevel;
    }
}
