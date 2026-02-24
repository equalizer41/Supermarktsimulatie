package supermarket.simulator.model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Inventory {
    private final Map<Item, Integer> items;

    //  lege constructor
    public Inventory() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item) {
        items.put(item, items.getOrDefault(item, 0) + 1);
    }

    public void removeItem(Item item) {
        if (items.containsKey(item)) {
            int count = items.get(item);
            if (count <= 1) {
                items.remove(item);  // helemaal weghalen
            } else {
                items.put(item, count - 1);  // één minder
            }
        }
    }
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }
}