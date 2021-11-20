package intellij_extension.utility;

import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.FileObject;

import java.util.*;

public class GroupFileObjectUtility
{
    private GroupFileObjectUtility() {
        //This is a utility class
    }


    /**
     * Returns a HashMap of package names to the list of files contained in each respective
     * package. Each package name includes only the folders inside the user's project.
     * <br/>
     * Example: If the project root path is "C:\Users\Dummy\my-project" and a FileObject has a path
     * "C:\Users\Dummy\my-project\package1\package2\my-file.java", then the package name for my-file.java
     * will be "\package1\package2\"
     */
    public static HashMap<String, ArrayList<FileObject>> groupByPackage(Codebase codebase)
    {
        final String projectRootPath = codebase.getProjectRootPath();
        HashMap<String, ArrayList<FileObject>> packageToFileMap = new HashMap<>();
        Iterator<FileObject> fileObjectIterator = codebase.getActiveFileObjects().iterator();
        while (fileObjectIterator.hasNext())
        {
            FileObject fileObject = fileObjectIterator.next();

            //Obtain the package name by removing the FileObject's absolute path part and file name from its path
            String packageName = fileObject.getPath().toString().replace(projectRootPath, "")
                    .replace(fileObject.getFilename(), "");
            //Add the FileObject under the package name
            packageToFileMap.computeIfAbsent(packageName, k -> new ArrayList<>()).add(fileObject);
        }

        return packageToFileMap;
    }
}
