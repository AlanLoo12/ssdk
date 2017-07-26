package model.item;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.Contract;

/**
 * Some legacy static constants for the items
 */
public class ItemEnum {
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
        return new Item[]{};
    }
}
