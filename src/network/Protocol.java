package network;

import model.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Protocol {
    @NotNull
    public static Set<Item> decodeItems(String string) throws ParseException {
        Set<Item> items = new HashSet<>();

        for (String itemName : string.split(" ")) {
            if (!itemName.equals("")) {
                try {
                    items.add(Item.valueOf(itemName));
                } catch (IllegalArgumentException e) {
                    throw new ParseException("Illegal item name", string.indexOf(itemName));
                }
            }
        }

        return items;
    }

    @NotNull
    static String encodeItems(Set<Item> items) {
        StringBuilder result = new StringBuilder();

        for (Item item : items) {
            result.append(" ");
            result.append(item);
        }

        return result.toString();
    }

    @Contract(value = "true -> !null; false -> !null", pure = true)
    static String encodeBoolean(boolean bool) {
        return bool? "TRUE" : "FALSE";
    }

    @Contract(pure = true)
    public static boolean decodeBoolean(String bool) {
        return bool.equals("TRUE");
    }
}
