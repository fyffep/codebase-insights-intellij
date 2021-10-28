package intellij_extension;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The app starts from here.
 */
public class ParentWindow extends Application
{
    private static Group rootGroup;
    private static Stage stage = null;
    private static Scene heatMapScene;



    /**
     * Configures the window by loading in the scenes
     */
    @Override
    public void start(Stage primaryStage) throws InterruptedException, IOException
    {
        System.out.println("Creating Parent");
        //Load in the XML of the testing window
        //Parent playRoot = FXMLLoader.load(getClass().getClassLoader().getResource("parent-play-window.fxml"));
        //Parent playRoot = FXMLLoader.load(getClass().getClassLoader().getResource("temp.fxml"));
        Parent playRoot = new VBox();

        //Set up the scenes
        heatMapScene = new Scene(playRoot, 300, 300);
        primaryStage.setScene(heatMapScene);
        primaryStage.show();

        //Change background colors
        playRoot.setStyle("-fx-background-color: #AAAAAA");

        //Set up the window
        primaryStage.setTitle("Heat Map Extension");
        primaryStage.show();
        this.stage = primaryStage;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}