package supermarket.simulator.model.world;

import javafx.scene.image.Image;
import supermarket.simulator.model.Checkout;
import supermarket.simulator.model.Refrigerator;
import supermarket.simulator.model.Shelf;
import supermarket.simulator.model.StorageShelf;
import supermarket.simulator.services.TilesetLoader;

import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder {

    private Grid grid;
    private TilesetLoader loader;

    // Collections of supermarket objects
    private List<Shelf> shelves;
    private List<Checkout> checkouts;
    private List<Refrigerator> refrigerators;
    private List<StorageShelf> storageshelves;

    public ObjectBuilder(Grid grid, TilesetLoader loader){
        this.grid = grid;
        this.loader = loader;
        this.shelves = new ArrayList<>();
        this.checkouts = new ArrayList<>();
        this.refrigerators = new ArrayList<>();
        this.storageshelves = new ArrayList<>();
    }
    public void buildObjects(){
        placeCheckouts();
        placeRefrigerators();
        placeShelves();
        placeStorageShelves();
    }

    private void placeStorageShelves() {
        Image storage_rack = loader.getNamedTile("storage_rack");

        StorageShelf storageShelf1 = new StorageShelf(3, 3, 4, 1, storage_rack);
        StorageShelf storageShelf2 = new StorageShelf(3, 5, 4, 1, storage_rack);

        StorageShelf storageShelf3 = new StorageShelf(13, 3, 4, 1, storage_rack);
        StorageShelf storageShelf4 = new StorageShelf(13, 5, 4, 1, storage_rack);

        storageShelf1.placeOnGrid(grid);
        storageShelf2.placeOnGrid(grid);
        storageShelf3.placeOnGrid(grid);
        storageShelf4.placeOnGrid(grid);


    }

    /**
     * Places checkout counters using the Checkout class
     */
    private void placeCheckouts() {
        Image checkoutSprite = loader.getNamedTile("checkout");
        int width = grid.getWidth();
        int height = grid.getHeight();
        int checkoutRow = height - 5;
        int centerX = width / 2;

        // Create three checkout objects
        Checkout checkout1 = new Checkout(centerX - 6, checkoutRow, checkoutSprite, 1);
        Checkout checkout2 = new Checkout(centerX - 3, checkoutRow, checkoutSprite, 2);
        Checkout checkout3 = new Checkout(centerX, checkoutRow, checkoutSprite, 3);

        // Place them on the grid
        checkout1.placeOnGrid(grid);
        checkout2.placeOnGrid(grid);
        checkout3.placeOnGrid(grid);

        // Add to list for management
        checkouts.add(checkout1);
        checkouts.add(checkout2);
        checkouts.add(checkout3);
    }

    /**
     * Places refrigerators in the supermarket
     */
    private void placeRefrigerators() {
        // Example placement - you can customize this
        Image fridgeSprite = loader.getNamedTile("fridge"); // Replace with actual fridge sprite
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Place frozen section on the left
        Refrigerator frozen1 = new Refrigerator(2, 8, fridgeSprite, Refrigerator.RefrigeratorType.FROZEN);
        frozen1.placeOnGrid(grid);
        refrigerators.add(frozen1);

        Refrigerator frozen2 = new Refrigerator(5, 8, fridgeSprite, Refrigerator.RefrigeratorType.FROZEN);
        frozen2.placeOnGrid(grid);
        refrigerators.add(frozen2);

        // Place chilled section on the right
        Refrigerator chilled1 = new Refrigerator(width - 6, 8, fridgeSprite, Refrigerator.RefrigeratorType.CHILLED);
        chilled1.placeOnGrid(grid);
        refrigerators.add(chilled1);
    }

    /**
     * Places shelves throughout the supermarket
     */
    private void placeShelves() {
        Image shelfSprite = loader.getNamedTile("vertshelf"); // Replace with actual shelf sprite
        int width = grid.getWidth();
        int height = grid.getHeight();
        int startY = 12;

        // Create produce shelves
        Shelf produceShelf1 = new Shelf(8, startY, shelfSprite, "produce");
        produceShelf1.placeOnGrid(grid);
        shelves.add(produceShelf1);

        Shelf produceShelf2 = new Shelf(11, startY, shelfSprite, "produce");
        produceShelf2.placeOnGrid(grid);
        shelves.add(produceShelf2);

        // Create bakery shelves
        Shelf bakeryShelf = new Shelf(14, startY, shelfSprite, "bakery");
        bakeryShelf.placeOnGrid(grid);
        shelves.add(bakeryShelf);

        // Create general shelves
        Shelf generalShelf = new Shelf(17, startY, shelfSprite, "general");
        generalShelf.placeOnGrid(grid);
        shelves.add(generalShelf);
    }


    // Getters for accessing the objects
    public List<Shelf> getShelves() {
        return new ArrayList<>(shelves);
    }

    public List<Checkout> getCheckouts() {
        return new ArrayList<>(checkouts);
    }

    public List<Refrigerator> getRefrigerators() {
        return new ArrayList<>(refrigerators);
    }

    public List<StorageShelf> getStorageRooms() {
        return new ArrayList<>(storageshelves);
    }

    /**
     * Updates all supermarket objects (called each tick)
     */
    public void updateAll() {
        // Update checkouts
        for (Checkout checkout : checkouts) {
            checkout.update();
        }

        // Update refrigerators
        for (Refrigerator fridge : refrigerators) {
            fridge.update();
        }


    }
}




