package supermarket.simulator.model;

import supermarket.simulator.services.TilesetLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Grid {
    private final int width;
    private final int height;
    private final int cellSize;
    private final Tile[][] tiles;

    public Grid(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.tiles = new Tile[width][height];

        TilesetLoader tileset = new TilesetLoader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(true, Color.WHITE, tileset.getFloor() );
            }
        }
        tiles[3][2] = new Shelves(tileset.getShelf());
        tiles[3][2].setOccupied(true);

        tiles[4][2] = new Shelves(tileset.getShelf());
        tiles[4][2].setOccupied(true);

        tiles[3][4] = new Shelves(tileset.getShelf());
        tiles[3][4].setOccupied(true);

        tiles[4][4] = new Shelves(tileset.getShelf());
        tiles[4][4].setOccupied(true);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                boolean isEdge = (x == 0 || y == 0 || x == width - 1 || y == height - 1);
                if (isEdge) {
                    tiles[x][y] = new Tile(false, Color.GRAY, tileset.getWall());
                    tiles[x][y].setOccupied(true);
                }
            }
        }
    }

    public Tile getTile(int x, int y) { return tiles[x][y]; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getCellSize() { return cellSize; }

    public void draw(GraphicsContext gc) {
        gc.setImageSmoothing(false);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {


                Tile t = tiles[x][y];

                gc.setFill(Color.DARKGRAY);
                gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);


                //gc.setFill(t.getColor());
                //gc.setFill(t.isWalkable() ? Color.WHITE : Color.DARKGRAY);
                //gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                if (t.getImage() != null) {
                    gc.drawImage(t.getImage(), x * cellSize, y * cellSize, cellSize, cellSize);
                }

                gc.setStroke(Color.LIGHTGRAY);
                gc.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }
}
