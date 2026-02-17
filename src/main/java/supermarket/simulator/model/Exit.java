package supermarket.simulator.model;

import javafx.scene.image.Image;
import supermarket.simulator.model.world.Grid;
import supermarket.simulator.model.world.Tile;

import java.util.ArrayList;
import java.util.List;

public class Exit extends SupermarketObject {

    private static final int MAX_PER_TICK = 3;

    private int totalExited = 0;
    private final List<Customer> exitQueue = new ArrayList<>();
    private boolean open = true;

    public Exit(int x, int y, Image sprite) {
        super(x, y, 1, 1, true, sprite, "Exit");
    }

    @Override
    public void placeOnGrid(Grid grid) {
        grid.setTile(x, y, new Tile(true, sprite, 1, 1));
    }

    @Override
    public void onInteract(Customer customer) {
        if (open) exitCustomer(customer);
        else exitQueue.add(customer);
    }

    @Override
    public void update() {
        int processed = 0;
        while (open && !exitQueue.isEmpty() && processed < MAX_PER_TICK) {
            exitCustomer(exitQueue.remove(0));
            processed++;
        }
    }

    @Override
    public int getAccessX() { return x; }

    @Override
    public int getAccessY() { return y - 1; } // tile BOVEN de exit, want exit is onderste rij

    private void exitCustomer(Customer customer) {
        totalExited++;
        customer.setActive(false);
        System.out.println("Customer #" + totalExited + " exited the supermarket");
    }

    public boolean hasReached(Customer customer) {
        return customer.getTileX() == x && customer.getTileY() == y;
    }

    public void open()  { open = true; }
    public void close() { open = false; }

    public boolean isOpen()          { return open; }
    public int getTotalExited()      { return totalExited; }
    public int getExitQueueSize()    { return exitQueue.size(); }
}