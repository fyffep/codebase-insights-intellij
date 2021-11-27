package intellij_extension.views;


import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.views.interfaces.IContainerView;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * The following Class is responsible for displaying the details of  file selected in the heat map
 * It is a TitledPane . It offers a terminal kind view where the user can maximize and minimize the window
 */
public class SelectedFileTitledPane implements IContainerView, CodeBaseObserver {

    //region Vars
    private TitledPane parent;

    private final Text fileName;
    private final Text packageName;
    private final Text authors;
    //endregion

    //region Constructor
    public SelectedFileTitledPane() {
        parent = new TitledPane();

        setTitledPaneProperties();

        // Create vbox that lays out text
        VBox vbox = new VBox();
        parent.setContent(vbox);

        // Filename
        fileName = new Text();
        setFileDetailsTextProperties(fileName); // set the font
        fileName.setText(Constants.SF_TEXT_FILENAME);
        vbox.getChildren().add(fileName);

        // Package Name Node
        packageName = new Text();
        setFileDetailsTextProperties(packageName);
        packageName.setText(Constants.SF_TEXT_PACKAGE_NAME);
        vbox.getChildren().add(packageName);

        // Author Node
        authors = new Text();
        setFileDetailsTextProperties(authors);
        authors.setText(Constants.SF_TEXT_AUTHORS);
        vbox.getChildren().add(authors);

        //Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
    }
    //endregion

    //region Properties setting
    private void setTitledPaneProperties() {
        parent.setText(Constants.SF_TITLE_TEXT);
        parent.setPrefHeight(10);
        parent.setExpanded(false);
        parent.setCollapsible(true);
        parent.setAnimated(true);
    }

    public void setFileDetailsTextProperties(Text text) {
        text.setFont(Font.font(Constants.SF_TEXT_FONT, Constants.SF_TEXT_FONT_WEIGHT, Constants.SF_TEXT_SIZE));
        text.wrappingWidthProperty().bind(parent.widthProperty().multiply(0.9f));
    }
    //endregion

    //region UI Action
    public void showPane() {
        parent.setExpanded(true);
    }

    public void hidePane() {
        parent.setExpanded(false);
    }
    //endregion

    //region CodeBaseObserver methods
    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode) {
        // Nothing to do for this action
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void newBranchSelected() {
        fileName.setText(Constants.SF_TEXT_FILENAME);
        packageName.setText(Constants.SF_TEXT_PACKAGE_NAME);
        authors.setText(Constants.SF_TEXT_AUTHORS);
        hidePane();
    }

    @Override
    public void fileSelected(@NotNull FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Filename
        fileName.setText(String.format("%s%s", Constants.SF_TEXT_FILENAME, selectedFile.getFilename()));

        // Package
        if (selectedFile.getPath().getParent() != null) {
            packageName.setText(String.format("%s%s",Constants.SF_TEXT_PACKAGE_NAME, selectedFile.getPath().getParent().toString()));
        } else {
            packageName.setText(String.format("%s%s", Constants.SF_TEXT_PACKAGE_NAME, "Unknown"));
        }

        // Gather all authors from list of commits
        ArrayList<String> uniqueAuthors = new ArrayList<>();
        while (filesCommits.hasNext()) {
            Commit commit = filesCommits.next();
            if (!uniqueAuthors.contains(commit.getAuthor())) {
                uniqueAuthors.add(commit.getAuthor());
            }
        }

        // Build the authors string
        String fileAuthors = "";
        for (String author : uniqueAuthors) {
            // If only 1 author
            if (uniqueAuthors.size() == 1) {
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
        }

        // Authors
        authors.setText(String.format("%s%s", Constants.SF_TEXT_AUTHORS, fileAuthors));

        // Show the Pane
        showPane();
    }

    @Override
    public void commitSelected(Commit commit) {
        // Nothing to do for this action
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return parent;
    }
    //endregion
}