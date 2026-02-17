package supermarket.simulator.model;

import javafx.scene.paint.Color;
import supermarket.simulator.model.world.Grid;
import supermarket.simulator.services.CharacterSpriteLoader;

import java.util.LinkedList;
import java.util.Queue;

public class Customer extends Person {

    private static final int CUSTOMER_PRIORITY = 10;

    private final Inventory inventory = new Inventory();
    private final Queue<int[]> destinations = new LinkedList<>();
    private boolean active = true;

    public Customer(int startX, int startY, Grid grid, CharacterSpriteLoader spriteLoader) {
        super(startX, startY, CUSTOMER_PRIORITY, grid, spriteLoader);
    }

    @Override
    public void update(Grid grid) {
        super.update(grid);

        if (hasReachedGoal() && !destinations.isEmpty()) {
            moveToNextDestination();
        }

        if (hasReachedGoal() && destinations.isEmpty()) {
            active = false;
        }
    }

    public void addDestination(int x, int y) {
        destinations.add(new int[]{x, y});
    }

    public void startShopping() {
        moveToNextDestination();
    }

    private void moveToNextDestination() {
        int[] next = destinations.poll();
        if (next != null) moveTo(next[0], next[1]);
    }

    @Override
    protected Color getFallbackColor() {
        return Color.BLUE;
    }

    public Inventory getInventory()               { return inventory; }
    public boolean isActive()                     { return active; }
    public void setActive(boolean active)         { this.active = active; }
}