package supermarket.simulator;

import supermarket.simulator.controller.Controller;
import supermarket.simulator.model.Grid;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("Marketview.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        stage.setTitle("Omnomnom PixelEater 2000");
        stage.setScene(scene);
        stage.show();

        Controller controller = fxmlLoader.getController();
        // oops
        // Bereken gridgrootte op basis van scene
        int cellSize = 48;
        // int voor afronding, aantal kolomnmen/rijen berekenen door breedte te delen door vakjes grootte
        int cols = (int) (scene.getWidth() / cellSize);
        int rows = (int) (scene.getHeight() / cellSize);
        // alles
        Grid grid = new Grid(cols, rows, cellSize);
        controller.setGrid(grid);

        // JavaFX shit dont ask me :)
        Timeline timeline = new Timeline(                                  //tick() = update loop uit controller
                new KeyFrame(Duration.seconds(1), event -> controller.tick())
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // laat de simulatie doorlopen
        timeline.play();
    }



    public static void main(String[] args) {
        launch();
    }
}