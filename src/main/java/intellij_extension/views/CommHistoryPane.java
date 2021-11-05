package intellij_extension.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

// TODO
    // REMOVE ALL MAGIC NUMBERS
    // MOVE CREATION TO ViewFactory Class
public class CommHistoryPane extends VBox {

    // TODO - MOVE TO TEST MOCK DATA EVENTUALLY
    // MOCK/TESTING DATA
    public static final ObservableList<CommitHistoryLine> mockCommitHistoryData = FXCollections.observableArrayList(
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "A small description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("5000", "Commit 5's Description is middle size.", "Brown", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c")
    );
    public static final ObservableList<String> mockBranches = FXCollections.observableArrayList(
            "master",
            "development",
            "major feature A",
            "release 1.0",
            "major feature B",
            "hotfix 1.1hf",
            "major feature C",
            "major feature D",
            "release 1.5",
            "major feature F",
            "release 2.0",
            "major feature H"
    );

    private HBox topHoriztontalBanner;
    private Text branchLabel;
    // This might have to change from String to something else.
    private ComboBox<String> branchDropdown;
    private TableView<CommitHistoryLine> commitList;

    public CommHistoryPane() {
        super();

        // TODO Extract to ViewFactory?
        topHoriztontalBanner = new HBox();

        Color backgroundColor = Color.DARKGREEN;
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(backgroundFill);
        topHoriztontalBanner.setBackground(background);

        // We want this so the user can make the Commit Details view as big
        // as the right side if desirable
        topHoriztontalBanner.setMinHeight(0);
        // TODO
        // We really want this to be a set, not a bind.
        // The header shouldn't grow with the window size
        // But it should be a percentage of the window size.
        topHoriztontalBanner.prefHeightProperty().bind(this.heightProperty().multiply(.25));
        topHoriztontalBanner.maxHeightProperty().bind(this.heightProperty().multiply(.25));
        topHoriztontalBanner.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(topHoriztontalBanner);
        // Child layout properties
        topHoriztontalBanner.setAlignment(Pos.CENTER_LEFT);
        topHoriztontalBanner.setSpacing(15.0);
        topHoriztontalBanner.setPadding(new Insets(0,00,0,10));


        // Branch Label
        branchLabel = new Text();
        branchLabel.setFont(Font.font("Veranda", FontWeight.BOLD, 24));
        branchLabel.setText("Selected Branch:");
        topHoriztontalBanner.getChildren().add(branchLabel);

        // Create the Branch Dropdown
        // TODO figure out how this gets data or maybe an observable update populates the dropdown
        branchDropdown = new ComboBox(mockBranches);
        // Select first entry by default
        branchDropdown.getSelectionModel().selectFirst();
        // Set up the select action
        branchDropdown.setOnAction(event -> {
            String selectedValue = branchDropdown.getValue();
            System.out.println("The " + selectedValue + " branch was selected. Update HeatMap, CommitHistory, and CommitDetails, Hide SelectedFileTerminal Window");
        });
        topHoriztontalBanner.getChildren().add(branchDropdown);


        // TODO Extract to ViewFactory?
        // Create Tableview with data
        commitList = new TableView(mockCommitHistoryData);
        // Turn off editable
        commitList.setEditable(false);
        // Do not create n + 1 columns with n + 1 being empty... (Why is that default behavior?!)
//        commitList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        commitList.setColumnResizePolicy((param) -> true);

        //Background to see if its created/laid out properly
        backgroundColor = Color.MEDIUMSPRINGGREEN;
        backgroundFill = new BackgroundFill(backgroundColor, null, null);
        background = new Background(backgroundFill);
        commitList.setBackground(background);

        // Set up constraints on width/height
        // We want this so the user can make the Commit Details view as big
        // as the right side if desirable
        commitList.setMinHeight(0);
        // TODO set this based on a portion of the view
        // like the top banner should get 90% real estate
        // But it should also be dynamic shrink with the parent
        commitList.prefWidthProperty().bind(this.widthProperty());
        commitList.prefHeightProperty().bind(this.heightProperty());

        // Set up columns
        TableColumn<CommitHistoryLine, String> rowColumn = new TableColumn("#");
        TableColumn<CommitHistoryLine, String> descriptionColumn = new TableColumn("Description");
        // TODO get rid of Magic Number
        descriptionColumn.setMaxWidth(200);
        descriptionColumn.setSortable(false);
        TableColumn<CommitHistoryLine, String> authorColumn = new TableColumn("Author");
        TableColumn<CommitHistoryLine, String> dateColumn = new TableColumn("Date");
        dateColumn.setSortable(false);
        TableColumn<CommitHistoryLine, String> hashColumn = new TableColumn("Hash");
        hashColumn.setSortable(false);

        //Associate data with columns - don't really get this part
        rowColumn.setCellValueFactory(cellData -> cellData.getValue().getRowNumber());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitDescription());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitAuthor());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitDate());
        hashColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitHash());

        // Add click method to rows
        commitList.setRowFactory(tv -> {
            TableRow<CommitHistoryLine> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    CommitHistoryLine rowData = row.getItem();
                    System.out.println("Commit " + rowData.getRowNumber().getValue() + " was double clicked!");
                }
            });
            return row;
        });

        // Add columns to table
        commitList.getColumns().addAll(rowColumn, descriptionColumn, authorColumn, dateColumn, hashColumn);

        // Add table view to CommitHistoryPane
        this.getChildren().add(commitList);

        // Fill background with default
        backgroundColor = Color.LAWNGREEN;
        backgroundFill = new BackgroundFill(backgroundColor, null, null);
        background = new Background(backgroundFill);
        setBackground(background);
    }
}

