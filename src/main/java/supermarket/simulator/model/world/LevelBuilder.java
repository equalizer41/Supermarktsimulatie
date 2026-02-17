package supermarket.simulator.model.world;

import supermarket.simulator.model.*;
import javafx.scene.image.Image;
import supermarket.simulator.services.TilesetLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * LevelBuilder that uses object-oriented supermarket objects
 * Manages all supermarket fixtures: shelves, checkouts, refrigerators, storage, entrance, and exit
 */
public class LevelBuilder {
    private Grid grid;
    private TilesetLoader loader;

    // Collections of supermarket objects
    private List<Shelf> shelves;
    private List<Checkout> checkouts;
    private List<Refrigerator> refrigerators;
    private List<Storage> storageRooms;
    private Entrance entrance;
    private Exit exit;

    public LevelBuilder(Grid grid, TilesetLoader loader) {
        this.grid = grid;
        this.loader = loader;
        this.shelves = new ArrayList<>();
        this.checkouts = new ArrayList<>();
        this.refrigerators = new ArrayList<>();
        this.storageRooms = new ArrayList<>();
    }

    /**
     * Builds the complete level with all components
     */
    public void buildLevel() {
        fillFloor();
        buildWalls();
        placeEntranceAndExit();
        placeCheckouts();
        placeRefrigerators();
        placeShelves();
        placeStorage();
    }

    /**
     * Fills the entire grid with floor tiles
     */
    private void fillFloor() {
        Image floor = loader.getNamedTile("floor");
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid.setTile(x, y, new Tile(true, floor, 1, 1));
            }
        }
    }

    /**
     * Builds all wall components
     */
    private void buildWalls() {
        buildCorners();
        buildTopWall();
        buildBottomWalls();
        buildSideWalls();
    }

    private void buildCorners() {
        Image wallcorner_topright = loader.getNamedTile("wallcorner_topright");
        Image wallcorner_topleft = loader.getNamedTile("wallcorner_topleft");
        Image wallcorner_bottomright = loader.getNamedTile("wallcorner_bottomright");
        Image wallcorner_bottomleft = loader.getNamedTile("wallcorner_bottomleft");

        int width = grid.getWidth();
        int height = grid.getHeight();

        grid.setTile(0, 0, new Tile(false, wallcorner_topleft, 1, 1));
        grid.setTile(width - 1, 0, new Tile(false, wallcorner_topright, 1, 1));
        grid.setTile(0, height - 1, new Tile(false,  wallcorner_bottomleft, 1, 1));
        grid.setTile(width - 1, height - 1, new Tile(false,  wallcorner_bottomright, 1, 1));
    }

    private void buildTopWall() {
        Image wall_top = loader.getNamedTile("wall_top");
        int width = grid.getWidth();

        for (int x = 1; x < width - 1; x++) {
            grid.setTile(x, 0, new Tile(false, wall_top, 1, 1));
        }
    }

    private void buildBottomWalls() {
        Image wall_bottom_top = loader.getNamedTile("wall_bottom_top");
        Image wall_bottom_ground = loader.getNamedTile("wall_bottom_ground");
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int x = 1; x < width - 1; x++) {
            grid.setTile(x, height - 2, new Tile(true, wall_bottom_top, 1, 1));
        }

        for (int x = 1; x < width - 1; x++) {
            grid.setTile(x, height - 1, new Tile(false, wall_bottom_ground, 1, 1));
        }
    }

    private void buildSideWalls() {
        Image wall_left = loader.getNamedTile("wall_left");
        Image wall_right = loader.getNamedTile("wall_right");
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int y = 1; y < height - 1; y++) {
            grid.setTile(0, y, new Tile(false, wall_left, 1, 1));
        }

        for (int y = 1; y < height - 1; y++) {
            grid.setTile(width - 1, y, new Tile(false,  wall_right, 1, 1));
        }
    }

    /**
     * Places entrance and exit using the new object classes
     */
    private void placeEntranceAndExit() {
        Image exitSprite = loader.getNamedTile("exit");
        Image entranceSprite = loader.getNamedTile("entrance");
        int width = grid.getWidth();
        int height = grid.getHeight();
        int bottomRow = height - 1;

        // Create and place Exit object
        exit = new Exit(3, bottomRow, exitSprite);
        exit.placeOnGrid(grid);

        // Create and place Entrance object
        entrance = new Entrance(width - 4, bottomRow, entranceSprite);
        entrance.placeOnGrid(grid);
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
        Refrigerator frozen1 = new Refrigerator(5, 5, fridgeSprite, Refrigerator.RefrigeratorType.FROZEN);
        frozen1.placeOnGrid(grid);
        refrigerators.add(frozen1);

        // Place chilled section on the right
        Refrigerator chilled1 = new Refrigerator(width - 6, 5, fridgeSprite, Refrigerator.RefrigeratorType.CHILLED);
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
        int startY = 8;

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

    /**
     * Places storage room in the back
     */
    private void placeStorage() {
        Image storageSprite = loader.getNamedTile("storage");
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Place storage in back corner
        Storage mainStorage = new Storage(1, 1, 18 , 4, storageSprite, 1000);
        mainStorage.placeOnGrid(grid);
        storageRooms.add(mainStorage);
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

    public List<Storage> getStorageRooms() {
        return new ArrayList<>(storageRooms);
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public Exit getExit() {
        return exit;
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

        // Update entrance and exit
        if (entrance != null) {
            entrance.update();
        }
        if (exit != null) {
            exit.update();
        }
    }
}