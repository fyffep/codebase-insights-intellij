package intellij_extension.utility.filesize;

import intellij_extension.Constants;
import intellij_extension.models.FileObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


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
             String line = null;
            while ((line = buffer.readLine()) != null)
            {
                lineCount++;
            }
            buffer.close();
        }
        catch(Exception e)
        {
            Constants.LOG.error("Unable to read file: " + fileObject.getFilePath());
            // TODO what does this mean for a plugin project??
            System.exit(0);
        }

        //Assign the line count
        fileObject.setLineCount(lineCount);
    }

    /**
     * Gets the line count of a file at the given filePath
     * @param filePath
     */
    // TODO: Need to find a way to get the number of lines through commit history
    //  The main question here is, is it required/worth to maintain the history of file per commit?
    public static long getLineCount(String filePath) {
        try {
            // This technique would return the lines of the file in the latest version of the repository
            return Files.lines(Paths.get(filePath)).count();
        } catch (IOException e) {
            return 0;
        }
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
}
