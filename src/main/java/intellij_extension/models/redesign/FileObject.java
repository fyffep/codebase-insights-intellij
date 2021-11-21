package intellij_extension.models.redesign;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * FileObjectV2
 * - filename acts as ID
 * - Path path
 * - Map<CommitHash, HeatObject>;
 * - HeatObject has the metrics for this file per commit
 */
public class FileObject {

    private Path path;
    // Id = filename = id;
    private String filename;
    private LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap;
    private Set<String> uniqueAuthors;
    private Set<String> uniqueAuthorEmails;


    // This would maintain the latest key commit hash added in the map to avoid any traversal again
    private String latestCommit;
    // FIXME implement me properly along with latest commit
    //  This is just an easy hacky way to sort FileObjects in  a list.
    public int latestCommitHeatLevel;

    public FileObject() {
        //Empty constructor
    }

    public FileObject(Path path, String filename) {
        this.path = path;
        this.filename = filename;
        this.commitHashToHeatObjectMap = new LinkedHashMap<>();
        this.uniqueAuthors = new LinkedHashSet<>();
        this.uniqueAuthorEmails = new LinkedHashSet<>();
    }

    public Path getPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }

    public Set<String> getUniqueAuthors() {
        return uniqueAuthors;
    }

    public Set<String> getUniqueAuthorEmails() {
        return uniqueAuthorEmails;
    }

    public int getLatestCommitHeatLevel() {
        return latestCommitHeatLevel;
    }

    /**
     * Returns a HeatObject that measures the heat of a certain version of this file.
     * If no HeatObject exists for this file, returns a new (blank) HeatObject.
     *
     * @param commitHash a Git commit hash, such as "1e589e61ef75003b1df88bdb738f9d9f4a4f5f8a" that the file is present in
     */
    public HeatObject getHeatObjectAtCommit(String commitHash) {
        if (commitHashToHeatObjectMap.containsKey(commitHash))
            return commitHashToHeatObjectMap.get(commitHash);
        //else: create a new HeatObject
        HeatObject newHeatObject = new HeatObject();
        commitHashToHeatObjectMap.put(commitHash, newHeatObject);
        return newHeatObject;
    }

    public LinkedHashMap<String, HeatObject> getCommitHashToHeatObjectMap() {
        return commitHashToHeatObjectMap;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(String latestCommit) {
        this.latestCommit = latestCommit;
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
     *
     * @return
     */
    public int getOverallHeat() {
        return (int) (Math.random() % 10); //TODO calculate overall heat here (this is a placeholder)
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == getClass()) {
            FileObject fileObject = (FileObject) object;
            return this.getFilename().equals(fileObject.getFilename());
        }
        return false;
    }

    public int compareTo(FileObject other) {
        if (this.latestCommitHeatLevel > other.latestCommitHeatLevel) {
            return 1;
        } else {
            return -1;
        }
    }
}
