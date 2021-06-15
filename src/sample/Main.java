package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    public static final int[] APPLICATION_SIZE = new int[]{1200,800};


    @Override
    public void start(Stage primaryStage) throws Exception{

        Graph g = new Graph(new int[]{800,600});

        ApplicationController appController = new ApplicationController(g);
        ApplicationView appView = new ApplicationView(appController, g);
        appController.setApplicationView(appView);
        primaryStage.setTitle("Hello World");


        primaryStage.setScene(new Scene(appView.asParent(), APPLICATION_SIZE[0], APPLICATION_SIZE[1]));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
