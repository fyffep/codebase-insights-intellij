package testcase;

import intellij_extension.Constants;
import intellij_extension.views.CommitDetailsPane;
import intellij_extension.views.CommitHistoryPane;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CommitHistoryPaneTest {

    private static JFXPanel fxPanel;
    private static CommitHistoryPane chp;

    @BeforeAll
    public static void setUpBeforeClass() {
        // Forces the JavaFX thread to start
        // Without tests will fail
        fxPanel = new JFXPanel();
        // Create the main Pane
        chp = new CommitHistoryPane();
    }

    @AfterAll
    public static void setUpAfterClass() {
        // Flush the view factory?
    }

    @Test
    public void constructor_BannerHBoxSuccessfullyCreated() {
        // Assert banner object was created
        List<Node> view = chp.getChildren().stream().filter(node -> node.getId() == Constants.CH_BANNER_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof HBox);
        assertEquals(1, view.size());
    }

    @Test
    public void constructor_BannerHeaderTextSuccessfullyCreated() {
        // Grab banner HBox
        List<Node> view = chp.getChildren().stream().filter(node -> node.getId() == Constants.CH_BANNER_ID).collect(Collectors.toList());
        HBox header = (HBox) view.get(0);

        // Assert header text
        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CH_HEADER_TEXT_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof Text);
        assertEquals(1, view.size());
    }

    @Test
    public void constructor_BannerComboBoxSuccessfullyCreated() {
        // Grab banner HBox
        List<Node> view = chp.getChildren().stream().filter(node -> node.getId() == Constants.CH_BANNER_ID).collect(Collectors.toList());
        HBox header = (HBox) view.get(0);

        // Assert combo box
        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CH_BRANCH_COMBOBOX_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof ComboBox);
        assertEquals(1, view.size());
    }

    @Test
    public void constructor_BannerTableViewSuccessfullyCreated() {
        // Grab banner HBox
        List<Node> view = chp.getChildren().stream().filter(node -> node.getId() == Constants.CH_BANNER_ID).collect(Collectors.toList());
        HBox header = (HBox) view.get(0);

        // Assert TableView
        view = chp.getChildren().stream().filter(node -> node.getId() == Constants.CH_BRANCH_TABLEVIEW_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof TableView);
        assertEquals(1, view.size());
    }

    // I haven't figured out how to assert info in table view is correct.
    // These tests are needed!
}
