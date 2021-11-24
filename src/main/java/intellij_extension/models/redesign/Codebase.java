package intellij_extension.models.redesign;

import intellij_extension.Constants;
import intellij_extension.observer.CodeBaseObservable;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.utility.RepositoryAnalyzer;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Codebase implements CodeBaseObservable {

    // region Vars
    private static Codebase instance; //singleton
    private final List<CodeBaseObserver> observerList = new LinkedList<>();
    private final LinkedHashSet<String> branchNameList;
    private String activeBranch;
    private LinkedHashSet<Commit> activeCommits;
    private LinkedHashSet<FileObject> activeFileObjects;
    // endregion

    // region Singleton Constructor
    private Codebase() {
        activeBranch = "master";
        branchNameList = new LinkedHashSet<>();
        activeCommits = new LinkedHashSet<>();
        activeFileObjects = new LinkedHashSet<>();
    }

    public static Codebase getInstance() {
        if (instance == null) {
            //synchronized block to remove overhead
            synchronized (Codebase.class) {
                // if instance is null, initialize
                if (instance == null) {
                    instance = new Codebase();
                    System.out.println("Model (Codebase) has been created"); //logger doesn't work here
                }
            }
        }
        return instance;
    }
    // endregion

    public void selectDefaultBranch() {
        String branch = "";
        for (String defaultBranch : Constants.DEFAULT_BRANCHES) {
            if (branchNameList.contains(defaultBranch.toLowerCase())) {
                branch = defaultBranch;
                break;
            }
        }

        // Means no default branches are in branchNameList
        if (branch.isEmpty()) {
            // So, just grab the first branch
            branch = branchNameList.stream().findFirst().get();
        }

        activeBranch = branch;
    }

    // region Getters
    public LinkedHashSet<Commit> getActiveCommits() {
        return activeCommits;
    }

    public HashSet<FileObject> getActiveFileObjects() {
        return activeFileObjects;
    }

    public String getActiveBranch() {
        return activeBranch;
    }

    public LinkedHashSet<String> getBranchNameList() {
        return branchNameList;
    }

    // Should only be used when building model data
    public FileObject createOrGetFileObjectFromPath(String path) {
        FileObject selectedFile = activeFileObjects.stream()
                .filter(file -> file.getFilename().equals(RepositoryAnalyzer.getFilename(path))).findAny().orElse(null);

        // Failed to find file associated with param id
        if (selectedFile == null) {
            // Create and return new FileObject
            selectedFile = new FileObject(Paths.get(path));
            activeFileObjects.add(selectedFile);
        }
        return selectedFile;
    }

    // If not building model data we want a null return
    // This ensures we know something went wrong. Which means we are looking for a filename that doesn't exist in our model's data
    public FileObject getFileObjectFromFilename(String filename) {
        FileObject selectedFile = activeFileObjects.stream()
                .filter(file -> file.getFilename().equals(filename)).findAny().orElse(null);

        return selectedFile;
    }

    public Commit getCommitFromCommitHash(String commitHash) {
        Commit selectedCommit = activeCommits.stream()
                .filter(commit -> commit.getHash().equals(commitHash)).findAny().orElse(null);

        // Failed to find file associated with param id
        if (selectedCommit == null) {
            throw new NullPointerException(String.format("Failed to find the proper commit associated with the selected commit in the TableView. Hash = %s", commitHash));
        }

        return selectedCommit;
    }
    // endregion

    // region Controller Communication
    public void heatMapComponentSelected(String path) {
        FileObject selectedFile = getFileObjectFromFilename(RepositoryAnalyzer.getFilename(path));

        // Get commits associated with file
        ArrayList<Commit> associatedCommits = (ArrayList<Commit>) activeCommits.stream()
                .filter(commit -> commit.getFileSet().contains(selectedFile.getFilename()))
                .collect(Collectors.toList());

        notifyObserversOfRefreshFileCommitHistory(selectedFile, associatedCommits);
    }

    public void branchListRequested() {
        notifyObserversOfBranchList();
    }

    public void branchSelected(String branchName) {
        // Branch doesn't exist - or we don't know about it some how...
        if (!branchNameList.contains(branchName) && !branchName.isEmpty()) {
            throw new UnsupportedOperationException(String.format("Branch %s was selected but is not present in branchNameList.", branchName));
        }

        this.activeBranch = branchName;

        // Dump old data and create new sets
        activeCommits.clear();
        activeCommits = new LinkedHashSet<>();
        activeFileObjects.clear();
        activeFileObjects = new LinkedHashSet<>();

        // TODO REBUILD MODEL DATA

        notifyObserversOfBranchChange();
    }

    public void commitSelected(String commitHash) {
        Commit selectedCommit = getCommitFromCommitHash(commitHash);

        notifyObserversOfRefreshCommitDetails(selectedCommit);
    }

    public void changeHeatMapToCommit(String commitHash) {
        System.out.println("Update HeatMapComponents to this commitHash: " + commitHash);
        // TODO - Implement UI and backend logic.
    }

    public void openFile(String filename) {
        FileObject selectedFile = getFileObjectFromFilename(filename);
        System.out.println("Open file in intellij: " + selectedFile.getFilename());
        // TODO - How to open this file via Intellij
    }
    // endregion

    // region Observable Methods
    @Override
    public void notifyObserversOfRefreshHeatMap() {
        for (CodeBaseObserver observer : observerList) {
            observer.refreshHeatMap(this);
        }
    }

    @Override
    public void notifyObserversOfBranchList() {
        for (CodeBaseObserver observer : observerList) {
            observer.branchListRequested(activeBranch, branchNameList.iterator());
        }
    }

    @Override
    public void notifyObserversOfBranchChange() {
        for (CodeBaseObserver observer : observerList) {
            observer.branchSelected();
        }
    }

    @Override
    public void notifyObserversOfRefreshFileCommitHistory(FileObject selectedFile, ArrayList<Commit> filesCommits) {
        for (CodeBaseObserver observer : observerList) {
            observer.fileSelected(selectedFile, filesCommits.iterator());
        }
    }

    @Override
    public void notifyObserversOfRefreshCommitDetails(Commit commit) {
        for (CodeBaseObserver observer : observerList) {
            observer.commitSelected(commit);
        }
    }

    @Override
    public void registerObserver(CodeBaseObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void unregisterObserver(CodeBaseObserver observer) {
        observerList.remove(observer);
    }
    // endregion
}
