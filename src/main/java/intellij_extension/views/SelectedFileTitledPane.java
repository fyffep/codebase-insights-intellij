package intellij_extension.views;


import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.xml.actions.xmlbeans.FileUtils;
import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.models.redesign.HeatObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.views.interfaces.IContainerView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;


import java.awt.*;
import java.io.File;
import java.net.URI;
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
    private FileObject selectedFile;

    private final Text fileName;
//    private Commit commit;
    private final Text packageName;
    private final Text authors;
    private final Text noOfCommits;
    private final Text fileSize;
    private final Text lineCount;
    private final Button openFile;


    //region Vars
    private int totalCommits;

    //endregion

    //region Constructor
    public SelectedFileTitledPane() {
        parent = new TitledPane();
        this.selectedFile = null;
        parent.setMinWidth(Constants.ZERO_WIDTH);

        setTitledPaneProperties();

        // Create vbox that lays out text
        VBox vbox = new VBox();
        parent.setContent(vbox);

        // Filename
        HBox hbox = new HBox();
        vbox.getChildren().add(hbox);
        hbox.setAlignment(Constants.BANNER_ALIGNMENT);
        hbox.setSpacing(15);

        fileName = new Text();
        setFileDetailsTextProperties(fileName); // set the font
        fileName.setText(Constants.SF_TEXT_FILENAME);
        hbox.getChildren().add(fileName);

        //open File Button
        openFile = new Button("Open File");
        openFile.setOnAction(this::openSelectedFileInEditor);
        hbox.getChildren().add(openFile);

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

        noOfCommits = new Text();
        setFileDetailsTextProperties(noOfCommits);
        noOfCommits.setText(Constants.SF_TEXT_NO_OF_COMMITS);
        vbox.getChildren().add(noOfCommits);

        // File Size  Node:
        fileSize = new Text();
        setFileDetailsTextProperties(fileSize);
        fileSize.setText(Constants.SF_TEXT_FILE_SIZE);
        vbox.getChildren().add(fileSize);

        // Line Count  Node:
        lineCount = new Text();
        setFileDetailsTextProperties(lineCount);
        lineCount.setText(Constants.SF_TEXT_LINE_COUNT);
        vbox.getChildren().add(lineCount);

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

    public void setSelectedFile(FileObject selectedFile) {
        this.selectedFile = selectedFile;
    }

    public FileObject getSelectedFile() {
        return this.selectedFile;
    }

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
    public void newBranchSelected(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode) {
        fileName.setText(Constants.SF_TEXT_FILENAME);
        packageName.setText(Constants.SF_TEXT_PACKAGE_NAME);
        authors.setText(Constants.SF_TEXT_AUTHORS);
        noOfCommits.setText(Constants.SF_TEXT_NO_OF_COMMITS);
        fileSize.setText(Constants.SF_TEXT_FILE_SIZE);
        lineCount.setText(Constants.SF_TEXT_LINE_COUNT);

        hidePane();
    }

    @Override
    public void fileSelected(@NotNull FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Filename
        fileName.setText(String.format("%s%s", Constants.SF_TEXT_FILENAME, selectedFile.getFilename()));
        setSelectedFile(selectedFile);

        // Package
        if (selectedFile.getPath().getParent() != null) {
            packageName.setText(String.format("%s%s", Constants.SF_TEXT_PACKAGE_NAME, selectedFile.getPath().getParent().toString()));
        } else {
            packageName.setText(String.format("%s%s", Constants.SF_TEXT_PACKAGE_NAME, "Unknown"));
        }

        // Gather all authors from list of commits
        ArrayList<String> uniqueAuthors = new ArrayList<>();
        totalCommits = 0;
        while (filesCommits.hasNext()) {
            totalCommits++;
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

        //No of Commits
        noOfCommits.setText(String.format("%s%s", Constants.SF_TEXT_NO_OF_COMMITS, totalCommits));

        HeatObject heatobject = selectedFile.getHeatObjectAtCommit(selectedFile.getLatestCommitInTreeWalk());

        //File Size
        fileSize.setText(String.format("%s%s", Constants.SF_TEXT_FILE_SIZE, heatobject.getFileSize()));


        //Line count
        lineCount.setText(String.format("%s%s", Constants.SF_TEXT_LINE_COUNT, heatobject.getLineCount()));

        // Show the Pane
        showPane();
    }

    @Override
    public void commitSelected(Commit commit) {
        //this.commit=commit;
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return parent;
    }
    //endregion


    // open a selected file in the editor
    public static void openFileInEditor(FileObject file) {
        try {
            ProjectManager pm = ProjectManager.getInstance();
            // TODO if we open more than one project in our plugin ,this will always consider the first opened project.Need to optimize
            Project project = pm.getOpenProjects()[0];

            //To get the absolute path of the project root within the system
            ProjectRootManager prm = ProjectRootManager.getInstance(project);
            VirtualFile[] projectRoot = prm.getContentRoots();

            String projectRootPath = projectRoot[0].getPath();
            projectRootPath = projectRootPath.replace('/', '\\');

            // relative path of the selected file
            String selectedFileRelativePath = file.getPath().toString();

            //full absolute path
            String fileAbsolutePath = projectRootPath + "\\" + selectedFileRelativePath;
            System.out.println("vFiles" + fileAbsolutePath);

            VirtualFile vFile = LocalFileSystem.getInstance().findFileByIoFile(new File(fileAbsolutePath));
            //open file
            if(vFile==null)
            {
                System.out.println("No File Found in specified path");
            }
            FileEditorManager.getInstance(project).openFile(vFile, true);

        } catch (Exception e) {

            System.out.println(e);
        }
    }

    // action listener to the "open file" button
    private void openSelectedFileInEditor(ActionEvent event) {
        // openFile has to be called from Event Dispatcher Thread (EDT)
        EventQueue.invokeLater(() -> {
            openFileInEditor(getSelectedFile());
        });
    }


}