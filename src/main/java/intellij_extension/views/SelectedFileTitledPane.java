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

import java.util.ArrayList;
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
        String formattedFieldName = formatFieldName(Constants.SF_FILE_NAME_SPECIFIER, Constants.SF_TEXT_FILENAME); //format the  Text node
        setFileDetailsTextProperties(fileName); // set the font
        fileName.setText(formattedFieldName);
        ViewFactory.setPaneChild(vbox, fileName); //add the Text Node to VBox

        // Package Name Node
        packageName = ViewFactory.getInstance().createOrGetText(Constants.SF_PACKAGE_NAME_TEXT_ID);
        formattedFieldName = formatFieldName(Constants.SF_PACKAGE_NAME_SPECIFIER, Constants.SF_TEXT_PACKAGE_NAME); //format the  Text node
        setFileDetailsTextProperties(packageName);
        packageName.setText(formattedFieldName);
        ViewFactory.setPaneChild(vbox, packageName);

        // Author Node
        authors = ViewFactory.getInstance().createOrGetText(Constants.SF_AUTHOR_TEXT_ID);
        formattedFieldName = formatFieldName(Constants.SF_FILE_AUTHOR_SPECIFIER, Constants.SF_TEXT_AUTHORS); //format the  Text node
        setFileDetailsTextProperties(authors);
        authors.setText(formattedFieldName);
        ViewFactory.setPaneChild(vbox, authors);

        //Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
    }

    /*
        UI Property Settings
    */
    private void setTitledPaneProperties() {
        setText(Constants.SF_TITLE_TEXT);
        setPrefHeight(10);
        this.setExpanded(false);
        this.setCollapsible(true);
        this.setAnimated(true);
    }

    public void setFileDetailsTextProperties(Text text) {
        text.setFont(Font.font(Constants.SF_TEXT_FONT, Constants.SF_TEXT_FONT_WEIGHT, Constants.SF_TEXT_SIZE));
        text.wrappingWidthProperty().bind(this.widthProperty().multiply(0.9f));
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
        this.setExpanded(true);
    }

    public void hidePane() {
        this.setExpanded(false);
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
    public void branchSelected() {
        fileName.setText(Constants.SF_TEXT_FILENAME);
        packageName.setText(Constants.SF_TEXT_PACKAGE_NAME);
        authors.setText(Constants.SF_TEXT_AUTHORS);
        hidePane();
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
//        System.out.println("Full circle: " + selectedFile.getFilename());

        fileName.setText(String.format("%s%s", formatFieldName(Constants.SF_FILE_NAME_SPECIFIER, Constants.SF_TEXT_FILENAME), selectedFile.getFilename()));
        if(selectedFile.getPath().getParent() != null) {
            packageName.setText(String.format("%s%s", formatFieldName(Constants.SF_PACKAGE_NAME_SPECIFIER, Constants.SF_TEXT_PACKAGE_NAME), selectedFile.getPath().getParent().toString()));
        } else {
            packageName.setText(String.format("%s%s", formatFieldName(Constants.SF_PACKAGE_NAME_SPECIFIER, Constants.SF_TEXT_PACKAGE_NAME), "Unknown"));
        }

        // Gather all authors from list of commits
        ArrayList<String> uniqueAuthors = new ArrayList<>();
        while (filesCommits.hasNext()) {
            Commit commit = filesCommits.next();
            if(!uniqueAuthors.contains(commit.getAuthor())) {
                uniqueAuthors.add(commit.getAuthor());
            }
        }

        // Build the authors string
        String fileAuthors = "";
        for(String author: uniqueAuthors) {
            // If only 1 author
            if(uniqueAuthors.size() == 1) {
                fileAuthors = author;
            }
            // If last author in list, don't add comma
            else if (uniqueAuthors.indexOf(author) + 1 == uniqueAuthors.size()) {
                fileAuthors = String.format("%s%s", fileAuthors, author);
            }
            // Else add author w/ comma
            else {
                fileAuthors = String.format("%s%s%s", fileAuthors, author, ", ");
            }
//            System.out.println(fileAuthors);
        }

        authors.setText(String.format("%s%s", formatFieldName(Constants.SF_FILE_AUTHOR_SPECIFIER, Constants.SF_TEXT_AUTHORS), fileAuthors));

        // Show the Pane
        showPane();
    }

    @Override
    public void commitSelected(Commit commit) {
        // Nothing to do for this action
    }
}