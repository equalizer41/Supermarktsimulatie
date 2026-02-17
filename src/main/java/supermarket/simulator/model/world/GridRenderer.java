package supermarket.simulator.model.world;

import supermarket.simulator.model.Person;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Responsible for rendering the Grid and all Persons onto a Canvas.
 * Separated from Grid to follow the Single Responsibility Principle.
 */
public class GridRenderer {

    public static boolean DEBUG = true;

    private final Grid grid;

    public GridRenderer(Grid grid) {
        this.grid = grid;
    }

    /** Teken de grid + alle persons in de juiste volgorde. */
    public void render(GraphicsContext gc, List<? extends Person> persons) {
        gc.setImageSmoothing(false);

        drawFloors(gc);
        drawLargeTiles(gc);
        drawPersons(gc, persons);

        if (DEBUG) {
            drawDebugOverlay(gc);
        }
    }

    // -------------------------------------------------------------------------
    // Fase 1: Floors
    // -------------------------------------------------------------------------

    private void drawFloors(GraphicsContext gc) {
        int cellSize = grid.getCellSize();

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Tile tile     = grid.getTile(x, y);
                double pixelX = x * cellSize;
                double pixelY = y * cellSize;

                if (tile.getFloorTexture() != null) {
                    gc.drawImage(tile.getFloorTexture(), pixelX, pixelY, cellSize, cellSize);
                } else if (tile.getWidthInCells() == 1 && tile.getHeightInCells() == 1) {
                    if (tile.getImage() != null) {
                        gc.drawImage(tile.getImage(), pixelX, pixelY, cellSize, cellSize);
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Fase 2: Multi-cell objecten (kasten, kassa's, etc.)
    // -------------------------------------------------------------------------

    private void drawLargeTiles(GraphicsContext gc) {
        int cellSize = grid.getCellSize();

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Tile tile     = grid.getTile(x, y);
                double pixelX = x * cellSize;
                double pixelY = y * cellSize;

                if (tile.getImage() != null &&
                        (tile.getWidthInCells() > 1 || tile.getHeightInCells() > 1)) {

                    int tileWidthPixels  = tile.getWidthInCells() * cellSize;
                    int tileHeightPixels = tile.getHeightInCells() * cellSize;
                    gc.drawImage(tile.getImage(), pixelX, pixelY, tileWidthPixels, tileHeightPixels);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Fase 3: Persons (customers, employees)
    // -------------------------------------------------------------------------

    private void drawPersons(GraphicsContext gc, List<? extends Person> persons) {
        int cellSize = grid.getCellSize();
        for (Person p : persons) {
            p.draw(gc, cellSize);
        }
    }

    // -------------------------------------------------------------------------
    // Fase 4: Debug overlay (grid lijnen, coordinaten, walkable/occupied)
    // -------------------------------------------------------------------------

    private void drawDebugOverlay(GraphicsContext gc) {
        int cellSize = grid.getCellSize();

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Tile tile     = grid.getTile(x, y);
                double pixelX = x * cellSize;
                double pixelY = y * cellSize;

                // Grid lijnen
                gc.setStroke(Color.DARKGRAY);
                gc.setLineWidth(0.5);
                gc.strokeRect(pixelX, pixelY, cellSize, cellSize);

                // Coordinaten
                gc.setFill(Color.BLACK);
                gc.setFont(new Font("Arial", 7));
                gc.fillText("[" + x + "," + y + "]", pixelX + 1, pixelY + 9);

                // Rode X voor niet-walkable
                if (!tile.isWalkable()) {
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(1.5);
                    double size   = 6;
                    double startX = pixelX + cellSize - size - 2;
                    double startY = pixelY + 2;
                    gc.strokeLine(startX, startY, startX + size, startY + size);
                    gc.strokeLine(startX + size, startY, startX, startY + size);
                }

                // Gele O voor occupied
                if (tile.isOccupied()) {
                    gc.setStroke(Color.YELLOW);
                    gc.setLineWidth(1.5);
                    double radius  = 3;
                    double centerX = pixelX + cellSize - radius - 3;
                    double centerY = pixelY + radius + 3;
                    gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                }
            }
        }
    }
}