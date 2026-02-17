package supermarket.simulator.model;

import supermarket.simulator.model.enums.Direction;
import supermarket.simulator.Pathfinding.AStarPathfinder;
import supermarket.simulator.Pathfinding.TileReservationSystem;
import supermarket.simulator.model.world.Grid;
import supermarket.simulator.services.CharacterSpriteLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Abstract base class for all persons (Customer, Employee).
 * Features:
 *  - A* pathfinding
 *  - Smooth sub-tile animation (ANIMATION_SPEED tiles per tick)
 *  - Reservation system: strictly 1 agent per tile
 *  - Anti-deadlock (3 stages): wait → reroute → sidestep
 *  - Priority: higher value = more important (Customer > Employee)
 *  - Directional sprite animation via CharacterSpriteLoader
 */
public abstract class Person {

    // Movement constants - no magic numbers
    private static final double ANIMATION_SPEED      = 1; // tiles per tick
    private static final int    TICKS_UNTIL_REROUTE  = 20;
    private static final int    TICKS_UNTIL_SIDESTEP = 40;
    private static final int    ANIM_SPEED           = 4;   // ticks per frame wissel

    // Direction arrays for 4-way movement (N, E, S, W)
    private static final int[] DX = {0, 1, 0, -1};
    private static final int[] DY = {-1, 0, 1, 0};

    // Tile position
    protected int tileX;
    protected int tileY;

    // Smooth visual position (sub-tile interpolation)
    private double visualX;
    private double visualY;

    // Pathfinding
    private final AStarPathfinder       pathfinder;
    private final TileReservationSystem reservations;
    private List<int[]> path;
    private int goalX;
    private int goalY;
    private boolean hasGoal;

    // Deadlock counter
    private int blockedTicks;

    // Rendering
    protected final CharacterSpriteLoader spriteLoader;
    protected final int priority;

    // Sprite animation
    private Direction direction = Direction.DOWN;
    private int animFrame       = 0;
    private int animTimer       = 0;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    protected Person(int startX, int startY, int priority, Grid grid, CharacterSpriteLoader spriteLoader) {
        this.tileX        = startX;
        this.tileY        = startY;
        this.visualX      = startX;
        this.visualY      = startY;
        this.priority     = priority;
        this.spriteLoader = spriteLoader;
        this.pathfinder   = new AStarPathfinder(grid);
        this.reservations = TileReservationSystem.getInstance();
        this.hasGoal      = false;
        this.blockedTicks = 0;

        reservations.reserve(tileX, tileY, this);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /** Set a new movement goal and immediately calculate the A* path. */
    public void moveTo(int goalX, int goalY) {
        this.goalX        = goalX;
        this.goalY        = goalY;
        this.hasGoal      = true;
        this.blockedTicks = 0;
        this.path         = pathfinder.findPath(tileX, tileY, goalX, goalY);
    }

    /** Call once per game tick. */
    public void update(Grid grid) {
        animateVisualPosition();
        updateDirection();
        updateAnimFrame();

        if (!hasGoal || path == null || path.isEmpty()) return;
        if (!isAnimationFinished()) return;

        tryStep(grid);
    }

    /** Draw the person using their current visual (interpolated) position. */
    public void draw(GraphicsContext gc, int tileSize) {
        double drawX = visualX * tileSize;
        double drawY = visualY * tileSize;

        if (spriteLoader != null) {
            gc.drawImage(spriteLoader.getFrame(direction, animFrame), drawX, drawY, tileSize, tileSize);
        } else {
            gc.setFill(getFallbackColor());
            gc.fillOval(drawX, drawY, tileSize, tileSize);
        }
    }

    /** True when this person has arrived at their goal tile and animation is done. */
    public boolean hasReachedGoal() {
        return hasGoal && tileX == goalX && tileY == goalY && isAnimationFinished();
    }

    public int getTileX()    { return tileX; }
    public int getTileY()    { return tileY; }
    public int getPriority() { return priority; }

    // -------------------------------------------------------------------------
    // Abstract
    // -------------------------------------------------------------------------

    protected abstract Color getFallbackColor();

    // -------------------------------------------------------------------------
    // Movement & deadlock
    // -------------------------------------------------------------------------

    private void tryStep(Grid grid) {
        int[] next = path.getFirst();
        int nx = next[0], ny = next[1];

        if (reservations.isFree(nx, ny, this)) {
            step(nx, ny);
        } else {
            blockedTicks++;
            resolveDeadlock(grid);
        }
    }

    private void step(int nx, int ny) {
        reservations.release(this);
        reservations.reserve(nx, ny, this);
        tileX = nx;
        tileY = ny;
        path.removeFirst();
        blockedTicks = 0;
    }

    private void resolveDeadlock(Grid grid) {
        if (blockedTicks == TICKS_UNTIL_REROUTE) {
            // Stage 1: find alternative route
            path = pathfinder.findPath(tileX, tileY, goalX, goalY);

        } else if (blockedTicks >= TICKS_UNTIL_SIDESTEP) {
            // Stage 2: force a sidestep onto any free adjacent tile
            sidestep(grid);
            blockedTicks = 0;
        }
        // Stage 0 (< TICKS_UNTIL_REROUTE): just wait
    }

    private void sidestep(Grid grid) {
        for (int i = 0; i < DX.length; i++) {
            int sx = tileX + DX[i];
            int sy = tileY + DY[i];

            if (isWalkable(grid, sx, sy) && reservations.isFree(sx, sy, this)) {
                step(sx, sy);
                path = pathfinder.findPath(tileX, tileY, goalX, goalY);
                return;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Sprite animation
    // -------------------------------------------------------------------------

    private void updateDirection() {
        if (path == null || path.isEmpty()) return;
        int[] next = path.getFirst();
        int dx = next[0] - tileX;
        int dy = next[1] - tileY;

        if      (dx > 0) direction = Direction.RIGHT;
        else if (dx < 0) direction = Direction.LEFT;
        else if (dy > 0) direction = Direction.DOWN;
        else if (dy < 0) direction = Direction.UP;
    }

    private void updateAnimFrame() {
        if (path != null && !path.isEmpty()) {
            animTimer++;
            if (animTimer >= ANIM_SPEED) {
                animTimer = 0;
                animFrame++;
            }
        } else {
            // Stilstaand: reset naar eerste frame
            animFrame = 0;
            animTimer = 0;
        }
    }

    // -------------------------------------------------------------------------
    // Smooth animation
    // -------------------------------------------------------------------------

    private void animateVisualPosition() {
        visualX = moveToward(visualX, tileX);
        visualY = moveToward(visualY, tileY);
    }

    private double moveToward(double current, double target) {
        if (Math.abs(current - target) <= ANIMATION_SPEED) return target;
        return current + Math.signum(target - current) * ANIMATION_SPEED;
    }

    private boolean isAnimationFinished() {
        return visualX == tileX && visualY == tileY;
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private boolean isWalkable(Grid grid, int x, int y) {
        if (x < 0 || x >= grid.getWidth() || y < 0 || y >= grid.getHeight()) return false;
        var tile = grid.getTile(x, y);
        return tile != null && tile.isWalkable();
    }
}