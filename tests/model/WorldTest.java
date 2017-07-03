package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class WorldTest {

    @Before
    public void runBefore() {
        World.getInstance().wipeClean();
    }

    @Test
    public void testConstructor() {
        assertTrue(World.getInstance().isEmpty());
        assertEquals(0, World.getInstance().size());
    }

    @Test
    public void testWipeClean() {
        World.getInstance().add(Position.ORIGIN, Item.FLOOR);
        assertFalse(World.getInstance().isEmpty());
        assertEquals(1, World.getInstance().size());
        assertEquals(1, World.getInstance().get(Position.ORIGIN).size());
        assertTrue(World.getInstance().get(Position.ORIGIN).contains(Item.FLOOR));
        assertTrue(World.getInstance().contains(Position.ORIGIN, Item.FLOOR));
        assertTrue(World.getInstance().contains(Position.ORIGIN));

        World.getInstance().wipeClean();
        assertTrue(World.getInstance().isEmpty());
        assertEquals(0, World.getInstance().size());
        assertEquals(0, World.getInstance().get(Position.ORIGIN).size());
        assertFalse(World.getInstance().get(Position.ORIGIN).contains(Item.FLOOR));
        assertFalse(World.getInstance().contains(Position.ORIGIN, Item.FLOOR));
        assertFalse(World.getInstance().contains(Position.ORIGIN));
    }

    @Test
    public void testAddTwice() {
        World.getInstance().add(Position.UP, Item.FLOOR);
        assertTrue(World.getInstance().contains(Position.UP, Item.FLOOR));

        World.getInstance().add(Position.UP, Item.FLOOR);
        assertTrue(World.getInstance().contains(Position.UP, Item.FLOOR));
    }

    @Test
    public void testAddDifferent() {
        World.getInstance().add(Position.UP, Item.FLOOR);
        assertTrue(World.getInstance().contains(Position.UP, Item.FLOOR));
        assertFalse(World.getInstance().contains(Position.UP, Item.WALL));

        World.getInstance().add(Position.UP, Item.WALL);
        assertTrue(World.getInstance().contains(Position.UP, Item.FLOOR));
        assertTrue(World.getInstance().contains(Position.UP, Item.WALL));
    }

    @Test
    public void testRemoveNothing() {
        assertFalse(World.getInstance().contains(Position.ORIGIN, Item.FLOOR));
        World.getInstance().remove(Position.ORIGIN, Item.FLOOR);
        assertFalse(World.getInstance().contains(Position.ORIGIN, Item.FLOOR));
    }

    @Test
    public void testRemoveOne() {
        World.getInstance().add(Position.DOWN, Item.FLOOR);
        assertTrue(World.getInstance().contains(Position.DOWN, Item.FLOOR));
        assertFalse(World.getInstance().isEmpty());
        assertEquals(1, World.getInstance().size());
        World.getInstance().remove(Position.DOWN, Item.FLOOR);
        assertFalse(World.getInstance().contains(Position.DOWN, Item.FLOOR));
        assertTrue(World.getInstance().isEmpty());
        assertEquals(0, World.getInstance().size());
    }

    @Test
    public void testRemoveOneFromMany() {
        World.getInstance().add(Position.DOWN, Item.FLOOR);
        World.getInstance().add(Position.DOWN, Item.PLAYER);
        assertTrue(World.getInstance().contains(Position.DOWN, Item.FLOOR));
        assertTrue(World.getInstance().contains(Position.DOWN, Item.PLAYER));
        assertFalse(World.getInstance().isEmpty());
        assertEquals(2, World.getInstance().size());
        World.getInstance().remove(Position.DOWN, Item.PLAYER);
        assertTrue(World.getInstance().contains(Position.DOWN, Item.FLOOR));
        assertFalse(World.getInstance().contains(Position.DOWN, Item.PLAYER));
        assertFalse(World.getInstance().isEmpty());
        assertEquals(1, World.getInstance().size());
    }
}
