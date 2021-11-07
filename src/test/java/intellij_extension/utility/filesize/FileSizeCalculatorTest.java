package intellij_extension.utility.filesize;

import intellij_extension.models.FileObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.TestData.*;
import static testdata.TestData.TEST_FILE_TESTDATA_SIZE;

class FileSizeCalculatorTest
{
    //Test assignLineCount()
    @Test
    void assignLineCountTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setFilePath(TEST_FILEPATH);
        FileSizeCalculator.assignLineCount(file);
        assertEquals(TEST_LINE_TESTDATA_COUNT, file.getLineCount());
    }

    //Test assignFileSize()
    @Test
    void assignFileSizeTest() {
        FileObject file = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        file.setFilePath(TEST_FILEPATH);
        FileSizeCalculator.assignFileSize(file);
        assertEquals(TEST_FILE_TESTDATA_SIZE, file.getFileSize());
    }
}
