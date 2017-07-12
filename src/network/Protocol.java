package network;

import model.Item;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
class Protocol {
    @NotNull
    static Set<Item> decodeItems(String string) throws ParseException {
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
}
