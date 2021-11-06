package intellij_extension.utility;

import intellij_extension.models.FileObject;

import java.util.HashMap;

public interface HeatCalculator
{
    /**
     * Changes the HashMap passed to it so that FileObjects inside of it
     * are either added (if they do not exist) or given more data.
     * @param existingFileMetricMap this holds all file data for one commit in the project
     * @return the input map but with all FileObjects in the project given more data.
     */
    HashMap<String, FileObject> editFileMetricMap(HashMap<String, FileObject> existingFileMetricMap);
    //Alernatively:
    //HashMap<String, FileObject> computeFileMetricMap();
}