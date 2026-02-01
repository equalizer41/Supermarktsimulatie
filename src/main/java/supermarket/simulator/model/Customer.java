package supermarket.simulator.model;

import supermarket.simulator.model.Inventory;
import supermarket.simulator.model.HasInventory;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Customer extends Person implements HasInventory {
    private Inventory inventory;
    private CustomerState state = CustomerState.ENTERING;
    private List<int[]> route = new ArrayList<>();
    private int routeIndex = 0;

    public Customer(int x, int y, Color color) {
        super(x, y, color);
        this.inventory = new Inventory(new ArrayList<>()); // lege lijst meegeven

    }

    public enum CustomerState {
        ENTERING,
        SHOPPING,
        CHECKOUT,
        DONE
    }

    @Override
    public void update(Grid grid) {
        switch (state) {
            case ENTERING -> moveTowardsEntrance(grid);
            case SHOPPING -> followRoute(grid);
            case CHECKOUT -> moveToCheckout(grid);
            case DONE -> {
            } // niets meer doen
        }
    }
    private void moveTowardsEntrance(Grid grid) {
        int targetX = 0;
        int targetY = 0;

        moveTowards(targetX, targetY, grid);

        if (atPosition(targetX, targetY)) {
            System.out.println("Klant is binnen!");

            // Voorbeeldroute: een paar Tiles door de winkel
            route.clear();
            route.add(new int[]{1, 0});
            route.add(new int[]{2, 0});
            route.add(new int[]{3, 1});
            route.add(new int[]{5, 2});
            route.add(new int[]{5, 4});
            routeIndex = 0;
            state = CustomerState.SHOPPING;
        }
    }

    private void followRoute(Grid grid) {
        if (routeIndex < route.size()) {
            int[] target = route.get(routeIndex);
            moveTowards(target[0], target[1], grid);

            if (atPosition(target[0], target[1])) {
                System.out.println("Klant bij tile (" + target[0] + "," + target[1] + ")");
                routeIndex++;
            }
        } else {
            System.out.println("Klant klaar met winkelen!");
            state = CustomerState.CHECKOUT;
        }
    }

    private void moveToCheckout(Grid grid) {
        int targetX = 2;
        int targetY = 6;
        moveTowards(targetX, targetY, grid);

        if (atPosition(targetX, targetY)) {
            System.out.println("Klant is bij de kassa!");
            state = CustomerState.DONE;
        }
    }

    // ----------------------
    // Hulpfuncties
    // ----------------------

    private void moveTowards(int targetX, int targetY, Grid grid) {
        int nextX = x;
        int nextY = y;

        if (x < targetX) nextX++;
        else if (x > targetX) nextX--;
        else if (y < targetY) nextY++;
        else if (y > targetY) nextY--;

        if (isInsideGrid(nextX, nextY, grid) && grid.getTile(nextX, nextY).isWalkable()) {
            x = nextX;
            y = nextY;
        }
    }

    private boolean atPosition(int tx, int ty) {
        return x == tx && y == ty;
    }

    private boolean isInsideGrid(int x, int y, Grid grid) {
        return x >= 0 && y >= 0 && x < grid.getWidth() && y < grid.getHeight();
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
