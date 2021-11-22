package intellij_extension.models.redesign;

import intellij_extension.utility.RepositoryAnalyzer;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    // region Variables
    private Path path;
    private String filename;
    private LinkedHashMap<String, HeatObject> commitHashToHeatObjectMap;
    private Set<String> uniqueAuthors;
    private Set<String> uniqueAuthorEmails;
    // This would maintain the latest key commit hash added in the map to avoid any traversal again
    private String latestCommit;

    // FIXME implement me properly along with latest commit
    //  This is just an easy hacky way to sort FileObjects in a list.
    public int latestCommitHeatLevel;
    // endregion

    // region Constructors
    public FileObject() {
        //Empty constructor
    }

    public FileObject(Path path) {
        this.path = path;
        this.filename = RepositoryAnalyzer.getFilename(this.path.toString());
        System.out.printf("FileObject Constructor: Filename %s from path %s.%n", this.filename, this.path.toString());
        this.commitHashToHeatObjectMap = new LinkedHashMap<>();
        this.uniqueAuthors = new LinkedHashSet<>();
        this.uniqueAuthorEmails = new LinkedHashSet<>();
    }
    // endregion

    public Path getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = Paths.get(path);
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    // Find/return existing or create new HeatObject for commitHash
    public HeatObject getHeatObjectAtCommit(String commitHash) {
        HeatObject existingHeatObject = commitHashToHeatObjectMap.get(commitHash);

        if(existingHeatObject != null) {
            return existingHeatObject;
        } else {
            HeatObject newHeatObject = new HeatObject();
            commitHashToHeatObjectMap.put(commitHash, newHeatObject);
            return newHeatObject;
        }
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
