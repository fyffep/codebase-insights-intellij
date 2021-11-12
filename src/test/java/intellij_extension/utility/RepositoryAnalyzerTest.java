package intellij_extension.utility;

import intellij_extension.models.FileObject;
import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.FileObjectV2;
import intellij_extension.models.redesign.HeatObject;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.TestData.*;

public class RepositoryAnalyzerTest
{
    @Test
    void defaultConstructorTest()
    {
        assertDoesNotThrow(() -> {
            File projectPath = new File("C:\\Users\\Pete\\Desktop\\team3-project");
            RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(projectPath);
        });
    }

    @Test
    void obtainFileContentsTest() throws IOException
    {
        File projectPath = new File("C:\\Users\\Pete\\Desktop\\team3-project"); //FIXME
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(projectPath);

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
        final String TEST_FILE_PATH = "intellij_extension/CodebaseInsightsToolWindowFactory.java";
        final String TEST_FILE_NAME = "intellij_extension/CodebaseInsightsToolWindowFactory.java";
        final String TEST_HASH = "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a";
        File projectPath = new File("C:\\Users\\Pete\\Desktop\\team3-project"); //FIXME
        RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer(projectPath);

        //Build a dummy model
        CodebaseV2 codebase = new CodebaseV2();
        FileObjectV2 fileObjectDummy = new FileObjectV2(Paths.get(TEST_FILE_PATH), TEST_FILE_NAME, new HashMap<>());

        //Compute the size of an old version of a file
        int EXPECTED_LINE_COUNT = 55;
        repositoryAnalyzer.attachLineCountToCodebase(codebase, TEST_HASH); //method being tested

        //Assert the output
        FileObjectV2 fileObject = codebase.getFileObjectFromId(TEST_HASH);
        HeatObject heatObject = fileObject.getHeatObjectAtCommit(TEST_HASH);
        assertEquals(EXPECTED_LINE_COUNT, heatObject.getLineCount());
        System.out.println("File size is"+heatObject.getFileSize());
    }
}
