package intellij_extension;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * THIS FILE IS UNUSED
 */
public class RootWindow extends Application
{
    //Views
    private static Stage stage = null;
    //private static HeatMapScene heatMapScene;

    //Logger TODO
    //public static final Logger logger = Logger.getLogger(RootWindow.class);

    /**
     * Configures the window by loading in the scenes
     */
    @Override
    public void start(Stage primaryStage) throws InterruptedException, IOException
    {
        // log4j configuration
        //BasicConfigurator.configure(); //TODO doesn't work

        //Load in the XML of the testing window
        //Parent playRoot = FXMLLoader.load(getClass().getClassLoader().getResource("parent-play-window.fxml"));
        /*URL fxmlFile = getClass().getClassLoader().getResource("HeatMapScene.fxml");
        if (fxmlFile == null) {
            throw new NullPointerException("The FXML file `HeatMapScene.fxml` could not be found or loaded");
        }
        Parent rootParent = FXMLLoader.load(fxmlFile);*
        Parent rootParent = new VBox();

        /*
        FXMLLoader fxmlLoader = new FXMLLoader(RootWindow.class.getResource("HeatMapScene.fxml"));
        // Load the scene
        Scene heatMapSceneView = new Scene(fxmlLoader.load());
        heatMapScene = fxmlLoader.getController();
        primaryStage.setScene(heatMapSceneView);*

        //Change background colors
        rootParent.setStyle("-fx-background-color: #AAAAAA");

        //Set up the scenes
        heatMapScene = new HeatMapScene(rootParent);
        primaryStage.setScene(heatMapScene);
        stage = primaryStage;

        //Set up the window
        primaryStage.setTitle("Heat Map Extension");
        primaryStage.show();*/



        //Test adding a pane
        /*FileToHeatMap model = new FileToHeatMap();
        File tempFile = new File("Constants.java");
        model.addHeatObject(tempFile, new CodeCoverageHeat(0.75));
        HeatMapController heatMapController = new HeatMapController(heatMapScene, model);*/








        /*Group root  =  new Group();
        Scene heatMapScene = new Scene(root, 300, 300);

        List<FileObject> fileObjectList = new LinkedList<>();
        fileObjectList.add(new FileObject("TestFile1", "TestDir1", 3));
        fileObjectList.add(new FileObject("TestFile2", "TestDir2", 3));
        fileObjectList.add(new FileObject("TestFile3", "TestDir3", 3));

        HeatMapContainer heatMapContainer = new HeatMapContainer();
        heatMapContainer.populate(fileObjectList);
        root.getChildren().add(heatMapContainer);

        primaryStage.setScene(heatMapScene);*/
    }
}