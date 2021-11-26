package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.eclipse.jgit.diff.DiffEntry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class CommitDetailsPane extends VBox implements CodeBaseObserver {

    private final VBox topHorizontalBanner;
    private final VBox fileList;
    private final Text descriptionText;
    private final Text authorText;
    private final Text dateText;
    private final Text hashText;

    // These are active lines in the VBox fileList
    // While the ViewFactory will hold reference to all created file text lines
    // this class will hold reference to the ones that are currently displaying.
    private final ArrayList<Text> activeFileTexts = new ArrayList<>();

    public CommitDetailsPane() {
        super();

        // Create the top horizontal banner
        topHorizontalBanner = ViewFactory.getInstance().createOrGetVBox(Constants.CD_BANNER_ID);
        setBannerProperties();
        ViewFactory.setPaneChild(this, topHorizontalBanner);

        // If we need references we can ask the ViewFactory and supply the ID
        // Create the banner text
        createHeaderText(Constants.CD_HEADER_TEXT_ID, Constants.CD_HEADER_TEXT, topHorizontalBanner);
        // Create the commit details text
        descriptionText = createCommitDetailsText(Constants.CD_DESCRIPTION_TEXT_ID, Constants.CD_DESCRIPTION, topHorizontalBanner);
        authorText = createCommitDetailsText(Constants.CD_AUTHOR_TEXT_ID, Constants.CD_AUTHOR, topHorizontalBanner);
        dateText = createCommitDetailsText(Constants.CD_DATE_TEXT_ID, Constants.CD_DATE, topHorizontalBanner);
        hashText = createCommitDetailsText(Constants.CD_HASH_TEXT_ID, Constants.CD_HASH, topHorizontalBanner);

        // Create the Commit Detail's file list container (i.e. scroll view)
        ScrollPane fileListContainer = ViewFactory.getInstance().createOrGetScrollPane(Constants.CD_FILE_LIST_CONTAINER_ID);
        setFileListContainerProperties(fileListContainer);
        ViewFactory.setPaneChild(this, fileListContainer);

        // Create the Commit Detail's file list
        fileList = ViewFactory.getInstance().createOrGetVBox(Constants.CD_FILE_LIST_ID);
        setFileListProperties();
        fileListContainer.setContent(fileList);

        //Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
    }

    /*
        Creation Methods
        -These don't really 'create' text, that's the ViewFactory's job
        -Their main purpose is to remove duplicated code.
     */
    private void createHeaderText(String id, String label, Pane parent) {
        Text headerText = createText(id, label, parent);
        setHeaderTextProperties(headerText);
    }

    private Text createCommitDetailsText(String id, String label, Pane parent) {
        Text text = createText(id, label, parent);
        setCommitDetailsTextProperties(text);
        return text;
    }

    private @NotNull Text createText(String id, String label, Pane parent) {
        Text text = ViewFactory.getInstance().createOrGetText(id);
        text.setText(label);
        ViewFactory.setPaneChild(parent, text);
        return text;
    }

    /*
        UI Property Settings
    */
    private void setBannerProperties() {
        // Set up constraints on width/height
        topHorizontalBanner.prefWidthProperty().bind(this.widthProperty());

        // Child layout properties
        topHorizontalBanner.setAlignment(Constants.CD_BANNER_ALIGNMENT);
        topHorizontalBanner.setSpacing(Constants.CD_BANNER_SPACING);
        topHorizontalBanner.setPadding(Constants.BANNER_INSETS);
    }

    private void setHeaderTextProperties(@NotNull Text headerText) {
        headerText.setFont(Font.font(Constants.HEADER_FONT, Constants.HEADER_TEXT_FONT_WEIGHT, Constants.HEADER_TEXT_SIZE));
    }

    private void setCommitDetailsTextProperties(@NotNull Text text) {
        text.wrappingWidthProperty().bind(this.widthProperty().multiply(Constants.CD_DETAILS_WRAPPING_PERCENTAGE));
    }

    private void setFileListContainerProperties(@NotNull ScrollPane fileListContainer) {
        // Set up constraints on width/height
        fileListContainer.minHeightProperty().bind(this.heightProperty().multiply(Constants.FILE_LIST_SIZE_MULTIPLIER));
        fileListContainer.prefWidthProperty().bind(this.widthProperty());
    }

    private void setFileListProperties() {
        // Set up constraints on width/height
        fileList.setMinHeight(Constants.FILE_LIST_MIN_HEIGHT);
        fileList.prefWidthProperty().bind(this.widthProperty());

        // Child layout properties
        fileList.setAlignment(Constants.CD_BANNER_ALIGNMENT);
        fileList.setSpacing(Constants.CD_BANNER_SPACING);
        fileList.setPadding(Constants.BANNER_INSETS);
    }

    private void setChangeTextProperties(Text fileText) {
        // Any formatting for text goes here.
    }

    private void setChangeHeaderTextProperties(Text headerText) {
        headerText.setFont(Font.font(Constants.HEADER_FONT, Constants.HEADER_TEXT_FONT_WEIGHT, headerText.getFont().getSize()));
    }

    /*
        Codebase Observer Implementation
    */
    @Override
    public void refreshHeatMap(Codebase codeBase) {
        // Nothing to do for this action
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void newBranchSelected() {
        // Clear
        descriptionText.setText(Constants.CD_DESCRIPTION);
        authorText.setText(Constants.CD_AUTHOR);
        dateText.setText(Constants.CD_DATE);
        hashText.setText(Constants.CD_HASH);

        // Clear
        activeFileTexts.clear();
        fileList.getChildren().clear();
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Nothing to do for this action
    }

    @Override
    public void commitSelected(Commit commit) {
        // Update Commit detail texts
        descriptionText.setText(Constants.CD_DESCRIPTION + commit.getShortMessage());
        authorText.setText(Constants.CD_AUTHOR + commit.getAuthor());
        dateText.setText(Constants.CD_DATE + commit.getDate());
        hashText.setText(Constants.CD_HASH + commit.getHash());

        // Clear and start fresh
        activeFileTexts.clear();
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

            switch(diffEntry.getChangeType()) {
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

        if(!addBuilder.toString().isEmpty()) {
            buildChangeHeaderText(Constants.CD_ADDED_FILES, DiffEntry.ChangeType.ADD);
            buildChangeText(addBuilder, DiffEntry.ChangeType.ADD);
        }
        if(!copyBuilder.toString().isEmpty()) {
            buildChangeHeaderText(Constants.CD_COPIED_FILES, DiffEntry.ChangeType.COPY);
            buildChangeText(copyBuilder, DiffEntry.ChangeType.COPY);
        }
        if(!modifyBuilder.toString().isEmpty()) {
            buildChangeHeaderText(Constants.CD_MODIFIED_FILES, DiffEntry.ChangeType.MODIFY);
            buildChangeText(modifyBuilder, DiffEntry.ChangeType.MODIFY);
        }
        if(!renameBuilder.toString().isEmpty()) {
            buildChangeHeaderText(Constants.CD_RENAMED_FILES, DiffEntry.ChangeType.RENAME);
            buildChangeText(renameBuilder, DiffEntry.ChangeType.RENAME);
        }
        if(!deleteBuilder.toString().isEmpty()) {
            buildChangeHeaderText(Constants.CD_DELETED_FILES, DiffEntry.ChangeType.DELETE);
            buildChangeText(deleteBuilder, DiffEntry.ChangeType.DELETE);
        }
    }

    public void buildChangeHeaderText(String text, DiffEntry.ChangeType changeType) {
        Text changeHeaderText = ViewFactory.getInstance().createOrGetText(Constants.CD_FILE_TEXT_PREFIX + changeType);
        setChangeHeaderTextProperties(changeHeaderText);
        changeHeaderText.setText(text);
        ViewFactory.setPaneChild(fileList, changeHeaderText);
    }

    public void buildChangeText(StringBuilder builder, DiffEntry.ChangeType changeType) {
        Text fileText = ViewFactory.getInstance().createOrGetText(Constants.CD_CHANGE_HEADER_TEXT_PREFIX + changeType);
        setChangeTextProperties(fileText);
        fileText.setText(builder.toString());
        activeFileTexts.add(fileText);
        ViewFactory.setPaneChild(fileList, fileText);
    }
}