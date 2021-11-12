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

    public LinkedHashMap<String, HeatObject> getCommitHashToHeatObjectMap() {
        return commitHashToHeatObjectMap;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public HeatObject getHeatForCommit(String commitHash) {
        // commitHash not found
        if (!commitHashToHeatObjectMap.containsKey(commitHash)) {
            throw new UnsupportedOperationException(String.format("Commit hash %s was not found in %s's commitHashToHeatObjectMap.", commitHash, filename));
        }

        //commitHash if not found would return null and the calling method would log accordingly
        return commitHashToHeatObjectMap.getOrDefault(commitHash, null);
    }

    public void setHeatForCommit(String commitHash, HeatObject heat) {
        // commitHash already present - was this intentional?
        if (commitHashToHeatObjectMap.putIfAbsent(commitHash, heat) != null) {
            throw new UnsupportedOperationException(String.format("Commit hash %s is already present in %s's commitHashToHeatObjectMap.", commitHash, filename));
        }

        this.latestCommit = commitHash;
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
