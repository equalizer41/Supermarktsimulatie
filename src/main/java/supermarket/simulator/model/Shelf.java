package supermarket.simulator.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shelf that holds items for customers to pick up
 */
public class Shelf extends SupermarketObject {
    private List<Item> items;
    private int maxCapacity;
    private String category; // e.g., "produce", "dairy", "bakery"

    /**
     * Constructor for Shelf
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width in tiles
     * @param height Height in tiles
     * @param sprite Shelf sprite
     * @param category Category of items on this shelf
     * @param maxCapacity Maximum number of items
     */
    public Shelf(int x, int y, int width, int height, Image sprite, String category, int maxCapacity) {
        super(x, y, width, height, false, sprite, "Shelf");
        this.items = new ArrayList<>();
        this.category = category;
        this.maxCapacity = maxCapacity;
    }

    /**
     * Simplified constructor with default values
     */
    public Shelf(int x, int y, Image sprite, String category) {
        this(x, y, 1, 2, sprite, category, 50);
    }

    @Override
    public void placeOnGrid(Grid grid) {
        Tile shelfTile = new Tile(false, sprite, width, height);
        if (width > 1 || height > 1) {
            grid.setLargeTile(x, y, shelfTile);
        } else {
            grid.setTile(x, y, shelfTile);
        }
    }

    @Override
    public void onInteract(Customer customer) {
        if (!items.isEmpty() && customer.getInventory() != null) {
            // Customer picks an item from the shelf
            Item item = items.get(0);
            customer.getInventory().addItem(item);
            System.out.println("Customer picked up: " + item.getName() + " from " + category + " shelf");
        }
    }

    /**
     * Restocks the shelf with items
     * @param item Item to add
     */
    public void addItem(Item item) {
        if (items.size() < maxCapacity) {
            items.add(item);
        }
    }

    /**
     * Removes an item from the shelf
     * @return The removed item, or null if empty
     */
    public Item removeItem() {
        if (!items.isEmpty()) {
            return items.remove(0);
        }
        return null;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isFull() {
        return items.size() >= maxCapacity;
    }

    public String getCategory() {
        return category;
    }

    public int getCurrentStock() {
        return items.size();
    }

    public int getMaxCapacity(){
        return maxCapacity;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items); // Return copy to prevent external modification
    }
}