package intellij_extension.views;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CommitInfoSplitPane extends SplitPane {

    public CommitInfoSplitPane() {
        super();

        setOrientation(Orientation.VERTICAL);

        Text text = new Text();
        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("ExtrasViews");
        getChildren().add(text);

        // Fill background with default
        Color backgroundColor = Color.ORANGERED;
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(backgroundFill);
        setBackground(background);
    }
}