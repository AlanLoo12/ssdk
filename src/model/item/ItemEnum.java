package model.item;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.Contract;

/**
 * Some legacy static constants for the items
 */
public class ItemEnum {
    public static final Item AIR = new Air();
    public static final Item WATER = new Water();
    public static final Item PLAYER = new Player();
    public static final Item MUSHROOM = new Mushroom();
    public static final Item WALL = new Wall();
    public static final Item PICK_AXE = new PickAxe();

    private ItemEnum() {}

    /**
     * Produce regular expression that would match all the valid item names
     * @return regular expression that would match all the valid item names
     */
    public static String getValidNamesRegex() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");

        for (Item item : ItemEnum.values()) {
            stringBuilder.append(item);
            stringBuilder.append("|");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private static Item[] values() {
        return new Item[]{AIR, WATER, PLAYER, MUSHROOM, WALL, PICK_AXE};
    }
}
