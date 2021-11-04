package intellij_extension;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
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
            MainScene mainScene = new MainScene(root, componentWidth, componentHeight);
            root.prefWidthProperty().bind(mainScene.widthProperty());
            root.prefHeightProperty().bind(mainScene.heightProperty());

            // Left Half of Tools Windows
            HeatMapPane heatMapPane = new HeatMapPane();
            root.getItems().add(heatMapPane);;

            // Right Half of Tools Window
            // Will split into two sections
            CommitInfoSplitPane commitInfoSplitPane = new CommitInfoSplitPane();
            root.getItems().add(commitInfoSplitPane);

            // Top half is Options/Commit Area
            CommHistoryPane commitHistoryPane = new CommHistoryPane();
            commitInfoSplitPane.getItems().add(commitHistoryPane);
            VBox.setVgrow(commitHistoryPane, Priority.ALWAYS);

            //Bottom half is Packages/Files Area
            CommitDetailsPane commitDetailsPane = new CommitDetailsPane();
            commitInfoSplitPane.getItems().add(commitDetailsPane);
            VBox.setVgrow(commitDetailsPane, Priority.ALWAYS);

            fxPanel.setScene(mainScene);
        });

        component.getParent().add(fxPanel);
    }
}