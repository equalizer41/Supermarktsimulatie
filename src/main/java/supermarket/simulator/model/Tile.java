package supermarket.simulator.model;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Tile {
    private boolean walkable;
    private boolean occupied;
    private Image image;
    private Image floorTexture;
    private int widthInCells;
    private int heightInCells;

    public enum TileType {
        Wall,
        Door,
        Floor,
        Shelves,
        Register,
        Counter,
        Fridge,
        Entrance
    }

    public Tile(boolean walkable, Image image, int widthInCells, int heightInCells) {
        this.walkable = walkable;
        this.image = image;
        this.widthInCells = widthInCells;
        this.heightInCells = heightInCells;
        this.floorTexture = null;
    }

    public boolean isWalkable() { return walkable; }
    public void setWalkable(boolean walkable) { this.walkable = walkable; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public Image getImage() { return image; }

    public Image getFloorTexture() { return floorTexture; }
    public void setFloorTexture(Image floorTexture) { this.floorTexture = floorTexture; }

    public int getHeightInCells() { return heightInCells; }
    public int getWidthInCells() { return widthInCells; }
}