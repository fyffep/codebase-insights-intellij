package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.views.interfaces.IContainerView;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Iterator;

public class HeatMapPane implements IContainerView, CodeBaseObserver {

    //region Vars
    // Basically this class' main node
    private VBox parent;
    // Banner that holds heat metric and branch comboBoxes
    private HBox topHorizontalBanner;
    private ComboBox<String> heatMetricComboBox;
    private ComboBox<String> branchComboBox;
    private final ObservableList<String> activeBranchList = FXCollections.observableArrayList();

    // Holds HeatMapPane and CommitGroupingPane
    // private HeatMapTabbedPane heatMapTabbedPane;
    // Heat Map for a single commit + the history up to the commit
    // TODO move out when HeatMapTabbedPane is ready
    private HeatMapFlowPane heatMapFlowPane;
    //endregion

    //region Constructors
    public HeatMapPane() {
        parent = new VBox();

        // Create the top horizontal banner
        topHorizontalBanner = new HBox();
        parent.getChildren().add(topHorizontalBanner);
        // Add constraints to width/height
        topHorizontalBanner.setMinHeight(Constants.BANNER_MIN_HEIGHT);
        topHorizontalBanner.prefWidthProperty().bind(parent.widthProperty());
        // Child layout properties
        topHorizontalBanner.setAlignment(Constants.BANNER_ALIGNMENT);
        topHorizontalBanner.setSpacing(Constants.BANNER_SPACING);
        topHorizontalBanner.setPadding(Constants.BANNER_INSETS);

        // Label for heatMetric ComboBox
        Text heatMetricTitle = new Text();
        topHorizontalBanner.getChildren().add(heatMetricTitle);
        heatMetricTitle.setText(Constants.HEAT_METRIC_COMBOBOX_TITLE);

        // Create heatMetric comboBox
        heatMetricComboBox = new ComboBox<>();
        topHorizontalBanner.getChildren().add(heatMetricComboBox);
        // Set up observable list
        heatMetricComboBox.setItems(Constants.HEAT_METRIC_OPTIONS);
        heatMetricComboBox.getSelectionModel().select(0);
        // Set up the select action
        heatMetricComboBox.setOnAction(this::heatMetricOptionSelectedAction);

        // Label for branch ComboBox
        Text branchTitle = new Text();
        topHorizontalBanner.getChildren().add(branchTitle);
        branchTitle.setText(Constants.BRANCH_COMBOBOX_TITLE);

        // Create branch comboBox
        branchComboBox = new ComboBox<>();
        topHorizontalBanner.getChildren().add(branchComboBox);
        // Set up observable list
        branchComboBox.setItems(activeBranchList);
        // Set up the select action
        branchComboBox.setOnAction(this::branchSelectedAction);

        // Tabbed view
        TabPane tabPane = new TabPane();
        parent.getChildren().add(tabPane);
        tabPane.prefHeightProperty().bind(parent.heightProperty());
        tabPane.getSelectionModel().selectedItemProperty().addListener(this::tabSelectedAction);
        // Set  up tabs
        // Package tab
        Tab tab = new Tab();
        tab.setText(Constants.HEAT_GROUPING_TEXT);
        HeatMapFlowPane heatMapTabContent = new HeatMapFlowPane(tabPane);
        tab.setContent(heatMapTabContent.getNode());
        tabPane.getTabs().add(tab);
        // Commit tab
        tab = new Tab();
        tab.setText(Constants.COMMIT_GROUPING_TEXT);
        HeatMapFlowPane commitTabContent = new HeatMapFlowPane(tabPane);
        tab.setContent(commitTabContent.getNode());
        tabPane.getTabs().add(tab);

        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
        HeatMapController.getInstance().branchListRequested();
    }

    //endregion

    //region UI actions
    private void branchSelectedAction(ActionEvent event) {
        String selectedValue = branchComboBox.getValue();
        System.out.printf("The %s branch was selected. Update HeatMap, CommitHistory, and CommitDetails, Hide SelectedFileTerminal Window.%n", selectedValue);

        HeatMapController.getInstance().newBranchSelected(selectedValue);
    }

    private void heatMetricOptionSelectedAction(ActionEvent event) {
        String selectedValue = heatMetricComboBox.getValue();
        System.out.printf("The %s option was selected. Update HeatMap with info based on it.%n", selectedValue);

        HeatMapController.getInstance().newHeatMetricSelected(selectedValue);
    }

    private void tabSelectedAction(Observable observable, Tab oldTab, Tab newTab) {
        System.out.printf("%s deselected, %s selected.%n", oldTab.getText(), newTab.getText());
        HeatMapController.getInstance().heatMapGroupingChanged(newTab.getText());
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return parent;
    }
    //endregion

    //region CodeBaseObserver methods
    @Override
    public void refreshHeatMap(Codebase codeBase) {
        // Nothing to do for this action
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        activeBranchList.clear();

        while (branchList.hasNext()) {
            String branchName = branchList.next();
            activeBranchList.add(branchName);
        }

        branchComboBox.getSelectionModel().select(activeBranch);
    }

    @Override
    public void newBranchSelected() {
        // Nothing to do for this action
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Nothing to do for this action

    }

    @Override
    public void commitSelected(Commit commit) {
        // Nothing to do for this action
    }
    //endregion
}
