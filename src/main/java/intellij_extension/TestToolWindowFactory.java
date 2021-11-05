package intellij_extension;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.CodeBase;
import intellij_extension.views.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class TestToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        final JFXPanel fxPanel = new JFXPanel();
        fxPanel.setBackground(Color.BLACK);
        JComponent component = toolWindow.getComponent();
        int componentWidth = component.getWidth();
        int componentHeight = component.getHeight();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            CodebaseHeatMapSplitPane root = new CodebaseHeatMapSplitPane();
            MainView mainView = new MainView(root, componentWidth, componentHeight);
//            root.setFillHeight(true);
            root.prefWidthProperty().bind(mainView.widthProperty());
            root.prefHeightProperty().bind(mainView.heightProperty());

            //Create the model
            CodeBase codeBase = new CodeBase();

            //Create the main heat map area
            HeatMapController heatMapController = new HeatMapController(codeBase);
            root.getItems().add(heatMapController.getView()); //show the view

            // Left Half of Tools Windows
            //HeatMapPane heatMapPane = new HeatMapPane();
            //root.getItems().add(heatMapPane);
//            codebaseScene.getSplitBox().getChildren().add(heatMapPane);
//            HBox.setHgrow(heatMapPane, Priority.ALWAYS);

            // Right Half of Tools Window
            // Will split into two sections
            ExtrasSplitPane extrasSplitPane = new ExtrasSplitPane();
            root.getItems().add(extrasSplitPane);
//            codebaseScene.getSplitBox().getChildren().add(extrasVBox);
//            HBox.setHgrow(extrasVBox, Priority.ALWAYS);

            // Top half is Options/Commit Area
            ExtrasTabPane extrasTabPane = new ExtrasTabPane();
            extrasSplitPane.getItems().add(extrasTabPane);
            VBox.setVgrow(extrasTabPane, Priority.ALWAYS);

            //Bottom half is Packages/Files Area
            PackagesFilesPane packagesFilesPane = new PackagesFilesPane();
            extrasSplitPane.getItems().add(packagesFilesPane);
            VBox.setVgrow(packagesFilesPane, Priority.ALWAYS);

            fxPanel.setScene(mainView);
        });

        component.getParent().add(fxPanel);
    }
}