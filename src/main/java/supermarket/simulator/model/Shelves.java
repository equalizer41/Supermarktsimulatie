package supermarket.simulator.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Shelves extends Tile {
    private List<Item> products;

    public Shelves(Image shelfImage) {
        super(false, Color.SADDLEBROWN, shelfImage,1,2);
        this.products = new ArrayList<>();
    }

    public void addProduct(Item item) {
        products.add(item);
    }

    public void removeProduct(Item item) {
        products.remove(item);
    }

    public List<Item> getProducts() {
        return new ArrayList<>(products);
    }

    public boolean hasProduct(Item item) {
        return products.contains(item);
    }
}
