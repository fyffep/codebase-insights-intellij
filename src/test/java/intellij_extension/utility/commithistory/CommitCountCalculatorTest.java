package intellij_extension.utility.commithistory;

import intellij_extension.models.FileObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static testdata.TestData.NUMBER_OF_COMMITS_IN_GITIGNORE;

/**
 * Checks that the .gitignore file from this plugin is being measured with
 * the correct number of commits.
 */
public class CommitCountCalculatorTest
{
    /**
     * Since it's a little tricky to test whether or not JGit is working,
     * this test just checks if the CommitCountCalculator threw an IOException
     * (which is caused by the repository not being opened correctly).
     */
    @Test
    public void defaultConstructorTest()
    {
        assertDoesNotThrow(() -> {
            CommitCountCalculator commitCountCalculator = new CommitCountCalculator(); //method under test
            System.out.println("Created a " + commitCountCalculator + " with a default constructor. " +
                    "This print message prevents an unused variable code smell.");
        });
    }

    //Ensure that the .gitignore has the expected number of commits
    @Test
    public void calculateNumberOfCommitsPerFileTest() throws IOException, GitAPIException
    {
        CommitCountCalculator commitCountCalculator = new CommitCountCalculator();
        //Method under test
        HashMap<String, Integer> filePathToCommitCountMap =
                commitCountCalculator.calculateNumberOfCommitsPerFile(commitCountCalculator.getAllCommits());

        assertEquals(NUMBER_OF_COMMITS_IN_GITIGNORE, (int) filePathToCommitCountMap.get(".gitignore"));
    }

    //Ensure that the .gitignore has the expected number of commits, and also check that the data is placed onto a HashMap correctly
    @Test
    public void editFileMetricMapTest() throws IOException, GitAPIException
    {
        CommitCountCalculator commitCountCalculator = new CommitCountCalculator();

        HashMap<String, FileObject> existingFileMetricMap = new HashMap<>();
        existingFileMetricMap =
                commitCountCalculator.editFileMetricMap(existingFileMetricMap); //method under test

        assertEquals(NUMBER_OF_COMMITS_IN_GITIGNORE, (int) existingFileMetricMap.get(".gitignore").getNumberOfCommits());
    }
}
