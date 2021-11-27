package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.Constants.GroupingMode;
import intellij_extension.controllers.HeatMapController;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.views.interfaces.IContainerView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class FileHistoryPane implements IContainerView, CodeBaseObserver {

    private VBox parent;

    private HBox topHorizontalBanner;
    private Text headerText;
    private TableView<CommitInfoRow> commitList;

    // This is all the lines we created so far - we should never remove from this list
    private final ArrayList<CommitInfoRow> commitLines = new ArrayList<>();
    // These are active lines in the TableView
    private final ObservableList<CommitInfoRow> activeCommitLines = FXCollections.observableArrayList();

    public FileHistoryPane() {
        parent = new VBox();

        // Create the top horizontal banner
        topHorizontalBanner = new HBox();
        setBannerProperties();
        parent.getChildren().add(topHorizontalBanner);

        // Create the banner text
        headerText = new Text();
        setHeaderTextProperties();
        topHorizontalBanner.getChildren().add(headerText);

        // Create Tableview with data
        commitList = new TableView<>();
        setCommitListProperties();
        setCommitListColumns();
        parent.getChildren().add(commitList);

        //Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
        HeatMapController.getInstance().branchListRequested();
    }

    //region Properties setting
    private void setBannerProperties() {
        // Add constraints to width/height
        topHorizontalBanner.setMinHeight(Constants.BANNER_MIN_HEIGHT);
        topHorizontalBanner.prefHeightProperty().bind(parent.heightProperty().multiply(Constants.BANNER_SIZE_MULTIPLIER));
        topHorizontalBanner.maxHeightProperty().bind(parent.heightProperty().multiply(Constants.BANNER_SIZE_MULTIPLIER));
        topHorizontalBanner.prefWidthProperty().bind(parent.widthProperty());

        // Child layout properties
        topHorizontalBanner.setAlignment(Constants.BANNER_ALIGNMENT);
        topHorizontalBanner.setSpacing(Constants.BANNER_SPACING);
        topHorizontalBanner.setPadding(Constants.BANNER_INSETS);
    }

    private void setHeaderTextProperties() {
        headerText.setFont(Font.font(Constants.HEADER_FONT, Constants.HEADER_TEXT_FONT_WEIGHT, Constants.HEADER_TEXT_SIZE));
        headerText.setText(Constants.FCH_DEFAULT_HEADER_TEXT);
    }

    private void setCommitListProperties() {
        commitList.setItems(activeCommitLines);

        // Turn off editable
        commitList.setEditable(false);

        // This forces columns to resize to their content
        commitList.setColumnResizePolicy((param) -> true);

        // Set up constraints on width/height
        commitList.setMinHeight(Constants.FCH_COMMIT_LIST_MIN_HEIGHT);
        commitList.prefWidthProperty().bind(parent.widthProperty());
        commitList.prefHeightProperty().bind(parent.heightProperty());

        // Add click method to rows
        commitList.setRowFactory(tableView -> {
            TableRow<CommitInfoRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    CommitInfoRow rowData = row.getItem();
                    HeatMapController.getInstance().commitSelected(rowData.getCommitHash().getValue());
                }
            });
            return row;
        });
    }
    //endregion

    private void setCommitListColumns() {
        // Number Column
        TableColumn<CommitInfoRow, String> rowColumn = new TableColumn<>("#");

        // Description Column
        TableColumn<CommitInfoRow, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setPrefWidth(Constants.FCH_DESCRIPTION_COLUMN_MAX_WIDTH);
        descriptionColumn.setSortable(false);

        // Author Column
        TableColumn<CommitInfoRow, String> authorColumn = new TableColumn<>("Author");

        // Date Column
        TableColumn<CommitInfoRow, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setSortable(false);

        // Hash Column
        TableColumn<CommitInfoRow, String> hashColumn = new TableColumn<>("Hash");
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

    //region CodeBaseObservable methods
    @Override
    public void refreshHeatMap(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode) {
        // Nothing to do for this action
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void newBranchSelected(TreeMap<String, TreeSet<FileObject>> setOfFiles, String targetCommit, GroupingMode groupingMode) {
        activeCommitLines.clear();
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Update header text to reflect selected filename
        headerText.setText(selectedFile.getFilename() + Constants.FCH_HEADER_SUFFIX_TEXT);

        // Clear out the tableView's observable list
        activeCommitLines.clear();

        int rowIndex = 0;
        while (filesCommits.hasNext()) {

            // Grab commit and make a null row
            Commit commit = filesCommits.next();
            CommitInfoRow row;

            // Populate row based on if it exists or not
            if (rowIndex < commitLines.size()) {
                // Recycling an old row
                row = commitLines.get(rowIndex);
                row.update(commit);
            } else {
                // Create a new row b/c all the old rows are used up
                row = new CommitInfoRow(String.valueOf(rowIndex + 1), commit.getShortMessage(), commit.getAuthor(), commit.getDate(), commit.getHash());
                commitLines.add(row);
            }

            // Add the row to the observable list
            activeCommitLines.add(row);
            rowIndex++;
        }
    }

    @Override
    public void commitSelected(Commit commit) {
        // Nothing to do for this action
    }
    //endregion

    //region IContainerView methods
    @Override
    public Node getNode() {
        return parent;
    }
    //endregion
}


