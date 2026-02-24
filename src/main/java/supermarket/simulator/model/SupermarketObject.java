package supermarket.simulator.model;

import javafx.scene.image.Image;
import supermarket.simulator.model.world.Grid;

import java.util.ArrayList;
import java.util.List;

public abstract class SupermarketObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean walkable;
    protected Image sprite;
    protected String name;
    private Inventory inventory;

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
    public int getAccessY() { return y; }

    /** Geeft de access coordinaten voor het interacten met het object die aan de zijkanten interactable zijn */
    public List<int[]> getAccessCoordinatesVertObj(){
        List<int[]> coords = new ArrayList<>();
        for(int i = 0;i < height; i++) {
            coords.add(new int[]{x - 1, y + i}); // links
            coords.add(new int[]{x + width, y + i}); // rechts
        }
        return coords;
    }
    /** Geeft de access coordinaten voor het interacten met het object die aan de onderkant interactable zijn */
    public List<int[]> getAccessCoordinatesHoriObjBottom(){
        List<int[]> coords = new ArrayList<>();
        for(int i = 0;i < width; i++) {
            coords.add(new int[]{x + i, y + height}); // onder
        }
        return coords;

    }
    /** Geeft de access coordinaten voor het interacten met het object die aan de boven- en onderkant interactable zijn */
    public List<int[]> getAccessCoordinatesHoriObj(){
        List<int[]> coords = new ArrayList<>();
        for(int i = 0;i < width; i++) {
            coords.add(new int[]{x + i, y + height}); // onder
            coords.add(new int[]{x + i, y - 1}); // boven

        }
        return coords;
    }

    public int getX()       { return x; }
    public int getY()       { return y; }
    public int getWidth()   { return width; }
    public int getHeight()  { return height; }
    public String getName() { return name; }

}