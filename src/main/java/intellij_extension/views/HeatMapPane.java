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
    private final VBox parent;
    // Banner that holds heat metric, branch comboBoxes, and filtering controls
    private final VBox topHorizontalBanner;
    private ComboBox<String> heatMetricComboBox;
    private ComboBox<String> branchComboBox;

    private Slider topFilesSlider;

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

        createComboBoxes();

        createTopAllControls();

        createTabs();

        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
        HeatMapController.getInstance().branchListRequested();
    }

    private void createComboBoxes() {
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
    }

    private void createTopAllControls() {
        // Top-Vs-All Files controls
        // Create the HBox for the radio buttons + slider
        HBox filesFilterContainer = new HBox();
        topHorizontalBanner.getChildren().add(filesFilterContainer);
        // Add constraints to width/height
        filesFilterContainer.setMinWidth(Constants.ZERO_WIDTH);
        filesFilterContainer.prefWidthProperty().bind(parent.widthProperty());
        filesFilterContainer.setMinHeight(Constants.BANNER_MIN_HEIGHT);
        // Child layout properties
        filesFilterContainer.setAlignment(Constants.BANNER_ALIGNMENT);
        filesFilterContainer.setSpacing(Constants.BANNER_SPACING);
        filesFilterContainer.setPadding(Constants.BANNER_INSETS);

        // Radio button group
        ToggleGroup fileFilteringGroup = new ToggleGroup();

        // Radio buttons
        RadioButton allFilesButton = new RadioButton("All Files");
        allFilesButton.setToggleGroup(fileFilteringGroup);
        allFilesButton.setSelected(false);
        allFilesButton.selectedProperty().addListener(this::allRadioButtonClicked);
        filesFilterContainer.getChildren().add(allFilesButton);

        RadioButton topFilesButton = new RadioButton("Top 10 Hottest Files");
        topFilesButton.setToggleGroup(fileFilteringGroup);
        topFilesButton.setSelected(true);
        allFilesButton.selectedProperty().addListener(this::topRadioButtonClicked);
        filesFilterContainer.getChildren().add(topFilesButton);

        // Slider to control # of top commits
        topFilesSlider = new Slider();
        topFilesSlider.setMin(1);
        topFilesSlider.setMax(20);
        topFilesSlider.setValue(20);
        topFilesSlider.setShowTickLabels(true);
        topFilesSlider.setSnapToTicks(true);
        topFilesSlider.setSnapToPixel(true);
        topFilesSlider.setMajorTickUnit(2);
        topFilesSlider.setMinorTickCount(0);
        topFilesSlider.setBlockIncrement(2);
        filesFilterContainer.getChildren().add(topFilesSlider);
    }

    private void createTabs() {
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
        Pane dashboardPane = new Pane();
//        tab.setContent(dashboardPane.getNode());
        tab.setContent(dashboardPane);
        tabPane.getTabs().add(tab);

        // Package tab
        tab = new Tab();
        tab.setText(Constants.HEAT_GROUPING_TEXT);
        HeatMapFlowPane heatMapTabContent = new HeatMapFlowPane();
        heatMapTabContent.setGroupingMode(GroupingMode.PACKAGES);
        tab.setContent(heatMapTabContent.getNode());
        tabPane.getTabs().add(tab);

        // Commit tab
        tab = new Tab();
        tab.setText(Constants.COMMIT_GROUPING_TEXT);
        HeatMapFlowPane commitTabContent = new HeatMapFlowPane();
        commitTabContent.setGroupingMode(GroupingMode.COMMITS);
        tab.setContent(commitTabContent.getNode());
        tabPane.getTabs().add(tab);

        tabPane.getSelectionModel().selectedItemProperty().addListener(this::tabSelectedAction);
        updateUIControlsBasedOnTab(Constants.DASHBOARD_TEXT);
    }
    //endregion

    //region UI actions
    private void branchSelectedAction(ActionEvent event) {
        String selectedValue = branchComboBox.getValue();

        HeatMapController.getInstance().newBranchSelected(selectedValue);
    }

    private void heatMetricOptionSelectedAction(ActionEvent event) {
        String selectedValue = heatMetricComboBox.getValue();

        HeatMapController.getInstance().newHeatMetricSelected(selectedValue);
    }

    private void tabSelectedAction(Observable observable, Tab oldTab, Tab newTab) {
        updateUIControlsBasedOnTab(newTab.getText());

        HeatMapController.getInstance().heatMapGroupingChanged(newTab.getText());
    }

    private void updateUIControlsBasedOnTab(String tab) {
        switch (tab) {
            case Constants.COMMIT_GROUPING_TEXT:
            case Constants.HEAT_GROUPING_TEXT:
                if(!topHorizontalBanner.isVisible()) {
                    parent.getChildren().add(0, topHorizontalBanner); // Make first child so its at the top
                }
                topHorizontalBanner.setVisible(true);
                break;
            case Constants.DASHBOARD_TEXT:
                parent.getChildren().remove(topHorizontalBanner);
                topHorizontalBanner.setVisible(false);
                break;
            default:
                // Unknown state/tab just leaving it as-is...
                break;
        }
        parent.layout();
    }

    private void allRadioButtonClicked(Observable observable, boolean oldValue, boolean newValue) {
        topFilesSlider.setVisible(oldValue);
    }

    private void topRadioButtonClicked(Observable observable, boolean oldValue, boolean newValue) {
        topFilesSlider.setVisible(oldValue);
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
