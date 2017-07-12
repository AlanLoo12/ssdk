package network;

import model.Item;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

/**
 *
 */
public class ProtocolTest {
    private static final int MAX_ITERATIONS = 10;

    @Test
    public void testDecodeEncodeSimple() throws ParseException {
        Set<Item> items = new HashSet<>();
        assertEquals(items, Protocol.decodeItems(Protocol.encodeItems(items)));

        items.add(Item.AIR);
        assertEquals(items, Protocol.decodeItems(Protocol.encodeItems(items)));

        items.add(Item.PLANT);
        assertEquals(items, Protocol.decodeItems(Protocol.encodeItems(items)));
    }

    @Test
    public void testDecodeEncodeRandom() throws ParseException {
        Random random = new Random();

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Set<Item> items = new HashSet<>();

            for (Item item : Item.values()) {
                if (random.nextBoolean()) {
                    items.add(item);
                    assertEquals(items, Protocol.decodeItems(Protocol.encodeItems(items)));
                }
            }
        }
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException {
        Protocol.decodeItems("LOL MOAR STONE");
    }
}
