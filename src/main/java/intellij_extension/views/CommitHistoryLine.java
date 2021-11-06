package intellij_extension.views;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CommitHistoryLine
{
    private final SimpleStringProperty rowNumber = new SimpleStringProperty();
    private final SimpleStringProperty commitDescription = new SimpleStringProperty();
    private final SimpleStringProperty commitAuthor = new SimpleStringProperty();
    private final SimpleStringProperty commitDate = new SimpleStringProperty();
    private final SimpleStringProperty commitHash = new SimpleStringProperty();

    public CommitHistoryLine(String rowNumber, String commitDescription, String commitAuthor, String commitDate, String commitHash) {
        this.rowNumber.set(rowNumber);
        this.commitDescription.set(commitDescription);
        this.commitAuthor.set(commitAuthor);
        this.commitDate.set(commitDate);
        this.commitHash.set(commitHash);
    }

    public SimpleStringProperty getRowNumber() {
        return rowNumber;
    }

    public SimpleStringProperty getCommitDescription() {
        return commitDescription;
    }

    public SimpleStringProperty getCommitAuthor() {
        return commitAuthor;
    }

    public SimpleStringProperty getCommitDate() {
        return commitDate;
    }

    public SimpleStringProperty getCommitHash() {
        return commitHash;
    }
}