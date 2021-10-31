package testcase;

import intellij_extension.filesize.Folder;
import intellij_extension.filesize.Folder;
import intellij_extension.models.FileObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.TestData.*;



/**
 * The following class contains the test cases of all the methods in the class Folder.java
 */


public class FolderTest {

    // Test Default Constructor
    @Test
    public void defaultConstructorTest() {
        Folder folder = new Folder();
        folder.setFolderPath(TEST_PARSE_FOLDER_PATH);
        assertEquals(TEST_PARSE_FOLDER_PATH, folder.getFolderPath());
        assertEquals(TEST_DEFAULT_FILE_COUNT, folder.getFileCount());
        assertEquals(TEST_DEFAULT_FOLDER_COUNT, folder.getFolderCount());
        assertEquals(TEST_DEFAULT_DEPTH, folder.getDepth());

        HashMap<String, Folder> actualFolderList = folder.getFolderList();
        assertEquals(TEST_DEFAULT_FOLDERLIST_SIZE, actualFolderList.size());

        HashMap<String, FileObject> actualFileList = folder.getFileList();
        assertEquals(TEST_DEFAULT_FILELIST_SIZE, actualFileList.size());
    }

    // Test parameterized Constructor
    @Test
    public void parameterizedConstructorTest() {
        Folder folder = new Folder(TEST_PARSE_FOLDER_NAME,TEST_PARSE_FOLDER_PATH,TEST_DEFAULT_DEPTH);
        assertEquals(TEST_PARSE_FOLDER_PATH, folder.getFolderPath());
        assertEquals(TEST_DEFAULT_FILE_COUNT, folder.getFileCount());
        assertEquals(TEST_DEFAULT_FOLDER_COUNT, folder.getFolderCount());
        assertEquals(TEST_DEFAULT_DEPTH, folder.getDepth());

        HashMap<String, Folder> actualFolderList = folder.getFolderList();
        assertEquals(TEST_DEFAULT_FOLDERLIST_SIZE, actualFolderList.size());

        HashMap<String, FileObject> actualFileList = folder.getFileList();
        assertEquals(TEST_DEFAULT_FILELIST_SIZE, actualFileList.size());
    }

    // Test setFolderPath()
    @Test
    public void setFolderPathTest() {
        Folder folder = new Folder();
        folder.setFolderPath(TEST_PARSE_FOLDER_PATH);    //method under  test
        assertEquals(TEST_PARSE_FOLDER_PATH, folder.getFolderPath());
    }

    // Test getFolderPath()
    @Test
    public void getFolderPathTest() {
        Folder folder = new Folder();
        folder.setFolderPath(TEST_PARSE_FOLDER_PATH);
        String expectedResult = folder.getFolderPath();  //method under  test
        assertEquals(TEST_PARSE_FOLDER_PATH, expectedResult);
    }

    // Test setFileCount()
    @Test
    public void setFileCountTest() {
        Folder folder = new Folder();
        folder.setFileCount(TEST_FILE_COUNT); //method under  test
        assertEquals(TEST_FILE_COUNT, folder.getFileCount());
    }

    // Test getFileCount()
    @Test
    public void getFileCountTest() {
        Folder folder = new Folder();
        folder.setFileCount(TEST_FILE_COUNT);
        long expectedResult = folder.getFileCount(); //method under  test
        assertEquals(TEST_FILE_COUNT, expectedResult);
    }

    //Test getFolderCount()
    @Test
    public void getFolderCountTest() {
        Folder folder = new Folder();
        folder.setFolderCount(TEST_FOLDER_COUNT);
        long expectedResult = folder.getFolderCount(); //method under  test
        assertEquals(TEST_FOLDER_COUNT, expectedResult);
    }

    //Test setFolderCount()
    @Test
    public void setFolderCountTest() {
        Folder folder = new Folder();
        folder.setFolderCount(TEST_FOLDER_COUNT);//method under  test
        assertEquals(TEST_FOLDER_COUNT, folder.getFolderCount());
    }

    //Test setDepth();
    @Test
    public void setDepthTest() {
        Folder folder = new Folder();
        folder.setDepth(TEST_DEPTH);
        assertEquals(TEST_DEPTH, folder.getDepth());
    }

    //Test getDepth();
    @Test
    public void getDepthTest() {
        Folder folder = new Folder();
        folder.setDepth(TEST_DEPTH);
        int expectedDepth = folder.getDepth();
        assertEquals(TEST_DEPTH, expectedDepth);
    }

    //Test parseDirectoryTest()
    @Test
    public void parseFolder() throws IOException {
        Folder folder = new Folder();
        folder.setFolderPath(TEST_PARSE_FOLDER_PATH);
        folder.parseFolder();
        assertEquals(TEST_PARSE_FOLDER_COUNT, folder.getFolderCount());

        folder = new Folder();
        folder.setFolderPath(TEST_PARSE_FILE);
        folder.parseFolder();
        assertEquals(TEST_PARSE_FILE_COUNT, folder.getFileCount());
    }

    //Test setFolderList()
    @Test
    public void setFolderListTest() {
        Folder folder = new Folder();
        Folder folder1 = new Folder();
        folder.setFolderList(TEST_NEW_FOLDER_1, folder1);
        assertEquals(TEST_NEW_FOLDER_1_COUNT, folder.getFolderList().size());
        Folder folder2 = new Folder();
        folder.setFolderList(TEST_NEW_FOLDER_2, folder2);
        assertEquals(TEST_NEW_FOLDER_2_COUNT, folder.getFolderList().size());

    }

    //Test getFolderList();
    @Test
    public void getFolderListTest() {
        Folder folder = new Folder();
        Folder folder1 = new Folder();
        folder.setFolderList(TEST_NEW_FOLDER_1, folder1);
        int foldercount = folder.getFolderList().size();
        assertEquals(TEST_NEW_FOLDER_1_COUNT, foldercount);

    }

    //Test setFileList()
    @Test
    public void setFileListTest() {
        Folder folder = new Folder();
        FileObject file1 = new FileObject();
        folder.setFileList(TEST_NEW_FILE_1, file1);
        assertEquals(TEST_NEW_FILE_1_COUNT, folder.getFileList().size());
        FileObject file2 = new FileObject();
        folder.setFileList(TEST_NEW_FILE_2, file2);
        assertEquals(TEST_NEW_FILE_2_COUNT, folder.getFileList().size());

    }

    //Test getFileList()
    @Test
    public void getFileListTest() {
        Folder folder = new Folder();
        FileObject file1 = new FileObject();
        folder.setFileList(TEST_NEW_FILE_1, file1);
        int fileCount = folder.getFileList().size();
        assertEquals(TEST_NEW_FILE_1_COUNT, fileCount);
    }

    //Test displayDirectory()
    @Test
    void displayDirectoryTest() {

    }

}
