package intellij_extension.views;

import intellij_extension.Constants;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommitDetailsPane extends VBox {

    private final VBox topHorizontalBanner;
    private final VBox fileList;

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
        createText(Constants.CD_DESCRIPTION_TEXT_ID, Constants.CD_DESCRIPTION + Constants.MOCK_COMMIT_DETAILS.getCommitDescription().getValue(), topHorizontalBanner);
        createText(Constants.CD_AUTHOR_TEXT_ID, Constants.CD_AUTHOR + Constants.MOCK_COMMIT_DETAILS.getCommitAuthor().getValue(), topHorizontalBanner);
        createText(Constants.CD_DATE_TEXT_ID, Constants.CD_DATE + Constants.MOCK_COMMIT_DETAILS.getCommitDate().getValue(), topHorizontalBanner);
        createText(Constants.CD_HASH_TEXT_ID, Constants.CD_HASH + Constants.MOCK_COMMIT_DETAILS.getCommitHash().getValue(), topHorizontalBanner);

        // Create the Commit Detail's file list container (i.e. scroll view)
        ScrollPane fileListContainer = ViewFactory.getInstance().createOrGetScrollPane(Constants.CD_FILE_LIST_CONTAINER_ID);
        setFileListContainerProperties(fileListContainer);
        ViewFactory.setPaneChild(this, fileListContainer);

        // Create the Commit Detail's file list
        fileList = ViewFactory.getInstance().createOrGetVBox(Constants.CD_FILE_LIST_ID);
        setFileListProperties();
        fileListContainer.setContent(fileList);

        // Create the texts for the files
        buildFileList();
    }

    /*
    Newly Selected Commit
        - Might be an observable update method call
    */
    private void buildFileList(/*Object allFilesInCommitData*/) {
        // Clear and start fresh
        activeFileTexts.clear();
        fileList.getChildren().clear();

        // Index is mainly for id
        int fileIndex = 0;
        for (String fileInfo : Constants.MOCK_COMMIT_FILE_DETAILS) {
            // Create or get a text
            Text fileText = ViewFactory.getInstance().createOrGetText(Constants.CD_FILE_TEXT_PREFIX + fileIndex);
            // Set the properties
            setFileTextProperties(fileText);
            // TODO make this proper with model data
            // set it's file info
            fileText.setText(fileInfo);
            // Track it as an active text
            activeFileTexts.add(fileText);
            // Add it to the file list VBox
            ViewFactory.setPaneChild(fileList, fileText);
            // Increment index
            fileIndex++;
        }
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
        // We want this so the user can make the Commit Details view as big
        // as the right side if desirable
        topHorizontalBanner.setMinHeight(Constants.BANNER_MIN_HEIGHT);
        // TODO We really want this to be a set, not a bind.
        // The header shouldn't grow with the window size. But it should be a percentage of the window size.
        topHorizontalBanner.prefHeightProperty().bind(this.heightProperty().multiply(Constants.BANNER_SIZE_MULTIPLIER));
        topHorizontalBanner.maxHeightProperty().bind(this.heightProperty().multiply(Constants.BANNER_SIZE_MULTIPLIER));
        topHorizontalBanner.prefWidthProperty().bind(this.widthProperty());

        // Child layout properties
        topHorizontalBanner.setAlignment(Constants.CD_BANNER_ALIGNMENT);
        topHorizontalBanner.setSpacing(Constants.CD_BANNER_SPACING);
        topHorizontalBanner.setPadding(Constants.BANNER_INSETS);
    }

    private void setHeaderTextProperties(@NotNull Text headerText) {
        headerText.setFont(Font.font(Constants.HEADER_FONT, Constants.HEADER_TEXT_FONT_WEIGHT, Constants.HEADER_TEXT_SIZE));
    }

    private void setFileListContainerProperties(@NotNull ScrollPane fileListContainer) {
        // Set up constraints on width/height
        // We want this so the user can make the Commit History view as big as the right side if desirable
        fileListContainer.setMinHeight(Constants.FILE_LIST_MIN_HEIGHT);
        fileListContainer.prefHeightProperty().bind(this.heightProperty().multiply(Constants.FILE_LIST_SIZE_MULTIPLIER));
        fileListContainer.maxHeightProperty().bind(this.heightProperty().multiply(Constants.FILE_LIST_SIZE_MULTIPLIER));
        fileListContainer.prefWidthProperty().bind(this.widthProperty());
    }

    private void setFileListProperties() {
        // Set up constraints on width/height

        // We want this so the user can make the Commit History view as big as the right side if desirable
        fileList.setMinHeight(Constants.FILE_LIST_MIN_HEIGHT);
        fileList.prefWidthProperty().bind(this.widthProperty());

        // Child layout properties
        fileList.setAlignment(Constants.CD_BANNER_ALIGNMENT);
        fileList.setSpacing(Constants.CD_BANNER_SPACING);
        fileList.setPadding(Constants.BANNER_INSETS);
    }

    private void setFileTextProperties(Text fileText) {
        // Any formatting for text goes here.
    }
}