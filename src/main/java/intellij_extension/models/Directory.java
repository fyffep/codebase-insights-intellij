package intellij_extension.models;

import intellij_extension.utility.HeatCalculator;
import intellij_extension.utility.filesize.FileSizeCalculator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Directory implements HeatCalculator
{
    private String path;
    private HashMap<String, FileObject> fileMap = new HashMap<>();
    private HashMap<String, Directory> folderMap = new HashMap<>();
    private long fileCount;
    private long folderCount;
    private int depth;

    //constructor
    public Directory(String path)
    {
        this.path = path;
        fileCount = 0;
        folderCount = 0;
        this.depth = 0;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Processes the current directory and its sub-directories to
     * form a tree structure with this Directory as a root or parent node.
     * @throws IOException when a pathname is null
     */
    public void parseDirectory() throws IOException
    {
        File directory = new File(path);
        this.fileCount = 0;
        System.out.println("Listing files for "+directory);
        for (String folderPath : directory.list())
        {
            String subPath = getPath() + "/" + folderPath;
            File verifyFolder = new File(subPath);
            if (verifyFolder.isFile())
            {
                FileObject file = new FileObject(verifyFolder.getName(), verifyFolder.getPath(),getDepth()+1);
                //Calculate the file's size
                FileSizeCalculator.assignFileSize(file);
                FileSizeCalculator.assignLineCount(file);

                fileMap.put(folderPath,file);
                fileCount++;
            }

            else if (verifyFolder.isDirectory())
            {
                Directory subDirectory = new Directory(subPath);
                subDirectory.depth = getDepth() + 1;
                subDirectory.parseDirectory();
                folderMap.put(folderPath, subDirectory);
                folderCount++;
            }
        }
        setFileCount(fileCount);
        setFolderCount(folderCount);
    }


    public int getDepth()
    {
        return this.depth;
    }

    public  void setFileCount(long fileCount)
    {
        this.fileCount=fileCount;
    }

    public long getFileCount()
    {
        return this.fileCount;
    }


    public  void setFolderCount(long folderCount)
    {
        this.folderCount=folderCount;
    }

    public long  getFolderCount()
    {
        return this.folderCount;
    }

    public HashMap<String, FileObject> getFileMap() {
        return fileMap;
    }

    public void setFileMap(HashMap<String, FileObject> fileMap) {
        this.fileMap = fileMap;
    }

    public HashMap<String, Directory> getFolderMap() {
        return folderMap;
    }

    public void setFolderMap(HashMap<String, Directory> folderMap) {
        this.folderMap = folderMap;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void displayDetails()
    {
        String output= getPath() + "---> FileCount : " + getFileCount() + " Folder Count : " + getFolderCount();
        System.out.println(String.format("%1$" + (output.length()+getDepth()) + "s", output));
        //Print the details of files in this directory
        for (Map.Entry<String, FileObject> entry : fileMap.entrySet())
        {
            FileObject file = entry.getValue();
            file.displayFileDetails();
        }

        //Recur on sub-directories
        for (Map.Entry<String, Directory> entry : folderMap.entrySet())
        {
            Directory directory = entry.getValue();
            directory.displayDetails();
        }
    }



    /**
     * Changes the HashMap passed to it so that FileObjects inside of it
     * are either added (if they do not exist) or given more data.
     * In this case, the FileObjects should be given file size data.
     * @param existingFileMetricMap this holds all file data for one commit in the project
     * @return the input map but with all FileObjects in the project given file size counts.
     */
    public HashMap<String, FileObject> editFileMetricMap(HashMap<String, FileObject> existingFileMetricMap)
    {
        //Place the FileObjects from this directory into the map
        for (Map.Entry<String, FileObject> entry : fileMap.entrySet())
        {
            //Merge the existing data (if it exists) with the newly computed data
            String filename = entry.getKey();
            FileObject existingData = existingFileMetricMap.get(filename); //what was passed in as a param
            FileObject fileSizeData = entry.getValue(); //what this class computed
            if (existingData == null)
                existingData = fileSizeData;
            existingData.setFileSize(fileSizeData.getFileSize());
            existingData.setLineCount(fileSizeData.getLineCount());

            existingFileMetricMap.put(filename, existingData);
        }

        //Recur on the sub-directories
        for (Map.Entry<String, Directory> entry : folderMap.entrySet())
        {
            Directory directory = entry.getValue();
            directory.editFileMetricMap(existingFileMetricMap);
        }

        return existingFileMetricMap;
    }

    public static void main(String[] args) throws IOException {
        Directory directory = new Directory("src/");
        directory.parseDirectory();
        directory.displayDetails();
    }
}
