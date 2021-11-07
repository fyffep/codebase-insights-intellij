package intellij_extension.views;


import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

/**
 * The following Class is responsible for displaying the details of  file selected in the heat map
 * It is a TitledPane . It offers a terminal kind view where the user can maximize and minimize the window
 */
public class SelectedFilePane extends TitledPane {

    public SelectedFilePane(VBox vBox) {
        super();
        setText("Selected File Details");
        setPrefHeight(10);
        this.setExpanded(false);
        this.setCollapsible(true);
        this.setContent(vBox);
        showPane();

    }

    // helps to control the visiblity of the pane  itself
    public void toggleVisiblity() {
        if (this.isVisible() == true) {
            hidePane();
        } else {
            showPane();
        }
    }

    public void showPane() {
        this.setVisible(true);
    }

    public void hidePane() {
        this.setVisible(false);
    }

    // updates the pane with the given VBox object
    public void updateSelectedFileView(VBox vBox) {
        this.setContent(vBox);
    }
}