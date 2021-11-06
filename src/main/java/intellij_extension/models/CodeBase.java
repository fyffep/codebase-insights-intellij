package intellij_extension.models;

import java.util.List;

public class CodeBase
{
    private List<String> branchName;
    private String activeBranch;
    private List<Commit> commitsInBranch;
    private Commit activeCommit = new Commit(); //TODO TEMP This should be set when the repos is loaded in
    //private int[] heatAccumulations; //length: number of commits


    public List<String> getBranchName() {
        return branchName;
    }

    public void setBranchName(List<String> branchName) {
        this.branchName = branchName;
    }

    public String getActiveBranch() {
        return activeBranch;
    }

    public void setActiveBranch(String activeBranch) {
        this.activeBranch = activeBranch;
    }

    public List<Commit> getCommitsInBranch() {
        return commitsInBranch;
    }

    public void setCommitsInBranch(List<Commit> commitsInBranch) {
        this.commitsInBranch = commitsInBranch;
    }

    public Commit getActiveCommit() {
        return activeCommit;
    }

    public void setActiveCommit(Commit activeCommit) {
        this.activeCommit = activeCommit;
    }
}
