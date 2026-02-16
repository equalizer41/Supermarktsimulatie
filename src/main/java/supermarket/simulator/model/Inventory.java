package supermarket.simulator.model;

import java.util.List;
import java.util.ArrayList;

public class Inventory {
    private List<Item> items;

    // Nieuwe lege constructor
    public Inventory() {
        this.items = new ArrayList<>();
    }

    // Bestaande constructor
    public Inventory(List<Item> items) {
        this.items = new ArrayList<>(items);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public boolean hasItem(Item item) {
        return items.contains(item);
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public int getItemCount() {
        return items.size();
    }

    public double getTotalValue() {
        return items.stream().mapToDouble(Item::getPrice).sum();
    }

    public void clear() {
        items.clear();
    }
}