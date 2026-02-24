package supermarket.simulator.model;

import javafx.scene.image.Image;
import supermarket.simulator.model.world.Grid;
import supermarket.simulator.model.world.Tile;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a storage room where inventory is kept before being stocked on shelves
 */
public class StorageShelf extends SupermarketObject {
    private Map<String, Integer> inventory; // Category -> quantity
    private int maxCapacity;

    /**
     * Constructor for Storage
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width in tiles
     * @param height Height in tiles
     * @param sprite Storage sprite
     * @param maxCapacity Maximum total items that can be stored
     */
    public StorageShelf(int x, int y, int width, int height, Image sprite, int maxCapacity) {
        super(x, y, width, height, false, sprite, "StorageShelf");
        this.inventory   = new HashMap<>();
        this.maxCapacity = maxCapacity;
    }

    public StorageShelf(int x, int y, int width, int height, Image sprite) {
        this(x, y, width, height, sprite, 1000);
    }

    @Override
    public void placeOnGrid(Grid grid) {
        Tile storageTile = new Tile(false, sprite, width, height);
        if (width > 1 || height > 1) {
            grid.setLargeTile(x, y, storageTile);
        } else {
            grid.setTile(x, y, storageTile);
        }
        for (int[] coord : getAccessCoordinatesHoriObj()){
            grid.getTile(coord[0], coord[1]).setLabel("Access");
        }
    }

    @Override
    public void onInteract(Customer customer) {
        // Customers cannot interact with storage - only employees can
        System.out.println("Storage is not accessible to customers");
    }

    /**
     * Adds items to storage inventory
     * @param category Category of items (e.g., "produce", "dairy")
     * @param quantity Number of items to add
     * @return true if items were added, false if storage is full
     */
    public boolean addInventory(String category, int quantity) {
        int currentTotal = getTotalItems();
        if (currentTotal + quantity <= maxCapacity) {
            inventory.put(category, inventory.getOrDefault(category, 0) + quantity);
            System.out.println("Added " + quantity + " " + category + " items to storage");
            return true;
        } else {
            System.out.println("Storage is full! Cannot add " + quantity + " items");
            return false;
        }
    }

    /**
     * Removes items from storage inventory
     * @param category Category of items
     * @param quantity Number of items to remove
     * @return Actual number of items removed
     */
    public int removeInventory(String category, int quantity) {
        int available = inventory.getOrDefault(category, 0);
        int toRemove = Math.min(available, quantity);

        if (toRemove > 0) {
            inventory.put(category, available - toRemove);
            if (inventory.get(category) == 0) {
                inventory.remove(category);
            }
            System.out.println("Removed " + toRemove + " " + category + " items from storage");
        }

        return toRemove;
    }

    /**
     * Transfers items from storage to a shelf
     * @param shelf The shelf to restock
     * @param quantity Number of items to transfer
     */
    public void restockShelf(Shelf shelf, int quantity) {
        String category = shelf.getCategory();
        int available = inventory.getOrDefault(category, 0);
        int toTransfer = Math.min(Math.min(available, quantity),
                shelf.getMaxCapacity() - shelf.getCurrentStock());

        if (toTransfer > 0) {
            removeInventory(category, toTransfer);
            // Note: You'd need to create Item objects to actually stock the shelf
            System.out.println("Restocked " + shelf.getName() + " with " + toTransfer + " " + category + " items");
        }
    }

    /**
     * Checks if storage needs restocking for a category
     * @param category Category to check
     * @param threshold Minimum quantity threshold
     * @return true if quantity is below threshold
     */
    public boolean needsRestock(String category, int threshold) {
        return inventory.getOrDefault(category, 0) < threshold;
    }

    /**
     * Gets total number of items in storage
     * @return Total item count
     */
    public int getTotalItems() {
        return inventory.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Gets quantity of a specific category
     * @param category Category to check
     * @return Quantity of items in that category
     */
    public int getQuantity(String category) {
        return inventory.getOrDefault(category, 0);
    }

    public boolean isFull() {
        return getTotalItems() >= maxCapacity;
    }

    public Map<String, Integer> getInventory() {
        return new HashMap<>(inventory); // Return copy
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}