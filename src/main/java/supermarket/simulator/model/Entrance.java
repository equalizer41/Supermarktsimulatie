package supermarket.simulator.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the entrance to the supermarket where customers enter
 */
public class Entrance extends SupermarketObject {
    private int totalCustomersEntered;
    private List<Customer> waitingCustomers;
    private boolean isOpen;
    private int maxCustomersPerTick;

    /**
     * Constructor for Entrance
     * @param x X coordinate
     * @param y Y coordinate
     * @param sprite Entrance sprite
     */
    public Entrance(int x, int y, Image sprite) {
        super(x, y, 1, 1, true, sprite, "Entrance");
        this.totalCustomersEntered = 0;
        this.waitingCustomers = new ArrayList<>();
        this.isOpen = true;
        this.maxCustomersPerTick = 2; // Max customers that can enter per tick
    }

    @Override
    public void placeOnGrid(Grid grid) {
        Tile entranceTile = new Tile(true, sprite, width, height);
        grid.setTile(x, y, entranceTile);
    }

    @Override
    public void onInteract(Customer customer) {
        // This is called when a customer wants to enter
        if (isOpen) {
            enterSupermarket(customer);
        } else {
            addToWaitingList(customer);
        }
    }

    @Override
    public void update() {
        // Process waiting customers if entrance is open
        if (isOpen && !waitingCustomers.isEmpty()) {
            int processed = 0;
            while (!waitingCustomers.isEmpty() && processed < maxCustomersPerTick) {
                Customer customer = waitingCustomers.remove(0);
                enterSupermarket(customer);
                processed++;
            }
        }
    }

    /**
     * Allows a customer to enter the supermarket
     * @param customer The customer entering
     */
    private void enterSupermarket(Customer customer) {
        if (isOpen) {
            totalCustomersEntered++;
            System.out.println("Customer #" + totalCustomersEntered + " entered the supermarket");
        }
    }

    /**
     * Adds a customer to the waiting list
     * @param customer Customer to add
     */
    private void addToWaitingList(Customer customer) {
        waitingCustomers.add(customer);
        System.out.println("Customer added to entrance waiting list. Queue: " + waitingCustomers.size());
    }

    /**
     * Opens the entrance for customers
     */
    public void open() {
        isOpen = true;
        System.out.println("Entrance is now open");
    }

    /**
     * Closes the entrance (no new customers)
     */
    public void close() {
        isOpen = false;
        System.out.println("Entrance is now closed. " + waitingCustomers.size() + " customers waiting");
    }

    /**
     * Spawns a new customer at the entrance
     * @param customer Customer to spawn
     */
    public void spawnCustomer(Customer customer) {
        if (isOpen) {
            enterSupermarket(customer);
        } else {
            addToWaitingList(customer);
        }
    }

    // Getters
    public int getTotalCustomersEntered() {
        return totalCustomersEntered;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getWaitingCustomerCount() {
        return waitingCustomers.size();
    }

    public List<Customer> getWaitingCustomers() {
        return new ArrayList<>(waitingCustomers);
    }

    // Setters
    public void setMaxCustomersPerTick(int max) {
        this.maxCustomersPerTick = max;
    }
}