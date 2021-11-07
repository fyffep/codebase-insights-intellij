package intellij_extension.views;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class SelectedFileViewPane extends Pane {
    private double paneHeight;
    private Color  backgroundColor;
    private BackgroundFill backgroundFill;
    private Background background;
    private HBox headerHBox;
    private Text headerText;


    public SelectedFileViewPane() {
        super();
        this.backgroundColor = Color.YELLOW;
        this.backgroundFill = new BackgroundFill(this.backgroundColor, null, null);
        this.background = new Background(backgroundFill);
        this.paneHeight = 100;
        setMaxHeight(this.paneHeight);
        setBackground(this.background);// Fill background with default
        headerHBox=new HBox();
//        headerText.setText("Selected File");
        headerHBox.setMinHeight(30);
//        headerHBox.getChildren().add(headerText);
        getChildren().add(headerHBox);

    }

    public void setPaneHeight(double paneHeight) {
        this.paneHeight = paneHeight;
        setMaxHeight(this.paneHeight);
    }

    public double getPaneHeight() {
        return this.paneHeight;
    }

    public  void setBackgroundColor(Color color)
    {
        this.backgroundColor=color;
    }
    public Color getBackgroundColor()
    {
        return this.backgroundColor;
    }



}