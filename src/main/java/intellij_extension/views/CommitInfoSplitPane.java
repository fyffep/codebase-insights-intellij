package intellij_extension.views;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class CommitInfoSplitPane extends SplitPane
{
    // Top half is Commit History for a single branch
    private CommitHistoryPane commitHistoryPane;

    // Bottom half is Commit Details for a single commit in the selected branch
    private CommitDetailsPane commitDetailsPane;

    public CommitInfoSplitPane() {
        super();

        setOrientation(Orientation.VERTICAL);

//        Text text = new Text();
//        text.setX(40);
//        text.setY(100);
//        text.setFont(new Font(25));
//        text.setText("ExtrasViews");
//        getChildren().add(text);

        // Fill background with default
        Color backgroundColor = Color.ORANGERED;
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(backgroundFill);
        setBackground(background);


        // Top half is Options/Commit Area
        commitHistoryPane = new CommitHistoryPane();
        getItems().add(commitHistoryPane);
        System.out.println((commitHistoryPane.getWidth()));
        System.out.println((commitHistoryPane.getHeight()));
//        VBox.setVgrow(commitHistoryPane, Priority.ALWAYS);

        //Bottom half is Packages/Files Area
        commitDetailsPane = new CommitDetailsPane();
        getItems().add(commitDetailsPane);
//        VBox.setVgrow(commitDetailsPane, Priority.ALWAYS);
    }
}