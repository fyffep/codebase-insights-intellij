package intellij_extension.utility;

import intellij_extension.models.FileObject;
import org.junit.jupiter.api.Test;

import java.io.*;

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
        InputStream in = repositoryAnalyzer.obtainFileContents(TEST_FILE, TEST_HASH);


        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        int lineCount = 0;
        while (reader.readLine() != null) lineCount++;
        reader.close();
        in.close();

        assertEquals(EXPECTED_LINE_COUNT, lineCount);
    }
}
