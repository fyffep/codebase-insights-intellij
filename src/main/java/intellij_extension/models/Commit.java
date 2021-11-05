package intellij_extension.models;

import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Commit
{
    private RevCommit revCommit; //data from JGit
    private HashMap<String, FileObject> fileMetricMap = new HashMap<>();

    public RevCommit getRevCommit() {
        return revCommit;
    }

    public void setRevCommit(RevCommit revCommit) {
        this.revCommit = revCommit;
    }

    public HashMap<String, FileObject> getFileMetricMap() {
        return fileMetricMap;
    }

    public void setFileMetricMap(HashMap<String, FileObject> fileMetricMap) {
        this.fileMetricMap = fileMetricMap;
    }
}
