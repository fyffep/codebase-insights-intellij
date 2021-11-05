package intellij_extension.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import intellij_extension.Constants;

/**
 * References on TableView:
 * https://stackoverflow.com/questions/25701087/javafx-tableview-resizeable-and-unresizeable-column
 * https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
 * https://stackoverflow.com/questions/38049734/java-setcellvaluefactory-lambda-vs-propertyvaluefactory-advantages-disadvant/38050982#38050982
 * http://tutorials.jenkov.com/javafx/tableview.html
 */

public class CommHistoryPane extends VBox
{

    private Pane topHoriztontalBanner;

    private TableView<CommitHistoryLine> commitList;

    // TODO - MOVE TO TEST MOCK DATA EVENTUALLY
    // MOCK/TESTING DATA
    public static final ObservableList<CommitHistoryLine> mockCommitHistoryData = FXCollections.observableArrayList(
            new CommitHistoryLine("1", "Commit 1's Description", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "Commit 4's Description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("5", "Commit 5's Description", "Brown", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c")
    );

    public CommHistoryPane() {
        super();

        // TODO Extract to ViewFactory?
        topHoriztontalBanner = new Pane();
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

        // TODO Extract to ViewFactory?
        // Create Tableview with data
        commitList = new TableView(mockCommitHistoryData);
        commitList.setEditable(false);

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
        TableColumn<CommitHistoryLine, String> authorColumn = new TableColumn("Author");
        TableColumn<CommitHistoryLine, String> dateColumn = new TableColumn("Date");
        TableColumn<CommitHistoryLine, String> hashColumn = new TableColumn("Hash");

        //Associate data with columns - don't really get this part
        rowColumn.setCellValueFactory(cellData -> cellData.getValue().getRowNumber());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitDescription());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitAuthor());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitDate());
        hashColumn.setCellValueFactory(cellData -> cellData.getValue().getCommitHash());


        // Add columns to table
        commitList.getColumns().addAll(rowColumn, descriptionColumn, authorColumn, dateColumn, hashColumn);

        // Add table view to CommitHistoryPane
        this.getChildren().add(commitList);

        Text text = new Text();
        text.setFont(new Font(12));
        text.setText("Commit History View");
        topHoriztontalBanner.getChildren().add(text);
        text.setX(0);
        text.setY(10);

        // Fill background with default
        backgroundColor = Color.LAWNGREEN;
        backgroundFill = new BackgroundFill(backgroundColor, null, null);
        background = new Background(backgroundFill);
        setBackground(background);
    }
}

