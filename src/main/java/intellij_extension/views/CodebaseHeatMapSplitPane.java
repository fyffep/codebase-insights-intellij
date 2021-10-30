package intellij_extension.views;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class CodebaseHeatMapSplitPane extends SplitPane {


    public CodebaseHeatMapSplitPane() {
        Color backgroundColor = Color.YELLOW;
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(backgroundFill);
        setBackground(background);
    }
}
