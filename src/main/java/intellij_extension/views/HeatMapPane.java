package intellij_extension.views;

import intellij_extension.Constants;
import intellij_extension.models.redesign.Codebase;
import intellij_extension.models.redesign.Commit;
import intellij_extension.models.redesign.FileObject;
import intellij_extension.observer.CodeBaseObserver;
import intellij_extension.utility.GroupFileObjectUtility;
import intellij_extension.utility.HeatCalculationUtility;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * A view that holds rectangles to represent files for a particular commit.
 * Each rectangle is colored based on the heat values assigned to its corresponding file.
 */
public class HeatMapPane extends FlowPane implements CodeBaseObserver {

    public HeatMapPane() {
        super();
        //Set margin around the heat boxes.
        this.setVgap(Constants.HEATMAP_VERTICAL_SPACING);
        this.setHgap(Constants.HEATMAP_HORIZONTAL_SPACING);
        this.setPadding(Constants.HEATMAP_PADDING);

        //this.setStyle("-fx-background-color: #2b2b2b");

        //Register self as an observer of the model
        Codebase model = Codebase.getInstance();
        model.registerObserver(this);
        //refreshHeatMap(model); //use latest appearance
    }

    /**
     * Clears the heat container, removing all child components.
     */
    public void clear() {
        getChildren().clear();
    }


    /**
     * Clears the pane, then displays all files present in the latest commit.
     * Each file is represented by a rectangular pane.
     *
     * @param codebase the model whose fileMetricMap will be read to extract
     *                 file-to-heat data.
     */
    @Override
    public void refreshHeatMap(Codebase codebase) {
        System.out.println("Called refresh");

        //Calculate heat based on file size (SHOULD BE MOVED)
        HeatCalculationUtility.assignHeatLevelsFileSize(codebase);

        Map<String, ArrayList<FileObject>> packageToFileMap = GroupFileObjectUtility.groupByPackage(codebase);
        Platform.runLater(() -> {
            clear();
            for (Map.Entry<String, ArrayList<FileObject>> entry : packageToFileMap.entrySet()) {
                String packageName = entry.getKey();
                HeatFileContainer heatFileContainer = new HeatFileContainer(packageName);
                heatFileContainer.maxWidthProperty().bind(this.widthProperty());
                for (FileObject fileObject : entry.getValue()) {
                    // TODO need Model to create a HeatObject at every commit for every FileObject regardless if in the TreeWalk or not.
                    //  Currently it only creates a HeatObject if found in the TreeWalk.
                    int heatLevel = fileObject.getHeatObjectAtCommit(fileObject.getLatestCommitInTreeWalk()).getHeatLevel();
//                  int heatLevel = fileObject.getHeatObjectAtCommit(codebase.getLatestCommitHash()).getHeatLevel();

                    //Generate color
                    Color fileHeatColor = HeatCalculationUtility.colorOfHeat(heatLevel);
                    // Convert color to hex
                    // This has a bug and doesn't properly convert colors - off by 1 or 2 error
//                    String colorString = String.format("%02x%02x%02x", (int) (fileHeatColor.getRed() * 255), (int) (fileHeatColor.getGreen() * 255), (int) (fileHeatColor.getBlue() * 255));
                    String colorString = fileHeatColor.toString();
                    String colorFormat = String.format("-fx-background-color: #%s", colorString.substring(colorString.indexOf("x") + 1));

                    //Add a pane (rectangle) to the screen
                    HeatFileComponent heatFileComponent = new HeatFileComponent(fileObject);
                    heatFileComponent.setStyle(colorFormat);
                    //heatFileContainer.getChildren().add(heatFileComponent);
                    heatFileContainer.addNode(heatFileComponent);

                    //Add a tooltip to the file pane
                    String fileName = fileObject.getFilename();
                    Tooltip tooltip = new Tooltip(String.format("%s\nHeat Level = %d", fileName, heatLevel));
                    tooltip.setFont(Constants.TOOLTIP_FONT);
                    tooltip.setShowDelay(Duration.seconds(0));
                    Tooltip.install(heatFileComponent, tooltip);
                }

                heatFileContainer.setStyle("-fx-background-color: #BBBBBB");
                this.getChildren().add(heatFileContainer);
            }
        });

        /*Platform.runLater(() -> {
            clear();

            //Iterate through the files and add them to the screen
            Iterator<FileObject> fileObjectIterator = codebase.getActiveFileObjects().iterator();
            System.out.println("Updating the heatmap view");
            // A list for sorting them by heat level
            ArrayList<FileObject> sortedFileObject = new ArrayList<>();
            while (fileObjectIterator.hasNext()) {
                FileObject fileObject = fileObjectIterator.next();

                // TODO maybe add a "current commit" field to the Codebase?
                //  Yea.. building the model should automatically set the latest commit as the current commit.
                // String commitHash = fileObject.getCo();
                int heatLevel = fileObject.getHeatObjectAtCommit("df24464eb2394991112ed60f5252ccf8c59da455").computeHeatLevel(); //retrieve or calculate heat level

                // Get commits associated with file
                ArrayList<Commit> associatedCommits = (ArrayList<Commit>) codebase.getActiveCommits().stream()
                        .filter(commit -> commit.getFileSet().contains(fileObject.getFilename()))
                        .collect(Collectors.toList());
                heatLevel = associatedCommits.size();

                fileObject.latestCommitHeatLevel = heatLevel;
                sortedFileObject.add(fileObject);
            }
            Collections.sort(sortedFileObject, (a, b) -> b.compareTo(a));


            for (FileObject fileObject : sortedFileObject) {
                //Generate color
                Color fileHeatColor = HeatCalculationUtility.colorOfHeat(fileObject.latestCommitHeatLevel);

                //Convert color to hex
                String colorString = String.format("%02x%02x%02x", (int) (fileHeatColor.getRed() * 255), (int) (fileHeatColor.getGreen() * 255), (int) (fileHeatColor.getBlue() * 255));

                //Add a pane (rectangle) to the screen
                HeatFileComponent heatFileComponent = new HeatFileComponent(fileObject);
                heatFileComponent.setStyle("-fx-background-color: #" + colorString);
                this.addNode(heatFileComponent);

                //Add a tooltip to the file pane
                String fileName = fileObject.getFilename();
                Tooltip tooltip = new Tooltip(String.format("%s\nHeat Level = %d", fileName, fileObject.latestCommitHeatLevel));
                tooltip.setFont(Constants.TOOLTIP_FONT);
                tooltip.setShowDelay(Duration.seconds(0));
                Tooltip.install(heatFileComponent, tooltip);

                System.out.println("Added a file pane for " + fileName + " with heat level " + fileObject.latestCommitHeatLevel); // logger only works sometimes here
            }
        });*/
        System.out.println("Finished adding panes to the heat map.");
    }

    @Override
    public void branchListRequested(String activeBranch, Iterator<String> branchList) {
        // Nothing to do for this action
    }

    @Override
    public void branchSelected() {
        // TODO
        //  Update heat map with latest commit in branch
    }

    @Override
    public void fileSelected(FileObject selectedFile, Iterator<Commit> filesCommits) {
        // Nothing to do for this action
    }

    public void commitSelected(Commit commit) {
        // Nothing to do for this action
    }
}