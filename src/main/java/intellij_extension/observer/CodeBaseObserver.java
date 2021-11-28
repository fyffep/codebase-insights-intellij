package intellij_extension.observer;


import intellij_extension.Constants.GroupingMode;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public interface CodeBaseObserver {
    void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode);

    // notifyObserversOfBranchList
    void branchListRequested(String activeBranch, Iterator<String> branchList);

    // notifyObserversOfBranchChange
    void newBranchSelected(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode);

    // notifyObserversOfRefreshFileCommitHistory
    void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits);

    // notifyObserversOfRefreshCommitDetails
    void commitSelected(Commit commit);
}
