package supermarket.simulator.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Shelves extends Tile {

    private List<Item> products;

    public Shelves(Image shelfImage) {
        super(false, Color.SADDLEBROWN, shelfImage);
        this.products = new ArrayList<>();
    }
}

