package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.FileObject;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class HeatFileComponent extends Pane {

    private HeatFileContainer parent;
    private boolean hasFadeTransition;
    private final String fileObjectId;
    private final Integer fileHeatLevel;

    public HeatFileComponent(FileObject fileObject, int fileHeatLevel, HeatFileContainer parent) {
        super();
        setMinWidth(Constants.ZERO_WIDTH);
        this.setPrefWidth(20);
        this.setPrefHeight(30);
        this.parent = parent;

        this.fileObjectId = fileObject.getPath().toString();
        this.fileHeatLevel = fileHeatLevel;

        this.setOnMouseClicked(event -> {
            HeatMapController.getInstance().heatMapComponentSelected(fileObjectId);
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
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.6), this);
            fadeTransition.setFromValue(0.5);
            fadeTransition.setToValue(1.5);
            fadeTransition.setCycleCount(Animation.INDEFINITE);
            fadeTransition.play();
        });
    }

    public boolean hasFadeTransition() {
        return hasFadeTransition;
    }
}