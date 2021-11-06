package intellij_extension.views;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.HashMap;

public class ViewFactory {

    private static ViewFactory instance;

    private HashMap<String, HBox> managedHBoxes;
    private HashMap<String, Text> managedTexts;
    private HashMap<String, ComboBox> managedComboBoxes;
    private HashMap<String, TableView> managedTableViews;

    private ViewFactory() {
        // TODO
        // initialize data structures as we add them.
        managedHBoxes = new HashMap<>();
        managedTexts = new HashMap<>();
        managedComboBoxes = new HashMap<>();
        managedTableViews = new HashMap<>();
    }

    public static ViewFactory getInstance() {
        if(instance == null) {
            instance = new ViewFactory();
        }

        return instance;
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
        if(managedHBoxes.containsKey(id)) {
            return managedHBoxes.get(id);
        }
        // Completely new object
        // Make it
        HBox newHBox = new HBox();
        // Set its id
        newHBox.setId(id);
        // Track it and put it in use
        managedHBoxes.put(id, newHBox);
        // Send back
        return newHBox;
    }

    public Text createOrGetText(String id) {
        // Check if ViewFactory already created this object
        if(managedTexts.containsKey(id)) {
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
        if(managedComboBoxes.containsKey(id)) {
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
        if(managedTableViews.containsKey(id)) {
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

    /*
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Children Management
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * These methods might seem ridiculous
    * But this will reduce SonarQube's duplication code count
     */
    public static void setPaneChild(Pane parent, Node child) {
        parent.getChildren().add(child);
    }



}
