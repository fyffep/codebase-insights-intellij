package intellij_extension.models.redesign;

import java.nio.file.Path;
import java.util.LinkedHashMap;

/**
 * FileObjectV2
 * - filename acts as ID
 * - Path path
 * - Map<CommitHash, HeatObject>;
 * - HeatObject has the metrics for this file per commit
 */
public class FileObjectV2 {

    private Path path;
    // Id = filename = id;
    private String filename;
    private LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap;

    // This would maintain the latest key commit hash added in the map to avoid any traversal again
    private String latestCommit;

    public FileObjectV2() {

    }

    public FileObjectV2(Path path, String filename) {
        this.path = path;
        this.filename = filename;
        this.commitHashToHeatObjectMap = new LinkedHashMap<>();
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
        //else: create a new HeatObject
        HeatObject newHeatObject = new HeatObject();
        //System.out.println("Put HeatObject for file "+filename+" at commit "+commitHash);
        commitHashToHeatObjectMap.put(commitHash, newHeatObject);
        return newHeatObject;
    }

    // TODO - DECISION - Do we want to throw an exception or return null when commitHash not found?
    public LinkedHashMap<String, HeatObject> getCommitHashToHeatObjectMap() {
        return commitHashToHeatObjectMap;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public void setHeatForCommit(String commitHash, HeatObject heat) {
        // commitHash already present - was this intentional?
        if (commitHashToHeatObjectMap.putIfAbsent(commitHash, heat) != null) {
            throw new UnsupportedOperationException(String.format("Commit hash %s is already present in %s's commitHashToHeatObjectMap.", commitHash, filename));
        }

        this.latestCommit = commitHash;
    }

    /**
     * Returns the total heat
     * @return
     */
    public int getOverallHeat()
    {
        return (int) (Math.random() % 10); //TODO calculate overall heat here (this is a placeholder)
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == getClass()) {
            FileObjectV2 fileObject = (FileObjectV2) object;
            if (this.getFilename().equals(fileObject.getFilename())) return true;
        }
        return false;
    }
}
