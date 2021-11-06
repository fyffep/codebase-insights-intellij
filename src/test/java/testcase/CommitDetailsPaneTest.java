package testcase;

import intellij_extension.Constants;
import intellij_extension.views.CommitDetailsPane;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CommitDetailsPaneTest {

    @Test
    public void defaultConstructorTest() {
        // Forces the JavaFX thread to start
        // Without tests will fail
        JFXPanel fxPanel = new JFXPanel();

        CommitDetailsPane cdp = new CommitDetailsPane();

        // Assert banner object was created
        List<Node> view = cdp.getChildren().stream().filter(node -> node.getId() == Constants.CD_BANNER_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof VBox);
        assertEquals(1, view.size());

        // Grab banner VBox
        VBox header = (VBox) view.get(0);

        // Assert header
        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CD_HEADER_TEXT_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof Text);
        assertEquals(1, view.size());

        // Assert commit detail text objects
        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CD_DESCRIPTION_TEXT_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof Text);
        assertEquals(1, view.size());

        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CD_AUTHOR_TEXT_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof Text);
        assertEquals(1, view.size());

        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CD_DATE_TEXT_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof Text);
        assertEquals(1, view.size());

        view = header.getChildren().stream().filter(node -> node.getId() == Constants.CD_HASH_TEXT_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof Text);
        assertEquals(1, view.size());

        // Assert File Scroll Pane
        view = cdp.getChildren().stream().filter(node -> node.getId() == Constants.CD_FILE_LIST_CONTAINER_ID).collect(Collectors.toList());
        assertNotNull(view);
        assertTrue(view.get(0) instanceof ScrollPane);
        assertEquals(1, view.size());

        // Grab details ScrollPane
        ScrollPane fileListContainer = (ScrollPane) view.get(0);

        // Assert VBox inside ScrollPane
        Node content = fileListContainer.getContent();
        assertNotNull(content);
        assertTrue(content instanceof VBox);
        assertTrue(content.getId() == Constants.CD_FILE_LIST_ID);

        // Assert Test Data created (36 entries)
        assertEquals(36, ((VBox) content).getChildren().size());
    }
}