package supermarket.simulator.model;

import supermarket.simulator.Pathfinding.*;

/**
 * Enhanced Customer class with A* pathfinding integration
 * Add these methods to your existing Customer class
 */
public class Customer {
    private int x;
    private int y;
    private Walker walker;
    private PathfindingManager pathfindingManager;
    private Inventory inventory;
    private CustomerState state;

    public enum CustomerState {
        ENTERING,       // Just entered store
        SHOPPING,       // Moving between shelves
        GOING_TO_CHECKOUT, // Going to checkout
        IN_QUEUE,       // Waiting in checkout queue
        LEAVING         // Exiting store
    }

    /**
     * Constructor
     * @param startX Starting X position
     * @param startY Starting Y position
     * @param pathfindingManager Reference to pathfinding manager
     */
    public Customer(int startX, int startY, PathfindingManager pathfindingManager) {
        this.x = startX;
        this.y = startY;
        this.walker = new Walker(startX, startY);
        this.pathfindingManager = pathfindingManager;
        this.inventory = new Inventory();
        this.state = CustomerState.ENTERING;
    }

    /**
     * Update customer behavior each tick
     * @param grid The game grid
     */
    public void update(Grid grid) {
        // Update walker movement
        walker.update();

        // Update position based on walker
        this.x = walker.getCurrentX();
        this.y = walker.getCurrentY();

        // Behavior based on state
        switch (state) {
            case ENTERING:
                if (walker.getState() == Walker.WalkerState.IDLE) {
                    // Start shopping
                    state = CustomerState.SHOPPING;
                }
                break;

            case SHOPPING:
                if (walker.getState() == Walker.WalkerState.REACHED_GOAL) {
                    // Reached a shelf, pick up item, then go to next shelf
                    System.out.println("Customer reached shelf, picking up item");
                    // After shopping, go to checkout
                    state = CustomerState.GOING_TO_CHECKOUT;
                }
                break;

            case GOING_TO_CHECKOUT:
                if (walker.getState() == Walker.WalkerState.REACHED_GOAL) {
                    state = CustomerState.IN_QUEUE;
                    System.out.println("Customer reached checkout queue");
                }
                break;

            case IN_QUEUE:
                // Waiting for checkout to process
                // (Checkout will handle this)
                break;

            case LEAVING:
                if (walker.getState() == Walker.WalkerState.REACHED_GOAL) {
                    System.out.println("Customer left the store");
                    // Remove customer from simulation
                }
                break;
        }
    }

    /**
     * Move to a specific location
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     */
    public void moveTo(int targetX, int targetY) {
        Path path = pathfindingManager.findPath(x, y, targetX, targetY);

        if (path != null) {
            walker.setPath(path);
            System.out.println("Customer pathfinding from (" + x + "," + y + ") to (" +
                    targetX + "," + targetY + ")");
            path.printPath();
        } else {
            System.out.println("Customer cannot find path to (" + targetX + "," + targetY + ")");
        }
    }

    /**
     * Move to nearest target from list
     * @param targets Array of target coordinates [[x, y], ...]
     */
    public void moveToNearest(int[][] targets) {
        Path path = pathfindingManager.findPathToNearest(x, y, targets);

        if (path != null) {
            walker.setPath(path);
            System.out.println("Customer moving to nearest target");
        } else {
            System.out.println("Customer cannot find path to any target");
        }
    }

    /**
     * Go to a specific shelf
     * @param shelf Target shelf
     */
    public void goToShelf(Shelf shelf) {
        moveTo(shelf.getX(), shelf.getY());
    }

    /**
     * Go to a specific checkout
     * @param checkout Target checkout
     */
    public void goToCheckout(Checkout checkout) {
        moveTo(checkout.getX(), checkout.getY());
        state = CustomerState.GOING_TO_CHECKOUT;
    }

    /**
     * Go to exit
     * @param exit Exit location
     */
    public void goToExit(Exit exit) {
        moveTo(exit.getX(), exit.getY());
        state = CustomerState.LEAVING;
    }

    /**
     * Stop moving
     */
    public void stop() {
        walker.stop();
    }

    /**
     * Set movement speed
     * @param speed Speed in tiles per tick
     */
    public void setSpeed(double speed) {
        walker.setSpeed(speed);
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Walker getWalker() {
        return walker;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public CustomerState getState() {
        return state;
    }

    public boolean isMoving() {
        return walker.isMoving();
    }

    public void setActive(boolean active) {
        // Implementation for removing customer
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        walker.setPosition(x, y);
    }
}
