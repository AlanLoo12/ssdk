package network;

import javafx.geometry.Pos;
import model.Item;
import model.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    @NotNull
    static String encodeMap(Position from, Position to, Map<Position, Set<Item>> map) {
        StringBuilder result = new StringBuilder();

        for (Position position : from.getIterator(to)) {
            assert (map.containsKey(position));

            result.append(";");
            result.append(encodeItems(map.get(position)));
        }

        return result.toString();
    }

    public static Map<Position, Set<Item>> decodeMap(Position from, Position to, String string) throws ParseException {
        Map<Position, Set<Item>> map = new HashMap<>();

        Position currentPosition = from;
        for (String block : string.split(";")) {
            if (!block.equals("")) {
                map.put(currentPosition, decodeItems(block));
            }
            currentPosition = currentPosition.add1(from, to);
        }

        return map;
    }
}
