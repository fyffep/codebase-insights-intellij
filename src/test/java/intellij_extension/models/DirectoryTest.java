package intellij_extension.models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import static testdata.TestData.*;

/**
 * Test cases for the class Directory.java
 */
public class DirectoryTest
{

    //Default constructor removed
    /*
    // Test Default Constructor
    @Test
    public void defaultConstructorTest() {
        Directory root = new Directory();
        assertEquals(TEST_ROOT_DEFAULT_PATH, root.getBaseDir());
        assertEquals(TEST_DEFAULT_FILE_COUNT, root.getFileCount());
        assertEquals(TEST_DEFAULT_FOLDER_COUNT, root.getFolderCount());
        assertEquals(TEST_DEFAULT_DEPTH, root.getDepth());

        HashMap<String, Directory> actualFolderList = root.getFolderList();
        assertEquals(TEST_DEFAULT_FOLDERLIST_SIZE, actualFolderList.size());

        HashMap<String, FileObject> actualFileList = root.getFileList();
        assertEquals(TEST_DEFAULT_FILELIST_SIZE, actualFileList.size());
    }*/

    // Test setBaseDir()
    @Test
    public void setBaseDirTest() {
        Directory root = new Directory("initial value");
        root.setPath(TEST_ROOT_PATH);    //method under test
        assertEquals(TEST_ROOT_PATH, root.getPath());

    }

    // Test getBaseDir()
    @Test
    public void getBaseDirTest() {
        Directory root = new Directory(TEST_ROOT_PATH);
        String expectedResult = root.getPath();  //method under  test
        assertEquals(TEST_ROOT_PATH, expectedResult);

    }

    // Test setFileCount()
    @Test
    public void setFileCountTest() {
        Directory root = new Directory(TEST_ROOT_PATH);
        root.setFileCount(TEST_FILE_COUNT); //method under  test
        assertEquals(TEST_FILE_COUNT, root.getFileCount());
    }

    // Test getFileCount()
    @Test
    public void getFileCountTest() {
        Directory root = new Directory(TEST_ROOT_PATH);
        root.setFileCount(TEST_FILE_COUNT);
        long expectedResult = root.getFileCount(); //method under  test
        assertEquals(TEST_FILE_COUNT, expectedResult);
    }

    //Test getFolderCount()
    @Test
    public void getFolderCountTest() {
        Directory root = new Directory(TEST_ROOT_PATH);
        root.setFolderCount(TEST_FOLDER_COUNT);
        long expectedResult = root.getFolderCount(); //method under  test
        assertEquals(TEST_FOLDER_COUNT, expectedResult);

    }

    //Test setFolderCount()
    @Test
    public void setFolderCountTest() {
        Directory root = new Directory(TEST_ROOT_PATH);
        root.setFolderCount(TEST_FOLDER_COUNT);//method under  test
        assertEquals(TEST_FOLDER_COUNT, root.getFolderCount());

    }
    //Test setDepth();
    @Test
    public void setDepthTest() {
        Directory root = new Directory(TEST_ROOT_PATH);
        root.setDepth(TEST_DEPTH);
        assertEquals(TEST_DEPTH, root.getDepth());
    }

    //Test getDepth();
    @Test
    public void getDepthTest() {
        Directory root = new Directory(TEST_ROOT_PATH);
        root.setDepth(TEST_DEPTH);
        int expectedDepth = root.getDepth();
        assertEquals(TEST_DEPTH, expectedDepth);
    }

    //Test parseDirectoryTest()
    @Test
    public void parseDirectoryTest() throws IOException {
        Directory root = new Directory(TEST_PARSE_FOLDER_PATH);
        root.parseDirectory();
        assertEquals(TEST_PARSE_FOLDER_COUNT, root.getFolderCount());

        root = new Directory(TEST_PARSE_FILE);
        root.parseDirectory();
        assertEquals(TEST_PARSE_FILE_COUNT, root.getFileCount());
    }
}
