package network;

import model.item.Item;
import model.Position;
import model.world.WorldManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Helps encode/decode messages between client and server and to transfer  updates
 */
public class Link {
    private static final String ITEMS_SPLITTER = " ";
    private static final String MAP_SPLITTER = "~";
    private static final String PAYLOAD_SPLITTER = "!";
    private static final String CHANGE_SPLITTER = ";";
    /**
     * Maximum changes sent at a time
     */
    private static final int MAX_CHANGE = 100;
    private BlockingQueue<Change> payLoad;

    Link() {
        payLoad = new LinkedBlockingQueue<>();
    }

    @NotNull
    static Set<Item> decodeItems(String string) throws ParseException {
        Set<Item> items = new HashSet<>();

        for (String itemName : string.split(ITEMS_SPLITTER)) {
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
    String encodeItems(Set<Item> items) {
        if (items == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (Item item : items) {
            result.append(ITEMS_SPLITTER);
            result.append(item);
        }

        return result.toString();
    }

    private String encodePayload() {
        StringBuilder result = new StringBuilder();

        int count = 0;
        while (!payLoad.isEmpty() && count < MAX_CHANGE) {
            try {
                Change change = payLoad.take();
                result.append(change);
                result.append(CHANGE_SPLITTER);

                count++;
            } catch (InterruptedException e) {
                break;
            }
        }

        return result.toString();
    }

    @Contract(value = "true -> !null; false -> !null", pure = true)
    static String encodeBoolean(boolean bool) {
        return bool? "TRUE" : "FALSE";
    }

    @Contract(pure = true)
    static boolean decodeBoolean(String bool) {
        return bool.equals("TRUE");
    }

    @NotNull
    String encodeMap(Position from, Position to, Map<Position, Set<Item>> map) {
        StringBuilder result = new StringBuilder();

        for (Position position : from.getIterator(to)) {
            result.append(encodeItems(map.get(position)));
            result.append(MAP_SPLITTER);
        }

        result.append(PAYLOAD_SPLITTER);
        result.append(encodePayload());

        return result.toString();
    }

    public static Set<Item> decodeMap(Position from, Position to, String string) throws ParseException {
        string = detachPayload(string);

        Set<Item> items = new HashSet<>();

        Position currentPosition = from;
        for (String block : string.split(MAP_SPLITTER)) {
            if (!block.equals("")) {
                items.add(Item.valueOf(block).setPosition(currentPosition));
            }
            currentPosition = currentPosition.add1(from, to);
        }

        return items;
    }

    private static String detachPayload(String string) {
        String[] words = string.split(PAYLOAD_SPLITTER);

        if (words.length > 1) {
            parsePayload(words[1]);
        }

        return words[0];
    }

    private static void parsePayload(String payload) {
        for (String instruction : payload.split(PAYLOAD_SPLITTER)) {
            try {
                Change change = Change.valueOf(instruction);
                WorldManager.getInstance().commitChange(change);
            } catch (IllegalArgumentException e) {
                assert (instruction.equals(""));
            }
        }
    }

    void addPayload(Change change) {
        try {
            payLoad.put(change);
        } catch (InterruptedException ignored) {}
    }
}
