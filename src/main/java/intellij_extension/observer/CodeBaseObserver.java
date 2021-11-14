package intellij_extension.observer;


import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.Iterator;

public interface CodeBaseObserver {
    void refreshHeatMap(Codebase codeBase);

    void branchSelected();

    void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits);

    void commitSelected(Commit commit, Iterator<DiffEntry> fileDiffs);
}
