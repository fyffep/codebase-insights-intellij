package intellij_extension.commithistory;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;

/**
 * Contains utility methods for opening Git repositories.
 * Credit to the JGit Cookbook for creating this class https://github.com/centic9/jgit-cookbook/tree/master/src/main/java/org/dstadler/jgit/helper
 */
public class JGitHelper
{
    public static Repository openLocalRepository() throws IOException
    {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
    }
}
