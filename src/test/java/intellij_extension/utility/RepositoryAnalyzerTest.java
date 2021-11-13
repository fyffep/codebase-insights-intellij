package intellij_extension.utility;

import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.FileObjectV2;
import intellij_extension.models.redesign.HeatObject;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepositoryAnalyzerTest
{
    public static final File PROJECT_ROOT = new File(".");

    @Test
    void defaultConstructorTest()
    {
        assertDoesNotThrow(() -> {
            RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
        });
    }

    @Test
    void obtainFileContentsTest() throws IOException
    {
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);

        final String TEST_FILE = "intellij_extension/CodebaseInsightsToolWindowFactory.java";
        final String TEST_HASH = "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a";
        int EXPECTED_LINE_COUNT = 55;
        InputStream in = repositoryAnalyzer.obtainFileContents(TEST_FILE, TEST_HASH); //method being tested

        //Read the number of lines for the file
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        int lineCount = 0;
        while (reader.readLine() != null) lineCount++;
        reader.close();
        in.close();

        assertEquals(EXPECTED_LINE_COUNT, lineCount);
    }

    @Test
    void attachLineCountToCodebase() throws IOException
    {
        //final String TEST_FILE_PATH = "src\\main\\java\\intellij_extension\\CodebaseInsightsToolWindowFactory.java";
        final String TEST_FILE_NAME = "CodebaseInsightsToolWindowFactory.java";
        final String TEST_HASH = "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a";
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);

        CodebaseV2 codebase = CodebaseV2.getInstance();

        //Compute the size of an old version of a file
        final long EXPECTED_LINE_COUNT = 55;
        final long EXPECTED_FILE_SIZE = 2135;
        repositoryAnalyzer.attachLineCountToCodebase(codebase, TEST_HASH); //method being tested

        //Verify the result
        FileObjectV2 fileObject = codebase.getFileObjectFromId(TEST_FILE_NAME);
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
        assertEquals(EXPECTED_FILE_SIZE, heatObject.getFileSize());
    }

    @Test
    void attachCodebaseDataTest() throws IOException
    {
        //final String TEST_FILE_PATH = "src\\main\\java\\intellij_extension\\CodebaseInsightsToolWindowFactory.java";
        final String TEST_FILE_NAME = "CodebaseInsightsToolWindowFactory.java";
        final String TEST_HASH = "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a";
        final long EXPECTED_LINE_COUNT = 55;
        final long EXPECTED_FILE_SIZE = 2135;
        final int EXPECTED_NUMBER_OF_COMMITS = 2;


        //Create test objects
        CodebaseV2 codebase = CodebaseV2.getInstance();
        codebase.setActiveBranch("development");
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);

        repositoryAnalyzer.attachCodebaseData(codebase); //method being tested

        //Verify the result
        FileObjectV2 fileObject = codebase.getFileObjectFromId(TEST_FILE_NAME);
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
        assertEquals(EXPECTED_FILE_SIZE, heatObject.getFileSize());
        assertEquals(EXPECTED_NUMBER_OF_COMMITS, heatObject.getNumberOfCommits());
    }
}
