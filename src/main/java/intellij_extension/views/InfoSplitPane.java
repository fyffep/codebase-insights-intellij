package intellij_extension.views;

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
        setMinWidth(0);
//        SplitPane.setResizableWithParent(this, Boolean.FALSE);


        // Top half
        fileHistoryPane = new FileHistoryPane();
        this.getItems().add(fileHistoryPane); // SplitPane isn't a pane, so we cannot use ViewFactory.setPaneChild

        //Bottom half
        commitDetailsPane = new CommitDetailsPane();
        this.getItems().add(commitDetailsPane); // SplitPane isn't a pane, so we cannot use ViewFactory.setPaneChild
    }
}