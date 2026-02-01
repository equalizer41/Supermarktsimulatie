package supermarket.simulator.model;

import supermarket.simulator.model.Item;
import java.util.List;
import java.util.ArrayList;

public class Inventory {
    private List<Item> items;

    public Inventory(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) { items.add(item); }
    public void removeItem(Item item) { items.remove(item); }
    public boolean hasItem(Item item) { return items.contains(item); }

    public List<Item> getItems() { return items; }
}
