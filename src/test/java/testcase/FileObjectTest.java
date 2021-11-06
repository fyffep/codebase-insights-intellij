package testcase;


import intellij_extension.models.FileObject;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.TestData.*;

/**
 * The following class contains the test cases of all the methods in the class FileObject.java
 */

public class FileObjectTest {

    // Test Default Constructor
    @Test
    public void defaultConstructorTest() {
        FileObject file = new FileObject();
        assertEquals("", file.getFileName());
        assertEquals("", file.getFilePath());
        assertEquals(0, file.getDepth());

    }

    // Test Constructor
    @Test
    public void constructor1Test() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        assertEquals(TEST_FILENAME, file.getFileName());
        assertEquals(TEST_FILEPATH, file.getFilePath());
        assertEquals(TEST_DEPTH, file.getDepth());
    }

    //Test setDepth();
    @Test
    public void setDepthTest() {
        FileObject file = new FileObject();
        file.setDepth(TEST_DEPTH);
        assertEquals(TEST_DEPTH, file.getDepth());
    }

    //Test getDepth();
    @Test
    public void getDepthTest() {
        FileObject file = new FileObject();
        file.setDepth(TEST_DEPTH);
        int expectedDepth = file.getDepth();
        assertEquals(TEST_DEPTH, expectedDepth);
    }

    // Test setFilePath()
    @Test
    public void setFilePathTest() {
        FileObject file = new FileObject();
        file.setFilePath(TEST_FILEPATH);    //method under  test
        assertEquals(TEST_FILEPATH, file.getFilePath());

    }

    // Test setFilePath()
    @Test
    public void getFilePathTest() {
        FileObject file = new FileObject();
        file.setFilePath(TEST_FILEPATH);
        String expectedResult = file.getFilePath();  //method under  test
        assertEquals(TEST_FILEPATH, expectedResult);
    }

    // Test setFileName()
    @Test
    public void setFileNameTest() {
        FileObject file = new FileObject();
        file.setFileName(TEST_NEW_FILE_1);    //method under  test
        assertEquals(TEST_NEW_FILE_1, file.getFileName());

    }

    // Test getFileName()
    @Test
    public void getFileNameTest() {
        FileObject file = new FileObject();
        file.setFileName(TEST_NEW_FILE_1);
        String expectedResult = file.getFileName();  //method under  test
        assertEquals(TEST_NEW_FILE_1, expectedResult);
    }

    // Test setLineCount()
    @Test
    public void setLineCountTest() {
        FileObject file = new FileObject();
        file.setLineCount(TEST_LINE_COUNT);    //method under  test
        assertEquals(TEST_LINE_COUNT, file.getLineCount());

    }

    // Test getLineCount()
    @Test
    public void getLineCountTest() {
        FileObject file = new FileObject();
        file.setLineCount(TEST_LINE_COUNT);
        long expectedResult = file.getLineCount();  //method under  test
        assertEquals(TEST_LINE_COUNT, expectedResult);
    }


    //Test setFileSize()
    @Test
    public void setFileSizeTest() {
        FileObject file = new FileObject();
        file.setFileSize(TEST_FILE_SIZE);    //method under  test
        assertEquals(TEST_FILE_SIZE, file.getFileSize());

    }

    // Test getFileSize()
    @Test
    public void getFileSizeTest() {
        FileObject file = new FileObject();
        file.setFileSize(TEST_FILE_SIZE);
        long expectedResult = file.getFileSize();  //method under  test
        assertEquals(TEST_FILE_SIZE, expectedResult);
    }

    //Test computeLineCount()
    @Test
    public void computeLineCountTest() {
        FileObject file = new FileObject();
        file.setFilePath(TEST_FILEPATH);
        file.computeLineCount();
        assertEquals(TEST_LINE_TESTDATA_COUNT, file.getLineCount());
    }

    //Test computeFileSize()
    @Test
    public void computeFileSizeTest() throws IOException {
        FileObject file = new FileObject();
        file.setFilePath(TEST_FILEPATH);
        file.computeFileSize();
        assertEquals(TEST_FILE_TESTDATA_SIZE, file.getFileSize());
    }
}