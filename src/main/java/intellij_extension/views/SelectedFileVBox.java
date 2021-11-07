package intellij_extension.views;

import intellij_extension.Constants;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * It adds /updates the file details (UI Components) within a VBox
 */
public class SelectedFileVBox extends VBox {
    private Text fileName, packageName, authors;

    //constructor
    public SelectedFileVBox() {

        //File Name Node
        fileName = ViewFactory.getInstance().createOrGetText(Constants.SF_FILENAME_TEXT_ID); // create a Text node
        String formattedFieldName = formatFieldName(Constants.SF_FILE_NAME_SPECIFIER, Constants.SF_TEXT_FILENAME); //format the  Text node
        setFileDetailsTextProperties(fileName); // set the font
        setSelectedFileText(fileName, formattedFieldName); // set the text of the Text Node
        ViewFactory.setPaneChild(this, fileName); //add the Text Node to VBox

        //Package Name Node
        packageName = ViewFactory.getInstance().createOrGetText(Constants.SF_PACKAGENAME_TEXT_ID);
        formattedFieldName = formatFieldName(Constants.SF_PACKAGE_NAME_SPECIFIER, Constants.SF_TEXT_PACKAGENAME); //format the  Text node
        setFileDetailsTextProperties(packageName);
        setSelectedFileText(packageName, formattedFieldName);
        ViewFactory.setPaneChild(this, packageName);

        //Author Node
        authors = ViewFactory.getInstance().createOrGetText(Constants.SF_AUTHOR_TEXT_ID);
        formattedFieldName = formatFieldName(Constants.SF_FILE_AUTHOR_SPECIFIER, Constants.SF_TEXT_AUTHORS); //format the  Text node
        setFileDetailsTextProperties(authors);
        setSelectedFileText(authors, formattedFieldName);
        ViewFactory.setPaneChild(this, authors);
    }

    public String updateFieldValue(Text fieldName, String fieldValue) {
        String updatedFieldValue = fieldName.getText();
        updatedFieldValue = updatedFieldValue + fieldValue;
        return updatedFieldValue;
    }


    public void setFileDetailsTextProperties(Text text) {
        text.setFont(Font.font(Constants.SF_TEXT_FONT, Constants.SF_TEXT_FONT_WEIGHT, Constants.SF_TEXT_SIZE));
    }

    public void setSelectedFileText(Text text, String fieldValue) {
        text.setText(fieldValue);
    }

    public String formatFieldName(String specifier, String fieldName) {
        String formattedFieldName = String.format(specifier, fieldName);
        formattedFieldName = formattedFieldName + Constants.SF_TEXT_SEPERATOR;
        return formattedFieldName;
    }
}