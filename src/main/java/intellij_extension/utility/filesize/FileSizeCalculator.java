package intellij_extension.utility.filesize;

import intellij_extension.Constants;
import intellij_extension.models.FileObject;

import java.io.*;


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
     * @param in an InputStream with a File open. This method does not close the InputStream when it is done.
     * @return the number of lines in a file or -1 if there was an IOException.
     */
    public static long computeLineCount(InputStream in)
    {
        try
        {
            //Compute number of lines
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            long lineCount = 0;
            while (reader.readLine() != null)
                lineCount++;
            reader.close();

            return lineCount;
        }
        catch (IOException ex)
        {
            Constants.LOG.error(ex);
            return -1;
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
