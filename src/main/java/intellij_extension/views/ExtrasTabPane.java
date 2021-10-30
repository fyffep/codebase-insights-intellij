package intellij_extension.views;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ExtrasTabPane extends TabPane {

    public ExtrasTabPane() {
        super();

        Tab tab = new Tab();
        tab.setText("Commit History");
        Pane commitHistoryPane = new Pane();
        Color backgroundColor = Color.THISTLE;
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(backgroundFill);
        commitHistoryPane.setBackground(background);
        tab.setContent(commitHistoryPane);
        getTabs().add(tab);

        tab = new Tab();
        tab.setText("Options");
        Pane optionsPane = new Pane();
        backgroundColor = Color.CHOCOLATE;
        backgroundFill = new BackgroundFill(backgroundColor, null, null);
        background = new Background(backgroundFill);
        optionsPane.setBackground(background);
        tab.setContent(optionsPane);
        getTabs().add(tab);

        Text text = new Text();
        text.setX(40);
        text.setY(150);
        text.setFont(new Font(25));
        text.setText("OptionsCommitHistoryView");
        getChildren().add(text);

        // Fill background with default
        backgroundColor = Color.LAWNGREEN;
        backgroundFill = new BackgroundFill(backgroundColor, null, null);
        background = new Background(backgroundFill);
        setBackground(background);
    }
}
