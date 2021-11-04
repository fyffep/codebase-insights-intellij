package intellij_extension.views;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CommHistoryPane extends VBox
{

    private Pane topHoriztontalBanner;

    private ScrollPane commitList;

    public CommHistoryPane() {
        super();

        // TODO Extract to ViewFactory?
        topHoriztontalBanner = new Pane();
        Color backgroundColor = Color.DARKGREEN;
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(backgroundFill);
        topHoriztontalBanner.setBackground(background);
        // TODO set this based on a portion of the view
        // like the top banner should get 10% real estate
        topHoriztontalBanner.setMinHeight(0);
        topHoriztontalBanner.setPrefHeight(100);
        topHoriztontalBanner.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(topHoriztontalBanner);

        // TODO Extract to ViewFactory?
        commitList = new ScrollPane();
        backgroundColor = Color.MEDIUMSPRINGGREEN;
        backgroundFill = new BackgroundFill(backgroundColor, null, null);
        background = new Background(backgroundFill);
        commitList.setBackground(background);
        // TODO set this based on a portion of the view
        // like the top banner should get 90% real estate
        // But it should also be dynamic shrink with the parent
        commitList.setMinHeight(0);
//        commitList.setPrefHeight(1000);
        commitList.prefWidthProperty().bind(this.widthProperty());
        commitList.prefViewportHeightProperty().bind(this.heightProperty());
//        commitList.maxHeightProperty().bind(this.heightProperty());
        this.getChildren().add(commitList);

        Text text = new Text();
        text.setFont(new Font(12));
        text.setText("Commit History View");
        topHoriztontalBanner.getChildren().add(text);
        text.setX(0);
        text.setY(10);

        // Fill background with default
        backgroundColor = Color.LAWNGREEN;
        backgroundFill = new BackgroundFill(backgroundColor, null, null);
        background = new Background(backgroundFill);
        setBackground(background);
    }
}
