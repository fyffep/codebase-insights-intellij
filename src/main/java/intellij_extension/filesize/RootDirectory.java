package intellij_extension.filesize;

import intellij_extension.models.FileObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;


/**
 * Tracks multiple folder/package within the basedir
 * //TODO : folder Size
 */

public class RootDirectory {

    // Members of RootDirectory
    private final HashMap<String, Folder> folderList;
    private final HashMap<String, FileObject> fileList;

    private String baseDir;
    private long folderCount, fileCount;
    private int depth;

    //default constructor
    public RootDirectory() {
        this.baseDir = "src";
        folderList = new HashMap<String, Folder>();
        fileList = new HashMap<String, FileObject>();
        folderCount = 0;
        fileCount = 0;
        depth = 0;
    }

    //constructor
    public RootDirectory(String baseDir) {
        this.baseDir = baseDir;
        folderList = new HashMap<String, Folder>();
        fileList = new HashMap<String, FileObject>();
        folderCount = 0;
        fileCount = 0;
        depth = 0;
    }

    public String getBaseDir() {
        return this.baseDir;
    }

    // base directory path setter and getter
    public void setBaseDir(String sourcename) {
        this.baseDir = sourcename;
    }

    // reads the base directory information and maintains the folder and file objects within the base dir in a hashmap
    public void parseDirectory() throws IOException {
        File directory = null;
        File verifyFileOrFolder = null;
        long folderCount = 0, fileCount = 0;

        try {
            directory = new File(getBaseDir()); //At a high level, Java considers both files and directories as files when working with file systems,

            for (String folderObject : directory.list())  // list() on directory returns a String array containing the names of files/folders
            {
                String path = getBaseDir() + "/" + folderObject;
                verifyFileOrFolder = new File(path);

                // folder search
                if (verifyFileOrFolder.isDirectory()) {
                    Folder folder = new Folder(folderObject, path, getDepth() + 1);
                    folder.parseFolder();
                    setFolderList(folderObject, folder);
                    folderCount++;
                }

                //file search
                else if (verifyFileOrFolder.isFile()) {
                    FileObject file = new FileObject(folderObject, path, getDepth() + 1);
                    file.parseFile();
                    setFileList(folderObject, file);
                    fileCount++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("One of the following  members which as the value null is responsible for the exception");
            System.out.println("Directory:" + directory);
            System.out.println("Folder/File under search:" + verifyFileOrFolder);

        } finally {
            setFolderCount(folderCount);
            setFileCount(fileCount);
        }

    }

    public long getFileCount() {
        return this.fileCount;
    }

    //fileCount getter and setter
    public void setFileCount(long fileCount) {
        this.fileCount = fileCount;
    }

    public long getFolderCount() {
        return this.folderCount;
    }

    //folderCount getter and setter
    public void setFolderCount(long folderCount) {
        this.folderCount = folderCount;
    }

    // to store the depth of a folder with respect to root. Used for display purpose
    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    //folderList hashmap getter and setter
    public void setFolderList(String name, Folder folder) {
        this.folderList.put(name, folder);
    }

    public HashMap<String, Folder> getFolderList() {
        return folderList;
    }

    //fileList hashmap getter and setter
    public void setFileList(String name, FileObject file) {
        this.fileList.put(name, file);
    }

    public HashMap<String, FileObject> getFileList() {
        return fileList;
    }

    //displays all the folders and file details within the root directory
    public void displayDetails() {
        System.out.println(getBaseDir() + " Folder Count : " + getFolderCount());

        // display all folders within the base directory
        for (String foldername : folderList.keySet()) {
            Folder folder = folderList.get(foldername);
            folder.displayDetails();
        }

        // display all files within the base directory
        for (String filename : fileList.keySet()) {
            FileObject file = fileList.get(filename);
            file.displayFileDetails();
        }
    }

}
