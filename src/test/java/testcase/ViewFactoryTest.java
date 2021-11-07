package testcase;

import intellij_extension.views.ViewFactory;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class ViewFactoryTest {

    private static JFXPanel fxPanel;
    private String id;

    public ViewFactoryTest(String id) {
        this.id = id;
    }

    @Contract(pure = true)
    @Parameterized.Parameters(name = "{0}")
    public static @NotNull Iterable<String> data() {
        return Arrays.asList(
                "Id_1",
                "Id_2",
                "Id_3",
                "Id_4");
    }

    @BeforeAll
    public static void setUpBeforeClass() {
        // Forces the JavaFX thread to start
        // Without tests will fail
        fxPanel = new JFXPanel();
    }

    @AfterAll
    public static void setUpAfterClass() {
        // Flush the view factory?
    }

    @Test
    public void getInstance_OnlyOneExists() {
        ViewFactory instance = ViewFactory.getInstance();
        Assert.assertTrue(instance != null);

        ViewFactory instance2 = ViewFactory.getInstance();
        Assert.assertEquals(instance, instance2);
    }

    @Test
    public void setPaneChild_ChildParentedCorrectly() {
        // Adding one child
        Pane parent = new Pane();
        Rectangle child1 = new Rectangle();
        ViewFactory.setPaneChild(parent, child1);
        Assert.assertEquals(parent.getChildren().get(0), child1);

        // Adding a 2nd child
        Pane parent2 = new Pane();
        ViewFactory.setPaneChild(parent, parent2);
        Assert.assertEquals(parent.getChildren().get(1), parent2);

        // Nested children; Parent->Parent2->child3
        Rectangle child3 = new Rectangle();
        ViewFactory.setPaneChild(parent2, child3);
        Assert.assertEquals(parent2.getChildren().get(0), child3);
    }

    @Test
    public void createOrGetHBox_CreateSuccessfullyWithProperId(String id) {
        HBox obj = ViewFactory.getInstance().createOrGetHBox(id);
        Assert.assertTrue(obj.getId() == id);
    }

    @Test
    public void createOrGetHBox_ReturnsSame(String id) {
        HBox obj = ViewFactory.getInstance().createOrGetHBox(id);
        HBox obj1 = ViewFactory.getInstance().createOrGetHBox(id);

        // Same HBox and same id across the board
        Assert.assertEquals(obj, obj1);
        Assert.assertEquals(id, obj1.getId());
        Assert.assertEquals(obj.getId(), obj1.getId());
    }

    @Test
    public void createOrGetVBox_CreateSuccessfullyWithProperId(String id) {
        VBox obj = ViewFactory.getInstance().createOrGetVBox(id);
        Assert.assertTrue(obj.getId() == id);
    }

    @Test
    public void createOrGetVBox_ReturnsSame(String id) {
        VBox obj = ViewFactory.getInstance().createOrGetVBox(id);
        VBox obj1 = ViewFactory.getInstance().createOrGetVBox(id);

        // Same HBox and same id across the board
        Assert.assertEquals(obj, obj1);
        Assert.assertEquals(id, obj1.getId());
        Assert.assertEquals(obj.getId(), obj1.getId());
    }

    @Test
    public void createOrGetScrollPane_CreateSuccessfullyWithProperId(String id) {
        ScrollPane obj = ViewFactory.getInstance().createOrGetScrollPane(id);
        Assert.assertTrue(obj.getId() == id);
    }

    @Test
    public void createOrGetScrollPane_ReturnsSame(String id) {
        ScrollPane obj = ViewFactory.getInstance().createOrGetScrollPane(id);
        ScrollPane obj1 = ViewFactory.getInstance().createOrGetScrollPane(id);

        // Same HBox and same id across the board
        Assert.assertEquals(obj, obj1);
        Assert.assertEquals(id, obj1.getId());
        Assert.assertEquals(obj.getId(), obj1.getId());
    }

    @Test
    public void createOrGetText_CreateSuccessfullyWithProperId(String id) {
        Text obj = ViewFactory.getInstance().createOrGetText(id);
        Assert.assertTrue(obj.getId() == id);
    }

    @Test
    public void createOrGetText_ReturnsSame(String id) {
        Text obj = ViewFactory.getInstance().createOrGetText(id);
        Text obj1 = ViewFactory.getInstance().createOrGetText(id);

        // Same HBox and same id across the board
        Assert.assertEquals(obj, obj1);
        Assert.assertEquals(id, obj1.getId());
        Assert.assertEquals(obj.getId(), obj1.getId());
    }

    @Test
    public void createOrGetComboBox_CreateSuccessfullyWithProperId(String id) {
        ComboBox obj = ViewFactory.getInstance().createOrGetComboBox(id);
        Assert.assertTrue(obj.getId() == id);
    }

    @Test
    public void createOrGetComboBox_ReturnsSame(String id) {
        ComboBox obj = ViewFactory.getInstance().createOrGetComboBox(id);
        ComboBox obj1 = ViewFactory.getInstance().createOrGetComboBox(id);

        // Same HBox and same id across the board
        Assert.assertEquals(obj, obj1);
        Assert.assertEquals(id, obj1.getId());
        Assert.assertEquals(obj.getId(), obj1.getId());
    }

    @Test
    public void createOrGetTableView_CreateSuccessfullyWithProperId(String id) {
        TableView obj = ViewFactory.getInstance().createOrGetTableView(id);
        Assert.assertTrue(obj.getId() == id);
    }

    @Test
    public void createOrGetTableView_ReturnsSame(String id) {
        TableView obj = ViewFactory.getInstance().createOrGetTableView(id);
        TableView obj1 = ViewFactory.getInstance().createOrGetTableView(id);

        // Same HBox and same id across the board
        Assert.assertEquals(obj, obj1);
        Assert.assertEquals(id, obj1.getId());
        Assert.assertEquals(obj.getId(), obj1.getId());
    }
}
