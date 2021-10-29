package file_utilities;

import java.io.File;
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

    private String foldername;
    private HashMap<String, FileObject> fileList;
    private HashMap<String, Folder> folderList;
    private String folderpath;
    private long fileCount, folderCount;
    private int depth;

    //default constructor
    public Folder() {
        this.foldername = "";
        fileList = new HashMap<String, FileObject>();
        folderList = new HashMap<String, Folder>();

        fileCount = 0;
        folderCount = 0;
        depth = 0;

    }

    //constructor
    public Folder(String foldername, String basedir, int depth) {

        this.foldername = foldername;
        fileList = new HashMap<String, FileObject>();
        setFolderPath(basedir);
        fileCount = 0;
        folderList = new HashMap<String, Folder>();
        folderCount = 0;
        this.depth = depth;
    }

    //set folder name
    public void setFolder(String foldername) {
        this.foldername = foldername;
    }

    //get folder name
    public String getFolder() {
        return this.foldername;
    }

    //set folder path
    public void setFolderPath(String basedir) {
        this.folderpath = basedir + "/" + getFolder();
    }


    //get the folder path
    public String getFolderPath() {
        return this.folderpath;
    }


    public void parseFolder() throws IOException {
        File directory = new File(getFolderPath());
        long fileCount = 0;
        for (String folderObject : directory.list()) {
            File verifyobj = new File(getFolderPath() + "/" + folderObject);
            if (verifyobj.isFile()) {
                FileObject file = new FileObject(folderObject, getFolderPath(), getDepth() + 1);
                file.parseFile();
                fileList.put(folderObject, file);
                fileCount++;
            } else if (verifyobj.isDirectory()) {
                Folder folder = new Folder(folderObject, getFolderPath(), getDepth() + 1);
                folder.parseFolder();
                folderList.put(folderObject, folder);
                folderCount++;
            }


        }
        setFileCount(fileCount);
        setFolderCount(folderCount);

    }


    private int getDepth() {

        return this.depth;
    }

    public void setFileCount(long fileCount) {
        this.fileCount = fileCount;

    }

    public long getFileCount() {
        return this.fileCount;
    }


    public void setFolderCount(long folderCount) {
        this.folderCount = folderCount;
    }

    public long getFolderCount() {
        return this.folderCount;
    }

    void displayDetails() {
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
