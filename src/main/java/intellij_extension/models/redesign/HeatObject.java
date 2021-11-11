package intellij_extension.models.redesign;

/**
 * filename acts as ID
 */
public class HeatObject {

    private float heatLevel;
    private String filename;
    private long lineCount;
    private long fileSize;
    private int numberOfCommits = 1;

    public HeatObject(float heatLevel, String filename, long lineCount, long fileSize, int numberOfCommits) {
        this.heatLevel = heatLevel;
        this.filename = filename;
        this.lineCount = lineCount;
        this.fileSize = fileSize;
        this.numberOfCommits = numberOfCommits;
    }

    public float getHeatLevel() {
        return heatLevel;
    }

    public String getFilename() {
        return filename;
    }

    public long getLineCount() {
        return lineCount;
    }

    public void setLineCount(long lineCount) {
        this.lineCount = lineCount;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getNumberOfCommits() {
        return numberOfCommits;
    }

    public void setNumberOfCommits(int numberOfCommits) {
        this.numberOfCommits = numberOfCommits;
    }
}
