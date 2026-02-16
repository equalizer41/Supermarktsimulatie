package supermarket.simulator.model;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Customer extends Person implements HasInventory {
    private Inventory inventory;
    private CustomerState state = CustomerState.ENTERING;
    private List<int[]> route = new ArrayList<>();
    private int routeIndex = 0;
    private int targetX;
    private int targetY;

    public Customer(int x, int y, Color color) {
        super(x, y, color);
        this.inventory = new Inventory(new ArrayList<>());
        this.targetX = x;
        this.targetY = y;
    }

    public enum CustomerState {
        ENTERING,
        SHOPPING,
        CHECKOUT,
        LEAVING,
        DONE
    }

    @Override
    public void update(Grid grid) {

    }




    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public CustomerState getState() {
        return state;
    }

    public boolean isDone() {
        return state == CustomerState.DONE;
    }
}
