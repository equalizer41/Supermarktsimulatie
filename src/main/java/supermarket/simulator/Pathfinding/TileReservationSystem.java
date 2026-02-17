package supermarket.simulator.Pathfinding;

import supermarket.simulator.model.Person;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages tile reservations to prevent multiple agents on the same tile.
 * Agents reserve their next tile before moving to it.
 */
public class TileReservationSystem {

    private static final TileReservationSystem INSTANCE = new TileReservationSystem();

    // Maps tile key "x,y" -> agent that reserved it
    private final Map<String, Person> reservations = new HashMap<>();

    private TileReservationSystem() {}

    public static TileReservationSystem getInstance() {
        return INSTANCE;
    }

    /**
     * Attempt to reserve a tile for an agent.
     * @return true if reservation succeeded, false if tile already reserved by another agent
     */
    public boolean reserve(int x, int y, Person agent) {
        String key = key(x, y);
        Person current = reservations.get(key);
        if (current == null || current == agent) {
            reservations.put(key, agent);
            return true;
        }
        return false;
    }

    /**
     * Release all tiles reserved by this agent.
     */
    public void release(Person agent) {
        reservations.values().removeIf(owner -> owner == agent);
    }

    /**
     * Check if a tile is free (not reserved by another agent).
     */
    public boolean isFree(int x, int y, Person agent) {
        Person current = reservations.get(key(x, y));
        return current == null || current == agent;
    }

    private String key(int x, int y) {
        return x + "," + y;
    }
}