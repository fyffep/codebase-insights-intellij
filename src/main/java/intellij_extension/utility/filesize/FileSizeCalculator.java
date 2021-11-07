package intellij_extension.utility.filesize;

import intellij_extension.models.FileObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static intellij_extension.Constants.HEAT_MAX;
import static intellij_extension.Constants.HEAT_MIN;

public class FileSizeCalculator
{
    private FileSizeCalculator() {
        //This is a utility class
    }


    /**
     * Computes and assigns a line count to the input file
     * @param fileObject
     */
    public static void assignLineCount(FileObject fileObject)
    {
        //Compute the line count of the file
        long lineCount=0;
        try
        {
            BufferedReader buffer = new BufferedReader(new FileReader(fileObject.getFilePath()));
            // SonarQube bug recommendation. "Use or store the value returned from "readLine" instead of throwing it away."
            String line = buffer.readLine();
            while(line != null)
            {
                lineCount++;
            }
            buffer.close();
        }
        catch(Exception e)
        {
            System.err.println("Unable to read file: " + fileObject.getFilePath());
            System.exit(0);
        }
        //Assign the line count
        fileObject.setLineCount(lineCount);
    }

    /**
     * Computes and assigns a file size (in bytes)
     * to the input FileObject.
     */
    public static void assignFileSize(FileObject fileObject)
    {
        File file = new File(fileObject.getFilePath());
        fileObject.setFileSize(file.length());
    }

    /**
     * Returns the level of heat caused by the fileObject's file size.
     * @param fileObject this should have its lineCount already assigned
     */
    public static int calculateHeat(FileObject fileObject)
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
}
