package supermarket.simulator.model;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Tile {
    private boolean walkable;
    private boolean occupied;
    private Color color;
    private Image image;


    public enum TileType {
        Wall,
        Door,
        Floor,
        Shelves,
        Register
    }

    public Tile(boolean walkable, Color color, Image image) {
        this.walkable = walkable;
        this.occupied = false;
        this.color = color;
        this.image = image;

    }
    public Color getColor() { return color; }
    public boolean isWalkable() { return walkable; }
    public void setWalkable(boolean walkable) { this.walkable = walkable; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public Image getImage() { return image; }
}
