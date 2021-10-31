package intellij_extension.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * filename
 * filesize
 * linecount
 * no of commits
 * last edited date
 * setpath
 */
public class FileObject {
    //FileObject members
    private String fileName;
    private long lineCount;
    private long fileSize;
    private int noOfCommits;
    private String filePath;
    private int depth;

    // default constructor
    public FileObject() {
        this.fileName = "";
        this.filePath = "";
        this.depth = 0;
    }

    //constructor
    public FileObject(String fileName, String filePath, int depth) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.depth = depth;
    }

    //fileName getter and setter
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    //filePath getter and setter
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    //lineCount getter and setter
    public void setLineCount(long lineCount) {
        this.lineCount = lineCount;
    }

    public long getLineCount() {
        return this.lineCount;
    }

    // compute line count within a file
    public void computeLineCount() {
        long lineCount = 0;
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(getFilePath()));

            while (buffer.readLine() != null) {
                lineCount++;
            }
            buffer.close();
        } catch (Exception e) {
            System.out.println("Unable to read:" + getFilePath());
            System.exit(0);
        }

        setLineCount(lineCount);
    }


    //fileSize getter and setter
    public void setFileSize(long length) {
        this.fileSize = length;

    }

    public long getFileSize() {
        return this.fileSize;
    }

    // compute file size in bytes
    public void computeFileSize() throws IOException {
        File file = new File(getFilePath());
        setFileSize(file.length());
    }


    // read through the file and get its line count and file size
    public void parseFile() throws IOException {
        computeLineCount();
        computeFileSize();
    }

    //display file details such as size and line count
    public void displayFileDetails() {
        String output = getFileName() + ",--->FileSize : " + getFileSize() + ", LineCount : " + getLineCount();
        System.out.println(String.format("%1$" + (output.length() + getDepth()) + "s", output));
    }

    // to store the depth of a file with respect to parent. Used for display purpose
    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int computeHeatLevel() {
        return (int) (Math.random() * 10); //TODO
    }
}
