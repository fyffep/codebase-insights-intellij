package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.CodebaseV2;
import intellij_extension.models.redesign.CommitV2;
import intellij_extension.models.redesign.FileObjectV2;
import intellij_extension.observer.CodeBaseObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.ArrayList;
import java.util.Iterator;

public class FileHistoryPane extends VBox implements CodeBaseObserver {

    private final HBox topHorizontalBanner;
    private final Text headerText;
    private final ComboBox<String> branchComboBox; // This might have to change from String to something else.
    private final TableView<CommitInfoRow> commitList;

    // Will be used when Model is sending data
    // This is all the lines we created so far - we should never remove from this list
    private final ArrayList<CommitInfoRow> commitLines = new ArrayList<>();
    // These are active lines in the TableView
    private final ObservableList<CommitInfoRow> activeCommitLines = FXCollections.observableArrayList();

    // Branch list
    private final ObservableList<String> branchList = FXCollections.observableArrayList();


    public FileHistoryPane() {
        super();

        // Create the top horizontal banner
        topHorizontalBanner = ViewFactory.getInstance().createOrGetHBox(Constants.FCH_BANNER_ID);
        setBannerProperties();
        ViewFactory.setPaneChild(this, topHorizontalBanner);

        // Create the banner text
        headerText = ViewFactory.getInstance().createOrGetText(Constants.FCH_HEADER_TEXT_ID);
        setHeaderTextProperties();
        ViewFactory.setPaneChild(topHorizontalBanner, headerText);

        // Create the banner combo box
        branchComboBox = ViewFactory.getInstance().createOrGetComboBox(Constants.FCH_BRANCH_COMBOBOX_ID);
        setBranchComboBoxProperties();
        ViewFactory.setPaneChild(topHorizontalBanner, branchComboBox);

        // Create Tableview with data
        commitList = ViewFactory.getInstance().createOrGetTableView(Constants.FCH_BRANCH_TABLEVIEW_ID);
        setCommitListProperties();
        setCommitListColumns();
        ViewFactory.setPaneChild(this, commitList);

        //Register self as an observer of the model
        CodebaseV2 model = CodebaseV2.getInstance();
        model.registerObserver(this);
    }

    /*
        UI Property Settings
     */
    private void setBannerProperties() {
        // Banner layout properties

        // We want this so the user can make the Commit Details view as big
        // as the right side if desirable
        topHorizontalBanner.setMinHeight(Constants.BANNER_MIN_HEIGHT);
        // TODO We really want this to be a set, not a bind.
        // The header shouldn't grow with the window size
        // But it should be a percentage of the window size.
        topHorizontalBanner.prefHeightProperty().bind(this.heightProperty().multiply(Constants.BANNER_SIZE_MULTIPLIER));
        topHorizontalBanner.maxHeightProperty().bind(this.heightProperty().multiply(Constants.BANNER_SIZE_MULTIPLIER));
        topHorizontalBanner.prefWidthProperty().bind(this.widthProperty());

        // Child layout properties
        topHorizontalBanner.setAlignment(Constants.BANNER_ALIGNMENT);
        topHorizontalBanner.setSpacing(Constants.BANNER_SPACING);
        topHorizontalBanner.setPadding(Constants.BANNER_INSETS);
    }

    private void setHeaderTextProperties() {
        headerText.setFont(Font.font(Constants.HEADER_FONT, Constants.HEADER_TEXT_FONT_WEIGHT, Constants.HEADER_TEXT_SIZE));
        headerText.setText(Constants.CH_HEADER_TEXT);
    }

    private void setBranchComboBoxProperties() {
        // TODO
        //  How to get branch list from model?
        branchComboBox.setItems(branchList);
        // Select first entry by default... for now
        branchComboBox.getSelectionModel().selectFirst();
        // Set up the select action
        branchComboBox.setOnAction(this::branchSelectedAction);
    }

    private void setCommitListProperties() {
        commitList.setItems(activeCommitLines);

        // Turn off editable
        commitList.setEditable(false);

        // This forces columns to resize to their content
        commitList.setColumnResizePolicy((param) -> true);
        // Can't have both at the same time =/ so we get that extra column...
        // Do not create n + 1 columns with n + 1 being empty... (Why is that default behavior?!)
        // commitList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set up constraints on width/height
        // We want this so the user can make the Commit Details view as big
        // as the right side if desirable
        commitList.setMinHeight(Constants.FCH_COMMIT_LIST_MIN_HEIGHT);
        // TODO set this based on a portion of the view
        // like the top banner should get 90% real estate
        // But it should also be dynamic shrink with the parent
        commitList.prefWidthProperty().bind(this.widthProperty());
        commitList.prefHeightProperty().bind(this.heightProperty());

        // Add click method to rows
        // I hate that I can't refactor this double lambda expression
        commitList.setRowFactory(tableView -> {
            TableRow<CommitInfoRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    CommitInfoRow rowData = row.getItem();
                    Constants.LOG.info("Commit " + rowData.getCommitHash().getValue() + " was double clicked! Update CommitDetails!");
                    HeatMapController.getInstance().commitSelected(rowData.getCommitHash().getValue());
                }
            });
            return row;
        });
    }

    private void setCommitListColumns() {
        // Number Column
        TableColumn<CommitInfoRow, String> rowColumn = new TableColumn("#");

        // Description Column
        TableColumn<CommitInfoRow, String> descriptionColumn = new TableColumn("Description");
        descriptionColumn.setPrefWidth(Constants.FCH_DESCRIPTION_COLUMN_MAX_WIDTH);
        descriptionColumn.setSortable(false);

        // Author Column
        TableColumn<CommitInfoRow, String> authorColumn = new TableColumn("Author");

        // Date Column
        TableColumn<CommitInfoRow, String> dateColumn = new TableColumn("Date");
        dateColumn.setSortable(false);

        // Hash Column
        TableColumn<CommitInfoRow, String> hashColumn = new TableColumn("Hash");
        hashColumn.setSortable(false);

        //Associate data with columns
        rowColumn.setCellValueFactory(cellData -> cellData.getValue().getRowNumber());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitDescription());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitAuthor());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitDate());
        hashColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitHash());

        // Add columns to table
        commitList.getColumns().addAll(rowColumn, descriptionColumn, authorColumn, dateColumn, hashColumn);
    }

    /*
        UI Actions
     */
    private void branchSelectedAction(ActionEvent event) {
        String selectedValue = branchComboBox.getValue();
        Constants.LOG.info("The " + selectedValue + " branch was selected. Update HeatMap, CommitHistory, and CommitDetails, Hide SelectedFileTerminal Window");

        // TODO Pseudo implementation of view communication with controller.
        // MainView has the reference to controller so it officially talks to the controller.
        // The children of the MainView tell the MainView when to talk to the controller.
        // MainView.getInstance().branchSelected(selectedValue);
    }

    /*
        Codebase Observer Implementation
    */
    @Override
    public void refreshHeatMap(CodebaseV2 codeBase) {
        // Nothing to do for this action
    }

    @Override
    public void branchSelected() {
        activeCommitLines.clear();
    }

    @Override
    public void fileSelected(FileObjectV2 selectedFile, Iterator<CommitV2> filesCommits) {
//        Constants.LOG.info("CLI: FileHistoryPane received new data.");
//        System.out.println("SOP: FileHistoryPane received new data.");
        // Clear out the tableView's observable list
        activeCommitLines.clear();

        System.out.println("Selected File: " + selectedFile.getPath());

        int rowIndex = 0;
        while (filesCommits.hasNext()) {
//            System.out.println(rowIndex + " VS " + commitLines.size());

            // Grab commit and make a null row
            CommitV2 commit = filesCommits.next();
//            System.out.println("Commit: " + commit.getHash());
            CommitInfoRow row;

            // Populate row based on if it exists or not
            if (rowIndex < commitLines.size()) {
                // Recycling an old row
                row = commitLines.get(rowIndex);
                row.update(commit);
//                System.out.println("Recycling new CIR. " + row.toString());
            } else {
                // Create a new row b/c all the old rows are used up
                row = new CommitInfoRow(String.valueOf(rowIndex + 1), commit.getShortMessage(), commit.getAuthor(), commit.getDate(), commit.getHash());
//                System.out.println("Creating new CIR. " + row.toString());
                commitLines.add(row);
            }

            // Add the row to the observable list
            activeCommitLines.add(row);
            rowIndex++;
        }

        System.out.println(commitLines.size());
        System.out.println(activeCommitLines.size());
    }

    @Override
    public void commitSelected(CommitV2 commit, Iterator<DiffEntry> fileDiffs) {
        // Nothing to do for this action
    }
}


