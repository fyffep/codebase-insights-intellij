package intellij_extension;

import com.intellij.openapi.diagnostic.Logger;
import intellij_extension.models.redesign.Codebase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Constants {


    //Prevent instantiation
    private Constants() {
    }

    public static final Logger LOG = Logger.getInstance(Codebase.class); // TODO This should probably change to a more reasonable class?

    // Heat
    public static final int HEAT_MIN = 1;
    public static final int HEAT_MAX = 10;


    /***************************/
    /* USER INTERFACE CONSTANTS*/
    /***************************/
    // File Commit History Pane
    public static final String FCH_BANNER_ID = "FCH_BANNER_01";
    public static final String FCH_HEADER_TEXT_ID = "FCH_BRANCH_LABEL_01";
    public static final String FCH_BRANCH_COMBOBOX_ID = "FCH_BRANCH_COMBOBOX_01";
    public static final String FCH_BRANCH_TABLEVIEW_ID = "FCH_TABLEVIEW_01";
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
    public static final String CD_CHANGE_HEADER_TEXT_PREFIX = "CD_HEADER_FILE_TEXT_";

    // Selected File Pane
    public static final String SF_VBOX_ID = "SF_VBOX";
    public static final String SF_FILENAME_TEXT_ID = "SF_FILENAME_01";
    public static final String SF_PACKAGE_NAME_TEXT_ID = "SF_PACKAGE_NAME_01";
    public static final String SF_AUTHOR_TEXT_ID = "SF_AUTHOR_01";
    // Banners
    public static final int BANNER_MIN_HEIGHT = 0;
    // General Properties
    public static final double BANNER_SIZE_MULTIPLIER = 0.20f;


    /***************************/
    /* UI Properties           */
    /***************************/
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
    public static final double FILE_LIST_SIZE_MULTIPLIER = 0.80;
    // Commit Details File List
    public static final double CD_DETAILS_WRAPPING_PERCENTAGE = 0.9f;
    public static final int FILE_LIST_MIN_HEIGHT = 0;
    public static final String FCH_DEFAULT_HEADER_TEXT = "File's Commit History:";
    public static final String FCH_HEADER_SUFFIX_TEXT = "'s Commit History:";

    /***************************/
    /* UI Strings              */
    /***************************/
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
    public static final String[] DEFAULT_BRANCHES = {"master", "development", "main"};
}
