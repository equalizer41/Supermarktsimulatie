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

    public static boolean DEBUG       = true;   // master toggle
    public static boolean DEBUG_GRID  = true;   // lijnen, coördinaten, walkable/occupied
    public static boolean DEBUG_PATHS = true;   // route-visualisatie per persoon

    // Kleur per persoon (herhaalt na 8)
    private static final Color[] PATH_COLORS = {
            Color.color(0.0, 0.8, 1.0, 0.75),  // cyaan
            Color.color(1.0, 0.4, 0.0, 0.75),  // oranje
            Color.color(0.2, 1.0, 0.2, 0.75),  // groen
            Color.color(1.0, 0.2, 0.8, 0.75),  // roze
            Color.color(1.0, 1.0, 0.0, 0.75),  // geel
            Color.color(0.8, 0.0, 1.0, 0.75),  // paars
            Color.color(1.0, 0.0, 0.0, 0.75),  // rood
            Color.color(0.0, 0.7, 0.5, 0.75),  // teal
    };

    private final Grid grid;

    public GridRenderer(Grid grid) {
        this.grid = grid;
    }

    /** Teken de grid + alle persons in de juiste volgorde. */
    public void render(GraphicsContext gc, List<? extends Person> persons) {
        gc.setImageSmoothing(false);

        drawFloors(gc);

        if (DEBUG && DEBUG_PATHS) {
            drawDebugPaths(gc, persons);
        }

        // Y-sorted rendering: per rij tiles dan persons
        drawLayered(gc, persons);

        if (DEBUG && DEBUG_GRID) {
            drawDebugOverlay(gc);
        }
    }

    // -------------------------------------------------------------------------
    // Fase 1: Floors (altijd als onderste laag)
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
    // Fase 2+3: Y-gesorteerd: per rij eerst objecten dan persons
    // -------------------------------------------------------------------------

    private void drawLayered(GraphicsContext gc, List<? extends Person> persons) {
        int cellSize = grid.getCellSize();

        for (int y = 0; y < grid.getHeight(); y++) {

            // -- Stap A: teken ALLEEN large tile origins op rij y --
            for (int x = 0; x < grid.getWidth(); x++) {
                Tile tile = grid.getTile(x, y);

                if (tile.getImage() == null) continue;
                if (tile.getWidthInCells() <= 1 && tile.getHeightInCells() <= 1) continue;

                double srcWidth   = tile.getImage().getWidth();
                double srcHeight  = tile.getImage().getHeight();
                double drawWidth  = tile.getWidthInCells() * cellSize;
                double scale      = drawWidth / srcWidth;
                double drawHeight = srcHeight * scale;

                double tileBottomY = (y + tile.getHeightInCells()) * cellSize;
                double drawY       = tileBottomY - drawHeight;

                gc.drawImage(tile.getImage(), x * cellSize, drawY, drawWidth, drawHeight);
            }

            // -- Stap A2: teken 1x1 tiles waarvan de image hoger is dan 1 cel --
            for (int x = 0; x < grid.getWidth(); x++) {
                Tile tile = grid.getTile(x, y);

                if (tile.getImage() == null) continue;
                if (tile.getWidthInCells() != 1 || tile.getHeightInCells() != 1) continue;

                double srcWidth   = tile.getImage().getWidth();
                double srcHeight  = tile.getImage().getHeight();
                double drawWidth  = cellSize;
                double scale      = drawWidth / srcWidth;
                double drawHeight = srcHeight * scale;

                // Alleen tekenen als de image significant groter is dan 1 cel
                if (drawHeight <= cellSize + 2) continue;

                // Onderkant uitgelijnd met bodem van de tile
                double drawY = (y + 1) * cellSize - drawHeight;

                gc.drawImage(tile.getImage(), x * cellSize, drawY, drawWidth, drawHeight);
            }

            // -- Stap B: teken persons op rij y --
            for (Person p : persons) {
                if (p.getTileY() == y) {
                    p.draw(gc, cellSize);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // [DEBUG] Route-visualisatie per persoon
    // -------------------------------------------------------------------------

    private void drawDebugPaths(GraphicsContext gc, List<? extends Person> persons) {
        int    cellSize = grid.getCellSize();
        double half     = cellSize / 2.0;

        for (int i = 0; i < persons.size(); i++) {
            Person      p    = persons.get(i);
            List<int[]> path = p.getPath();
            if (path == null || path.isEmpty()) continue;

            Color color = PATH_COLORS[i % PATH_COLORS.length];

            gc.setStroke(color);
            gc.setLineWidth(2.5);

            double fromX = p.getTileX() * cellSize + half;
            double fromY = p.getTileY() * cellSize + half;

            for (int[] step : path) {
                double toX = step[0] * cellSize + half;
                double toY = step[1] * cellSize + half;
                gc.strokeLine(fromX, fromY, toX, toY);
                fromX = toX;
                fromY = toY;
            }

            gc.setFill(color);
            for (int[] step : path) {
                double cx = step[0] * cellSize + half;
                double cy = step[1] * cellSize + half;
                gc.fillOval(cx - 3, cy - 3, 6, 6);
            }

            int[]  goal = path.get(path.size() - 1);
            double gx   = goal[0] * cellSize + half;
            double gy   = goal[1] * cellSize + half;
            double arm  = cellSize * 0.35;

            gc.setStroke(color);
            gc.setLineWidth(2.0);
            gc.strokeLine(gx - arm, gy, gx + arm, gy);
            gc.strokeLine(gx, gy - arm, gx, gy + arm);
            gc.strokeOval(gx - arm, gy - arm, arm * 2, arm * 2);
        }
    }

    // -------------------------------------------------------------------------
    // [DEBUG] Grid overlay
    // -------------------------------------------------------------------------

    private void drawDebugOverlay(GraphicsContext gc) {
        int cellSize = grid.getCellSize();

        java.util.Map<String, Color> labelColors = new java.util.HashMap<>();
        labelColors.put("Checkout",  Color.color(0.2, 0.6, 1.0));
        labelColors.put("Queue",     Color.color(1.0, 0.6, 0.0));
        labelColors.put("Employee",  Color.color(0.2, 0.9, 0.2));
        labelColors.put("Access",    Color.color(0.8, 0.0, 0.8));
        labelColors.put("Shelf",     Color.color(0.6, 0.4, 0.1));
        labelColors.put("Entrance",  Color.color(0.0, 0.9, 0.7));
        labelColors.put("Exit",      Color.color(1.0, 0.3, 0.3));

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Tile tile     = grid.getTile(x, y);
                double pixelX = x * cellSize;
                double pixelY = y * cellSize;

                gc.setStroke(Color.DARKGRAY);
                gc.setLineWidth(0.5);
                gc.strokeRect(pixelX, pixelY, cellSize, cellSize);

                gc.setFill(Color.BLACK);
                gc.setFont(new Font("Arial", 7));
                gc.fillText("[" + x + "," + y + "]", pixelX + 1, pixelY + 9);

                if (!tile.isWalkable()) {
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(1.5);
                    double size   = 6;
                    double startX = pixelX + cellSize - size - 2;
                    double startY = pixelY + 2;
                    gc.strokeLine(startX, startY, startX + size, startY + size);
                    gc.strokeLine(startX + size, startY, startX, startY + size);
                }

                if (tile.isOccupied()) {
                    gc.setStroke(Color.YELLOW);
                    gc.setLineWidth(1.5);
                    double radius  = 3;
                    double centerX = pixelX + cellSize - radius - 3;
                    double centerY = pixelY + radius + 3;
                    gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                }

                String label = tile.getLabel();
                if (label != null) {
                    Color c = labelColors.getOrDefault(label, Color.WHITE);
                    gc.setFill(Color.color(c.getRed(), c.getGreen(), c.getBlue(), 0.25));
                    gc.fillRect(pixelX + 1, pixelY + 1, cellSize - 2, cellSize - 2);
                    gc.setStroke(c);
                    gc.setLineWidth(1.5);
                    gc.strokeRect(pixelX + 1, pixelY + 1, cellSize - 2, cellSize - 2);
                    gc.setFill(c);
                    gc.setFont(new Font("Arial", 6));
                    gc.fillText(label, pixelX + 2, pixelY + cellSize - 2);
                }
            }
        }
    }
}