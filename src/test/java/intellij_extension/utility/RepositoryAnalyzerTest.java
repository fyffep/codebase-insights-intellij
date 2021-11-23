package intellij_extension.utility;

import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.models.redesign.HeatObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryAnalyzerTest
{
    public static final File PROJECT_ROOT = new File(".");
    public static final File BOOGUS_PROJECT_ROOT = new File("BOOGUS PROJECT ROOT $#^&(#$*)!(_@");

//    @Test
//    void constructor_Default_Success()
//    {
//        assertDoesNotThrow(() -> {
//            RepositoryAnalyzer repo = new RepositoryAnalyzer();
//        });
//    }

    @Test
    void constructor_FilepathParameter_Success()
    {
        assertDoesNotThrow(() -> {
           new RepositoryAnalyzer(PROJECT_ROOT);
        });
    }

    @Test
    void constructor_FilepathParameter_ThrowsIOException()
    {
        assertThrows(IOException.class, () -> {
            new RepositoryAnalyzer(BOOGUS_PROJECT_ROOT);
        });
    }

    // If you fail this test locally it might be because you don't have the branches checked out.
    // Are these branches checked out locally?
    @Test
    void attachBranchNameListTest_BranchesMasterDevelopment_Success() throws IOException, GitAPIException
    {
        // Set up test data...
        Codebase codebase = Codebase.getInstance();
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
        repositoryAnalyzer.attachBranchNameList(codebase); //method being tested

        //Our branch list is always changing, so I just check if there are at least 3 branches.
        assertTrue(codebase.getBranchNameList().size() >= 3);
        // Ensure certain branches are present
        assertTrue(codebase.getBranchNameList().contains("master"));
        assertTrue(codebase.getBranchNameList().contains("development"));
        // We could check more but everyone might not have the same branches checked out
        // Master/development are reasonable to have and should be sufficient for the test.
    }

    // Should tests be throwing exceptions? I don't think so...
//    @Test
//    void attachLineCountToCodebase_FileLineCountAndFileSize_CodebaseInsightsToolWindowFactoryHash1e589e_Success() throws IOException {
//        // Set up test data
//        final String TEST_HASH = "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a";
//        final String TEST_FILE_NAME = "CodebaseInsightsToolWindowFactory.java";
//        final long EXPECTED_LINE_COUNT = 55;
//        final long EXPECTED_FILE_SIZE = 2135;
//
//        // Create test objects
//        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
//        Codebase codebase = Codebase.getInstance();
//        // Get the RevCommit to test
//        ObjectId commitId = repositoryAnalyzer.getGit().getRepository().resolve(TEST_HASH);
//        assertNotEquals(null, commitId);
//        RevWalk revWalk = new RevWalk(repositoryAnalyzer.getGit().getRepository());
//        RevCommit testCommit = revWalk.parseCommit(commitId);
//        // Compute the size of an old version of a file
//        repositoryAnalyzer.processHeatMetrics(codebase, testCommit); // method being tested
//
//        // Verify the result
//        FileObject fileObject = codebase.getFileObjectFromFilename(TEST_FILE_NAME);
//        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
//        // Ensures we have the right object
//        assertEquals(TEST_FILE_NAME, fileObject.getFilename());
//        // Check line count/file size
//        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
//        assertEquals(EXPECTED_FILE_SIZE, heatObject.getFileSize());
//    }

    @Test
    void attachCodebaseData_FileHeatMetrics_CodebaseInsightsToolWindowFactoryHash9db512_Success() throws IOException, GitAPIException {
        // Set up test data
        final String TEST_HASH = "9db51280e8bffb279acb8b1f36abaa209bc6e9a2";
        final String TEST_FILE_NAME = "CodebaseInsightsToolWindowFactory.java";
        final long EXPECTED_LINE_COUNT = 56;
        final long EXPECTED_FILE_SIZE = 2204;
        final int EXPECTED_NUMBER_OF_COMMITS = 14;
        final int EXPECTED_NUMBER_OF_AUTHORS = -1;

        // Create test objects
        Codebase codebase = Codebase.getInstance();
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
        repositoryAnalyzer.attachBranchNameList(codebase);
        codebase.branchSelected("development");
        repositoryAnalyzer.attachCodebaseData(codebase); // method being tested

        // Verify the results
        FileObject fileObject = codebase.getFileObjectFromFilename(TEST_FILE_NAME);
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
        // Ensures we have the right object
        assertEquals(TEST_FILE_NAME, fileObject.getFilename());
        // Check data
        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
        assertEquals(EXPECTED_FILE_SIZE, heatObject.getFileSize());
        assertEquals(EXPECTED_NUMBER_OF_COMMITS, heatObject.getNumberOfCommits());
        assertEquals(EXPECTED_NUMBER_OF_AUTHORS, heatObject.getNumberOfAuthors());
        // TODO CHECK AUTHOR EMAIL VALUES
        // TODO CHECK AUTHOR NAME VALUES
    }

    //same as above test case but with different data
    @Test
    void attachCodebaseData_FileHeatMetrics_CodebaseInsightsToolWindowFactoryHash419c65_Success() throws IOException, GitAPIException {
        // Set up test data
        final String TEST_HASH = "419c658dadf645d647ea9bf3068bac588cdff740";
        final String TEST_BLOB_ID = "ayeeeee update me"; // Taken from output logs
        final String TEST_FILE_NAME = "CodebaseInsightsToolWindowFactory.java";
        final long EXPECTED_LINE_COUNT = 56;
        final long EXPECTED_FILE_SIZE = 2204;
        final int EXPECTED_NUMBER_OF_COMMITS = 16;
        final int EXPECTED_NUMBER_OF_AUTHORS = -1;

        //Create test objects
        Codebase codebase = Codebase.getInstance();
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
        repositoryAnalyzer.attachBranchNameList(codebase);
        codebase.branchSelected("development");
        repositoryAnalyzer.attachCodebaseData(codebase); //method being tested

        // Verify the results
        FileObject fileObject = codebase.getFileObjectFromFilename(TEST_FILE_NAME);
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
        // Ensures we have the right object
        assertEquals(TEST_FILE_NAME, fileObject.getFilename());
        // Check data
        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
        assertEquals(EXPECTED_FILE_SIZE, heatObject.getFileSize());
        assertEquals(EXPECTED_NUMBER_OF_COMMITS, heatObject.getNumberOfCommits());
        assertEquals(EXPECTED_NUMBER_OF_AUTHORS, heatObject.getNumberOfAuthors());
        // TODO CHECK AUTHOR EMAIL VALUES
        // TODO CHECK AUTHOR NAME VALUES
    }

    //same as above test case but with different data
    @Test
    void attachCodebaseData_FileHeatMetrics_TestDataHashfb5b5d_Success() throws IOException, GitAPIException {
        final String TEST_HASH = "fb5b5d27631eee85b16b63ae84873975706d4f4a";
        final String TEST_BLOB_ID = "ayeeeee update me"; // Taken from output logs
        final String TEST_FILE_NAME = "TestData.java";
        final long EXPECTED_LINE_COUNT = 44;
        final long EXPECTED_FILE_SIZE = 2462;
        final int EXPECTED_NUMBER_OF_COMMITS = 16;
        final int EXPECTED_NUMBER_OF_AUTHORS = -1;

        //Create test objects
        Codebase codebase = Codebase.getInstance();
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
        repositoryAnalyzer.attachBranchNameList(codebase);
        codebase.branchSelected("development");
        repositoryAnalyzer.attachCodebaseData(codebase); //method being tested

        // Verify the results
        FileObject fileObject = codebase.getFileObjectFromFilename(TEST_FILE_NAME);
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
        // Ensures we have the right object
        assertEquals(TEST_FILE_NAME, fileObject.getFilename());
        // Check data
        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
        assertEquals(EXPECTED_FILE_SIZE, heatObject.getFileSize());
        assertEquals(EXPECTED_NUMBER_OF_COMMITS, heatObject.getNumberOfCommits());
        assertEquals(EXPECTED_NUMBER_OF_AUTHORS, heatObject.getNumberOfAuthors());
        // TODO CHECK AUTHOR EMAIL VALUES
        // TODO CHECK AUTHOR NAME VALUES
    }

    //same as above test case but with different data
    @Test
    void attachCodebaseData_FileHeatMetrics_CodeBaseObservableHash9db512_Success() throws IOException, GitAPIException {
        final String TEST_HASH = "723a3eae7a8524b06733e9568f1b2240a0537b0b";
        final String TEST_FILE_NAME = "CodeBaseObservable.java";
        final long EXPECTED_LINE_COUNT = 21;
        final long EXPECTED_FILE_SIZE = 675;
        final int EXPECTED_NUMBER_OF_COMMITS = 2;
        final int EXPECTED_NUMBER_OF_AUTHORS = -1;

        //Create test objects
        Codebase codebase = Codebase.getInstance();
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(PROJECT_ROOT);
        repositoryAnalyzer.attachBranchNameList(codebase);
        codebase.branchSelected("view-model-communication");
        repositoryAnalyzer.attachCodebaseData(codebase); //method being tested

        // Verify the results
        FileObject fileObject = codebase.getFileObjectFromFilename(TEST_FILE_NAME);
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
        // Ensures we have the right object
        assertEquals(TEST_FILE_NAME, fileObject.getFilename());
        // Check data
        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
        assertEquals(EXPECTED_FILE_SIZE, heatObject.getFileSize());
        assertEquals(EXPECTED_NUMBER_OF_COMMITS, heatObject.getNumberOfCommits());
        assertEquals(EXPECTED_NUMBER_OF_AUTHORS, heatObject.getNumberOfAuthors());
        // TODO CHECK AUTHOR EMAIL VALUES
        // TODO CHECK AUTHOR NAME VALUES
    }
}