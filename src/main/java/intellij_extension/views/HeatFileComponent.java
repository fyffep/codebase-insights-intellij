package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.FileObject;
import javafx.scene.layout.Pane;

public class HeatFileComponent extends Pane {

    private HeatFileContainer parent;
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
}