package supermarket.simulator.model.world;

import javafx.scene.image.Image;

public class Grid {
    private final int width;
    private final int height;
    private final int cellSize;
    private final Tile[][] tiles;

    public Grid(int width, int height, int cellSize) {
        this.width    = width;
        this.height   = height;
        this.cellSize = cellSize;
        this.tiles    = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(true, null, 1, 1);
            }
        }
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = tile;
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return null;
        return tiles[x][y];
    }

    public void setLargeTile(int x, int y, Tile tile) {
        int widthInCells  = tile.getWidthInCells();
        int heightInCells = tile.getHeightInCells();

        for (int dx = 0; dx < widthInCells; dx++) {
            for (int dy = 0; dy < heightInCells; dy++) {
                int cellX = x + dx;
                int cellY = y + dy;

                Tile currentFloor = tiles[cellX][cellY];
                Image floorImage  = currentFloor.getImage();

                if (dx == 0 && dy == 0) {
                    tile.setFloorTexture(floorImage);
                    setTile(cellX, cellY, tile);
                } else {
                    Tile placeholder = new Tile(false, floorImage, 1, 1);
                    setTile(cellX, cellY, placeholder);
                }
            }
        }
    }

    public int getWidth()    { return width; }
    public int getHeight()   { return height; }
    public int getCellSize() { return cellSize; }
}