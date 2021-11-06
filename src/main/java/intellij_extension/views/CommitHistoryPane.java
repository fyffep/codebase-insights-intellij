package intellij_extension.views;

import intellij_extension.Constants;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * References on TableView:
 * https://stackoverflow.com/questions/25701087/javafx-tableview-resizeable-and-unresizeable-column
 * https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
 * https://stackoverflow.com/questions/38049734/java-setcellvaluefactory-lambda-vs-propertyvaluefactory-advantages-disadvant/38050982#38050982
 * http://tutorials.jenkov.com/javafx/tableview.html
 * https://www.superglobals.net/remove-extra-column-tableview-javafx/
 * https://stackoverflow.com/questions/14650787/javafx-column-in-tableview-auto-fit-size
 */

public class CommitHistoryPane extends VBox {

    private final HBox topHorizontalBanner;
    private final Text branchLabel;
    private final ComboBox<String> branchComboBox; // This might have to change from String to something else.
    private final TableView<CommitHistoryLine> commitList;

    public CommitHistoryPane() {
        super();

        // Create the top horizontal banner
        topHorizontalBanner = ViewFactory.getInstance().createOrGetHBox(Constants.COMMIT_HISTORY_BANNER_ID);
        setBannerProperties();
        ViewFactory.setPaneChild(this, topHorizontalBanner);

        // Create the banner label
        branchLabel = ViewFactory.getInstance().createOrGetText(Constants.COMMIT_HISTORY_BANNER_BRANCH_LABEL_ID);
        setBranchLabelProperties();
        ViewFactory.setPaneChild(topHorizontalBanner, branchLabel);

        // Create the banner combo box
        branchComboBox = ViewFactory.getInstance().createOrGetComboBox(Constants.COMMIT_HISTORY_BRANCH_COMBOBOX_ID);
        setBranchComboBoxProperties();
        ViewFactory.setPaneChild(topHorizontalBanner, branchComboBox);

        // Create Tableview with data
        commitList = ViewFactory.getInstance().createOrGetTableView(Constants.COMMIT_HISTORY_BRANCH_TABLEVIEW_ID);
        setCommitListProperties();
        setCommitListColumns();
        ViewFactory.setPaneChild(this, commitList);
    }

    /*
        UI Property Settings
     */
    private void setBannerProperties() {
        // Banner layout properties

        // We want this so the user can make the Commit Details view as big
        // as the right side if desirable
        topHorizontalBanner.setMinHeight(Constants.CH_BANNER_MIN_HEIGHT);
        // TODO We really want this to be a set, not a bind.
        // The header shouldn't grow with the window size
        // But it should be a percentage of the window size.
        topHorizontalBanner.prefHeightProperty().bind(this.heightProperty().multiply(Constants.CH_BANNER_SIZE_MULTIPLER));
        topHorizontalBanner.maxHeightProperty().bind(this.heightProperty().multiply(Constants.CH_BANNER_SIZE_MULTIPLER));
        topHorizontalBanner.prefWidthProperty().bind(this.widthProperty());

        // Child layout properties
        topHorizontalBanner.setAlignment(Constants.CH_BANNER_ALIGNMENT);
        topHorizontalBanner.setSpacing(Constants.CH_BANNER_SPACING);
        topHorizontalBanner.setPadding(Constants.CH_BANNER_INSETS);
    }

    private void setBranchLabelProperties() {
        branchLabel.setFont(Font.font(Constants.LABEL_FONT, Constants.CH_BRANCH_LABEL_FONT_WEIGHT, Constants.CH_BRANCH_LABEL_SIZE));
        branchLabel.setText(Constants.BRANCH_LABEL_TEXT);
    }

    private void setBranchComboBoxProperties() {
        // TODO figure out how this gets data or maybe an observable update populates the combobox
        branchComboBox.setItems(Constants.MOCK_BRANCHES);
        // Select first entry by default... for now
        branchComboBox.getSelectionModel().selectFirst();
        // Set up the select action
        branchComboBox.setOnAction(this::branchSelectedAction);
    }

    private void setCommitListProperties() {
        // TODO figure out how this gets data or maybe an observable update populates the combobox
        commitList.setItems(Constants.MOCK_COMMIT_HISTORY_DATA);

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
        commitList.setMinHeight(Constants.CH_COMMIT_LIST_MIN_HEIGHT);
        // TODO set this based on a portion of the view
        // like the top banner should get 90% real estate
        // But it should also be dynamic shrink with the parent
        commitList.prefWidthProperty().bind(this.widthProperty());
        commitList.prefHeightProperty().bind(this.heightProperty());

        // Add click method to rows
        // I hate that I can't refactor this double lambda expression
        commitList.setRowFactory(tableView -> {
            TableRow<CommitHistoryLine> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    CommitHistoryLine rowData = row.getItem();
                    System.out.println("Commit " + rowData.getRowNumber().getValue() + " was double clicked! Update CommitDetails!");
                }
            });
            return row;
        });
    }

    private void setCommitListColumns() {
        // Number Column
        TableColumn<CommitHistoryLine, String> rowColumn = new TableColumn("#");

        // Description Column
        TableColumn<CommitHistoryLine, String> descriptionColumn = new TableColumn("Description");
        descriptionColumn.setMaxWidth(Constants.CH_DESCRIPTION_COLUMN_MAX_WIDTH);
        descriptionColumn.setSortable(false);

        // Author Column
        TableColumn<CommitHistoryLine, String> authorColumn = new TableColumn("Author");

        // Date Column
        TableColumn<CommitHistoryLine, String> dateColumn = new TableColumn("Date");
        dateColumn.setSortable(false);

        // Hash Column
        TableColumn<CommitHistoryLine, String> hashColumn = new TableColumn("Hash");
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
        System.out.println("The " + selectedValue + " branch was selected. Update HeatMap, CommitHistory, and CommitDetails, Hide SelectedFileTerminal Window");
    }
}


