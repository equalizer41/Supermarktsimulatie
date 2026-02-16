package supermarket.simulator.controller;

import supermarket.simulator.model.Customer;
import supermarket.simulator.model.Grid;
import supermarket.simulator.model.Tile;
import supermarket.simulator.services.TilesetLoader;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private int tickCount = 0;
    private Map<Integer, Runnable> scheduledActions = new HashMap<>();
    private Grid grid;
    private TilesetLoader loader = new TilesetLoader();
    private List<Customer> customers = new ArrayList<>();
    private int maxCustomers = 5;
    private int spawnInterval = 5; // Spawn a customer every 5 ticks

    @FXML
    private Canvas canvas;

    public Controller() {
        // Constructor
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        setupLevel();
    }

    private void setupLevel() {

        Image wallcorner_topright = loader.getNamedTile("wallcorner_topright");
        Image wallcorner_topleft = loader.getNamedTile("wallcorner_topleft");
        Image wallcorner_bottomright = loader.getNamedTile("wallcorner_bottomright");
        Image wallcorner_bottomleft = loader.getNamedTile("wallcorner_bottomleft");
        Image wall_left = loader.getNamedTile("wall_left");
        Image wall_top = loader.getNamedTile("wall_top");
        Image wall_right = loader.getNamedTile("wall_right");
        Image wall_bottom_top = loader.getNamedTile("wall_bottom_top");
        Image wall_bottom_ground = loader.getNamedTile("wall_bottom_ground");
        Image floor = loader.getNamedTile("floor");
        Image exit = loader.getNamedTile("exit");
        Image entrance = loader.getNamedTile("entrance");
        Image checkout = loader.getNamedTile("checkout");

        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Linker-boven hoek
                if (x == 0 && y == 0) {
                    grid.setTile(x, y, new Tile(false, Color.GRAY, wallcorner_topleft,1,1));
                }
                // Rechter-boven hoek
                else if (x == width - 1 && y == 0){
                    grid.setTile(x,y,new Tile(false, Color.GRAY, wallcorner_topright,1,1));
                }
                // Muur links
                else if (x == 0 && y >= 1 && y <= height - 2) {
                    grid.setTile(x, y, new Tile(false, Color.GRAY, wall_left,1,1));
                }
                // Muur bovenin
                else if (x >= 1 && x <= width - 2 && y == 0 ) {
                    grid.setTile(x, y, new Tile(false, Color.GRAY, wall_top,1,1));
                }
                // Muur beneden 1 na laatste
                else if (x >= 1 && x <= width -2 && y == height - 2) {
                    grid.setTile(x, y, new Tile(true, Color.GRAY, wall_bottom_top,1,1));
                }
                // Muur onderin
                else if (x >= 1 && x <= width -2 && y == height - 1) {
                    grid.setTile(x, y, new Tile(false, Color.GRAY, wall_bottom_ground,1,1));
                }
                // Muur rechts
                else if (x == width - 1 && y >= 1 && y <= height - 2) {
                    grid.setTile(x, y, new Tile(false, Color.GRAY, wall_right,1,1));
                }
                // Linker-onder hoek
                else if (x == 0 && y == height - 1) {
                    grid.setTile(x, y, new Tile(false, Color.GRAY, wallcorner_bottomleft,1,1));
                }
                // Rechter-onder hoek
                else if (x == width - 1 && y == height - 1) {
                    grid.setTile(x, y, new Tile(false, Color.GRAY, wallcorner_bottomright,1,1));
                }
                else {
                    // default tile
                    grid.setTile(x, y, new Tile(true, Color.TRANSPARENT, floor,1,1));
                }


            }

        }
        // ENTRANCE & EXIT - Bottom row, spaced from edges
        int bottomRow = height - 1;
        grid.setTile(3, bottomRow, new Tile(true, Color.LIGHTGRAY, exit, 1, 1));
        grid.setTile(width - 4, bottomRow, new Tile(true, Color.LIGHTGRAY, entrance, 1, 1));

        // CHECKOUTS - Centered horizontally, specific rows from bottom
        int checkoutRow = height - 5; // 5 cells from bottom
        int centerX = width / 2;

        // Space checkouts evenly around center
        grid.setLargeTile(centerX - 6, checkoutRow, new Tile(true, Color.TRANSPARENT, checkout, 1, 2));
        grid.setLargeTile(centerX - 3, checkoutRow, new Tile(true, Color.TRANSPARENT, checkout, 1, 2));
        grid.setLargeTile(centerX, checkoutRow, new Tile(true, Color.TRANSPARENT, checkout, 1, 2));

    }

    private Color randomColor() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }

    public void tick() {
        tickCount++;
        System.out.println("Tick #" + tickCount);

        // Execute scheduled actions
        if (scheduledActions.containsKey(tickCount)) {
            scheduledActions.get(tickCount).run();
        }

        // Update all customers
        for (Customer c : customers) {
            c.update(grid);
        }
        // Draw the scene
        draw();
    }

    private void draw() {
        if (canvas == null || grid == null) {
            return;
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw grid
        grid.draw(gc);

    }

    public void scheduleAction(int tick, Runnable action) {
        scheduledActions.put(tick, action);
    }
}