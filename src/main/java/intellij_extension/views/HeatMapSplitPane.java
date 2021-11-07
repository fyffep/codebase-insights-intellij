package intellij_extension.views;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;

public class HeatMapSplitPane extends SplitPane {

    // Top half is Commit History for a single branch
    private HeatMapPane heatMapPane;

    // Bottom half is Commit Details for a single commit in the selected branch
    private SelectedFileViewPane selectedFileView;

    public HeatMapSplitPane() {
        super();

        setOrientation(Orientation.VERTICAL);

        // Top half
        heatMapPane = new HeatMapPane();
        this.getItems().add(heatMapPane); // SplitPane isn't a pane, so we cannot use ViewFactory.setPaneChild

        //Bottom half
        selectedFileView = new SelectedFileViewPane();
        this.getItems().add(selectedFileView); // SplitPane isn't a pane, so we cannot use ViewFactory.setPaneChild
    }
}