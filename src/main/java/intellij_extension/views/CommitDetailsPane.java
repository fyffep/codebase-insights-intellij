package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.views.interfaces.IContainerView;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.eclipse.jgit.diff.DiffEntry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class CommitDetailsPane implements IContainerView, CodeBaseObserver {

    private final VBox topHorizontalBanner;
    private final VBox fileList;
    private final Text descriptionText;
    private final Text authorText;
    private final Text dateText;
    private final Text hashText;
    private final Text addHeader;
    private final Text addBody;
    private final Text copyHeader;
    private final Text copyBody;
    private final Text modifyHeader;
    private final Text modifyBody;
    private final Text renameHeader;
    private final Text renameBody;
    private final Text deleteHeader;
    private final Text deleteBody;
    private VBox parent;

    public CommitDetailsPane() {
        parent = new VBox();

        // Create the top horizontal banner
        topHorizontalBanner = new VBox();
        setBannerProperties();
        parent.getChildren().add(topHorizontalBanner);

        // Create the banner text
        createHeaderText(Constants.CD_HEADER_TEXT, topHorizontalBanner);
        // Create the commit details text
        descriptionText = createCommitDetailsText(Constants.CD_DESCRIPTION, topHorizontalBanner);
        authorText = createCommitDetailsText(Constants.CD_AUTHOR, topHorizontalBanner);
        dateText = createCommitDetailsText(Constants.CD_DATE, topHorizontalBanner);
        hashText = createCommitDetailsText(Constants.CD_HASH, topHorizontalBanner);

        // Create the Commit Detail's file list container (i.e. scroll view)
        ScrollPane fileListContainer = new ScrollPane();
        setFileListContainerProperties(fileListContainer);
        parent.getChildren().add(fileListContainer);

        fileList = new VBox();
        setFileListProperties();
        fileListContainer.setContent(fileList);

        addHeader = new Text();
        setChangeHeaderTextProperties(addHeader);
        addHeader.setText(Constants.CD_ADDED_FILES);
        addBody = new Text();
        setChangeTextProperties(addBody);

        copyHeader = new Text();
        setChangeHeaderTextProperties(copyHeader);
        copyHeader.setText(Constants.CD_COPIED_FILES);
        copyBody = new Text();
        setChangeTextProperties(copyBody);

        modifyHeader = new Text();
        setChangeHeaderTextProperties(modifyHeader);
        modifyHeader.setText(Constants.CD_MODIFIED_FILES);
        modifyBody = new Text();
        setChangeTextProperties(modifyBody);

        renameHeader = new Text();
        setChangeHeaderTextProperties(renameHeader);
        renameHeader.setText(Constants.CD_RENAMED_FILES);
        renameBody = new Text();
        setChangeTextProperties(renameBody);

        deleteHeader = new Text();
        setChangeHeaderTextProperties(deleteHeader);
        deleteHeader.setText(Constants.CD_DELETED_FILES);
        deleteBody = new Text();
        setChangeTextProperties(deleteBody);

        //Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
    }

    //region Creation methods
    private void createHeaderText(String label, Pane parent) {
        Text headerText = createText(label, parent);
        setHeaderTextProperties(headerText);
    }

    private @NotNull Text createCommitDetailsText(String label, Pane parent) {
        Text text = createText(label, parent);
        setCommitDetailsTextProperties(text);
        return text;
    }

    private @NotNull Text createText(String label, @NotNull Pane parent) {
        Text text = new Text(); // ViewFactory.getInstance().createOrGetText(id);
        text.setText(label);
        parent.getChildren().add(text); // ViewFactory.setPaneChild(parent, text);
        return text;
    }
    //endregion

    //region Properties setting
    private void setBannerProperties() {
        // Set up constraints on width/height
        topHorizontalBanner.setMinHeight(Constants.BANNER_MIN_HEIGHT);
        topHorizontalBanner.prefWidthProperty().bind(parent.widthProperty());

        // Child layout properties
        topHorizontalBanner.setAlignment(Constants.CD_BANNER_ALIGNMENT);
        topHorizontalBanner.setSpacing(Constants.CD_BANNER_SPACING);
        topHorizontalBanner.setPadding(Constants.BANNER_INSETS);
    }

    private void setHeaderTextProperties(@NotNull Text headerText) {
        headerText.setFont(Font.font(Constants.HEADER_FONT, Constants.HEADER_TEXT_FONT_WEIGHT, Constants.HEADER_TEXT_SIZE));
    }

    private void setCommitDetailsTextProperties(@NotNull Text text) {
        text.wrappingWidthProperty().bind(parent.widthProperty().multiply(Constants.CD_DETAILS_WRAPPING_PERCENTAGE));
    }

    private void setFileListContainerProperties(@NotNull ScrollPane fileListContainer) {
        // Set up constraints on width/height
//        fileListContainer.minHeightProperty().bind(parent.heightProperty().multiply(Constants.FILE_LIST_SIZE_MULTIPLIER));
        fileListContainer.prefWidthProperty().bind(parent.widthProperty());
    }

    private void setFileListProperties() {
        // Set up constraints on width/height
//        fileList.setMinHeight(Constants.FILE_LIST_MIN_HEIGHT);
        fileList.prefWidthProperty().bind(parent.widthProperty());

        // Child layout properties
        fileList.setAlignment(Constants.CD_BANNER_ALIGNMENT);
        fileList.setSpacing(Constants.CD_BANNER_SPACING);
        fileList.setPadding(Constants.BANNER_INSETS);
    }

    private void setChangeTextProperties(Text fileText) {
        // Any formatting for text goes here.
    }

    private void setChangeHeaderTextProperties(@NotNull Text headerText) {
        headerText.setFont(Font.font(Constants.HEADER_FONT, Constants.HEADER_TEXT_FONT_WEIGHT, headerText.getFont().getSize()));
    }
    //endregion

    //region CodeBaseObservable methods
    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode) {
        // Nothing to do for this action
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void newBranchSelected(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode) {
        // Clear
        descriptionText.setText(Constants.CD_DESCRIPTION);
        authorText.setText(Constants.CD_AUTHOR);
        dateText.setText(Constants.CD_DATE);
        hashText.setText(Constants.CD_HASH);

        // Clear
        fileList.getChildren().clear();
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Nothing to do for this action
    }

    @Override
    public void commitSelected(@NotNull Commit commit) {
        // Update Commit detail texts
        descriptionText.setText(Constants.CD_DESCRIPTION + commit.getShortMessage());
        authorText.setText(Constants.CD_AUTHOR + commit.getAuthor());
        dateText.setText(Constants.CD_DATE + commit.getDate());
        hashText.setText(Constants.CD_HASH + commit.getHash());

        // Clear and start fresh
        fileList.getChildren().clear();

        StringBuilder addBuilder = new StringBuilder();
        StringBuilder copyBuilder = new StringBuilder();
        StringBuilder modifyBuilder = new StringBuilder();
        StringBuilder renameBuilder = new StringBuilder();
        StringBuilder deleteBuilder = new StringBuilder();

        Iterator<DiffEntry> commitDiffs = commit.getCommitDiffs().iterator();
        while (commitDiffs.hasNext()) {
            // Grab the diff for a file
            DiffEntry diffEntry = commitDiffs.next();

            switch (diffEntry.getChangeType()) {
                case ADD:
                    addBuilder.append(new File(diffEntry.getNewPath()).getName() + "\n");
                    break;
                case COPY:
                    copyBuilder.append(new File(diffEntry.getNewPath()).getName() + "\n");
                    break;
                case MODIFY:
                    modifyBuilder.append(new File(diffEntry.getNewPath()).getName() + "\n");
                    break;
                case RENAME:
                    renameBuilder.append(new File(diffEntry.getOldPath()).getName() + " renamed to: " + new File(diffEntry.getNewPath()).getName() + "\n");
                    break;
                case DELETE:
                    deleteBuilder.append(new File(diffEntry.getOldPath()).getName() + "\n");
                    break;
                default:
                    break;
            }
        }

        if (!addBuilder.toString().isEmpty()) {
            fileList.getChildren().add(addHeader);
            addBody.setText(addBuilder.toString());
            fileList.getChildren().add(addBody);
        }
        if (!copyBuilder.toString().isEmpty()) {
            fileList.getChildren().add(copyHeader);
            copyBody.setText(copyBuilder.toString());
            fileList.getChildren().add(copyBody);
        }
        if (!modifyBuilder.toString().isEmpty()) {
            fileList.getChildren().add(modifyHeader);
            modifyBody.setText(modifyBuilder.toString());
            fileList.getChildren().add(modifyBody);
        }
        if (!renameBuilder.toString().isEmpty()) {
            fileList.getChildren().add(renameHeader);
            renameBody.setText(renameBuilder.toString());
            fileList.getChildren().add(renameBody);
        }
        if (!deleteBuilder.toString().isEmpty()) {
            fileList.getChildren().add(deleteHeader);
            deleteBody.setText(deleteBuilder.toString());
            fileList.getChildren().add(deleteBody);
        }
    }
    //endregion

    //region IContainerView
    @Override
    public Node getNode() {
        return parent;
    }
    //endregion
}