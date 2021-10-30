import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import intellij_extension.models.FileObject;
import intellij_extension.views.HeatMapContainer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class TestToolWindowFactory implements ToolWindowFactory
{
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        final JFXPanel fxPanel = new JFXPanel();
        JComponent component = toolWindow.getComponent();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            Group root  =  new Group();
            Scene heatMapScene = new Scene(root, 300, 300);
            /*
            Text text  =  new Text();
            text.setX(40);
            text.setY(100);
            text.setFont(new Font(25));
            text.setText("Welcome JavaFX!");
            root.getChildren().add(text);
            */

            List<FileObject> fileObjectList = new LinkedList<>();
            fileObjectList.add(new FileObject("TestFile1", "TestDir1", 3));
            fileObjectList.add(new FileObject("TestFile2", "TestDir2", 3));
            fileObjectList.add(new FileObject("TestFile3", "TestDir3", 3));

            HeatMapContainer heatMapContainer = new HeatMapContainer();
            heatMapContainer.populate(fileObjectList);
            root.getChildren().add(heatMapContainer);

            fxPanel.setScene(heatMapScene);
        });

        component.getParent().add(fxPanel);
    }
}