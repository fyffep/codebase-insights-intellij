package intellij_extension.utility.filesize;

import intellij_extension.models.FileObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


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
            while(buffer.readLine() != null)
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
}
