package intellij_extension.views;

import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.FileObject;
import javafx.scene.layout.Pane;

public class HeatFileComponent extends Pane {

    private final String fileObjectId;

    public HeatFileComponent(FileObject fileObject) {
        super();
        this.setPrefWidth(20);
        this.setPrefHeight(30);

        this.fileObjectId = fileObject.getPath().toString();

        this.setOnMouseClicked(event -> {
            HeatMapController.getInstance().heatMapComponentSelected(fileObjectId);
        });
    }
}