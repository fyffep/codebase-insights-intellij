package intellij_extension.observer;


import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;

import java.util.Iterator;

public interface CodeBaseObserver {
    void refreshHeatMap(Codebase codeBase);

    void branchListRequested(String activeBranch, Iterator<String> branchList);

    void branchSelected();

    void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits);

    void commitSelected(Commit commit);
}
