package intellij_extension.views;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SelectedFileViewPane extends AnchorPane{

    public SelectedFileViewPane() {
        super();

        Text text = new Text();
        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("SelectedFileViewPane");
        getChildren().add(text);

        // Fill background with default
        Color backgroundColor = Color.YELLOW;
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(backgroundFill);
        setBackground(background);
    }
    public VBox createVBox()
    {
        VBox vbox=new VBox();
        Label label=new Label("Test");
        vbox.getChildren().add(label);
        return vbox;

    }

    public void createAnchorPane()
    {
        SplitPane splitPane=new SplitPane();
        VBox vbox=this.createVBox();
        VBox.setVgrow(this, Priority.ALWAYS);
        splitPane.getItems().add(vbox);
        splitPane.setOrientation(Orientation.HORIZONTAL);
    }
}