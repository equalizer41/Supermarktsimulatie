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
        // Definieer je tiles met namen en coördinaten
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

        namedTiles.put("floor",       getTile(96,80));
        namedTiles.put("entrance",       getTile(32,144));
        namedTiles.put("exit",       getTile(32,144));
        namedTiles.put("checkout",  getDoubleVertTile(96,0));

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
    // Methode om tile op naam op te halen
    public Image getNamedTile(String name) {
        return namedTiles.get(name);
    }


}