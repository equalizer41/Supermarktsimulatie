package supermarket.simulator.model;

import javafx.scene.image.Image;
import supermarket.simulator.model.world.Grid;
import supermarket.simulator.model.world.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a refrigerator unit that stores cold/frozen items
 */
public class Refrigerator extends SupermarketObject {
    private List<Item> items;
    private int maxCapacity;
    private double temperature; // Temperature in Celsius
    private RefrigeratorType type;
    private boolean isPoweredOn;

    public enum RefrigeratorType {
        CHILLED,    // For dairy, drinks (2-4°C)
        FROZEN      // For frozen goods (-18°C)
    }

    /**
     * Constructor for Refrigerator
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width in tiles
     * @param height Height in tiles
     * @param sprite Refrigerator sprite
     * @param type Type of refrigerator (chilled or frozen)
     * @param maxCapacity Maximum number of items
     */
    public Refrigerator(int x, int y, int width, int height, Image sprite,
                        RefrigeratorType type, int maxCapacity) {
        super(x, y, width, height, false, sprite, "Refrigerator");
        this.items = new ArrayList<>();
        this.type = type;
        this.maxCapacity = maxCapacity;
        this.isPoweredOn = true;
        this.temperature = (type == RefrigeratorType.FROZEN) ? -18.0 : 3.0;
    }

    /**
     * constructor
     */
    public Refrigerator(int x, int y, Image sprite, RefrigeratorType type) {
        this(x, y, 3, 2, sprite, type, 40);
    }

    @Override
    public void placeOnGrid(Grid grid) {
        Tile fridgeTile = new Tile(false, sprite, width, height);
        if (width > 1 || height > 1) {
            grid.setLargeTile(x, y, fridgeTile);
        } else {
            grid.setTile(x, y, fridgeTile);
        }
    }

    @Override
    public void onInteract(Customer customer) {
        if (!items.isEmpty() && customer.getInventory() != null) {
            Item item = items.get(0);
            customer.getInventory().addItem(item);
            System.out.println("Customer picked up: " + item.getName() + " from " + type + " refrigerator");
        }
    }

    @Override
    public void update() {
        if (!isPoweredOn) {
            // Temperature rises when power is off
            if (type == RefrigeratorType.FROZEN) {
                temperature += 0.5; // Frozen items warm up faster
            } else {
                temperature += 0.2;
            }
            checkSpoilage();
        }
    }

    /**
     * Checks if items are spoiling due to temperature
     */
    private void checkSpoilage() {
        if (type == RefrigeratorType.FROZEN && temperature > -10) {
            System.out.println("Warning: Frozen items thawing in refrigerator at " + x + "," + y);
        } else if (type == RefrigeratorType.CHILLED && temperature > 8) {
            System.out.println("Warning: Chilled items warming in refrigerator at " + x + "," + y);
        }
    }

    /**
     * Adds an item to the refrigerator
     * @param item Item to add
     */
    public void addItem(Item item) {
        if (items.size() < maxCapacity) {
            items.add(item);
        }
    }

    /**
     * Removes an item from the refrigerator
     * @return The removed item, or null if empty
     */
    public Item removeItem() {
        if (!items.isEmpty()) {
            return items.remove(0);
        }
        return null;
    }

    /**
     * Powers on the refrigerator
     */
    public void powerOn() {
        isPoweredOn = true;
        // Gradually return to target temperature
        temperature = (type == RefrigeratorType.FROZEN) ? -18.0 : 3.0;
        System.out.println("Refrigerator at " + x + "," + y + " powered on");
    }

    /**
     * Powers off the refrigerator (for maintenance)
     */
    public void powerOff() {
        isPoweredOn = false;
        System.out.println("Refrigerator at " + x + "," + y + " powered off");
    }

    // Getters
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isFull() {
        return items.size() >= maxCapacity;
    }

    public double getTemperature() {
        return temperature;
    }

    public RefrigeratorType getType() {
        return type;
    }

    public boolean isPoweredOn() {
        return isPoweredOn;
    }

    public int getCurrentStock() {
        return items.size();
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }
}