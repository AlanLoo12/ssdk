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
        World.getInstance().add(Position.ORIGIN, Item.AIR);
        assertFalse(World.getInstance().isEmpty());
        assertEquals(1, World.getInstance().size());
        assertEquals(1, World.getInstance().get(Position.ORIGIN).size());
        assertTrue(World.getInstance().get(Position.ORIGIN).contains(Item.AIR));
        assertTrue(World.getInstance().contains(Position.ORIGIN, Item.AIR));
        assertTrue(World.getInstance().contains(Position.ORIGIN));

        World.getInstance().wipeClean();
        assertTrue(World.getInstance().isEmpty());
        assertEquals(0, World.getInstance().size());
        assertEquals(0, World.getInstance().get(Position.ORIGIN).size());
        assertFalse(World.getInstance().get(Position.ORIGIN).contains(Item.AIR));
        assertFalse(World.getInstance().contains(Position.ORIGIN, Item.AIR));
        assertFalse(World.getInstance().contains(Position.ORIGIN));
    }

    @Test
    public void testAddTwice() {
        World.getInstance().add(Position.UP, Item.AIR);
        assertTrue(World.getInstance().contains(Position.UP, Item.AIR));

        World.getInstance().add(Position.UP, Item.AIR);
        assertTrue(World.getInstance().contains(Position.UP, Item.AIR));
    }

    @Test
    public void testAddDifferent() {
        World.getInstance().add(Position.UP, Item.AIR);
        assertTrue(World.getInstance().contains(Position.UP, Item.AIR));
        assertFalse(World.getInstance().contains(Position.UP, Item.WALL));

        World.getInstance().add(Position.UP, Item.WALL);
        assertFalse(World.getInstance().contains(Position.UP, Item.AIR));
        assertTrue(World.getInstance().contains(Position.UP, Item.WALL));
    }

    @Test
    public void testRemoveNothing() {
        assertFalse(World.getInstance().contains(Position.ORIGIN, Item.AIR));
        World.getInstance().remove(Position.ORIGIN, Item.AIR);
        assertFalse(World.getInstance().contains(Position.ORIGIN, Item.AIR));
    }

    @Test
    public void testRemoveOne() {
        World.getInstance().add(Position.DOWN, Item.AIR);
        assertTrue(World.getInstance().contains(Position.DOWN, Item.AIR));
        assertFalse(World.getInstance().isEmpty());
        assertEquals(1, World.getInstance().size());
        World.getInstance().remove(Position.DOWN, Item.AIR);
        assertFalse(World.getInstance().contains(Position.DOWN, Item.AIR));
        assertTrue(World.getInstance().isEmpty());
        assertEquals(0, World.getInstance().size());
    }

    @Test
    public void testRemoveOneFromMany() {
        World.getInstance().add(Position.DOWN, Item.AIR);
        World.getInstance().add(Position.DOWN, Item.PLAYER);
        assertTrue(World.getInstance().contains(Position.DOWN, Item.AIR));
        assertTrue(World.getInstance().contains(Position.DOWN, Item.PLAYER));
        assertFalse(World.getInstance().isEmpty());
        assertEquals(2, World.getInstance().size());
        World.getInstance().remove(Position.DOWN, Item.PLAYER);
        assertTrue(World.getInstance().contains(Position.DOWN, Item.AIR));
        assertFalse(World.getInstance().contains(Position.DOWN, Item.PLAYER));
        assertFalse(World.getInstance().isEmpty());
        assertEquals(1, World.getInstance().size());
    }

    @Test
    public void testRemoveWall() {
        World.getInstance().add(Position.ORIGIN, Item.WALL);
        assertEquals(1, World.getInstance().size());
        World.getInstance().remove(Position.ORIGIN, Item.WALL);
        assertEquals(9, World.getInstance().size());
        assertTrue(World.getInstance().contains(Position.UP, Item.WALL));
        assertTrue(World.getInstance().contains(Position.DOWN, Item.WALL));
        assertTrue(World.getInstance().contains(Position.LEFT, Item.WALL));
        assertTrue(World.getInstance().contains(Position.RIGHT, Item.WALL));
        assertTrue(World.getInstance().contains(Position.UP.add(Position.LEFT), Item.WALL));
        assertTrue(World.getInstance().contains(Position.DOWN.add(Position.RIGHT), Item.WALL));
        assertTrue(World.getInstance().contains(Position.LEFT.add(Position.DOWN), Item.WALL));
        assertTrue(World.getInstance().contains(Position.RIGHT.add(Position.UP), Item.WALL));
        assertTrue(World.getInstance().contains(Position.ORIGIN, Item.AIR));
    }
}
