package supermarket.simulator.model;

import javafx.scene.image.Image;
import supermarket.simulator.model.world.Grid;
import supermarket.simulator.model.world.Tile;

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
    private int processingTime;

    public Checkout(int x, int y, Image sprite, int checkoutId) {
        super(x, y, 1, 2, true, sprite, "Checkout");
        this.queue           = new LinkedList<>();
        this.currentCustomer = null;
        this.isOpen          = true;
        this.checkoutId      = checkoutId;
        this.itemsProcessed  = 0;
        this.processingTime  = 0;
    }

    @Override
    public void placeOnGrid(Grid grid) {
        Tile checkoutTile = new Tile(true, sprite, width, height);
        grid.setLargeTile(x, y, checkoutTile);
    }

    @Override
    public void onInteract(Customer customer) {
        if (isOpen) addToQueue(customer);
    }

    @Override
    public void update() {
        if (currentCustomer != null) {
            processCurrentCustomer();
        } else if (!queue.isEmpty()) {
            startProcessingNextCustomer();
        }
    }

    /** Checkout is 1x2 — access tile is direct eronder */
    @Override
    public int getAccessX() { return x; }

    @Override
    public int getAccessY() { return y + height; }

    public void addToQueue(Customer customer) {
        queue.add(customer);
        System.out.println("Customer added to checkout " + checkoutId + " queue. Size: " + queue.size());
    }

    private void startProcessingNextCustomer() {
        currentCustomer = queue.poll();
        if (currentCustomer != null) {
            processingTime = currentCustomer.getInventory() != null
                    ? currentCustomer.getInventory().getItemCount() * 2
                    : 5;
            System.out.println("Checkout " + checkoutId + " started processing customer");
        }
    }

    private void processCurrentCustomer() {
        processingTime--;
        if (processingTime <= 0) finishProcessingCustomer();
    }

    private void finishProcessingCustomer() {
        if (currentCustomer != null) {
            if (currentCustomer.getInventory() != null) {
                itemsProcessed += currentCustomer.getInventory().getItemCount();
                currentCustomer.getInventory().clear();
            }
            System.out.println("Checkout " + checkoutId + " finished. Total items: " + itemsProcessed);
            currentCustomer = null;
        }
    }

    public void open()  { isOpen = true;  System.out.println("Checkout " + checkoutId + " open"); }
    public void close() { isOpen = false; System.out.println("Checkout " + checkoutId + " closed"); }

    public int getQueueSize()              { return queue.size(); }
    public boolean isOpen()               { return isOpen; }
    public boolean isBusy()               { return currentCustomer != null; }
    public int getCheckoutId()            { return checkoutId; }
    public int getItemsProcessed()        { return itemsProcessed; }
    public Customer getCurrentCustomer()  { return currentCustomer; }
}