package supermarket.simulator.services;

import supermarket.simulator.model.enums.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CharacterSpriteLoader {

    private static final String BASE_PATH = "/supermarket/simulator/sprites/people/";

    // Beschikbare customer sprites
    private static final String[] CUSTOMER_SPRITES = {
            "MarketSet_Customer1.png",
            "MarketSet_Customer2.png",
            "MarketSet_Customer3.png",
            "MarketSet_Customer4.png"
    };

    private static final String EMPLOYEE_SPRITE = "MarketSet_StoreEmployee.png";

    private static final int FRAME_WIDTH  = 7;
    private static final int FRAME_HEIGHT = 16;

    // Rij 1 (y=0):  4x DOWN, 4x UP, 2x LEFT
    // Rij 2 (y=16): 4x LEFT, 6x RIGHT
    private static final Map<Direction, int[]> DIRECTION_DATA = new HashMap<>();

    static {
        DIRECTION_DATA.put(Direction.DOWN,  new int[]{0,  0,  4});
        DIRECTION_DATA.put(Direction.UP,    new int[]{28, 0,  4});
        DIRECTION_DATA.put(Direction.LEFT,  new int[]{0,  16, 4});
        DIRECTION_DATA.put(Direction.RIGHT, new int[]{28, 16, 6});
    }

    private final Image sheet;

    /** Laad een specifiek bestand (bijv. voor employee) */
    public CharacterSpriteLoader(String filename) {
        String fullPath = BASE_PATH + filename;
        var stream = getClass().getResourceAsStream(fullPath);

        if (stream == null) {
            throw new IllegalStateException("Sprite niet gevonden: " + fullPath);
        }

        sheet = new Image(stream);
        System.out.println("Sprite geladen: " + filename +
                " (" + sheet.getWidth() + "x" + sheet.getHeight() + ")");
    }

    /** Maak een loader met een willekeurige customer sprite */
    public static CharacterSpriteLoader randomCustomer() {
        String filename = CUSTOMER_SPRITES[new Random().nextInt(CUSTOMER_SPRITES.length)];
        return new CharacterSpriteLoader(filename);
    }

    /** Maak een loader voor de employee sprite */
    public static CharacterSpriteLoader employee() {
        return new CharacterSpriteLoader(EMPLOYEE_SPRITE);
    }

    /** Geef het juiste animatieframe terug op basis van richting en framenummer */
    public Image getFrame(Direction dir, int frame) {
        int[] data     = DIRECTION_DATA.get(dir);
        int startX     = data[0];
        int startY     = data[1];
        int maxFrames  = data[2];

        int safeFrame = frame % maxFrames;
        int x = startX + safeFrame * FRAME_WIDTH;

        return new WritableImage(sheet.getPixelReader(), x, startY, FRAME_WIDTH, FRAME_HEIGHT);
    }
}