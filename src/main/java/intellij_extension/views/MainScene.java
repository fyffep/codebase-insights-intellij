package intellij_extension.views;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class MainScene extends Scene {

    public MainScene(Parent root, double width, double height) {
        super(root, width, height);

        setFill(Color.DARKMAGENTA);
    }
}