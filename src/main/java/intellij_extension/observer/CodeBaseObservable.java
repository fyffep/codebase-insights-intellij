package intellij_extension.observer;

import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.ArrayList;

public interface CodeBaseObservable {
    void notifyObserversOfRefreshHeatMap();

    void notifyObserversOfRefreshFileCommitHistory(FileObject selectedFile, ArrayList<Commit> filesCommits);

    void notifyObserversOfRefreshCommitDetails(Commit commit, ArrayList<DiffEntry> fileDiffs);

    void registerObserver(CodeBaseObserver observer);

    void unregisterObserver(CodeBaseObserver observer);
}
