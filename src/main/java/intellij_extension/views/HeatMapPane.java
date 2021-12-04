package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.Constants.HeatMetricOptions;
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
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class HeatMapPane implements IContainerView, CodeBaseObserver {

    //region Vars
    private final ObservableList<String> activeBranchList = FXCollections.observableArrayList();
    // Basically this class' main node
    private VBox parent;
    // Banner that holds heat metric and branch comboBoxes
    private VBox topHorizontalBanner;
    private ComboBox<String> heatMetricComboBox;
    private ComboBox<String> branchComboBox;
    // Holds HeatMapPane and CommitGroupingPane
    // private HeatMapTabbedPane heatMapTabbedPane;
    // Heat Map for a single commit + the history up to the commit
    // TODO move out when HeatMapTabbedPane is ready
    private HeatMapFlowPane heatMapFlowPane;
    //endregion

    //region Constructors
    public HeatMapPane() {
        parent = new VBox();
        parent.setMinWidth(Constants.ZERO_WIDTH);

        // Create the top horizontal banner
        topHorizontalBanner = new VBox();
        topHorizontalBanner.setMinWidth(Constants.ZERO_WIDTH);
        topHorizontalBanner.prefWidthProperty().bind(parent.widthProperty());
        // Child layout properties
        topHorizontalBanner.setAlignment(Constants.BANNER_ALIGNMENT);
        parent.getChildren().add(topHorizontalBanner);

        // Create the HBox for the combo boxes
        HBox comboBoxContainer = new HBox();
        topHorizontalBanner.getChildren().add(comboBoxContainer);
        // Add constraints to width/height
        comboBoxContainer.setMinHeight(Constants.BANNER_MIN_HEIGHT);
        comboBoxContainer.setMinWidth(Constants.ZERO_WIDTH);
        comboBoxContainer.prefWidthProperty().bind(parent.widthProperty());
        // Child layout properties
        comboBoxContainer.setAlignment(Constants.BANNER_ALIGNMENT);
        comboBoxContainer.setSpacing(Constants.BANNER_SPACING);
        comboBoxContainer.setPadding(Constants.BANNER_INSETS);

        // Label for heatMetric ComboBox
        Text heatMetricTitle = new Text();
        comboBoxContainer.getChildren().add(heatMetricTitle);
        heatMetricTitle.setText(Constants.HEAT_METRIC_COMBOBOX_TITLE);

        // Create heatMetric comboBox
        heatMetricComboBox = new ComboBox<>();
        comboBoxContainer.getChildren().add(heatMetricComboBox);
        // Set up observable list
        heatMetricComboBox.setItems(Constants.HEAT_METRIC_OPTIONS);
        heatMetricComboBox.getSelectionModel().select(0);
        // Set up the select action
        heatMetricComboBox.setOnAction(this::heatMetricOptionSelectedAction);
        //Set default option (TEMPORARY to avoid confusion while Overall heat is not yet implemented)
        heatMetricComboBox.setValue("Number of Commits");

        // Label for branch ComboBox
        Text branchTitle = new Text();
        comboBoxContainer.getChildren().add(branchTitle);
        branchTitle.setText(Constants.BRANCH_COMBOBOX_TITLE);

        // Create branch comboBox
        branchComboBox = new ComboBox<>();
        comboBoxContainer.getChildren().add(branchComboBox);
        // Set up observable list
        branchComboBox.setItems(activeBranchList);
        // Set up the select action
        branchComboBox.setOnAction(this::branchSelectedAction);

        // Top-Vs-All commit controls
        // Create the HBox for the radio buttons +
        HBox commitFilterContainer = new HBox();
        topHorizontalBanner.getChildren().add(commitFilterContainer);
        // Add constraints to width/height
        commitFilterContainer.setMinWidth(Constants.ZERO_WIDTH);
        commitFilterContainer.prefWidthProperty().bind(parent.widthProperty());
        commitFilterContainer.setMinHeight(Constants.BANNER_MIN_HEIGHT);
        // Child layout properties
        commitFilterContainer.setAlignment(Constants.BANNER_ALIGNMENT);
        commitFilterContainer.setSpacing(Constants.BANNER_SPACING);
        commitFilterContainer.setPadding(Constants.BANNER_INSETS);

        // Radio button group
        ToggleGroup commitFilteringGroup = new ToggleGroup();

        // Radio buttons
        RadioButton allCommitsButton = new RadioButton("All Files");
        allCommitsButton.setToggleGroup(commitFilteringGroup);
        allCommitsButton.setSelected(false);
        commitFilterContainer.getChildren().add(allCommitsButton);

        RadioButton topCommitsButton = new RadioButton("Top 10 Hottest Files");
        topCommitsButton.setToggleGroup(commitFilteringGroup);
        topCommitsButton.setSelected(true);
        commitFilterContainer.getChildren().add(topCommitsButton);

        // Slider to control # of top commits
        Slider topCommitsSlider = new Slider();
        topCommitsSlider.setMin(1);
        topCommitsSlider.setMax(10);
        topCommitsSlider.setValue(10/2);
        topCommitsSlider.setShowTickLabels(true);
//        topCommitsSlider.setShowTickMarks(true);
        topCommitsSlider.setSnapToTicks(true);
        topCommitsSlider.setSnapToPixel(true);
        topCommitsSlider.setMajorTickUnit(1);
        topCommitsSlider.setMinorTickCount(0);
        topCommitsSlider.setBlockIncrement(1);
        commitFilterContainer.getChildren().add(topCommitsSlider);

        // Tabbed view
        TabPane tabPane = new TabPane();
        parent.getChildren().add(tabPane);
        tabPane.setMinHeight(Constants.ZERO_WIDTH);
        tabPane.prefHeightProperty().bind(parent.heightProperty());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Set  up tabs
        // Dashboard tab
        Tab tab = new Tab();
        tab.setText(Constants.DASHBOARD_TEXT);
        DashboardPane dashboardContent = new DashboardPane();
        tab.setContent(dashboardContent.getNode());
        tabPane.getTabs().add(tab);

        // Package tab
        tab = new Tab();
        tab.setText(Constants.HEAT_GROUPING_TEXT);
        HeatMapFlowPane heatMapTabContent = new HeatMapFlowPane();
        heatMapTabContent.setGroupingMode(Constants.GroupingMode.PACKAGES);
        tab.setContent(heatMapTabContent.getNode());
        tabPane.getTabs().add(tab);

        // Commit tab
        tab = new Tab();
        tab.setText(Constants.COMMIT_GROUPING_TEXT);
        HeatMapFlowPane commitTabContent = new HeatMapFlowPane();
        commitTabContent.setGroupingMode(Constants.GroupingMode.COMMITS);
        tab.setContent(commitTabContent.getNode());
        tabPane.getTabs().add(tab);

        tabPane.getSelectionModel().selectedItemProperty().addListener(this::tabSelectedAction);

        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
        HeatMapController.getInstance().branchListRequested();
    }

    //endregion

    //region UI actions
    private void branchSelectedAction(ActionEvent event) {
        String selectedValue = branchComboBox.getValue();
//        System.out.printf("The %s branch was selected. Update HeatMap, CommitHistory, and CommitDetails, Hide SelectedFileTerminal Window.%n", selectedValue);

        HeatMapController.getInstance().newBranchSelected(selectedValue);
    }

    private void heatMetricOptionSelectedAction(ActionEvent event) {
        String selectedValue = heatMetricComboBox.getValue();
//        System.out.printf("The %s option was selected. Update HeatMap with info based on it.%n", selectedValue);

        HeatMapController.getInstance().newHeatMetricSelected(selectedValue);
    }

    private void tabSelectedAction(Observable observable, Tab oldTab, Tab newTab) {
//        System.out.printf("%s deselected, %s selected.%n", oldTab.getText(), newTab.getText());
        HeatMapController.getInstance().heatMapGroupingChanged(newTab.getText());
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return parent;
    }

    @Override
    public void clearPane() {

    }
    //endregion

    //region CodeBaseObserver methods
    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode, HeatMetricOptions heatMetricOption) {
        // Nothing to do for this action
    }

    @Override
    public void branchListRequested(String activeBranch, @NotNull Iterator<String> branchList) {
        activeBranchList.clear();

        while (branchList.hasNext()) {
            String branchName = branchList.next();
            activeBranchList.add(branchName);
        }

        branchComboBox.getSelectionModel().select(activeBranch);
    }

    @Override
    public void newBranchSelected(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode, HeatMetricOptions heatMetricOption) {
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
