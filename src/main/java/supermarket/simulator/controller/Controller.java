package supermarket.simulator.controller;

import supermarket.simulator.model.*;
import supermarket.simulator.services.CharacterSpriteLoader;
import supermarket.simulator.model.world.*;
import supermarket.simulator.model.world.LevelBuilder;
import supermarket.simulator.services.TilesetLoader;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

    private static final int MAX_CUSTOMERS  = 5;
    private static final int SPAWN_INTERVAL = 5;

    private int tickCount = 0;
    private final Map<Integer, Runnable> scheduledActions = new HashMap<>();
    private Grid grid;
    private ObjectBuilder objectBuilder;
    private GridRenderer renderer;
    private final TilesetLoader loader = new TilesetLoader();
    private LevelBuilder levelBuilder;
    private final List<Customer> customers = new ArrayList<>();
    private int customerIdCounter = 0;

    @FXML
    private Button debugButton;

    @FXML
    private void toggleDebug() {
        GridRenderer.DEBUG = !GridRenderer.DEBUG;
        debugButton.setText(GridRenderer.DEBUG ? "Debug ON" : "Debug OFF");
    }
    @FXML
    private Canvas canvas;

    public Controller() {}

    public void setGrid(Grid grid) {
        this.grid     = grid;
        this.renderer = new GridRenderer(grid);
        setupLevel();
        setupObject();
    }

    private void setupLevel() {
        levelBuilder = new LevelBuilder(grid, loader);
        levelBuilder.buildLevel();
    }
    private void setupObject() {
        objectBuilder = new ObjectBuilder(grid, loader);
        objectBuilder.buildObjects();
    }



    public void tick() {
        tickCount++;

        if (scheduledActions.containsKey(tickCount)) {
            scheduledActions.get(tickCount).run();
        }

        if (tickCount % SPAWN_INTERVAL == 0 && customers.size() < MAX_CUSTOMERS) {
            //Start simulation
        }
        levelBuilder.updateAll();
        objectBuilder.updateAll();

        for (Customer c : customers) {
            c.update(grid);
        }

        // Log en verwijder inactieve customers
        customers.removeIf(c -> {
            if (!c.isActive()) {
                System.out.println("[Tick " + tickCount + "] Customer #" + c.getId()
                        + " removed from simulation.");
                return true;
            }
            return false;
        });

        draw();
    }

    private void draw() {
        if (canvas == null || grid == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        renderer.render(gc, customers);
    }

    public void scheduleAction(int tick, Runnable action) {
        scheduledActions.put(tick, action);
    }
}