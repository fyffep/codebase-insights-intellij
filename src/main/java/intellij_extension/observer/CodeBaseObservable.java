package intellij_extension.observer;

import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public interface CodeBaseObservable {
    void notifyObserversOfRefreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode);

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
