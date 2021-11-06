package intellij_extension.utility;

import static intellij_extension.Constants.HEAT_MAX;

public class HeatColorUtility //can be renamed if adding more methods
{
    /**
     * Converts the input heat level to a color.
     * Higher heat levels are indicated by higher intensities of red.
     * @param heatLevel a number from 1 to 10
     * @return a hexadecimal String of the form "FFFFFF" representing a color
     */
    public static String colorOfHeat(int heatLevel)
    {
        //Generate color (TODO -- this is just a placeholder version that arbitrarily chooses a red color)
        int redValue = (int) ((((double)heatLevel) / HEAT_MAX) * 255);
        String color = String.format("%02x", redValue) + "0000";

        return color;
    }
}
