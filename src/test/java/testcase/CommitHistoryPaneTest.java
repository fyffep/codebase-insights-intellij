package testcase;

import intellij_extension.Constants;
import intellij_extension.views.CommitHistoryPane;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CommitHistoryPaneTest {

    @Test
    public void defaultConstructorTest() {
        // Forces the JavaFX thread to start
        // Without tests will fail
        JFXPanel fxPanel = new JFXPanel();

        CommitHistoryPane chp = new CommitHistoryPane();

        // Assert banner object was created
        List<Node> view = chp.getChildren().stream().filter(node -> node.getId() == Constants.CH_BANNER_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof HBox);
        assertEquals(1, view.size());

        // Grab banner VBox
        HBox header = (HBox) view.get(0);

        // Assert header text
        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CH_HEADER_TEXT_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof Text);
        assertEquals(1, view.size());

        // Assert combo box
        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CH_BRANCH_COMBOBOX_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof ComboBox);
        assertEquals(1, view.size());

        // Assert TableView
        view = chp.getChildren().stream().filter(node -> node.getId() == Constants.CH_BRANCH_TABLEVIEW_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof TableView);
        assertEquals(1, view.size());
    }
}
