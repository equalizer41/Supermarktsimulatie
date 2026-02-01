package supermarket.simulator.services;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class TilesetLoader {

    private final Image tileset;
    private final int TILE_SIZE = 16;

    // -----------------------
    // Afzonderlijke tile-afbeeldingen
    // -----------------------
    private final Image floor;
    private final Image wall;
    private final Image shelf;
    private final Image fridge;
    private final Image checkout;
    private final Image door;
    private final Image road;
    private final Image counter;
    private final Image entrance;
    private final Image register;
    private final Image storageShelf;
    private final Image outerWall;


    public TilesetLoader() {
        // Tileset laden
        tileset = new Image(getClass().getResourceAsStream(
                "/supermarket/simulator/sprites/tiles/MarketSet_Tileset.png"
        ));


        // -----------------------
        // Tile-onderdelen uitsnijden
        // (coördinaten in tiles, 0-indexed)
        // -----------------------

        //  vloer direct laden uit je eigen bestand
        floor = new Image(getClass().getResourceAsStream(
                "/supermarket/simulator/sprites/tiles/FloorTile.png"
        ));

        outerWall = new Image(getClass().getResourceAsStream(
                "/supermarket/simulator/sprites/tiles/Outerwall.png"
        ));

        wall         = getTile(1, 0); // wit/grijs frame
        shelf        = getTile(2, 0); // rek met producten
        checkout     = getTile(3, 0); // kassa met lopende band
        fridge       = getTile(3, 1); // blauwe koelvitrine
        door         = getTile(2, 2); // ingang / deur
        road         = getTile(4, 2); // asfalt / pad buiten
        counter      = getTile(0, 2); // balie / geld-teken
        entrance     = getTile(3, 2); // dubbele schuifdeur
        register     = getTile(4, 0); // rek met kleurige strepen (kassa display)
        storageShelf = getTile(2, 1); // groen rek / magazijnstelling
    }

    // -----------------------
    // Helper om een stukje uit de tileset te halen
    // -----------------------
    private Image getTile(int tileX, int tileY) {
        return new WritableImage(
                tileset.getPixelReader(),
                tileX * TILE_SIZE,
                tileY * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE
        );
    }

    // -----------------------
    // Publieke getters
    // -----------------------
    public Image getOuterWall()    { return outerWall; }
    public Image getFloor()        { return floor; }
    public Image getWall()         { return wall; }
    public Image getShelf()        { return shelf; }
    public Image getFridge()       { return fridge; }
    public Image getCheckout()     { return checkout; }
    public Image getDoor()         { return door; }
    public Image getRoad()         { return road; }
    public Image getCounter()      { return counter; }
    public Image getEntrance()     { return entrance; }
    public Image getRegister()     { return register; }
    public Image getStorageShelf() { return storageShelf; }

}
