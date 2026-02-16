package supermarket.simulator.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a checkout counter where customers pay for their items
 */
public class Checkout extends SupermarketObject {
    private Queue<Customer> queue;
    private Customer currentCustomer;
    private boolean isOpen;
    private int checkoutId;
    private int itemsProcessed;
    private int processingTime; // Time in ticks to process current customer

    /**
     * Constructor for Checkout
     * @param x X coordinate
     * @param y Y coordinate
     * @param sprite Checkout sprite
     * @param checkoutId Unique identifier for this checkout
     */
    public Checkout(int x, int y, Image sprite, int checkoutId) {
        super(x, y, 1, 2, true, sprite, "Checkout");
        this.queue = new LinkedList<>();
        this.currentCustomer = null;
        this.isOpen = true;
        this.checkoutId = checkoutId;
        this.itemsProcessed = 0;
        this.processingTime = 0;
    }

    @Override
    public void placeOnGrid(Grid grid) {
        Tile checkoutTile = new Tile(true, sprite, width, height);
        grid.setLargeTile(x, y, checkoutTile);
    }

    @Override
    public void onInteract(Customer customer) {
        if (isOpen) {
            addToQueue(customer);
        }
    }

    @Override
    public void update() {
        if (currentCustomer != null) {
            processCurrentCustomer();
        } else if (!queue.isEmpty()) {
            startProcessingNextCustomer();
        }
    }

    /**
     * Adds a customer to the checkout queue
     * @param customer Customer to add
     */
    public void addToQueue(Customer customer) {
        queue.add(customer);
        System.out.println("Customer added to checkout " + checkoutId + " queue. Queue size: " + queue.size());
    }

    /**
     * Starts processing the next customer in queue
     */
    private void startProcessingNextCustomer() {
        currentCustomer = queue.poll();
        if (currentCustomer != null) {
            // Calculate processing time based on number of items
            if (currentCustomer.getInventory() != null) {
                processingTime = currentCustomer.getInventory().getItemCount() * 2; // 2 ticks per item
            } else {
                processingTime = 5; // Default processing time
            }
            System.out.println("Checkout " + checkoutId + " started processing customer");
        }
    }

    /**
     * Processes the current customer (reduces processing time each tick)
     */
    private void processCurrentCustomer() {
        processingTime--;
        if (processingTime <= 0) {
            finishProcessingCustomer();
        }
    }

    /**
     * Completes the checkout process for current customer
     */
    private void finishProcessingCustomer() {
        if (currentCustomer != null) {
            if (currentCustomer.getInventory() != null) {
                itemsProcessed += currentCustomer.getInventory().getItemCount();
                currentCustomer.getInventory().clear();
            }
            System.out.println("Checkout " + checkoutId + " finished processing customer. Total items: " + itemsProcessed);
            currentCustomer = null;
        }
    }

    /**
     * Opens the checkout for business
     */
    public void open() {
        isOpen = true;
        System.out.println("Checkout " + checkoutId + " is now open");
    }

    /**
     * Closes the checkout (no new customers accepted)
     */
    public void close() {
        isOpen = false;
        System.out.println("Checkout " + checkoutId + " is now closed");
    }

    // Getters
    public int getQueueSize() {
        return queue.size();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isBusy() {
        return currentCustomer != null;
    }

    public int getCheckoutId() {
        return checkoutId;
    }

    public int getItemsProcessed() {
        return itemsProcessed;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}