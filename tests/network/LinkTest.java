package network;

import model.Item;
import model.Position;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

/**
 *
 */
public class LinkTest {
    private static final int MAX_ITERATIONS = 10;
    private Link link;

    @Before
    public void runBefore() {
        link = new Link();
    }

    @Test
    public void testDecodeEncodeItemsSimple() throws ParseException {
        Set<Item> items = new HashSet<>();
        assertEquals(items, Link.decodeItems(link.encodeItems(items)));

        items.add(Item.AIR);
        assertEquals(items, Link.decodeItems(link.encodeItems(items)));

        items.add(Item.MUSHROOM);
        assertEquals(items, Link.decodeItems(link.encodeItems(items)));
    }

    @Test
    public void testDecodeEncodeItemsRandom() throws ParseException {
        Random random = new Random();

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Set<Item> items = new HashSet<>();

            for (Item item : Item.values()) {
                if (random.nextBoolean()) {
                    items.add(item);
                    assertEquals(items, Link.decodeItems(link.encodeItems(items)));
                }
            }
        }
    }

    @Test
    public void testDecodeEncodeItemsNull() throws ParseException {
        assertTrue(Link.decodeItems(link.encodeItems(null)).isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException {
        Link.decodeItems("LOL MOAR STONE");
    }

    // boolean

    @Test
    public void testDecodeEncodeBoolean() {
        assertTrue(Link.decodeBoolean(Link.encodeBoolean(true)));
        assertFalse(Link.decodeBoolean(Link.encodeBoolean(false)));
    }

    // map

    @Test
    public void testDecodeEncodeMapEmptyOrigin() throws ParseException {
        assertTrue(Link.decodeMap(Position.ORIGIN, Position.ORIGIN,
                link.encodeMap(Position.ORIGIN, Position.ORIGIN, new HashMap<>())).isEmpty());
    }

    @Test
    public void testDecodeEncodeMapEmptyUp() throws ParseException {
        assertTrue(Link.decodeMap(Position.ORIGIN, Position.UP,
                link.encodeMap(Position.ORIGIN, Position.UP, new HashMap<>())).isEmpty());
    }

    @Test
    public void testDecodeEncodeMapEmptyOtherDirections() throws ParseException {
        for (Position direction : Position.DIRECTIONS) {
            assertTrue(Link.decodeMap(Position.ORIGIN, direction,
                    link.encodeMap(Position.ORIGIN, direction, new HashMap<>())).isEmpty());
        }
    }

    @Test
    public void testDecodeEncodeMapEmptyDirectionsStartingPosition() throws ParseException {
        for (Position directionA : Position.DIRECTIONS) {
            for (Position directionB : Position.DIRECTIONS) {
                assertTrue(Link.decodeMap(directionA, directionB,
                        link.encodeMap(directionA, directionB, new HashMap<>())).isEmpty());
            }
        }
    }

    @Test
    public void testDecodeEncodeMapSingleton() throws ParseException {
        Map<Position, Set<Item>> expected = new HashMap<>();

        expected.put(Position.UP, Collections.singleton(Item.WALL));

        Map<Position, Set<Item>> actual = Link.decodeMap(Position.ORIGIN, new Position(10, -10),
                link.encodeMap(Position.ORIGIN, new Position(10, -10), expected));
        assertEquals(expected, actual);

        assertEquals(expected, Link.decodeMap(Position.ORIGIN, new Position(10, -1),
                link.encodeMap(Position.ORIGIN, new Position(10, -1), expected)));

        assertEquals(expected, Link.decodeMap(Position.ORIGIN, new Position(10, -1),
                link.encodeMap(Position.ORIGIN, new Position(-10, -1), expected)));

        assertTrue(Link.decodeMap(Position.ORIGIN, new Position(10, -10),
                link.encodeMap(Position.ORIGIN, new Position(-10, 0), expected)).isEmpty());
    }

    @Test
    public void testDecodeEncodeMapSquare() throws ParseException {
        Map<Position, Set<Item>> expected = new HashMap<>();

        for (Position position : Position.ORIGIN.getIterator(new Position(10, 10))) {
            expected.put(position, Collections.singleton(Item.WALL));
        }

        Map<Position, Set<Item>> actual = Link.decodeMap(Position.ORIGIN, new Position(10, 10),
                link.encodeMap(Position.ORIGIN, new Position(10, 10), expected));
        assertEquals(expected, actual);

        Map<Position, Set<Item>> truncated = Link.decodeMap(Position.ORIGIN, new Position(5, 5),
                link.encodeMap(Position.ORIGIN, new Position(10, 10), expected));
        assertEquals(36, truncated.size());
        assertTrue(truncated.containsKey(Position.ORIGIN));
        assertTrue(truncated.containsKey(new Position(5, 5)));
        assertTrue(truncated.containsKey(new Position(0, 5)));
        assertTrue(truncated.containsKey(new Position(5, 0)));

        truncated = Link.decodeMap(Position.ORIGIN, new Position(10, 10),
                link.encodeMap(Position.ORIGIN, new Position(5, 5), expected));
        assertEquals(36, truncated.size());
        assertTrue(truncated.containsKey(Position.ORIGIN));
        assertFalse(truncated.containsKey(new Position(10, 10)));
    }
}
