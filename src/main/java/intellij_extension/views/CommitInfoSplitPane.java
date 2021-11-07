package intellij_extension.views;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;

public class CommitInfoSplitPane extends SplitPane {

    // Top half is Commit History for a single branch
    private CommitHistoryPane commitHistoryPane;

    // Bottom half is Commit Details for a single commit in the selected branch
    private CommitDetailsPane commitDetailsPane;

    public CommitInfoSplitPane() {
        super();

        setOrientation(Orientation.VERTICAL);

        // Top half
        commitHistoryPane = new CommitHistoryPane();
        this.getItems().add(commitHistoryPane); // SplitPane isn't a pane, so we cannot use ViewFactory.setPaneChild

        //Bottom half
        commitDetailsPane = new CommitDetailsPane();
        this.getItems().add(commitDetailsPane); // SplitPane isn't a pane, so we cannot use ViewFactory.setPaneChild
    }
}