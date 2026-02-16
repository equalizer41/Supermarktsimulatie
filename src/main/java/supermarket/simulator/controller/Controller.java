package supermarket.simulator.controller;

import supermarket.simulator.model.Customer;
import supermarket.simulator.model.Grid;
import supermarket.simulator.services.LevelBuilder;
import supermarket.simulator.services.TilesetLoader;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main controller for the supermarket simulator
 * Handles game loop, customer management, and rendering
 */
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

    /**
     * Sets the grid and initializes the level
     * @param grid The game grid
     */
    public void setGrid(Grid grid) {
        this.grid = grid;
        setupLevel();
    }

    /**
     * Initializes the level using LevelBuilder
     * This delegates all level construction to the LevelBuilder class
     */
    private void setupLevel() {
        LevelBuilder levelBuilder = new LevelBuilder(grid, loader);
        levelBuilder.buildLevel();

        System.out.println("Level built with:");
        System.out.println("- " + levelBuilder.getShelves().size() + " shelves");
        System.out.println("- " + levelBuilder.getCheckouts().size() + " checkouts");
        System.out.println("- " + levelBuilder.getRefrigerators().size() + " refrigerators");
        System.out.println("- " + levelBuilder.getStorageRooms().size() + " storage rooms");
        System.out.println("- 1 entrance");
        System.out.println("- 1 exit");
    }

    /**
     * Generates a random color
     * @return Random Color object
     */
    private Color randomColor() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }

    /**
     * Main game loop tick
     * Updates customers and renders the scene
     */
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

    /**
     * Renders the current game state
     */
    private void draw() {
        if (canvas == null || grid == null) {
            return;
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw grid
        grid.draw(gc);
    }

    /**
     * Schedules an action to be executed at a specific tick
     * @param tick The tick number when the action should execute
     * @param action The action to execute
     */
    public void scheduleAction(int tick, Runnable action) {
        scheduledActions.put(tick, action);
    }
}