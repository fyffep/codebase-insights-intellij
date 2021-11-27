package intellij_extension.utility;

import intellij_extension.models.redesign.FileObject;

import java.util.*;

public class GroupFileObjectUtility {

    private GroupFileObjectUtility() {
        //This is a utility class
    }


    /**
     * Returns a TreeMap(for sorting capabilities) of package names to the list of files contained in each respective
     * package. Each package name includes only the folders inside the user's project.
     * <br/>
     * Example: If the project root path is "C:\Users\Dummy\my-project" and a FileObject has a path
     * "C:\Users\Dummy\my-project\package1\package2\my-file.java", then the package name for my-file.java
     * will be "\package1\package2\"
     */
    public static TreeMap<String, TreeSet<FileObject>> groupByPackage(String projectRootPath, HashSet<FileObject> activeFiles) {
        // <Package, <Set of Files in package>>
        // TreeMap sorts by string natural order when keys are added
        // TreeSet sorts by comparator below when entries are added to set
        TreeMap<String, TreeSet<FileObject>> packageToFileMap = new TreeMap<>(String::compareTo);;

        // Sorting for TreeSet
        Comparator<FileObject> FILE_NAME = Comparator.comparing(FileObject::getFilename);

        for (FileObject fileObject : activeFiles) {
            // Obtain the package name by removing the FileObject's absolute path part and file name from its path
            String packageName = fileObject.getPath().toString().replace(projectRootPath, "")
                    .replace(fileObject.getFilename(), "");
            // Add the FileObject under the package name
            packageToFileMap.computeIfAbsent(packageName, k -> new TreeSet<>(FILE_NAME)).add(fileObject);
        }

        return packageToFileMap;
    }
}
