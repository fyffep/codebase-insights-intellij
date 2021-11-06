package intellij_extension.views;

import intellij_extension.Constants;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class CommitDetailsPane extends VBox {

    private final VBox topHorizontalBanner;
    private final Text headerText;

    private final Text commitDescriptionText;
    private final Text commitAuthorText;
    private final Text commitDateText;
    private final Text commitHashText;

    private final ScrollPane fileListContainer;
    private final VBox fileList;
    // This is all the lines we created so far - we should never remove from this list
    private final ArrayList<Text> allFileTexts = new ArrayList<>();
    // These are active lines in the VBox fileList
    private final ArrayList<Text> activeFileTexts = new ArrayList<>();

    public CommitDetailsPane() {
        super();

        // Create the top horizontal banner
        topHorizontalBanner = ViewFactory.getInstance().createOrGetVBox(Constants.CD_BANNER_ID);
        setBannerProperties();
        ViewFactory.setPaneChild(this, topHorizontalBanner);

        // Create the banner text
        headerText = ViewFactory.getInstance().createOrGetText(Constants.CD_HEADER_TEXT_ID);
        setHeaderTextProperties();
        ViewFactory.setPaneChild(topHorizontalBanner, headerText);

        // Create the commit details text
        commitDescriptionText = ViewFactory.getInstance().createOrGetText(Constants.CD_DESCRIPTION_TEXT_ID);
        commitDescriptionText.setText("Description: " + Constants.MOCK_COMMIT_DETAILS.getCommitDescription().getValue());
        ViewFactory.setPaneChild(topHorizontalBanner, commitDescriptionText);

        commitAuthorText = ViewFactory.getInstance().createOrGetText(Constants.CD_AUTHOR_TEXT_ID);
        commitAuthorText.setText("Author: " + Constants.MOCK_COMMIT_DETAILS.getCommitAuthor().getValue());
        ViewFactory.setPaneChild(topHorizontalBanner, commitAuthorText);

        commitDateText = ViewFactory.getInstance().createOrGetText(Constants.CD_DATE_TEXT_ID);
        commitDateText.setText("Date: " + Constants.MOCK_COMMIT_DETAILS.getCommitDate().getValue());
        ViewFactory.setPaneChild(topHorizontalBanner, commitDateText);

        commitHashText = ViewFactory.getInstance().createOrGetText(Constants.CD_HASH_TEXT_ID);
        commitHashText.setText("Hash: " + Constants.MOCK_COMMIT_DETAILS.getCommitHash().getValue());
        ViewFactory.setPaneChild(topHorizontalBanner, commitHashText);

        // Create the Commit Detail's file list container (i.e. scroll view)
        fileListContainer = ViewFactory.getInstance().createOrGetScrollPane(Constants.CD_FILE_LIST_CONTAINER_ID);
        setFileListContainerProperties();
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
    public void buildFileList(/*Object allFilesInCommitData*/) {
        activeFileTexts.clear();
        fileList.getChildren().clear();

        int counter = 0;
        for(String fileInfo: Constants.MOCK_COMMIT_FILE_DETAILS) {
            Text fileText = ViewFactory.getInstance().createOrGetText(Constants.CD_FILE_TEXT_PREFIX + counter);
            setFileTextProperties(fileText);
            fileText.setText(fileInfo);
            if(!allFileTexts.contains(fileText)) {
                allFileTexts.add(fileText);
            }
            activeFileTexts.add(fileText);
            ViewFactory.setPaneChild(fileList, fileText);

            counter++;
        }
    }

    /*
    UI Property Settings
    */
    private void setBannerProperties() {
        // Banner layout properties

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

    private void setHeaderTextProperties() {
        headerText.setFont(Font.font(Constants.HEADER_FONT, Constants.HEADER_TEXT_FONT_WEIGHT, Constants.HEADER_TEXT_SIZE));
        headerText.setText(Constants.CD_HEADER_TEXT);
    }

    private void setFileListContainerProperties() {
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