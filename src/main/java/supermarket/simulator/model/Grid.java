package supermarket.simulator.model;

import supermarket.simulator.services.TilesetLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

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

        // Initialiseer alle tiles met een standaard vloer tile
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
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null; // Of throw new IndexOutOfBoundsException
        }
        return tiles[x][y];
    }

    public void setLargeTile(int x, int y, Tile tile) {
        int widthInCells = tile.getWidthInCells();
        int heightInCells = tile.getHeightInCells();

        // Blokkeer alle cellen die deze tile inneemt
        for (int dx = 0; dx < widthInCells; dx++) {
            for (int dy = 0; dy < heightInCells; dy++) {
                int cellX = x + dx;
                int cellY = y + dy;

                // Bewaar de huidige floor texture
                Tile currentFloor = tiles[cellX][cellY];
                Image floorImage = currentFloor.getImage();

                if (dx == 0 && dy == 0) {
                    // Hoofdcel: bewaar floor texture IN de tile
                    tile.setFloorTexture(floorImage);
                    setTile(cellX, cellY, tile);
                } else {
                    // Placeholder: behoud floor texture maar markeer als occupied
                    Tile placeholder = new Tile(true, floorImage, 1, 1);
                    setTile(cellX, cellY, placeholder);
                }
            }
        }
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void draw(GraphicsContext gc) {
        gc.setImageSmoothing(false);

        // FASE 1: Teken eerst ALLE floors (inclusief onder multi-cell objecten)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = tiles[x][y];
                double pixelX = x * cellSize;
                double pixelY = y * cellSize;

                // Als de tile een floor texture heeft, teken die eerst
                if (tile.getFloorTexture() != null) {
                    gc.drawImage(tile.getFloorTexture(), pixelX, pixelY, cellSize, cellSize);
                }
                // Anders teken de normale tile image (voor reguliere 1x1 floors/walls)
                else if (tile.getWidthInCells() == 1 && tile.getHeightInCells() == 1) {
                    if (tile.getImage() != null) {
                        gc.drawImage(tile.getImage(), pixelX, pixelY, cellSize, cellSize);

                    }
                }
            }
        }
            // FASE 2: Teken daarna alle multi-cell objecten EROVERHEEN
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Tile tile = tiles[x][y];
                    double pixelX = x * cellSize;
                    double pixelY = y * cellSize;

                    // Teken alleen multi-cell objecten
                    if (tile.getImage() != null &&
                            (tile.getWidthInCells() > 1 || tile.getHeightInCells() > 1)) {

                        int tileWidthPixels = tile.getWidthInCells() * cellSize;
                        int tileHeightPixels = tile.getHeightInCells() * cellSize;
                        gc.drawImage(tile.getImage(), pixelX, pixelY, tileWidthPixels, tileHeightPixels);
                    }
                }
            }

            // FASE 3: Teken grid lijnen en debug info
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Tile tile = tiles[x][y];
                    double pixelX = x * cellSize;
                    double pixelY = y * cellSize;

                    // Grid lijnen
                    gc.setStroke(Color.DARKGRAY);
                    gc.setLineWidth(0.5);
                    gc.strokeRect(pixelX, pixelY, cellSize, cellSize);

                    // Coordinaten
                    gc.setFill(Color.BLACK);
                    gc.setFont(new Font("Arial", 7));
                    String coords = "[" + x + "," + y + "]";
                    gc.fillText(coords, pixelX + 1, pixelY + 9);

                    // Rode X voor niet-walkable
                    if (!tile.isWalkable()) {
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(1.5);
                        double size = 6;
                        double startX = pixelX + cellSize - size - 2;
                        double startY = pixelY + 2;
                        gc.strokeLine(startX, startY, startX + size, startY + size);
                        gc.strokeLine(startX + size, startY, startX, startY + size);
                    }

                    // Gele O voor occupied
                    if (tile.isOccupied()) {
                        gc.setStroke(Color.YELLOW);
                        gc.setLineWidth(1.5);
                        double radius = 3;
                        double centerX = pixelX + cellSize - radius - 3;
                        double centerY = pixelY + radius + 3;
                        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                    }
                }
            }
        }
    }
