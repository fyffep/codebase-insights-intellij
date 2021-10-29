package intellij_extension.models;

public class CodeCoverageHeat implements HeatObject
{
    double overallPercentage = 0;

    public CodeCoverageHeat(double overallPercentage) {
        this.overallPercentage = overallPercentage;
    }

    public double getOverallPercentage() {
        return overallPercentage;
    }

    public void setOverallPercentage(double overallPercentage) {
        this.overallPercentage = overallPercentage;
    }



    @Override
    public double computeHeat() {
        return overallPercentage;
    }
}
