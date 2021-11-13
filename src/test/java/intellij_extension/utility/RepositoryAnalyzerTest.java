package intellij_extension.utility;

import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.FileObjectV2;
import intellij_extension.models.redesign.HeatObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

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
    void attachLineCountToCodebaseTest() throws IOException
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
    void attachBranchNameListTest() throws IOException, GitAPIException
    {
        //Create test objects
        CodebaseV2 codebase = CodebaseV2.getInstance();
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
        repositoryAnalyzer.attachBranchNameList(codebase); //method being tested

        //Our branch list is always changing, so I just check if there are at least 3 branches.
        assertTrue(codebase.getBranchNameList().size() >= 3);
        //Ensure certain branches are present
        assertTrue(codebase.getBranchNameList().contains("master"));
        assertTrue(codebase.getBranchNameList().contains("development"));
    }

    @Test
    void attachCodebaseDataTest() throws IOException, GitAPIException {
        final String TEST_FILE_NAME = "CodebaseInsightsToolWindowFactory.java";
        final String TEST_HASH = "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a";
        final long EXPECTED_LINE_COUNT = 55;
        final long EXPECTED_FILE_SIZE = 2135;
        final int EXPECTED_NUMBER_OF_COMMITS = 2;


        //Create test objects
        CodebaseV2 codebase = CodebaseV2.getInstance();
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
        repositoryAnalyzer.attachBranchNameList(codebase); //FIXME bad place to obtain the list of branches for a Codebase?
        codebase.branchSelected("development");

        repositoryAnalyzer.attachCodebaseData(codebase); //method being tested

        //Verify the result
        FileObjectV2 fileObject = codebase.getFileObjectFromId(TEST_FILE_NAME);
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
        assertEquals(EXPECTED_FILE_SIZE, heatObject.getFileSize());
        assertEquals(EXPECTED_NUMBER_OF_COMMITS, heatObject.getNumberOfCommits());
    }
}
