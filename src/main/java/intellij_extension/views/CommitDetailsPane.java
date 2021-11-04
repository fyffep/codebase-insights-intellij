package intellij_extension.views;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CommitDetailsPane extends Pane {

    public CommitDetailsPane() {
        super();

        Text text = new Text();
        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("PackagesFilesView");
        getChildren().add(text);

        // Fill background with default
        Color backgroundColor = Color.INDIANRED;
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(backgroundFill);
        setBackground(background);
    }
}