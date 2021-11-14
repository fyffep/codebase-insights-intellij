package intellij_extension.views;

import intellij_extension.Constants;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;

public class InfoSplitPane extends SplitPane {

    // Top half is Commit History for a single branch
    private FileHistoryPane fileHistoryPane;

    // Bottom half is Commit Details for a single commit in the selected branch
    private CommitDetailsPane commitDetailsPane;

    public InfoSplitPane() {
        super();

        setOrientation(Orientation.VERTICAL);
        // Ensure heat map can take over full tool  window
        setMinWidth(Constants.INFO_SPLIT_PANE_MIN_WIDTH);

        // Top half
        fileHistoryPane = new FileHistoryPane();
        this.getItems().add(fileHistoryPane); // SplitPane isn't a pane, so we cannot use ViewFactory.setPaneChild

        //Bottom half
        commitDetailsPane = new CommitDetailsPane();
        this.getItems().add(commitDetailsPane); // SplitPane isn't a pane, so we cannot use ViewFactory.setPaneChild
    }
}