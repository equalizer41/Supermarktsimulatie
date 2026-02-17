package supermarket.simulator.model;

import javafx.scene.paint.Color;
import supermarket.simulator.model.world.Grid;
import supermarket.simulator.services.CharacterSpriteLoader;
import supermarket.simulator.Pathfinding.TileReservationSystem;

import java.util.LinkedList;
import java.util.Queue;

public class Customer extends Person {

    private static final int CUSTOMER_PRIORITY = 10;
    private static int nextId = 1;

    private final int id;
    private final Inventory inventory = new Inventory();
    private final Queue<int[]> destinations = new LinkedList<>();
    private boolean active = true;

    public Customer(int startX, int startY, Grid grid, CharacterSpriteLoader spriteLoader) {
        super(startX, startY, CUSTOMER_PRIORITY, grid, spriteLoader);
        this.id = nextId++;
        System.out.println("Customer #" + id + " spawned at [" + startX + "," + startY + "]");
    }

    @Override
    public void update(Grid grid) {
        super.update(grid);

        if (hasReachedGoal() && !destinations.isEmpty()) {
            moveToNextDestination();
        }

        if (hasReachedGoal() && destinations.isEmpty()) {
            leave();
        }
    }

    /**
     * Called when the customer has reached all destinations (including the exit).
     * Releases their tile reservation and marks them as inactive.
     */
    private void leave() {
        System.out.println("Customer #" + id + " has left the supermarket.");
        active = false;
        TileReservationSystem.getInstance().release(this);
    }

    public void addDestination(int x, int y) {
        destinations.add(new int[]{x, y});
    }

    public void startShopping() {
        moveToNextDestination();
    }

    private void moveToNextDestination() {
        int[] next = destinations.poll();
        if (next != null) {
            System.out.println("Customer #" + id + " moving to [" + next[0] + "," + next[1] + "]");
            moveTo(next[0], next[1]);
        }
    }

    @Override
    protected Color getFallbackColor() {
        return Color.BLUE;
    }

    public int getId()                            { return id; }
    public Inventory getInventory()               { return inventory; }
    public boolean isActive()                     { return active; }
    public void setActive(boolean active)         { this.active = active; }
}