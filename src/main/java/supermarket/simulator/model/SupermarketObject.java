package supermarket.simulator.model;

import javafx.scene.image.Image;
import supermarket.simulator.model.world.Grid;

public abstract class SupermarketObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean walkable;
    protected Image sprite;
    protected String name;

    protected SupermarketObject(int x, int y, int width, int height,
                                boolean walkable, Image sprite, String name) {
        this.x        = x;
        this.y        = y;
        this.width    = width;
        this.height   = height;
        this.walkable = walkable;
        this.sprite   = sprite;
        this.name     = name;
    }

    public abstract void placeOnGrid(Grid grid);

    public abstract void onInteract(Customer customer);

    /** Optional per-tick update logic. Override when needed. */
    public void update() {}
    /** Geeft de tile direct onder het object terug — altijd walkable */
    public int getAccessX() { return x; }
    public int getAccessY() { return y + height; } // tile onder het object

    public int getX()       { return x; }
    public int getY()       { return y; }
    public int getWidth()   { return width; }
    public int getHeight()  { return height; }
    public boolean isWalkable() { return walkable; }
    public String getName() { return name; }
}