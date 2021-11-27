package intellij_extension;

import com.intellij.openapi.diagnostic.Logger;
import intellij_extension.models.redesign.Codebase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public enum GroupingMode {
    Commits,
    Packages
}

public class Constants {

    public static final Logger LOG = Logger.getInstance(Codebase.class); // TODO This should probably change to a more reasonable class?

    // Heat
    public static final int HEAT_MIN = 1;
    public static final int HEAT_MAX = 10;

    //region UI Properties
    // Banners
    public static final int BANNER_MIN_HEIGHT = 40;
    public static final double BANNER_SIZE_MULTIPLIER = 0.40f;
    // HeatMap FlowPane
    public static final int HEATMAP_VERTICAL_SPACING = 10;
    public static final int HEATMAP_HORIZONTAL_SPACING = 10;
    public static final Insets HEATMAP_PADDING = new Insets(10, 10, 10, 10);
    // Heat Colors
    public static final Color HEAT_MIN_COLOR = Color.BLUE;
    public static final Color HEAT_MAX_COLOR = Color.RED;
    // General Banner Constants
    public static final int INFO_SPLIT_PANE_MIN_WIDTH = 0;
    public static final Pos BANNER_ALIGNMENT = Pos.CENTER_LEFT;
    public static final int BANNER_SPACING = 15;
    public static final Insets BANNER_INSETS = new Insets(0, 0, 0, 10);
    // Headers (in Banners)
    public static final String HEADER_FONT = "Veranda";
    public static final int HEADER_TEXT_SIZE = 24;
    public static final FontWeight HEADER_TEXT_FONT_WEIGHT = FontWeight.BOLD;
    // Text Font properties in Selected File Pane
    public static final String SF_TEXT_FONT = "Veranda";
    public static final int SF_TEXT_SIZE = 14;
    public static final FontWeight SF_TEXT_FONT_WEIGHT = FontWeight.BOLD;
    public static final String SF_FILE_NAME_SPECIFIER = "%-20s";
    public static final String SF_PACKAGE_NAME_SPECIFIER = "%-15s";
    public static final String SF_FILE_AUTHOR_SPECIFIER = "%-22s";
    // File Commit History Commit List
    public static final int FCH_COMMIT_LIST_MIN_HEIGHT = 0;
    // File Commit History Commit List Columns
    public static final int FCH_DESCRIPTION_COLUMN_MAX_WIDTH = 200;
    // Commit Details Banner
    public static final Font TOOLTIP_FONT = new Font(16);
    // Commit History Commit List
    public static final int CH_COMMIT_LIST_MIN_HEIGHT = 0;
    // Commit History Commit List Columns
    public static final int CH_DESCRIPTION_COLUMN_MAX_WIDTH = 200;
    //Commit Details Banner
    public static final Pos CD_BANNER_ALIGNMENT = Pos.TOP_LEFT;
    public static final int CD_BANNER_SPACING = 5;
    public static final double FILE_LIST_SIZE_MULTIPLIER = 0.60;
    // Commit Details File List
    public static final double CD_DETAILS_WRAPPING_PERCENTAGE = 0.9f;
    public static final int FILE_LIST_MIN_HEIGHT = 0;
    //endregion

    //region UI Strings
    public static final String FCH_DEFAULT_HEADER_TEXT = "File's Commit History:";
    public static final String FCH_HEADER_SUFFIX_TEXT = "'s Commit History:";
    public static final String CD_HEADER_TEXT = "Commit Details:";
    public static final String CD_DESCRIPTION = "Description: ";
    public static final String CD_AUTHOR = "Author: ";
    public static final String CD_DATE = "Date: ";
    public static final String CD_HASH = "Hash: ";
    public static final String CD_ADDED_FILES = "Added Files:";
    public static final String CD_COPIED_FILES = "Copied Files:";
    public static final String CD_MODIFIED_FILES = "Modified Files:";
    public static final String CD_RENAMED_FILES = "Renamed Files:";
    public static final String CD_DELETED_FILES = "Deleted Files:";
    public static final String SF_TITLE_TEXT = "Selected File Details";
    public static final String SF_TEXT_FILENAME = "Filename: ";
    public static final String SF_TEXT_PACKAGE_NAME = "Package Name: ";
    public static final String SF_TEXT_AUTHORS = "Authors: ";
    public static final String SF_TEXT_SEPERATOR = ":";
    public static final String[] DEFAULT_BRANCHES = {"development", "master", "main"};
    public static final String BRANCH_COMBOBOX_TITLE = "Evaluating Branch:";
    public static final String HEAT_METRIC_COMBOBOX_TITLE = "Heat Data:";
    public static final String HEAT_GROUPING_TEXT = "Group by Package";
    public static final String COMMIT_GROUPING_TEXT = "Group by Commit";
    // Heat Metric List
    public static final ObservableList<String> HEAT_METRIC_OPTIONS = FXCollections.observableArrayList(
            "Line Count",
            "Number of Commits",
            "Number of Authors"
    );
    //endregion

    //Prevent instantiation
    private Constants() {
    }
}
