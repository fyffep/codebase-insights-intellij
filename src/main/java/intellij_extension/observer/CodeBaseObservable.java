package intellij_extension.observer;

import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;

import java.util.ArrayList;

public interface CodeBaseObservable {
    void notifyObserversOfRefreshHeatMap();

    // branchListRequested
    void notifyObserversOfBranchList();

    // branchSelected
    void notifyObserversOfBranchChange();

    // fileSelected
    void notifyObserversOfRefreshFileCommitHistory(FileObject selectedFile, ArrayList<Commit> filesCommits);

    // commitSelected
    void notifyObserversOfRefreshCommitDetails(Commit commit);

    void registerObserver(CodeBaseObserver observer);

    void unregisterObserver(CodeBaseObserver observer);
}
