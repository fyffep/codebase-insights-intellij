package intellij_extension.observer;

import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.CommitV2;
import intellij_extension.models.redesign.FileObjectV2;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.Iterator;

public interface CodeBaseObservable
{
    void notifyObserversOfRefreshHeatMap();

    void notifyObserversOfRefreshFileCommitHistory(FileObjectV2 selectedFile, Iterator<CommitV2> filesCommits);

    void notifyObserversOfRefreshCommitDetails(CommitV2 commit, Iterator<DiffEntry> fileDiffs);

    void registerObserver(CodeBaseObserver observer);

    void unregisterObserver(CodeBaseObserver observer);
}
