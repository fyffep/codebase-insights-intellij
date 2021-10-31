package intellij_extension.filesize;

import intellij_extension.models.FileObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * holds files
 * folder name
 * file count
 * path
 * folder size
 */

public class Folder {
    //Folder members
    private String folderName;
    private final HashMap<String, FileObject> fileList;
    private final HashMap<String, Folder> folderList;
    private String folderPath;
    private long fileCount, folderCount;
    private int depth;

    //default constructor
    public Folder() {
        this.folderName = "";
        this.fileList = new HashMap<String, FileObject>();
        this.folderList = new HashMap<String, Folder>();
        this.fileCount = 0;
        this.folderCount = 0;
        this.depth = 0;

    }

    //constructor
    public Folder(String folderName, String folderPath, int depth) {
        this.folderName = folderName;
        this.folderPath = folderPath;
        this.fileCount = 0;
        this.folderList = new HashMap<String, Folder>();
        this.fileList = new HashMap<String, FileObject>();
        this.folderCount = 0;
        this.depth = depth;
    }

    public String getFolder() {
        return this.folderName;
    }

    // folderName : getter and setter
    public void setFolder(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    // folderPath : getter and setter
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    // reads the folder information and maintains the folder and file objects within the folder in a hashmap
    public void parseFolder() throws IOException {
        File directory = null;
        File verifyFileOrFolder = null;
        long folderCount = 0, fileCount = 0;
        try {
            directory = new File(getFolderPath()); //At a high level, Java considers both files and directories as files when working with file systems,

            for (String folderObject : directory.list())  // list() on directory returns a String array containing the names of files/folders
            {
                String path = getFolderPath() + "/" + folderObject;
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
                    System.out.println(path);
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

    // to store the depth of a folder with respect to parent. Used for display purpose
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

    public long getFileCount() {
        return this.fileCount;
    }

    //fileCount getter setter
    public void setFileCount(long fileCount) {
        this.fileCount = fileCount;
    }

    public long getFolderCount() {
        return this.folderCount;
    }

    //folderCount getter setter
    public void setFolderCount(long folderCount) {
        this.folderCount = folderCount;
    }

    //displays all the folders and file details within the folder
    public void displayDetails() {
        String output = getFolder() + "---> FileCount : " + getFileCount() + " Folder Count : " + getFolderCount();
        System.out.println(String.format("%1$" + (output.length() + getDepth()) + "s", output));
        for (String filename : fileList.keySet()) {
            FileObject file = fileList.get(filename);
            file.displayFileDetails();
        }

        for (String foldername : folderList.keySet()) {
            Folder folder = folderList.get(foldername);
            folder.displayDetails();
        }
    }


}
