package intellij_extension.models.redesign;

import intellij_extension.utility.HeatCalculationUtility;

/**
 * filename acts as ID
 */
public class HeatObject {

    private float heatLevel;
    private String filename;
    private long lineCount;
    private long fileSize;
    private int numberOfCommits = 0;
    private int numberOfAuthors = 0;

    public HeatObject() {
        //This allows the metrics to be filled out gradually
    }

    public HeatObject(float heatLevel, String filename, long lineCount, long fileSize, int numberOfCommits, int numberOfAuthors) {
        this.heatLevel = heatLevel;
        this.filename = filename;
        this.lineCount = lineCount;
        this.fileSize = fileSize;
        this.numberOfCommits = numberOfCommits;
        this.numberOfAuthors = numberOfAuthors;
    }

    //TODO this should be deleted in favor of computeHeatLevel()
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

    public int getNumberOfAuthors() {
        return numberOfAuthors;
    }

    public void setNumberOfAuthors(int numberOfAuthors) {
        this.numberOfAuthors = numberOfAuthors;
    }


    /**
     * Assigns a heat level to this HeatObject based on its metrics.
     *
     * @return a value from 1 to 10, with 10 being the hottest
     */
    public int computeHeatLevel() //maybe this class (a model) is not the best place for this method
    {
        //Currently, this does not support accumulated heat FIXME

        //Compute the heat of each metric
        //File size
        int sizeHeat = HeatCalculationUtility.calculateHeatForFileSize(this);
        //Number of commits
        int numberOfCommitsHeat = HeatCalculationUtility.calculateHeatForNumberOfCommits(this);
        System.out.println("sizeHeat=" + sizeHeat + " for linecount2=" + lineCount + " and numberOfCommitsHeat=" + numberOfCommitsHeat);

        //Average all the metrics
        /*return (
                sizeHeat +
                numberOfCommitsHeat
                //Add more metrics here...
        ) / 2;*/
        return numberOfCommits; //FIXME AHHHH IT'S NOT WORKING BUT IT'S SUNDAY NIGHT
    }
}
