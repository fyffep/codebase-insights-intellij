/*package testcase;

import intellij_extension.filesize.Folder;
import intellij_extension.filesize.RootDirectory;
import intellij_extension.models.FileObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.TestData.*;

import java.io.IOException;
import java.util.HashMap;


/**
 * The follwoing class contains the test cases of all the methods in the class RootDirectory.java
 *

public class RootDirectoryTest {

    // Test Default Constructor
    @Test
    public void defaultConstructorTest() {
        RootDirectory root = new RootDirectory();
        assertEquals(TEST_ROOT_DEFAULT_PATH, root.getBaseDir());
        assertEquals(TEST_DEFAULT_FILE_COUNT, root.getFileCount());
        assertEquals(TEST_DEFAULT_FOLDER_COUNT, root.getFolderCount());
        assertEquals(TEST_DEFAULT_DEPTH, root.getDepth());

        HashMap<String, Folder> actualFolderList = root.getFolderList();
        assertEquals(TEST_DEFAULT_FOLDERLIST_SIZE, actualFolderList.size());

        HashMap<String, FileObject> actualFileList = root.getFileList();
        assertEquals(TEST_DEFAULT_FILELIST_SIZE, actualFileList.size());
    }

    // Test setBaseDir()
    @Test
    public void setBaseDirTest() {
        RootDirectory root = new RootDirectory();
        root.setBaseDir(TEST_ROOT_PATH);    //method under  test
        assertEquals(TEST_ROOT_PATH, root.getBaseDir());

    }

    // Test getBaseDir()
    @Test
    public void getBaseDirTest() {
        RootDirectory root = new RootDirectory();
        root.setBaseDir(TEST_ROOT_PATH);
        String expectedResult = root.getBaseDir();  //method under  test
        assertEquals(TEST_ROOT_PATH, expectedResult);

    }

    // Test setFileCount()
    @Test
    public void setFileCountTest() {
        RootDirectory root = new RootDirectory();
        root.setFileCount(TEST_FILE_COUNT); //method under  test
        assertEquals(TEST_FILE_COUNT, root.getFileCount());
    }

    // Test getFileCount()
    @Test
    public void getFileCountTest() {
        RootDirectory root = new RootDirectory();
        root.setFileCount(TEST_FILE_COUNT);
        long expectedResult = root.getFileCount(); //method under  test
        assertEquals(TEST_FILE_COUNT, expectedResult);
    }

    //Test getFolderCount()
    @Test
    public void getFolderCountTest() {
        RootDirectory root = new RootDirectory();
        root.setFolderCount(TEST_FOLDER_COUNT);
        long expectedResult = root.getFolderCount(); //method under  test
        assertEquals(TEST_FOLDER_COUNT, expectedResult);

    }

    //Test setFolderCount()
    @Test
    public void setFolderCountTest() {
        RootDirectory root = new RootDirectory();
        root.setFolderCount(TEST_FOLDER_COUNT);//method under  test
        assertEquals(TEST_FOLDER_COUNT, root.getFolderCount());

    }
    //Test setDepth();
    @Test
    public void setDepthTest() {
        RootDirectory root = new RootDirectory();
        root.setDepth(TEST_DEPTH);
        assertEquals(TEST_DEPTH, root.getDepth());
    }

    //Test getDepth();
    @Test
    public void getDepthTest() {
        RootDirectory root = new RootDirectory();
        root.setDepth(TEST_DEPTH);
        int expectedDepth = root.getDepth();
        assertEquals(TEST_DEPTH, expectedDepth);
    }

    //Test parseDirectoryTest()
    @Test
    public void parseDirectoryTest() throws IOException {
        RootDirectory root = new RootDirectory(TEST_PARSE_FOLDER_PATH);
        root.parseDirectory();
        assertEquals(TEST_PARSE_FOLDER_COUNT, root.getFolderCount());

        root = new RootDirectory(TEST_PARSE_FILE);
        root.parseDirectory();
        assertEquals(TEST_PARSE_FILE_COUNT, root.getFileCount());
    }

    //Test setFolderList()
    @Test
    public void setFolderListTest() {
        RootDirectory root = new RootDirectory();
        Folder folder1 = new Folder();
        root.setFolderList(TEST_NEW_FOLDER_1, folder1);
        assertEquals(TEST_NEW_FOLDER_1_COUNT, root.getFolderList().size());
        Folder folder2 = new Folder();
        root.setFolderList(TEST_NEW_FOLDER_2, folder2);
        assertEquals(TEST_NEW_FOLDER_2_COUNT, root.getFolderList().size());

    }

    //Test getFolderList();
    @Test
    public void getFolderListTest() {
        RootDirectory root = new RootDirectory();
        Folder folder1 = new Folder();
        root.setFolderList(TEST_NEW_FOLDER_1, folder1);
        int foldercount = root.getFolderList().size();
        assertEquals(TEST_NEW_FOLDER_1_COUNT, foldercount);

    }

    //Test setFileList()
    @Test
    public void setFileListTest() {
        RootDirectory root = new RootDirectory();
        FileObject file1 = new FileObject();
        root.setFileList(TEST_NEW_FILE_1, file1);
        assertEquals(TEST_NEW_FILE_1_COUNT, root.getFileList().size());
        FileObject file2 = new FileObject();
        root.setFileList(TEST_NEW_FILE_2, file2);
        assertEquals(TEST_NEW_FILE_2_COUNT, root.getFileList().size());

    }

    //Test getFileList()
    @Test
    public void getFileListTest() {
        RootDirectory root = new RootDirectory();
        FileObject file1 = new FileObject();
        root.setFileList(TEST_NEW_FILE_1, file1);
        int fileCount = root.getFileList().size();
        assertEquals(TEST_NEW_FILE_1_COUNT, fileCount);
    }


}

*/

