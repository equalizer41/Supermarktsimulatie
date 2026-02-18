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
    private List<StorageShelf> storageshelves;
    private Entrance entrance;
    private Exit exit;

    // Storage configuration
    private static final int STORAGE_X = 1;
    private static final int STORAGE_Y = 1;
    private static final int STORAGE_WIDTH = 18;
    private static final int STORAGE_HEIGHT = 4;
    private static final int STORAGE_DOORWAY_WIDTH = 3;
    private static final int STORAGE_START_X = 1;
    private static final int STORAGE_START_Y = 1;
    private static final int STORAGE_END_X   = 18;
    private static final int STORAGE_BG_START_X  = 1;
    private static final int STORAGE_BG_END_X    = 18;
    private static final int STORAGE_BG_START_Y  = 2;
    private static final int STORAGE_BG_END_Y    = 6;
    private static final int STORAGE_ENTRANCE_Y  = 7;
    private static final int STORAGE_ENTRANCE_X1 = 8;
    private static final int STORAGE_ENTRANCE_X2 = 11;
    private static final int STORAGE_ENTRANCE_LEFTCORNER = 8;
    private static final int STORAGE_ENTRANCE_RIGHTCORNER = 12;



    public LevelBuilder(Grid grid, TilesetLoader loader) {
        this.grid = grid;
        this.loader = loader;
        this.shelves = new ArrayList<>();
        this.checkouts = new ArrayList<>();
        this.refrigerators = new ArrayList<>();
        this.storageshelves = new ArrayList<>();

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
        placeStorageShelves();

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
        buildStorageWalls();
        buildStorageBackground();
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

    private void buildStorageWalls() {
        Image wall_storage = loader.getNamedTile("wall_storage");
        Image background_storage = loader.getNamedTile("background_storage");
        // Muur plaatsen 2 rijen onder de storage
        int wallY = STORAGE_Y + STORAGE_HEIGHT + 2;

        // Bereken de ingang: 3 tiles breed, gecentreerd
        int storageCenter = STORAGE_X + STORAGE_WIDTH / 2;
        int doorwayStart = storageCenter - STORAGE_DOORWAY_WIDTH / 2;
        int doorwayEnd = doorwayStart + STORAGE_DOORWAY_WIDTH - 1;

        // Bouw de muur van STORAGE_X tot STORAGE_X + STORAGE_WIDTH - 1
        for (int x = STORAGE_X; x < STORAGE_X + STORAGE_WIDTH; x++) {
            // Skip de ingang-tiles
            if (x >= doorwayStart && x <= doorwayEnd) continue;

            grid.setTile(x, wallY, new Tile(false, wall_storage, 1, 1));
        }
        for (int x = STORAGE_START_X; x <= STORAGE_END_X; x++) {
            grid.setTile(x, STORAGE_START_Y, new Tile(false, background_storage, 1, 1));
        }

    }
    private void buildStorageBackground() {
        Image storage_floor       = loader.getNamedTile("storage_floor");
        Image storage_entrance = loader.getNamedTile("storage_entrance");
        Image storage_entrance_rightside = loader.getNamedTile("storage_entrance_rightside");
        Image storage_entrance_leftside = loader.getNamedTile("storage_entrance_leftside");

        // Vul het rechthoekige achtergrondgebied
        for (int y = STORAGE_BG_START_Y; y <= STORAGE_BG_END_Y; y++) {
            for (int x = STORAGE_BG_START_X; x <= STORAGE_BG_END_X; x++) {
                grid.setTile(x, y, new Tile(true, storage_floor, 1, 1));
            }
        }

        // Plaats de 3 entrance tiles op rij 7
        for (int x = STORAGE_ENTRANCE_X1; x <= STORAGE_ENTRANCE_X2; x++) {
            grid.setTile(x, STORAGE_ENTRANCE_Y, new Tile(true, storage_entrance, 1, 1));
        }

        grid.setTile(STORAGE_ENTRANCE_LEFTCORNER, STORAGE_ENTRANCE_Y, new Tile(true,storage_entrance_rightside , 1, 1));
        grid.setTile(STORAGE_ENTRANCE_RIGHTCORNER, STORAGE_ENTRANCE_Y, new Tile(true,storage_entrance_leftside , 1, 1));

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

    private void placeStorageShelves(){
        Image storage_rack = loader.getNamedTile("storage_rack");

        StorageShelf storageShelf1= new StorageShelf(2, 3, 4, 1, storage_rack);

        storageShelf1.placeOnGrid(grid);

    }
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