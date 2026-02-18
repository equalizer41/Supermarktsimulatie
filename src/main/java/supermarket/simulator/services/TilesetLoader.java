package supermarket.simulator.services;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import java.util.HashMap;
import java.util.Map;

public class TilesetLoader {
    private static final String SPRITE_PATH = "/supermarket/simulator/sprites/tiles/";
    private Image tileset;
    private Map<String, Image> namedTiles;

    public TilesetLoader() {
        // Laad de hele tileset
        tileset = new Image(getClass().getResourceAsStream(SPRITE_PATH + "MarketSet_Tileset.png"));
        System.out.println("Tileset loaded: " + tileset.getWidth() + "x" + tileset.getHeight());
        namedTiles = new HashMap<>();
        loadNamedTiles();
    }
    /**
     * Laad een tile uit de tileset
     * @param x X positie in pixels (bijv. 0, 16, 32, 48...)
     * @param y Y positie in pixels (bijv. 0, 16, 32, 48...)
     * @param width Breedte van de tile (meestal 16)
     * @param height Hoogte van de tile (meestal 16)
     * @return Een Image van de geselecteerde tile
     */
    public Image getTile(int x, int y, int width, int height) {
        PixelReader reader = tileset.getPixelReader();
        return new WritableImage(reader, x, y, width, height);
    }
    private void loadNamedTiles() {
        // Definieren van tiles met namen en coördinaten
        namedTiles.put("wallcorner_topleft",     getTile(0, 0));
        namedTiles.put("wallcorner_topright",    getTile(32, 0));
        namedTiles.put("wallcorner_bottomleft",  getTile(48,144));
        namedTiles.put("wallcorner_bottomright",  getTile(64,144));

        // Zijkanten vanuit de sprite tileset
        namedTiles.put("wall_left",   getTile(0, 16));
        namedTiles.put("wall_right",  getTile(32, 16));
        namedTiles.put("wall_top",    getTile(16, 0));
        namedTiles.put("wall_bottom_top", getTile(112, 0));
        namedTiles.put("wall_bottom_ground", getTile(14, 128));
        namedTiles.put("wall_storage", getTallTile(14,123));
        namedTiles.put("background_storage", getTile(16, 144));

        // Misc.
        namedTiles.put("floor",       getTile(96,80));
        namedTiles.put("storage_floor",       getTile(96,96));
        namedTiles.put("entrance",       getTile(32,144));
        namedTiles.put("exit",       getTile(32,144));
        namedTiles.put("storage",       getTile(96,96));
        namedTiles.put("storage_entrance",       getTile(96,112));
        namedTiles.put("storage_entrance_leftside",       getTallTile(12,123));
        namedTiles.put("storage_entrance_rightside",       getTallTile(20,123));
        namedTiles.put("storage_rack",       getStorageRackTile(48,98));

        //Objecten
        namedTiles.put("checkout",  getDoubleVertTile(96,0));
        namedTiles.put("vertshelf", getQuadVertTile(0,48));
        namedTiles.put("fridge", getFridgeTile(48,64));

    }
    /**
     * Shorthand voor 16x16 tiles
     */
    public Image getTile(int x, int y) {
        return getTile(x, y, 16, 16);
    }
    /**
     * Shorthand voor 16x32 tiles
     */
    public Image getDoubleVertTile(int x, int y) {
        return getTile(x, y, 16, 32);
    }
    public Image getQuadVertTile(int x, int y) {
        return getTile(x, y, 16, 64);
    }
    public Image getFridgeTile(int x, int y){
        return getTile(x,y, 48,32);
    }
    public Image getStorageRackTile(int x, int y){
        return getTile(x,y, 48,21);
    }
    public Image getTallTile(int x, int y){
        return getTile(x,y,16,21);
    }
    // Methode om tile op naam op te halen
    public Image getNamedTile(String name) {
        return namedTiles.get(name);
    }


}