package intellij_extension.models.redesign;

import intellij_extension.Constants;
import intellij_extension.observer.CodeBaseObservable;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.utility.HeatCalculationUtility;
import intellij_extension.utility.commithistory.CommitCountCalculator;
import intellij_extension.utility.filesize.FileSizeCalculator;
import javafx.util.Pair;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CodebaseV2 implements CodeBaseObservable {
    private static CodebaseV2 instance; //singleton
    private final List<CodeBaseObserver> observerList = new LinkedList<>();

    private String activeBranch;
    private LinkedHashSet<String> branchNameList;
    private LinkedHashSet<CommitV2> activeCommits;
    private LinkedHashSet<FileObjectV2> activeFileObjects;


    public CodebaseV2() {
        activeBranch = "master";
        branchNameList = new LinkedHashSet<>();
        activeCommits = new LinkedHashSet<>();
        activeFileObjects = new LinkedHashSet<>();
    }

    /**
     * @return a singleton instance of this class.
     */
    public static CodebaseV2 getInstance() {
        if (instance == null) {
            //synchronized block to remove overhead
            synchronized (CodebaseV2.class) {
                if (instance == null) {
                    // if instance is null, initialize
                    instance = new CodebaseV2();
                }
            }
        }
        System.out.println("Model (Codebase) has been created"); //logger doesn't work here
        return instance;
    }

    public HashSet<FileObjectV2> getActiveFileObjects() {
        return activeFileObjects;
    }

    public String getActiveBranch() {
        return activeBranch;
    }

    public void setActiveBranch(String activeBranch) { //FIXME from Pete-- this is just a quick fix so that I can test non-master branches, but it seems that there's another method for branch selection
        this.activeBranch = activeBranch;
    }

    public LinkedHashSet<String> getBranchNameList() {
        return branchNameList;
    }

    public LinkedHashSet<CommitV2> getActiveCommits() {
        return activeCommits;
    }

    /**
     * @param path to file
     * @return a FileObject corresponding to the target path - if not corresponding FileObject found, it is created.
     */
    public FileObjectV2 createOrGetFileObjectFromPath(String path) {
        String fileName = new File(path).getName(); //convert file path to file name

        FileObjectV2 selectedFile = activeFileObjects.stream()
                .filter(file -> file.getFilename().equals(fileName)).findAny().orElse(null);

        // Failed to find file associated with param id
        if (selectedFile == null) {
            // Create and return new FileObject
            selectedFile = new FileObjectV2(Paths.get(path), fileName);
            activeFileObjects.add(selectedFile);
        }

        return selectedFile;
    }

    /**
     * @param path the file name
     * @return a FileObject corresponding to the target filename
     */
    private FileObjectV2 getFileObjectFromPath(String path) {
        String fileName = new File(path).getName(); //convert file path to file name

        FileObjectV2 selectedFile = activeFileObjects.stream()
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
    public CommitV2 getCommitFromId(String id) {
        CommitV2 selectedCommit = activeCommits.stream()
                .filter(commit -> commit.getHash().equals(id)).findAny().orElse(null);

        // Failed to find file associated with param id
        if (selectedCommit == null) {
            throw new NullPointerException(String.format("Failed to find the proper commit associated with the selected commit in the TableView. Hash = %s", id));
        }

        return selectedCommit;
    }

    // TODO
    // Is this the JGit object?
    public void buildBranchData(String branch) throws IOException {
        Pair<Iterable<RevCommit>, TreeWalk> treeWalkCommitsPair = CommitCountCalculator.getCommitsAndTreeByBranch(branch);
        for (RevCommit revCommit : treeWalkCommitsPair.getKey()) {
            TreeWalk treeWalk = treeWalkCommitsPair.getValue();
            treeWalk.addTree(revCommit.getTree());
            CommitV2 newCommit = new CommitV2(revCommit);
            // Track newCommit object
            activeCommits.add(newCommit);

            while (treeWalk.next()) {
                //This can be used if there's a need for the File object to be stored
                //treeWalk.getObjectId(0).copyTo(System.out);
                String fileName = treeWalk.getNameString();
                String filePath = treeWalk.getPathString();
                ObjectLoader loader = CommitCountCalculator.getObjectLoader(treeWalk.getObjectId(0));

                FileObjectV2 fileObject = new FileObjectV2(Paths.get(filePath), fileName);

                // Nice, love me a good stream implementation
                // For those unfamiliar
                // Stream() = loop over all elements
                // Filter() = while looping over all elements only give me objects that match my filter
                // In this case if the above fileObject = a fileObject in the list already
                // FindAny() = Give me any object that satisfies the filter
                // OrElse() = If FindAny fails return the above fileObject (meaning there is no already created fileObject for this file)
                FileObjectV2 existingFileObject = activeFileObjects.stream()
                        .filter(fileObjectV2 -> fileObject.equals(fileObjectV2)).findAny().orElse(null);

                // First time adding this fileObject to the list.
                if (existingFileObject == null) {
                    activeFileObjects.add(fileObject);
                    existingFileObject = fileObject;
                }

                LinkedHashMap<String, HeatObject> commitHashMap = existingFileObject.getCommitHashToHeatObjectMap();
                HeatObject heatObject;
                if (commitHashMap.isEmpty()) {
                    // TODO - A question from Ethan
                    //  Why aren't we calculating the heat here?
                    heatObject = new HeatObject(1, fileName, FileSizeCalculator.getLineCount(filePath),
                            loader.getSize(), 1);
                } else {
                    heatObject = HeatCalculationUtility.computeHeatObjectFromHistory(commitHashMap, existingFileObject,
                            fileName, filePath, loader);
                }
                existingFileObject.setHeatForCommit(newCommit.getHash(), heatObject);
                activeFileObjects.add(existingFileObject);


                /** I gave the 2D array/HashBasedTable idea another thought and came to the conclusion that either way is
                 * a very memory-intensive approach.
                 * If we go via the 2D array approach, iterating through a huge one for instance 1000 indexes of primitives
                 * can cause a bottleneck. On the other way round, if we discard are to get away from the idea of 2D array,
                 * there is no need to maintain 0 as a value for broken links (i.e. files that were never a part of a commit)
                 * It would be too much to hold for a drop of blood.
                 * This leads us to see HashMaps v/s HashBasedTable v/s Set of fileNames in every CommitV2 object
                 * In this comparison, HashMaps would win for sizes ranging from 1 to 10000 and it is only beyond that value
                 * is when using HashBasedTable would actually make sense. If we are no longer maintaining the broken links (0)
                 * as a value, a simple HashMap would have done the job for us, However, it's better to just have a simple
                 * Set of fileName string per CommitV2 object.
                 * If the above justification sounds OK, we can remove the HashBasedTable init.
                 * */
                // TODO - Ethan's Comment - I prefer simplicity and to worry about size/slow down problems when that actually happens.
                //  Ethan's Comment - Like Prof. Rawlins said during the Command Pattern talk, do worry about it being too much data until it becomes a problem.
                //  Ethan's Comment - Another approach is to limit how far we go back in the history of a branch - Just an idea - Don't have to act on this.
                newCommit.addFileToSet(existingFileObject.getFilename());
            }
        }
    }

    public void heatMapComponentSelected(String path) {
        Constants.LOG.info("CLI: Controller told Model " + path + " was clicked. Extracting data.");
        System.out.println("SOP: Controller told Model " + path + " was clicked. Extracting data.");

        FileObjectV2 selectedFile = getFileObjectFromPath(path);

        // Get commits associated with file
        ArrayList<CommitV2> associatedCommits = (ArrayList<CommitV2>) activeCommits.stream()
                .filter(commit -> commit.getFileSet().contains(selectedFile.getFilename()))
                .collect(Collectors.toList());

        notifyObserversOfRefreshFileCommitHistory(selectedFile, associatedCommits.iterator());
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
        activeCommits = null; // Yeah... I know probably overkill.
        activeCommits = new LinkedHashSet<>();
        activeFileObjects.clear();
        activeFileObjects = null;
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
        CommitV2 selectedCommit = getCommitFromId(id);

        ArrayList<DiffEntry> diffs = new ArrayList<>();

        notifyObserversOfRefreshCommitDetails(selectedCommit, diffs.iterator());
    }

    public void changeHeatMapToCommit(String commitHash) {

    }

    public void openFile(String id) {
        FileObjectV2 selectedFile = getFileObjectFromPath(id);
        // TODO - How to open this file via Intellij
    }

    @Override
    public void notifyObserversOfRefreshHeatMap() {
        for (CodeBaseObserver observer : observerList) {
            observer.refreshHeatMap(this);
        }
    }

    @Override
    public void notifyObserversOfRefreshFileCommitHistory(FileObjectV2 selectedFile, Iterator<CommitV2> filesCommits) {
        Constants.LOG.info("CLI: Notifying view of change in data.");
        System.out.println("SOP: Notifying view of change in data.");
        for (CodeBaseObserver observer : observerList) {
            observer.fileSelected(selectedFile, filesCommits);
        }
    }

    @Override
    public void notifyObserversOfRefreshCommitDetails(CommitV2 commit, Iterator<DiffEntry> fileDiffs) {
        for (CodeBaseObserver observer : observerList) {
            observer.commitSelected(commit, fileDiffs);
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
