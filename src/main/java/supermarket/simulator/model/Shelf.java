package supermarket.simulator.model;

import javafx.scene.image.Image;
import supermarket.simulator.model.world.Grid;
import supermarket.simulator.model.world.Tile;

import java.util.ArrayList;
import java.util.List;

public class Shelf extends SupermarketObject {

    private static final int DEFAULT_CAPACITY = 50;

    private final List<Item> items = new ArrayList<>();
    private final int maxCapacity;
    private final String category;


    public Shelf(int x, int y, int width, int height, Image sprite, String category, int maxCapacity) {
        super(x, y, width, height, false, sprite, "Shelf");
        this.category    = category;
        this.maxCapacity = maxCapacity;
    }

    public Shelf(int x, int y, Image sprite, String category) {
        this(x, y, 1, 4, sprite, category, DEFAULT_CAPACITY);
    }

    @Override
    public void placeOnGrid(Grid grid) {
        Tile tile = new Tile(false, sprite, width, height);
        if (width > 1 || height > 1) grid.setLargeTile(x, y, tile);
        else grid.setTile(x, y, tile);

        // labeling access tiles
        for (int[] coord : getAccessCoordinatesVertObj()) {
            grid.getTile(coord[0], coord[1]).setLabel("Access");
        }
    }

    @Override
    public void onInteract(Customer customer) {
        if (!items.isEmpty() && customer.getInventory() != null) {
            Item item = items.get(0);
            customer.getInventory().addItem(item);
            System.out.println("Customer picked up: " + item.getName() + " from " + category + " shelf");
        }
    }


    public void addItem(Item item) {
        if (items.size() < maxCapacity) items.add(item);
    }

    public Item removeItem() {
        return items.isEmpty() ? null : items.remove(0);
    }

    public boolean isEmpty()     { return items.isEmpty(); }
    public boolean isFull()      { return items.size() >= maxCapacity; }
    public String getCategory()  { return category; }
    public int getCurrentStock() { return items.size(); }
    public int getMaxCapacity()  { return maxCapacity; }
}