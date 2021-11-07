package intellij_extension.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * It adds /updates the file details (UI Components) within a VBox
 */
public class SelectedFileVBox extends VBox {
    private Label fileName, packageName, authors;

    public SelectedFileVBox() {
        fileName = new Label("File Name    :");
        packageName = new Label("Package Name :");
        authors = new Label("Authors      :");
        this.getChildren().add(fileName);
        this.getChildren().add(packageName);
        this.getChildren().add(authors);
    }


    public void updateFileName(String file) {
        String temp = String.format("%-20s", "File Name");
        temp = temp + ": " + file;
        this.fileName.setText(temp);
    }


    public void updatePackageName(String packageName) {
        String temp = String.format("%-15s", "Package Name");
        temp = temp + ": " + packageName;
        this.packageName.setText(temp);
    }

    public void updateAuthoreName(String authors) {
        String temp = String.format("%-22s", "Authors");
        temp = temp + ": " + authors;
        this.authors.setText(temp);
    }
}