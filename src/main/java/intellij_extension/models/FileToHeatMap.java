package intellij_extension.models;

import intellij_extension.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FileToHeatMap //implements IFileToHeatMap
{
    private HashMap<File, List<HeatObject>> fileToHeatListMap;

    public FileToHeatMap() {
        this.fileToHeatListMap = new HashMap<File, List<HeatObject>>();
    }

    public HashMap<File, List<HeatObject>> getMap()
    {
        return fileToHeatListMap;
    }

    /**
     * Computes the average of heat assigned to a file.
     * @param file a File that has one or more HeatObjects mapped to it.
     * @return the overall heat of the input file or INVALID_HEAT if the
     * file has no HeatObjects assigned to it.
     */
    public double getHeatAverage(File file)
    {
        //Get the heat list corresponding to the file
        List<HeatObject> heatList = fileToHeatListMap.get(file);
        if (heatList == null || heatList.isEmpty())
        {
            System.out.println("There are no HeatObjects mapped to file `"+file+"`. " +
                    "Returning "+ Constants.INVALID_HEAT+" instead");
            return Constants.INVALID_HEAT;
        }

        //Compute the average heat
        double sum = 0;
        int count = 0;
        for (HeatObject heatObject : heatList)
        {
            sum += heatObject.computeHeat();
            count++;
        }
        return sum / count;
    }

    public void addHeatObject(File file, HeatObject heatObject)
    {
        //Get the heat list corresponding to the file
        List<HeatObject> heatList = fileToHeatListMap.get(file);
        if (heatList == null) {
            heatList = new LinkedList<>(); //create it if it does not exist
            fileToHeatListMap.put(file, heatList);
        }

        heatList.add(heatObject);
    }

    public void removeHeatObject(File file)
    {
        List<HeatObject> heatList = fileToHeatListMap.remove(file);
    }
}
