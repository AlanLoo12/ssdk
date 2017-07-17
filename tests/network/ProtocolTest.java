package network;

import model.Item;
import model.Position;
import org.junit.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

/**
 *
 */
public class ProtocolTest {
    private static final int MAX_ITERATIONS = 10;

    @Test
    public void testDecodeEncodeItemsSimple() throws ParseException {
        Set<Item> items = new HashSet<>();
        assertEquals(items, Protocol.decodeItems(Protocol.encodeItems(items)));

        items.add(Item.AIR);
        assertEquals(items, Protocol.decodeItems(Protocol.encodeItems(items)));

        items.add(Item.MUSHROOM);
        assertEquals(items, Protocol.decodeItems(Protocol.encodeItems(items)));
    }

    @Test
    public void testDecodeEncodeItemsRandom() throws ParseException {
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

    @Test
    public void testDecodeEncodeItemsNull() throws ParseException {
        assertTrue(Protocol.decodeItems(Protocol.encodeItems(null)).isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException {
        Protocol.decodeItems("LOL MOAR STONE");
    }

    // boolean

    @Test
    public void testDecodeEncodeBoolean() {
        assertTrue(Protocol.decodeBoolean(Protocol.encodeBoolean(true)));
        assertFalse(Protocol.decodeBoolean(Protocol.encodeBoolean(false)));
    }

    // map

    @Test
    public void testDecodeEncodeMapEmptyOrigin() throws ParseException {
        assertTrue(Protocol.decodeMap(Position.ORIGIN, Position.ORIGIN,
                Protocol.encodeMap(Position.ORIGIN, Position.ORIGIN, new HashMap<>())).isEmpty());
    }

    @Test
    public void testDecodeEncodeMapEmptyUp() throws ParseException {
        assertTrue(Protocol.decodeMap(Position.ORIGIN, Position.UP,
                Protocol.encodeMap(Position.ORIGIN, Position.UP, new HashMap<>())).isEmpty());
    }

    @Test
    public void testDecodeEncodeMapEmptyOtherDirections() throws ParseException {
        for (Position direction : Position.DIRECTIONS) {
            assertTrue(Protocol.decodeMap(Position.ORIGIN, direction,
                    Protocol.encodeMap(Position.ORIGIN, direction, new HashMap<>())).isEmpty());
        }
    }

    @Test
    public void testDecodeEncodeMapEmptyDirectionsStartingPosition() throws ParseException {
        for (Position directionA : Position.DIRECTIONS) {
            for (Position directionB : Position.DIRECTIONS) {
                assertTrue(Protocol.decodeMap(directionA, directionB,
                        Protocol.encodeMap(directionA, directionB, new HashMap<>())).isEmpty());
            }
        }
    }

    @Test
    public void testDecodeEncodeMapSingleton() throws ParseException {
        Map<Position, Set<Item>> expected = new HashMap<>();

        expected.put(Position.UP, Collections.singleton(Item.WALL));

        Map<Position, Set<Item>> actual = Protocol.decodeMap(Position.ORIGIN, new Position(10, -10),
                Protocol.encodeMap(Position.ORIGIN, new Position(10, -10), expected));
        assertEquals(expected, actual);

        assertEquals(expected, Protocol.decodeMap(Position.ORIGIN, new Position(10, -1),
                Protocol.encodeMap(Position.ORIGIN, new Position(10, -1), expected)));

        assertEquals(expected, Protocol.decodeMap(Position.ORIGIN, new Position(10, -1),
                Protocol.encodeMap(Position.ORIGIN, new Position(-10, -1), expected)));

        assertTrue(Protocol.decodeMap(Position.ORIGIN, new Position(10, -10),
                Protocol.encodeMap(Position.ORIGIN, new Position(-10, 0), expected)).isEmpty());
    }

    @Test
    public void testDecodeEncodeMapSquare() throws ParseException {
        Map<Position, Set<Item>> expected = new HashMap<>();

        for (Position position : Position.ORIGIN.getIterator(new Position(10, 10))) {
            expected.put(position, Collections.singleton(Item.WALL));
        }

        Map<Position, Set<Item>> actual = Protocol.decodeMap(Position.ORIGIN, new Position(10, 10),
                Protocol.encodeMap(Position.ORIGIN, new Position(10, 10), expected));
        assertEquals(expected, actual);

        Map<Position, Set<Item>> truncated = Protocol.decodeMap(Position.ORIGIN, new Position(5, 5),
                Protocol.encodeMap(Position.ORIGIN, new Position(10, 10), expected));
        assertEquals(36, truncated.size());
        assertTrue(truncated.containsKey(Position.ORIGIN));
        assertTrue(truncated.containsKey(new Position(5, 5)));
        assertTrue(truncated.containsKey(new Position(0, 5)));
        assertTrue(truncated.containsKey(new Position(5, 0)));

        truncated = Protocol.decodeMap(Position.ORIGIN, new Position(10, 10),
                Protocol.encodeMap(Position.ORIGIN, new Position(5, 5), expected));
        assertEquals(36, truncated.size());
        assertTrue(truncated.containsKey(Position.ORIGIN));
        assertFalse(truncated.containsKey(new Position(10, 10)));
    }
}
