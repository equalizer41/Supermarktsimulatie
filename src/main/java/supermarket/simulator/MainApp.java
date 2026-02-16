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
        Scene scene = new Scene(fxmlLoader.load(), 1500, 1100); // Groter venster
        stage.setTitle("Supermarket Simulator - Omnomnom PixelEater 2000");
        stage.setScene(scene);
        stage.show();

        Controller controller = fxmlLoader.getController();

        // Gebruik grotere cellSize voor betere zichtbaarheid
        int cellSize = 48; // Of 64 voor nog groter
        int cols = 20;
        int rows = 20;

        Grid grid = new Grid(cols, rows, cellSize);
        controller.setGrid(grid);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> controller.tick())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
