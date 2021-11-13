package intellij_extension.models;

import intellij_extension.observer.CodeBaseObservable;
import intellij_extension.observer.CodeBaseObserver;

import java.util.LinkedList;
import java.util.List;

public class CodeBase implements CodeBaseObservable
{
    private static CodeBase instance;

    private List<String> branchName;
    private String activeBranch;
    private List<Commit> commitsInBranch;
    private Commit activeCommit = new Commit(); //TODO TEMP This should be set when the repos is loaded in
    //private int[] heatAccumulations; //length: number of commits
    private List<CodeBaseObserver> observerList = new LinkedList<>();

    private CodeBase() {
        //Singleton
    }

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



    /**
     * @return a singleton instance of this class.
     */
    public static CodeBase getInstance()
    {
        if (instance == null) {
            //synchronized block to remove overhead
            synchronized (CodeBase.class) {
                if (instance == null) {
                    // if instance is null, initialize
                    instance = new CodeBase();
                }
            }
        }
        System.out.println("Model (CodeBase) has been created"); //logger doesn't work here
        return instance;
    }


    @Override
    public void notifyObservers()
    {
        for (CodeBaseObserver observer : observerList)
        {
            //observer.refresh(this);
        }
    }

    @Override
    public void registerObserver(CodeBaseObserver observer)
    {
        observerList.add(observer);
    }

    @Override
    public void unregisterObserver(CodeBaseObserver observer)
    {
        observerList.remove(observer);
    }
}
