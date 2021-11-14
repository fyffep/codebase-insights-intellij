package intellij_extension.views;


import intellij_extension.Constants;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.Iterator;

/**
 * The following Class is responsible for displaying the details of  file selected in the heat map
 * It is a TitledPane . It offers a terminal kind view where the user can maximize and minimize the window
 */
public class SelectedFileTitledPane extends TitledPane implements CodeBaseObserver {

    private final Text fileName;
    private final Text packageName;
    private final Text authors;

    public SelectedFileTitledPane() {
        super();

        setTitledPaneProperties();

        // Create vbox that lays out text
        VBox vbox = ViewFactory.getInstance().createOrGetVBox(Constants.SF_VBOX_ID);
        this.setContent(vbox);

        // Filename
        fileName = ViewFactory.getInstance().createOrGetText(Constants.SF_FILENAME_TEXT_ID); // create a Text node
//        String formattedFieldName = formatFieldName(Constants.SF_FILE_NAME_SPECIFIER, Constants.SF_TEXT_FILENAME); //format the  Text node
        setFileDetailsTextProperties(fileName); // set the font
        fileName.setText(Constants.SF_TEXT_FILENAME);
        ViewFactory.setPaneChild(vbox, fileName); //add the Text Node to VBox

        // Package Name Node
        packageName = ViewFactory.getInstance().createOrGetText(Constants.SF_PACKAGE_NAME_TEXT_ID);
//        formattedFieldName = formatFieldName(Constants.SF_PACKAGE_NAME_SPECIFIER, Constants.SF_TEXT_PACKAGE_NAME); //format the  Text node
        setFileDetailsTextProperties(packageName);
        packageName.setText(Constants.SF_TEXT_PACKAGE_NAME);
        ViewFactory.setPaneChild(vbox, packageName);

        // Author Node
        authors = ViewFactory.getInstance().createOrGetText(Constants.SF_AUTHOR_TEXT_ID);
//        formattedFieldName = formatFieldName(Constants.SF_FILE_AUTHOR_SPECIFIER, Constants.SF_TEXT_AUTHORS); //format the  Text node
        setFileDetailsTextProperties(authors);
        authors.setText(Constants.SF_TEXT_AUTHORS);
        ViewFactory.setPaneChild(vbox, authors);
    }

    /*
        UI Property Settings
    */
    private void setTitledPaneProperties() {
        setText(Constants.SF_TITLE_TEXT);
        setPrefHeight(10);
        this.setExpanded(false);
        this.setCollapsible(true);
    }

    public void setFileDetailsTextProperties(Text text) {
        text.setFont(Font.font(Constants.SF_TEXT_FONT, Constants.SF_TEXT_FONT_WEIGHT, Constants.SF_TEXT_SIZE));
    }

    public String formatFieldName(String specifier, String fieldName) {
        String formattedFieldName = String.format(specifier, fieldName);
        formattedFieldName = formattedFieldName + Constants.SF_TEXT_SEPERATOR;
        return formattedFieldName;
    }

    /*
        UI Actions
    */
    public void showPane() {
        this.setVisible(true);
    }

    public void hidePane() {
        this.setVisible(false);
    }

    /*
        Codebase Observer Implementation
    */
    @Override
    public void refreshHeatMap(Codebase codeBase) {
        // Nothing to do for this action
    }

    @Override
    public void branchSelected() {
        fileName.setText(Constants.SF_TEXT_FILENAME);
        packageName.setText(Constants.SF_TEXT_PACKAGE_NAME);
        authors.setText(Constants.SF_TEXT_AUTHORS);
        hidePane();
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
        fileName.setText(selectedFile.getFilename());
        packageName.setText(selectedFile.getPath().getParent().toString());

        // Gather all authors from list of commits
        String fileAuthors = "";
        while (filesCommits.hasNext()) {
            Commit commit = filesCommits.next();
            fileAuthors = String.format(fileAuthors, commit.getAuthor(), ", ");
        }
        authors.setText(fileAuthors);

        // Show the Pane
        showPane();
    }

    @Override
    public void commitSelected(Commit commit, Iterator<DiffEntry> fileDiffs) {
        // Nothing to do for this action
    }
}