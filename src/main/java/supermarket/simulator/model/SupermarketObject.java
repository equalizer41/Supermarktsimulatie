package supermarket.simulator.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Abstract base class for all placeable objects in the supermarket
 * Provides common properties and behavior for shelves, checkouts, refrigerators, etc.
 */
public abstract class SupermarketObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean walkable;
    protected Image sprite;
    protected String name;

    /**
     * Constructor for SupermarketObject
     * @param x X coordinate on the grid
     * @param y Y coordinate on the grid
     * @param width Width in tiles
     * @param height Height in tiles
     * @param walkable Whether customers can walk through this object
     * @param sprite Image sprite for rendering
     * @param name Display name of the object
     */
    public SupermarketObject(int x, int y, int width, int height, boolean walkable,
                             Image sprite, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.walkable = walkable;
        this.sprite = sprite;
        this.name = name;
    }

    /**
     * Places this object on the grid
     * @param grid The grid to place the object on
     */
    public abstract void placeOnGrid(Grid grid);

    /**
     * Called when a customer interacts with this object
     * @param customer The customer interacting
     */
    public abstract void onInteract(Customer customer);

    /**
     * Update method called each game tick
     */
    public void update() {
        // Default: no update behavior
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public Image getSprite() {
        return sprite;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }
}