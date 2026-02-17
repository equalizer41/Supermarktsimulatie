package supermarket.simulator.controller;

import supermarket.simulator.model.*;
import supermarket.simulator.services.CharacterSpriteLoader;
import supermarket.simulator.model.world.*;
import supermarket.simulator.model.world.LevelBuilder;
import supermarket.simulator.services.TilesetLoader;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

    private static final int MAX_CUSTOMERS  = 5;
    private static final int SPAWN_INTERVAL = 50;

    private int tickCount = 0;
    private final Map<Integer, Runnable> scheduledActions = new HashMap<>();
    private Grid grid;
    private GridRenderer renderer;
    private final TilesetLoader loader = new TilesetLoader();

    private LevelBuilder levelBuilder;
    private final List<Customer> customers = new ArrayList<>();
    private int customerIdCounter = 0;

    @FXML
    private Canvas canvas;

    public Controller() {}

    public void setGrid(Grid grid) {
        this.grid     = grid;
        this.renderer = new GridRenderer(grid);
        setupLevel();
    }

    private void setupLevel() {
        levelBuilder = new LevelBuilder(grid, loader);
        levelBuilder.buildLevel();
        spawnCustomer();
    }

    private void spawnCustomer() {
        if (customers.size() >= MAX_CUSTOMERS) return;

        Entrance entrance        = levelBuilder.getEntrance();
        Exit exit                = levelBuilder.getExit();
        List<Shelf> shelves      = levelBuilder.getShelves();
        List<Checkout> checkouts = levelBuilder.getCheckouts();

        customerIdCounter++;

        // Spawn op de tile boven de entrance (entrance zit op de onderste muur rij)
        Customer customer = new Customer(entrance.getX(), entrance.getY() - 2, grid, CharacterSpriteLoader.randomCustomer());

        // Pick 2 different random shelves from LevelBuilder
        int i1 = (int) (Math.random() * shelves.size());
        int i2 = (int) (Math.random() * shelves.size());
        while (i2 == i1) i2 = (int) (Math.random() * shelves.size());

        Shelf shelf1 = shelves.get(i1);
        Shelf shelf2 = shelves.get(i2);

        // Pick checkout with shortest queue
        Checkout checkout = checkouts.stream()
                .min((a, b) -> a.getQueueSize() - b.getQueueSize())
                .orElse(checkouts.get(0));

        customer.addDestination(shelf1.getAccessX(),   shelf1.getAccessY());
        customer.addDestination(shelf2.getAccessX(),   shelf2.getAccessY());
        customer.addDestination(checkout.getAccessX(), checkout.getAccessY());
        customer.addDestination(exit.getAccessX(),     exit.getAccessY());

        customer.startShopping();
        customers.add(customer);

        System.out.println("Spawned customer #" + customerIdCounter);
    }

    public void tick() {
        tickCount++;
        System.out.println("Tick #" + tickCount);

        if (scheduledActions.containsKey(tickCount)) {
            scheduledActions.get(tickCount).run();
        }

        if (tickCount % SPAWN_INTERVAL == 0 && customers.size() < MAX_CUSTOMERS) {
            spawnCustomer();
        }

        levelBuilder.updateAll();

        for (Customer c : customers) {
            c.update(grid);
        }

        customers.removeIf(c -> !c.isActive());

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