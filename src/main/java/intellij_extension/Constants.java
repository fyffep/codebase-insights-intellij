package intellij_extension;

import intellij_extension.views.CommitHistoryLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.FontWeight;

import java.awt.*;

public class Constants {
    public static final int HEAT_MIN = 1;
    public static final int HEAT_MAX = 10;

    /***************************/
    /* USER INTERFACE CONSTANTS*/
    /***************************/

    // IDs
    public static final String COMMIT_HISTORY_BANNER_ID = "CH_BANNER_01";
    public static final String COMMIT_HISTORY_BANNER_BRANCH_LABEL_ID = "CH_BRANCH_LABEL_01";
    public static final String COMMIT_HISTORY_BRANCH_COMBOBOX_ID = "CH_BRANCH_COMBOBOX_01";
    public static final String COMMIT_HISTORY_BRANCH_TABLEVIEW_ID = "CH_TABLEVIEW_01";

    // UI Properties
    // General Properties
    public static final String LABEL_FONT = "Veranda";

    // Commit History Banner
    public static final int CH_BANNER_MIN_HEIGHT = 0;
    public static final float CH_BANNER_SIZE_MULTIPLER = 0.25f;
    public static final Pos CH_BANNER_ALIGNMENT = Pos.CENTER_LEFT;
    public static final int CH_BANNER_SPACING = 15;
    public static final Insets CH_BANNER_INSETS = new Insets(0, 0, 0, 10);

    // Commit History Branch Label
    public static final int CH_BRANCH_LABEL_SIZE = 24;
    public static final FontWeight CH_BRANCH_LABEL_FONT_WEIGHT = FontWeight.BOLD;

    // Commit History Commit List
    public static final int CH_COMMIT_LIST_MIN_HEIGHT = 0;
    // Commit History Commit List Columns
    public static final int CH_DESCRIPTION_COLUMN_MAX_WIDTH = 200;




    // UI Strings
    public static final String BRANCH_LABEL_TEXT = "Selected Branch: ";

    // MOCK DATA
    // TODO - MOVE TO TEST MOCK DATA FILE IN TEST DIRECTORY EVENTUALLY
    public static final ObservableList<CommitHistoryLine> MOCK_COMMIT_HISTORY_DATA = FXCollections.observableArrayList(
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "A small description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("5000", "Commit 5's Description is middle size.", "Brown", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c")
    );
    public static final ObservableList<String> MOCK_BRANCHES = FXCollections.observableArrayList(
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
}
