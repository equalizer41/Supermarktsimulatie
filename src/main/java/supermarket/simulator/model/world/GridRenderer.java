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
        drawLargeTiles(gc);

        if (DEBUG && DEBUG_PATHS) {
            drawDebugPaths(gc, persons);    // onder sprites tekenen
        }

        drawPersons(gc, persons);

        if (DEBUG && DEBUG_GRID) {
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
    // [DEBUG] Fase 3b: Route-visualisatie per persoon
    // -------------------------------------------------------------------------

    private void drawDebugPaths(GraphicsContext gc, List<? extends Person> persons) {
        int    cellSize = grid.getCellSize();
        double half     = cellSize / 2.0;

        for (int i = 0; i < persons.size(); i++) {
            Person      p    = persons.get(i);
            List<int[]> path = p.getPath();
            if (path == null || path.isEmpty()) continue;

            Color color = PATH_COLORS[i % PATH_COLORS.length];

            // --- Lijn van huidige positie door alle stappen ---
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

            // --- Stip op elke tussenstap ---
            gc.setFill(color);
            for (int[] step : path) {
                double cx = step[0] * cellSize + half;
                double cy = step[1] * cellSize + half;
                gc.fillOval(cx - 3, cy - 3, 6, 6);
            }

            // --- Kruis-in-cirkel op het doel ---
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
    // [DEBUG] Fase 4: Grid overlay (grid lijnen, coordinaten, walkable/occupied)
    // -------------------------------------------------------------------------

    private void drawDebugOverlay(GraphicsContext gc) {
        int cellSize = grid.getCellSize();

        // Label-kleur per naam
        java.util.Map<String, Color> labelColors = new java.util.HashMap<>();
        labelColors.put("Checkout",  Color.color(0.2, 0.6, 1.0));   // blauw
        labelColors.put("Queue",     Color.color(1.0, 0.6, 0.0));   // oranje
        labelColors.put("Employee",  Color.color(0.2, 0.9, 0.2));   // groen
        labelColors.put("Access",    Color.color(0.8, 0.0, 0.8));   // paars
        labelColors.put("Shelf",     Color.color(0.6, 0.4, 0.1));   // bruin
        labelColors.put("Entrance",  Color.color(0.0, 0.9, 0.7));   // teal
        labelColors.put("Exit",      Color.color(1.0, 0.3, 0.3));   // rood

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

                // ← Nieuw: gekleurd label + gekleurde rand als tile een label heeft
                String label = tile.getLabel();
                if (label != null) {
                    Color c = labelColors.getOrDefault(label, Color.WHITE);

                    // Gekleurde semi-transparante achtergrond
                    gc.setFill(Color.color(c.getRed(), c.getGreen(), c.getBlue(), 0.25));
                    gc.fillRect(pixelX + 1, pixelY + 1, cellSize - 2, cellSize - 2);

                    // Gekleurde rand
                    gc.setStroke(c);
                    gc.setLineWidth(1.5);
                    gc.strokeRect(pixelX + 1, pixelY + 1, cellSize - 2, cellSize - 2);

                    // Label tekst onderaan de tile
                    gc.setFill(c);
                    gc.setFont(new Font("Arial", 6));
                    gc.fillText(label, pixelX + 2, pixelY + cellSize - 2);
                }
            }
        }
    }
}