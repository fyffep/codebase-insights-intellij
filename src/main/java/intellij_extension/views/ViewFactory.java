package intellij_extension.views;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


/**
 * Purpose of this class:
 * 1) Remove and isolate view creation code out of other classes.
 * This keeps the other classes a bit more clean.
 * 2) Hold references for classes if they do not want to.
 * Simply retrieve reference by calling createOrGet and supplying the proper id.
 * 3) A pooling mechanism.
 */
public class ViewFactory {

    private static ViewFactory instance;

    private HashMap<String, HBox> managedHBoxes;
    private HashMap<String, VBox> managedVBoxes;
    private HashMap<String, ScrollPane> managedScrollPanes;
    private HashMap<String, Text> managedTexts;
    private HashMap<String, ComboBox> managedComboBoxes;
    private HashMap<String, TableView> managedTableViews;

    private ViewFactory() {
        managedHBoxes = new HashMap<>();
        managedVBoxes = new HashMap<>();
        managedScrollPanes = new HashMap<>();
        managedTexts = new HashMap<>();
        managedComboBoxes = new HashMap<>();
        managedTableViews = new HashMap<>();
    }

    public static ViewFactory getInstance() {
        if (instance == null) {
            instance = new ViewFactory();
        }

        return instance;
    }

    /*
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Children Management
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * These methods might seem ridiculous
     * But this will reduce SonarQube's duplication code count
     */
    public static void setPaneChild(@NotNull Pane parent, Node child) {
        parent.getChildren().add(child);
    }

    /*
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Creation/Get Management
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * These methods might seem ridiculous
     * But this will reduce SonarQube's duplication code count
     */
    // TODO Rethink these methods... its very redundant...
    // Can we make this a generic T method?
    // Maybe only UI Classes that have multiple instaniatations get the managed hash?
    // So like text or hbox, but not tableview or combo box? (Assuming we only have one tableView and comboBox)
    public HBox createOrGetHBox(String id) {
        // Check if ViewFactory already created this object
        if (managedHBoxes.containsKey(id)) {
            return managedHBoxes.get(id);
        }
        // Completely new object so make it
        HBox newHBox = new HBox();
        // Set its id
        newHBox.setId(id);
        // Track it and put it in use
        managedHBoxes.put(id, newHBox);
        // Send back
        return newHBox;
    }

    public VBox createOrGetVBox(String id) {
        // Check if ViewFactory already created this object
        if (managedVBoxes.containsKey(id)) {
            return managedVBoxes.get(id);
        }
        // Completely new object so make it
        VBox newVBox = new VBox();
        // Set its id
        newVBox.setId(id);
        // Track it and put it in use
        managedVBoxes.put(id, newVBox);
        // Send back
        return newVBox;
    }

    public ScrollPane createOrGetScrollPane(String id) {
        // Check if ViewFactory already created this object
        if (managedScrollPanes.containsKey(id)) {
            return managedScrollPanes.get(id);
        }
        // Completely new object so make it
        ScrollPane newScrollPane = new ScrollPane();
        // Set its id
        newScrollPane.setId(id);
        // Track it and put it in use
        managedScrollPanes.put(id, newScrollPane);
        // Send back
        return newScrollPane;
    }

    public Text createOrGetText(String id) {
        // Check if ViewFactory already created this object
        if (managedTexts.containsKey(id)) {
            return managedTexts.get(id);
        }
        // Completely new object so make it
        Text newText = new Text();
        // Set its id
        newText.setId(id);
        // Track it and put it in use
        managedTexts.put(id, newText);
        // Send back
        return newText;
    }

    public ComboBox createOrGetComboBox(String id) {
        // Check if ViewFactory already created this object
        if (managedComboBoxes.containsKey(id)) {
            return managedComboBoxes.get(id);
        }
        // Completely new object so make it
        ComboBox newComboBox = new ComboBox();
        // Set its id
        newComboBox.setId(id);
        // Track it and put it in use
        managedComboBoxes.put(id, newComboBox);
        // Send back
        return newComboBox;
    }

    public TableView createOrGetTableView(String id) {
        // Check if ViewFactory already created this object
        if (managedTableViews.containsKey(id)) {
            return managedTableViews.get(id);
        }
        // Completely new object so make it
        TableView newTableView = new TableView();
        // Set its id
        newTableView.setId(id);
        // Track it and put it in use
        managedTableViews.put(id, newTableView);
        // Send back
        return newTableView;
    }
}
