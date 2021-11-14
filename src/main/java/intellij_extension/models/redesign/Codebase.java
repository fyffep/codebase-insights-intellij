package intellij_extension.models.redesign;

import intellij_extension.Constants;
import intellij_extension.observer.CodeBaseObservable;
import intellij_extension.observer.CodeBaseObserver;
import org.eclipse.jgit.diff.DiffEntry;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Codebase implements CodeBaseObservable {
    private static Codebase instance; //singleton
    private final List<CodeBaseObserver> observerList = new LinkedList<>();

    private String activeBranch;
    private final  LinkedHashSet<String> branchNameList;
    private LinkedHashSet<Commit> activeCommits;
    private LinkedHashSet<FileObject> activeFileObjects;


    public Codebase() {
        activeBranch = "master";
        branchNameList = new LinkedHashSet<>();
        activeCommits = new LinkedHashSet<>();
        activeFileObjects = new LinkedHashSet<>();
    }

    /**
     * @return a singleton instance of this class.
     */
    public static Codebase getInstance() {
        if (instance == null) {
            //synchronized block to remove overhead
            synchronized (Codebase.class) {
                if (instance == null) {
                    // if instance is null, initialize
                    instance = new Codebase();
                }
            }
        }
        System.out.println("Model (Codebase) has been created"); //logger doesn't work here
        return instance;
    }

    public HashSet<FileObject> getActiveFileObjects() {
        return activeFileObjects;
    }

    public String getActiveBranch() {
        return activeBranch;
    }

    public void selectDefaultBranch() {
        String branch = "";
        for(String defaultBranch: Constants.DEFAULT_BRANCHES) {
            if(branchNameList.contains(defaultBranch.toLowerCase())) {
                branch = defaultBranch;
                break;
            }
        }

        // Means no default branches are in branchNameList
        if(branch.equals("")) {
            // So, just grab the first branch
            branch = branchNameList.stream().findFirst().get();
        }

        activeBranch = branch;
    }

    public LinkedHashSet<String> getBranchNameList() {
        return branchNameList;
    }

    public LinkedHashSet<Commit> getActiveCommits() {
        return activeCommits;
    }

    /**
     * @param path to file
     * @return a FileObject corresponding to the target path - if not corresponding FileObject found, it is created.
     */
    public FileObject createOrGetFileObjectFromPath(String path) {
        String fileName = new File(path).getName(); //convert file path to file name

        FileObject selectedFile = activeFileObjects.stream()
                .filter(file -> file.getFilename().equals(fileName)).findAny().orElse(null);

        // Failed to find file associated with param id
        if (selectedFile == null) {
            // Create and return new FileObject
            selectedFile = new FileObject(Paths.get(path), fileName);
            activeFileObjects.add(selectedFile);
        }

        return selectedFile;
    }

    /**
     * @param path to file
     * @return a FileObject corresponding to the target filename
     */
    private FileObject getFileObjectFromPath(String path) {
        String fileName = new File(path).getName(); //convert file path to file name

        FileObject selectedFile = activeFileObjects.stream()
                .filter(file -> file.getFilename().equals(fileName)).findAny().orElse(null);

        // Failed to find file associated with param path
        if (selectedFile == null) {
            throw new NullPointerException(String.format("Failed to find the proper file associated with the selected HeatMapObject. ID = %s", path));
        }

        return selectedFile;
    }

    /**
     * @param id a Git commit hash
     * @return a Commit corresponding to the target commit hash
     */
    public Commit getCommitFromId(String id) {
        Commit selectedCommit = activeCommits.stream()
                .filter(commit -> commit.getHash().equals(id)).findAny().orElse(null);

        // Failed to find file associated with param id
        if (selectedCommit == null) {
            throw new NullPointerException(String.format("Failed to find the proper commit associated with the selected commit in the TableView. Hash = %s", id));
        }

        return selectedCommit;
    }

    public void heatMapComponentSelected(String path) {
//        Constants.LOG.info("CLI: Controller told Model " + path + " was clicked. Extracting data.");
//        System.out.println("SOP: Controller told Model " + path + " was clicked. Extracting data.");

        FileObject selectedFile = getFileObjectFromPath(path);

        // Get commits associated with file
        ArrayList<Commit> associatedCommits = (ArrayList<Commit>) activeCommits.stream()
                .filter(commit -> commit.getFileSet().contains(selectedFile.getFilename()))
                .collect(Collectors.toList());

        notifyObserversOfRefreshFileCommitHistory(selectedFile, associatedCommits);
    }

    public void branchListRequested() {
        notifyObserversOfBranchList();
    }

    // TODO - Based on Pete's changes this needs a big update..
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

        /*try {
            buildBranchData(branchName);
        } catch (IOException e) {
            Constants.LOG.error("Exception throwing when building branch data!");
            Constants.LOG.error(e.getStackTrace());
        }*/

        // TODO need the observer relationship here
        //  Update HeatMapPane with new data
        //  Clear FileHistoryCommitPane
        //  Clear CommitDetailsPane
        //  Hide and Clear SelectedFilePane

    }

    public void commitSelected(String id) {
        Commit selectedCommit = getCommitFromId(id);

        ArrayList<DiffEntry> diffs = new ArrayList<>();

        notifyObserversOfRefreshCommitDetails(selectedCommit, diffs);
    }

    public void changeHeatMapToCommit(String commitHash) {
        System.out.println("Update HeatMapComponents to this commitHash: " + commitHash);
    }

    public void openFile(String id) {
        FileObject selectedFile = getFileObjectFromPath(id);
        System.out.println("Open file in intellij: " + selectedFile.getFilename());
        // TODO - How to open this file via Intellij
    }

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
    public void notifyObserversOfRefreshFileCommitHistory(FileObject selectedFile, ArrayList<Commit> filesCommits) {
//        Constants.LOG.info("CLI: Notifying view of change in data.");
//        System.out.println("SOP: Notifying view of change in data.");
        for (CodeBaseObserver observer : observerList) {
            observer.fileSelected(selectedFile, filesCommits.iterator());
        }
    }

    @Override
    public void notifyObserversOfRefreshCommitDetails(Commit commit, ArrayList<DiffEntry> fileDiffs) {
        for (CodeBaseObserver observer : observerList) {
            observer.commitSelected(commit, fileDiffs.iterator());
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
}
