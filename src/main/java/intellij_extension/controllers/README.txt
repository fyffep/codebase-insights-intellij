How to add a new file metric:
    1. Add a new variable to FileObject to represent your metric.
    2. Create a class that implements the HeatCalculator interface.
    There should be a method to apply your file metric data to an existing HashMap<String path name, FileObject>
    so that each FileObject is given the new attribute.
    3. HeatMapController: Edit recalculateHeat() so that it calls your new HeatCalculator.
    4. Add a calculation method in FileObject::computeHeatLevel() to convert the metric to a heat level.