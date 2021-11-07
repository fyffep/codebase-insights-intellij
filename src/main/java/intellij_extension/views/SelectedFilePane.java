package intellij_extension.views;


import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

/**
 * The following Class is responsible for displaying the details of  file selected in the heat map
 * It is a TitledPane . It offers a terminal kind view where the user can maximize and minimize the window
 */
public class SelectedFilePane extends TitledPane {
    private SelectedFileVBox selectedfilevbox;

    public SelectedFilePane() {
        super();
        setText("Selected File Details");
        selectedfilevbox = new SelectedFileVBox();
        setPrefHeight(10);
        this.setExpanded(false);
        this.setCollapsible(true);
        this.setContent(selectedfilevbox);
        showPane();

    }

    // helps to control the visibility of the pane  itself
    public void toggleVisibility() {
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


    // TODO this isn't a good method.
    // TODO this implies we are re-creating the VBox every time.
    // TODO Why do that, when we can reuse the VBox that is already there?
    // Updates the pane with the given VBox object
    public void updateSelectedFileView(VBox vBox) {
        this.setContent(vBox);
    }
}