How to add a new file metric:
    1. Add a new variable to HeatObject to represent your metric.
    //TODO we can update these docs once accumulated heat is implemented
    3. HeatMapController: Edit recalculateHeat() so that it calls your new HeatCalculator.
    4. Add a calculation method in FileObject::computeHeatLevel() to convert the metric to a heat level.