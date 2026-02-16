package supermarket.simulator.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the exit from the supermarket where customers leave
 */
public class Exit extends SupermarketObject {
    private int totalCustomersExited;
    private List<Customer> exitingCustomers;
    private boolean isOpen;
    private int maxCustomersPerTick;

    /**
     * Constructor for Exit
     * @param x X coordinate
     * @param y Y coordinate
     * @param sprite Exit sprite
     */
    public Exit(int x, int y, Image sprite) {
        super(x, y, 1, 1, true, sprite, "Exit");
        this.totalCustomersExited = 0;
        this.exitingCustomers = new ArrayList<>();
        this.isOpen = true;
        this.maxCustomersPerTick = 3; // Max customers that can exit per tick
    }

    @Override
    public void placeOnGrid(Grid grid) {
        Tile exitTile = new Tile(true, sprite, width, height);
        grid.setTile(x, y, exitTile);
    }

    @Override
    public void onInteract(Customer customer) {
        // This is called when a customer wants to leave
        if (isOpen) {
            exitSupermarket(customer);
        } else {
            addToExitQueue(customer);
        }
    }

    @Override
    public void update() {
        // Process customers trying to exit
        if (isOpen && !exitingCustomers.isEmpty()) {
            int processed = 0;
            while (!exitingCustomers.isEmpty() && processed < maxCustomersPerTick) {
                Customer customer = exitingCustomers.remove(0);
                exitSupermarket(customer);
                processed++;
            }
        }
    }

    /**
     * Allows a customer to exit the supermarket
     * @param customer The customer exiting
     */
    private void exitSupermarket(Customer customer) {
        if (isOpen) {
            totalCustomersExited++;
            // Check if customer has paid
            if (customer.getInventory() != null && customer.getInventory().getItemCount() > 0) {
                System.out.println("Warning: Customer #" + totalCustomersExited +
                        " left with unpaid items! Possible shoplifting!");
            } else {
                System.out.println("Customer #" + totalCustomersExited + " exited the supermarket");
            }
            // Remove customer from simulation ??

        }
    }

    /**
     * Adds a customer to the exit queue
     * @param customer Customer to add
     */
    private void addToExitQueue(Customer customer) {
        if (!exitingCustomers.contains(customer)) {
            exitingCustomers.add(customer);
            System.out.println("Customer added to exit queue. Queue: " + exitingCustomers.size());
        }
    }

    /**
     * Opens the exit for customers
     */
    public void open() {
        isOpen = true;
        System.out.println("Exit is now open");
    }

    /**
     * Closes the exit (emergency lockdown)
     */
    public void close() {
        isOpen = false;
        System.out.println("Exit is now closed (lockdown). " + exitingCustomers.size() + " customers waiting");
    }

    /**
     * Checks if a customer has reached the exit
     * @param customer Customer to check
     * @return true if customer is at exit position
     */
    public boolean hasReached(Customer customer) {
        return customer.getX() == this.x && customer.getY() == this.y;
    }

    /**
     * Records statistics when customer exits
     * @param customer The exiting customer
     */
    public void recordExitStats(Customer customer) {
        int itemsPurchased = 0;
        if (customer.getInventory() != null) {
            itemsPurchased = customer.getInventory().getItemCount();
        }
        System.out.println("Customer exited with " + itemsPurchased + " items purchased");
    }

    // Getters
    public int getTotalCustomersExited() {
        return totalCustomersExited;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getExitQueueSize() {
        return exitingCustomers.size();
    }

    public List<Customer> getExitingCustomers() {
        return new ArrayList<>(exitingCustomers);
    }

    // Setters
    public void setMaxCustomersPerTick(int max) {
        this.maxCustomersPerTick = max;
    }
}