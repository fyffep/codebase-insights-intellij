package intellij_extension;

import intellij_extension.models.CodeBase;
import intellij_extension.views.CommitHistoryLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.intellij.openapi.diagnostic.Logger;

import java.util.ArrayList;

public class Constants {

    public static final Logger LOG = Logger.getInstance(CodeBase.class); // TODO This should probably change to a more reasonable class?

    public static final int HEAT_MIN = 1;
    public static final int HEAT_MAX = 10;
    //Heat Colors
    public static final Color HEAT_COLOR_1 = Color.rgb(80, 255, 80); //green
    public static final Color HEAT_COLOR_2 = Color.rgb(120, 255, 120); //green
    public static final Color HEAT_COLOR_3 = Color.rgb(165, 212, 106); //green
    public static final Color HEAT_COLOR_4 = Color.rgb(165, 212, 106); //light green
    public static final Color HEAT_COLOR_5 = Color.rgb(255, 223, 128); //pale yellow
    public static final Color HEAT_COLOR_6 = Color.rgb(255, 200, 80); //dark yellow
    public static final Color HEAT_COLOR_7 = Color.rgb(255, 200, 40); //light orange
    public static final Color HEAT_COLOR_8 = Color.rgb(229, 177, 52); //bright orange
    public static final Color HEAT_COLOR_9 = Color.rgb(255, 50, 50); //red
    public static final Color HEAT_COLOR_10 = Color.rgb(200, 20, 20); //dark red
    /***************************/

    // IDs
    // Commit History Pane
    public static final String CH_BANNER_ID = "CH_BANNER_01";

    /***************************/
    /* USER INTERFACE CONSTANTS*/
    public static final String CH_HEADER_TEXT_ID = "CH_BRANCH_LABEL_01";
    public static final String CH_BRANCH_COMBOBOX_ID = "CH_BRANCH_COMBOBOX_01";
    public static final String CH_BRANCH_TABLEVIEW_ID = "CH_TABLEVIEW_01";
    // Commit Details Pane
    public static final String CD_BANNER_ID = "CD_BANNER_01";
    public static final String CD_HEADER_TEXT_ID = "CD_BANNER_01";
    public static final String CD_DESCRIPTION_TEXT_ID = "CD_DESCRIPTION_01";
    public static final String CD_AUTHOR_TEXT_ID = "CD_AUTHOR_01";
    public static final String CD_DATE_TEXT_ID = "CD_DATE_01";
    public static final String CD_HASH_TEXT_ID = "CD_HASH_01";
    public static final String CD_FILE_LIST_CONTAINER_ID = "CD_FILE_LIST_CONTAINER_01";
    public static final String CD_FILE_LIST_ID = "CD_FILE_LIST_01";
    public static final String CD_FILE_TEXT_PREFIX = "CD_FILE_TEXT_";
    // Selected File Details Pane
    public static final String SF_FILENAME_TEXT_ID = "SF_FILENAME_01";
    public static final String SF_PACKAGENAME_TEXT_ID = "SF_PACKAGENAME_01";
    public static final String SF_AUTHOR_TEXT_ID = "SF_AUTHOR_01";
    // Banners
    public static final int BANNER_MIN_HEIGHT = 0;


    // UI Properties
    // General Properties
    public static final double BANNER_SIZE_MULTIPLIER = 0.20f;
    public static final Pos BANNER_ALIGNMENT = Pos.CENTER_LEFT;
    public static final int BANNER_SPACING = 15;
    public static final Insets BANNER_INSETS = new Insets(0, 0, 0, 10);
    // Headers (in Banners)
    public static final String HEADER_FONT = "Veranda";
    public static final int HEADER_TEXT_SIZE = 24;
    public static final FontWeight HEADER_TEXT_FONT_WEIGHT = FontWeight.BOLD;
    //Text Font properties in Selected File Pane
    public static final String SF_TEXT_FONT = "Veranda";
    public static final int SF_TEXT_SIZE = 14;
    public static final FontWeight SF_TEXT_FONT_WEIGHT = FontWeight.BOLD;
    public static final String SF_FILE_NAME_SPECIFIER = "%-20s";
    public static final String SF_PACKAGE_NAME_SPECIFIER = "%-15s";
    public static final String SF_FILE_AUTHOR_SPECIFIER = "%-22s";
    public static final Font TOOLTIP_FONT = new Font(16);
    // Commit History Commit List
    public static final int CH_COMMIT_LIST_MIN_HEIGHT = 0;
    // Commit History Commit List Columns
    public static final int CH_DESCRIPTION_COLUMN_MAX_WIDTH = 200;
    //Commit Details Banner
    public static final Pos CD_BANNER_ALIGNMENT = Pos.TOP_LEFT;
    public static final int CD_BANNER_SPACING = 5;
    // Commit Details File List
    public static final double FILE_LIST_SIZE_MULTIPLIER = 0.80;
    public static final int FILE_LIST_MIN_HEIGHT = 0;
    // UI Strings
    public static final String CH_HEADER_TEXT = "Commit History:";
    public static final String CD_HEADER_TEXT = "Commit Details:";
    public static final String CD_DESCRIPTION = "Description: ";
    public static final String CD_AUTHOR = "Author: ";
    public static final String CD_DATE = "Date: ";
    public static final String CD_HASH = "Hash: ";
    public static final String SF_TEXT_FILENAME = "File Name";
    public static final String SF_TEXT_PACKAGENAME = "Package Name";
    public static final String SF_TEXT_AUTHORS = "Authors";
    public static final String SF_TEXT_SEPERATOR = ":";
    public static final ObservableList<CommitHistoryLine> MOCK_COMMIT_HISTORY_DATA = FXCollections.observableArrayList(
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "A small description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "A small description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "A small description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "A small description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "A small description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("4", "A small description", "Jones", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("2", "Commit 2's Description is middle size.", "Johnson", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("3", "Commit 3's Description a much longer description for testing is needed", "Williams", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c"),
            new CommitHistoryLine("5", "Commit 5's Description is middle size.", "Brown", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c")
    );


    // MOCK DATA
    // TODO - MOVE TO TEST MOCK DATA FILE IN TEST DIRECTORY EVENTUALLY
    // TODO - SYNC WITH MODEL DATA
    public static final CommitHistoryLine MOCK_COMMIT_DETAILS = new CommitHistoryLine("1", "Commit 1's Description is middle size.", "Smith", "11/5/2021", "72c26743deb9e6270ae2a7fe8b7367c56b7cb09c");
    public static final ArrayList<String> MOCK_COMMIT_FILE_DETAILS = new ArrayList<>() {
        {
            add("File 1: 10 Additions");
            add("File 2: 2 Additions");
            add("File 3: 6 Deletions");
            add("File 4: 3 Deletion");
            add("File 5: 2 Deletion");
            add("File 6: 3 Addition, 2 Deletion");
            add("File 7: 1 Addition, 1 Deletion");
            add("File 8: Added");
            add("File 9: Added");
            add("File 10: Deleted");
            add("File 11: Deleted");
            add("File 12: Deleted");
            add("File 1: 10 Additions");
            add("File 2: 2 Additions");
            add("File 3: 6 Deletions");
            add("File 4: 3 Deletion");
            add("File 5: 2 Deletion");
            add("File 6: 3 Addition, 2 Deletion");
            add("File 7: 1 Addition, 1 Deletion");
            add("File 8: Added");
            add("File 9: Added");
            add("File 10: Deleted");
            add("File 11: Deleted");
            add("File 12: Deleted");
            add("File 1: 10 Additions");
            add("File 2: 2 Additions");
            add("File 3: 6 Deletions");
            add("File 4: 3 Deletion");
            add("File 5: 2 Deletion");
            add("File 6: 3 Addition, 2 Deletion");
            add("File 7: 1 Addition, 1 Deletion");
            add("File 8: Added");
            add("File 9: Added");
            add("File 10: Deleted");
            add("File 11: Deleted");
            add("File 12: Deleted");
        }
    };
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

    private Constants() {
        //Prevent instantiation
    }
}
