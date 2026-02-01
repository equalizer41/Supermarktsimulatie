package supermarket.simulator.controller;

import supermarket.simulator.model.Customer;
import supermarket.simulator.model.Grid;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


///Customer loop
///
/// Gaat winkel in
/// - loopt route
/// - vindt item
/// - stopt in inventory
/// - klaar met route
/// - alles gevonden blij ++ niet alles --
/// - naar kassa boodschappen afrekenen
///
public class Controller {

    private int tickCount = 0;
    private Map<Integer, Runnable> scheduledActions = new HashMap<>();
    private Grid grid;
    private List<Customer> customers = new ArrayList<>();
    private int maxCustomers = 5;

    @FXML
    private Canvas canvas;


    public Controller() {
        // Plan alvast wat acties

    }
    public void setGrid(Grid grid) {
        this.grid = grid;
    }


    private void spawnCustomer() {
        Customer newCustomer = new Customer(0, 0, randomColor());
        customers.add(newCustomer);
        System.out.println("Nieuwe klant toegevoegd op tick " + tickCount + " bij (0," + 0 + ")");
    }
    private Color randomColor() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }
    public void tick() {
        tickCount++;
        System.out.println("Tick #" + tickCount);

        // Check of er een actie is voor deze tick
        if (scheduledActions.containsKey(tickCount)) {
            scheduledActions.get(tickCount).run();
        }
        for (Customer c : customers) {
            c.update(grid);
        }
        if (tickCount % 5 == 0) {
            spawnCustomer();
        }
        //draw() = render
        draw();
    }
    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        grid.draw(gc);

        for (Customer c : customers) {
            gc.setFill(c.getColor());
            gc.fillOval(c.getX() * grid.getCellSize(), c.getY() * grid.getCellSize(),
                    grid.getCellSize(), grid.getCellSize());
        }

    }
}