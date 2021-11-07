package intellij_extension.models;

import intellij_extension.utility.filesize.FileSizeCalculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.TestData.*;

/**
 * The following class contains the test cases of all the methods in the class FileObject.java
 */
class FileObjectTest
{
    //Default constructor was removed
    /*/ Test Default Constructor
    @Test
    void defaultConstructorTest() {
        FileObject file = new FileObject();
        assertEquals("", file.getFileName());
        assertEquals("", file.getFilePath());
        assertEquals(0, file.getDepth());
    }*/

    // Test Constructor
    @Test
    void constructor1Test() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        assertEquals(TEST_FILENAME, file.getFileName());
        assertEquals(TEST_FILEPATH, file.getFilePath());
        assertEquals(TEST_DEPTH, file.getDepth());
    }

    //Test setDepth();
    @Test
    void setDepthTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH * -1);
        file.setDepth(TEST_DEPTH);
        assertEquals(TEST_DEPTH, file.getDepth());
    }

    //Test getDepth();
    @Test
    void getDepthTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH * -1);
        file.setDepth(TEST_DEPTH);
        int expectedDepth = file.getDepth();
        assertEquals(TEST_DEPTH, expectedDepth);
    }

    // Test setFilePath()
    @Test
    void setFilePathTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setFilePath(TEST_FILEPATH);    //method under  test
        assertEquals(TEST_FILEPATH, file.getFilePath());

    }

    // Test setFilePath()
    @Test
    void getFilePathTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setFilePath(TEST_FILEPATH);
        String expectedResult = file.getFilePath();  //method under  test
        assertEquals(TEST_FILEPATH, expectedResult);
    }

    // Test setFileName()
    @Test
    void setFileNameTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setFileName(TEST_NEW_FILE_1);    //method under  test
        assertEquals(TEST_NEW_FILE_1, file.getFileName());

    }

    // Test getFileName()
    @Test
    void getFileNameTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setFileName(TEST_NEW_FILE_1);
        String expectedResult = file.getFileName();  //method under  test
        assertEquals(TEST_NEW_FILE_1, expectedResult);
    }

    // Test setLineCount()
    @Test
    void setLineCountTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setLineCount(TEST_LINE_COUNT);    //method under  test
        assertEquals(TEST_LINE_COUNT, file.getLineCount());

    }

    // Test getLineCount()
    @Test
    void getLineCountTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setLineCount(TEST_LINE_COUNT);
        long expectedResult = file.getLineCount();  //method under  test
        assertEquals(TEST_LINE_COUNT, expectedResult);
    }


    //Test setFileSize()
    @Test
    void setFileSizeTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setFileSize(TEST_FILE_SIZE);    //method under  test
        assertEquals(TEST_FILE_SIZE, file.getFileSize());

    }

    // Test getFileSize()
    @Test
    void getFileSizeTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setFileSize(TEST_FILE_SIZE);
        long expectedResult = file.getFileSize();  //method under  test
        assertEquals(TEST_FILE_SIZE, expectedResult);
    }

    //Test computeHeatLevel()
    @Test
    void computeHeatLevelTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setLineCount(TEST_LINE_COUNT);
        file.setNumberOfCommits(TEST_NUMBER_OF_COMMITS);
        int heatLevel = file.computeHeatLevel(); //method under test
        assertEquals(TEST_EXPECTED_HEAT, heatLevel);
    }
}
