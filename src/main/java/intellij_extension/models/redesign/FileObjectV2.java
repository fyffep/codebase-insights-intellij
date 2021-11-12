package intellij_extension.models.redesign;

import intellij_extension.Constants;

import java.nio.file.Path;
import java.util.HashMap;

/**
 * FileObjectV2
 * - filename acts as ID
 * - Path path
 * - Map<CommitHash, HeatObject>;
 * - HeatObject has the metrics for this file per commit
 */
public class FileObjectV2 {

    private Path path;
    private String filename;
    private HashMap<String, HeatObject> commitHashToHeatObjectMap;

    public FileObjectV2() {

    }

    public FileObjectV2(Path path, String filename, HashMap<String, HeatObject> commitHashToHeatObjectMap) {
        this.path = path;
        this.filename = filename;
        this.commitHashToHeatObjectMap = commitHashToHeatObjectMap;
    }

    public Path getPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }

    /**
     * Returns a HeatObject that measures the heat of a certain version of this file.
     * If no HeatObject exists for this file, returns a new (blank) HeatObject.
     * @param commitHash a Git commit hash, such as "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a" that the file is present in
     */
    public HeatObject getHeatObjectAtCommit(String commitHash) {
        if (commitHashToHeatObjectMap.containsKey(commitHash))
            return commitHashToHeatObjectMap.get(commitHash);
        //else
        return new HeatObject();
    }

    // TODO - DECISION - Do we want to throw an exception or return null when commitHash not found?
    public HeatObject getHeatForCommit(String commitHash) {
        // commitHash not found
        if (!commitHashToHeatObjectMap.containsKey(commitHash)) {
            Constants.LOG.error(
                    String.format("Commit hash %s was not found in %s's commitHashToHeatObjectMap. Returning null.", commitHash, filename));
        }

        //commitHash found
        return commitHashToHeatObjectMap.get(commitHash);
    }

    public void setHeatForCommit(String commitHash, HeatObject heat) {
        // commitHash already present - was this intentional?
        if (commitHashToHeatObjectMap.containsKey(commitHash)) {
            Constants.LOG.warn(
                    String.format("Commit hash %s is already present in %s's commitHashToHeatObjectMap, was this intentional?. Overriding with new HeatObject.", commitHash, filename));
        }

        // Add or replace with new HeatObject
        commitHashToHeatObjectMap.put(commitHash, heat);
    }
}
