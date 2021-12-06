package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.FileObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static intellij_extension.Constants.DROP_SHADOW_DEPTH;

public class HeatFileComponent extends Pane {

    private HeatFileContainer parent;
    private boolean hasFadeTransition;
    private final String fileObjectPath;
    private final String fileName;
    private final Integer fileHeatLevel;

    private String heatMetric;

    public HeatFileComponent(FileObject fileObject, int fileHeatLevel, HeatFileContainer parent) {
        super();
        setMinWidth(Constants.ZERO_WIDTH);
        this.setPrefWidth(20);
        this.setPrefHeight(30);
        this.parent = parent;

        this.fileName = fileObject.getFilename();
        this.fileObjectPath = fileObject.getPath().toString();
        this.fileHeatLevel = fileHeatLevel;

        this.setOnMouseClicked(event -> {
            HeatMapController.getInstance().heatMapComponentSelected(fileObjectPath);
        });
    }

    public Integer getFileHeatLevel() {
        return fileHeatLevel;
    }

    public HeatFileContainer getContainer() {
        return parent;
    }

    // Adds a fade transition to the file when called
    public void setFadeTransition() {
        this.hasFadeTransition = true;
        Glow glow = new Glow();
        glow.setLevel(1.5);
        this.setEffect(glow);
        Platform.runLater(() -> {
            DropShadow borderGlow = new DropShadow();
            borderGlow.setOffsetY(0f);
            borderGlow.setOffsetX(0f);
            borderGlow.setColor(Color.RED);
            borderGlow.setWidth(DROP_SHADOW_DEPTH);
            borderGlow.setHeight(DROP_SHADOW_DEPTH);
            this.setEffect(borderGlow);  //Apply the borderGlow effect to the JavaFX node

            Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO,
                            new KeyValue(borderGlow.widthProperty(), DROP_SHADOW_DEPTH),
                            new KeyValue(borderGlow.heightProperty(), DROP_SHADOW_DEPTH)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(borderGlow.widthProperty(), 0),
                            new KeyValue(borderGlow.heightProperty(), 0)));

            timeline.setAutoReverse(true);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        });
    }

    public boolean hasFadeTransition() {
        return hasFadeTransition;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHeatMetric() {
        return heatMetric;
    }

    public void setHeatMetric(String heatMetric) {
        this.heatMetric = heatMetric;
    }
}