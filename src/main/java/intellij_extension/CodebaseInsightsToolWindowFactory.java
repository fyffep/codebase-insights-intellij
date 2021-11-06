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

public class CodebaseInsightsToolWindowFactory implements ToolWindowFactory {

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
            MainScene mainScene = new MainScene(root, componentWidth, componentHeight);
            root.prefWidthProperty().bind(mainScene.widthProperty());
            root.prefHeightProperty().bind(mainScene.heightProperty());

            //Create the model
            CodeBase codeBase = new CodeBase();

            //Create the main heat map area
//            HeatMapController heatMapController = new HeatMapController(codeBase);
//            root.getItems().add(heatMapController.getView()); //show the view

            // Left Half of Tools Windows
            HeatMapPane heatMapPane = new HeatMapPane();
            root.getItems().add(heatMapPane);;

            // Right Half of Tools Window
            // Will split into two sections
            CommitInfoSplitPane commitInfoSplitPane = new CommitInfoSplitPane();
            root.getItems().add(commitInfoSplitPane);
            fxPanel.setScene(mainScene);
        });

        component.getParent().add(fxPanel);
    }
}