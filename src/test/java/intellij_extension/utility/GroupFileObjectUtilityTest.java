package intellij_extension.utility;

import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.FileObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupFileObjectUtilityTest
{
    @Test
    void groupByPackageTest()
    {
        //Create test objects
        Codebase codebase = Codebase.getInstance();

        //Set up the Codebase with dummy data
        final String PROJECT_ROOT = "C:\\Users\\Dummy\\my-project";
        codebase.setProjectRootPath(PROJECT_ROOT);
        codebase.createOrGetFileObjectFromPath(PROJECT_ROOT + "\\package1\\myfileA.java");
        codebase.createOrGetFileObjectFromPath(PROJECT_ROOT + "\\package1\\myfileB.java");
        codebase.createOrGetFileObjectFromPath(PROJECT_ROOT + "\\package2\\myfileC.java");
        codebase.createOrGetFileObjectFromPath(PROJECT_ROOT + "\\package3\\myfileD.java");
        codebase.createOrGetFileObjectFromPath(PROJECT_ROOT + "\\package3\\myfileE.java");
        codebase.createOrGetFileObjectFromPath(PROJECT_ROOT + "\\package3\\myfileF.java");
        codebase.createOrGetFileObjectFromPath(PROJECT_ROOT + "\\package3\\package4\\myfileG.java");
        codebase.createOrGetFileObjectFromPath(PROJECT_ROOT + "\\package5\\package6\\myfileH.java");

        Map<String, ArrayList<FileObject>> packageToFileMap = GroupFileObjectUtility.groupByPackage(codebase); //method being tested

        //Verify that some of the above files were categorized under the correct packages
        assertTrue(packageToFileMap.containsKey("\\package1\\"));
        assertTrue(packageToFileMap.get("\\package1\\").stream()
                .anyMatch(fileObject -> fileObject.getFilename().equals("myfileA.java")));
        assertTrue(packageToFileMap.get("\\package1\\").stream()
                .anyMatch(fileObject -> fileObject.getFilename().equals("myfileB.java")));

        assertTrue(packageToFileMap.containsKey("\\package3\\"));
        assertTrue(packageToFileMap.get("\\package3\\").stream()
                .anyMatch(fileObject -> fileObject.getFilename().equals("myfileE.java")));

        assertTrue(packageToFileMap.containsKey("\\package3\\package4\\"));
        assertTrue(packageToFileMap.get("\\package3\\package4\\").stream()
                .anyMatch(fileObject -> fileObject.getFilename().equals("myfileG.java")));

        assertTrue(packageToFileMap.containsKey("\\package5\\package6\\"));
        assertTrue(packageToFileMap.get("\\package5\\package6\\").stream()
                .anyMatch(fileObject -> fileObject.getFilename().equals("myfileH.java")));
    }
}
