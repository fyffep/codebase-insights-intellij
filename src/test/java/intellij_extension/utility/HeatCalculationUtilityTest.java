package intellij_extension.utility;

import intellij_extension.models.FileObject;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.TestData.*;

class HeatCalculationUtilityTest
{
    @Test
    void calculateColorOfHeat()
    {
        final int TEST_HEAT_LEVEL = 1;
        Color colorString = HeatCalculationUtility.colorOfHeat(TEST_HEAT_LEVEL); //method under test
        assertEquals("0xff0000ff", colorString.toString());
    }

    @Test
    void calculateHeatForFileSizeTest()
    {
        FileObject fileObject = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        fileObject.setLineCount(200);
        int heatLevel = HeatCalculationUtility.calculateHeatForFileSize(fileObject); //method under test
        assertEquals(2, heatLevel);
    }

    @Test
    void calculateHeatForNumberOfCommitsTest()
    {
        FileObject fileObject = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        fileObject.setNumberOfCommits(TEST_NUMBER_OF_COMMITS);
        int heatLevel = HeatCalculationUtility.calculateHeatForNumberOfCommits(fileObject); //method under test
        assertEquals(TEST_NUMBER_OF_COMMITS, heatLevel);
    }
}
