package supermarket.simulator.model;

import javafx.scene.image.Image;

public class Freezer extends Refrigerator{

    public Freezer(int x, int y, int width, int height, Image sprite, RefrigeratorType type, int maxCapacity) {
        super(x, y, width, height, sprite, type, maxCapacity);
    }
}
