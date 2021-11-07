package intellij_extension.utility;

import intellij_extension.models.FileObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.TestData.*;

public class HeatCalculationUtilityTest
{
    @Test
    public void defaultConstructorTest()
    {
        final int TEST_HEAT_LEVEL = 1;
        String colorString = HeatCalculationUtility.colorOfHeat(TEST_HEAT_LEVEL); //method under test
        assertEquals(colorString, "50ff50");
    }

    @Test
    public void calculateHeatForFileSizeTest()
    {
        FileObject fileObject = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        fileObject.setLineCount(200);
        int heatLevel = HeatCalculationUtility.calculateHeatForFileSize(fileObject); //method under test
        assertEquals(heatLevel, 2);
    }

    @Test
    public void calculateHeatForNumberOfCommitsTest()
    {
        FileObject fileObject = new FileObject(TEST_FILENAME, TEST_FILEPATH, TEST_DEPTH);
        fileObject.setNumberOfCommits(TEST_NUMBER_OF_COMMITS);
        int heatLevel = HeatCalculationUtility.calculateHeatForNumberOfCommits(fileObject); //method under test
        assertEquals(heatLevel, TEST_NUMBER_OF_COMMITS);
    }
}
