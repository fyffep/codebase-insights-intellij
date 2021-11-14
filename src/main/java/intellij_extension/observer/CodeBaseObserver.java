package intellij_extension.observer;


import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.CommitV2;
import intellij_extension.models.redesign.FileObjectV2;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.Iterator;

public interface CodeBaseObserver {
    void refreshHeatMap(CodebaseV2 codeBase);

    void branchSelected();

    void fileSelected(FileObjectV2 selectedFile, Iterator<CommitV2> filesCommits);

    void commitSelected(CommitV2 commit, Iterator<DiffEntry> fileDiffs);
}
