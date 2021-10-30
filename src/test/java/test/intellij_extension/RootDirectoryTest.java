package test.intellij_extension;

import intellij_extension.filesize.Folder;
import intellij_extension.filesize.RootDirectory;
import intellij_extension.models.FileObject;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class RootDirectoryTest {
    @Test
    public void defaultConstructorTest() {
        RootDirectory root = new RootDirectory();   // Test Default Constructor
        assertEquals("src", root.getBaseDir());
        assertEquals(0, root.getFileCount());
        assertEquals(0, root.getFolderCount());
        assertEquals(0, root.getDepth());

        HashMap<String, Folder> actualFolderList = root.getFolderList();
        assertEquals(0, actualFolderList.size());

        HashMap<String, FileObject> actualFileList = root.getFileList();
        assertEquals(0, actualFileList.size());
    }

    @Test
    public void setBaseDirTest() {
        RootDirectory root = new RootDirectory();
        root.setBaseDir("");    //method under  test
        assertEquals("", root.getBaseDir());
        root.setBaseDir("abc"); //method under  test
        assertEquals("abc", root.getBaseDir());
    }

    @Test
    public void getBaseDirTest() {
        RootDirectory root = new RootDirectory();
        root.setBaseDir("");
        String expectedResult = root.getBaseDir();  //method under  test
        assertEquals("", expectedResult);
        root.setBaseDir("abc");
        expectedResult = root.getBaseDir();  //method under  test
        assertEquals("abc", expectedResult);
    }

    @Test
    public void setFileCountTest() {
        RootDirectory root = new RootDirectory();
        root.setFileCount(0);
        assertEquals(0, root.getFileCount());
        root.setFileCount(100); //method under  test
        assertEquals(100, root.getFileCount());
    }

    @Test
    public void getFileCountTest() {
        RootDirectory root = new RootDirectory();
        root.setFileCount(0);
        long expectedResult = root.getFileCount(); //method under  test
        assertEquals(0, expectedResult);
        root.setFileCount(100); //method under  test
        expectedResult = root.getFileCount(); //method under  test
        assertEquals(100, expectedResult);
    }

    @Test
    public void getFolderCountTest() {
        RootDirectory root = new RootDirectory();
        root.setFolderCount(0);
        long expectedResult = root.getFolderCount(); //method under  test
        assertEquals(0, expectedResult);
        root.setFolderCount(100); //method under  test
        expectedResult = root.getFolderCount(); //method under  test
        assertEquals(100, expectedResult);
    }

    @Test
    public void setFolderCountTest() {
        RootDirectory root = new RootDirectory();
        root.setFolderCount(0);//method under  test
        assertEquals(0, root.getFolderCount());
        root.setFolderCount(100); //method under  test
        assertEquals(100, root.getFolderCount());
    }

    @Test
    public void setDepthTest() {
        RootDirectory root = new RootDirectory();
        root.setDepth(2);
        assertEquals(2, root.getDepth());
    }


    @Test
    public void getDepthTest() {
        RootDirectory root = new RootDirectory();
        root.setDepth(10);
        int expectedDepth = root.getDepth();
        assertEquals(10, expectedDepth);
    }

    @Test
    public void parseDirectoryTest() {

    }


}
