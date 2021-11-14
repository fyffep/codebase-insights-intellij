package intellij_extension.utility.filesize;

import intellij_extension.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;


public class FileSizeCalculator {
    private FileSizeCalculator() {
        //This is a utility class
    }


    /**
     * Gets the line count of a file at the given filePath
     */
    public static long getLineCount(String filePath) {
        try {
            // This technique would return the lines of the file in the latest version of the repository
            return Files.lines(Paths.get(filePath)).count();
        } catch (IOException e) {
            return 0;
        }
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
}
