package intellij_extension.observer;

import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.Iterator;

public interface CodeBaseObservable
{
    void notifyObserversOfRefreshHeatMap();

    void notifyObserversOfRefreshFileCommitHistory(FileObject selectedFile, Iterator<Commit> filesCommits);

    void notifyObserversOfRefreshCommitDetails(Commit commit, Iterator<DiffEntry> fileDiffs);

    void registerObserver(CodeBaseObserver observer);

    void unregisterObserver(CodeBaseObserver observer);
}
