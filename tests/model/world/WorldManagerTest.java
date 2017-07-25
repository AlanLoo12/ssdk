package model.world;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class WorldManagerTest {
/*
    @Before
    public void runBefore() {
        WorldManager.getInstance().wipeClean();
    }

    @Test
    public void testConstructor() {
        assertTrue(WorldManager.getInstance().isEmpty());
        assertEquals(0, WorldManager.getInstance().size());
    }

    @Test
    public void testWipeClean() {
        WorldManager.getInstance().add(Position.ORIGIN, Item.AIR);
        assertFalse(WorldManager.getInstance().isEmpty());
        assertEquals(1, WorldManager.getInstance().size());
        assertEquals(1, WorldManager.getInstance().get(Position.ORIGIN).size());
        assertTrue(WorldManager.getInstance().get(Position.ORIGIN).contains(Item.AIR));
        assertTrue(WorldManager.getInstance().contains(Position.ORIGIN, Item.AIR));
        assertTrue(WorldManager.getInstance().contains(Position.ORIGIN));

        WorldManager.getInstance().wipeClean();
        assertTrue(WorldManager.getInstance().isEmpty());
        assertEquals(0, WorldManager.getInstance().size());
        assertEquals(0, WorldManager.getInstance().get(Position.ORIGIN).size());
        assertFalse(WorldManager.getInstance().get(Position.ORIGIN).contains(Item.AIR));
        assertFalse(WorldManager.getInstance().contains(Position.ORIGIN, Item.AIR));
        assertFalse(WorldManager.getInstance().contains(Position.ORIGIN));
    }

    @Test
    public void testAddTwice() {
        WorldManager.getInstance().add(Position.UP, Item.AIR);
        assertTrue(WorldManager.getInstance().contains(Position.UP, Item.AIR));

        WorldManager.getInstance().add(Position.UP, Item.AIR);
        assertTrue(WorldManager.getInstance().contains(Position.UP, Item.AIR));
    }

    @Test
    public void testAddDifferent() {
        WorldManager.getInstance().add(Position.UP, Item.AIR);
        assertTrue(WorldManager.getInstance().contains(Position.UP, Item.AIR));
        assertFalse(WorldManager.getInstance().contains(Position.UP, Item.WALL));

        WorldManager.getInstance().add(Position.UP, Item.WALL);
        assertFalse(WorldManager.getInstance().contains(Position.UP, Item.AIR));
        assertTrue(WorldManager.getInstance().contains(Position.UP, Item.WALL));
    }

    @Test
    public void testRemoveNothing() {
        assertFalse(WorldManager.getInstance().contains(Position.ORIGIN, Item.AIR));
        WorldManager.getInstance().remove(Position.ORIGIN, Item.AIR);
        assertFalse(WorldManager.getInstance().contains(Position.ORIGIN, Item.AIR));
    }

    @Test
    public void testRemoveOne() {
        WorldManager.getInstance().add(Position.DOWN, Item.AIR);
        assertTrue(WorldManager.getInstance().contains(Position.DOWN, Item.AIR));
        assertFalse(WorldManager.getInstance().isEmpty());
        assertEquals(1, WorldManager.getInstance().size());
        WorldManager.getInstance().remove(Position.DOWN, Item.AIR);
        assertFalse(WorldManager.getInstance().contains(Position.DOWN, Item.AIR));
        assertTrue(WorldManager.getInstance().isEmpty());
        assertEquals(0, WorldManager.getInstance().size());
    }

    @Test
    public void testRemoveOneFromMany() {
        WorldManager.getInstance().add(Position.DOWN, Item.AIR);
        WorldManager.getInstance().add(Position.DOWN, Item.PLAYER);
        assertTrue(WorldManager.getInstance().contains(Position.DOWN, Item.AIR));
        assertTrue(WorldManager.getInstance().contains(Position.DOWN, Item.PLAYER));
        assertFalse(WorldManager.getInstance().isEmpty());
        assertEquals(2, WorldManager.getInstance().size());
        WorldManager.getInstance().remove(Position.DOWN, Item.PLAYER);
        assertTrue(WorldManager.getInstance().contains(Position.DOWN, Item.AIR));
        assertFalse(WorldManager.getInstance().contains(Position.DOWN, Item.PLAYER));
        assertFalse(WorldManager.getInstance().isEmpty());
        assertEquals(1, WorldManager.getInstance().size());
    }

    @Test
    public void testRemoveWall() {
        WorldManager.getInstance().add(Position.ORIGIN, Item.WALL);
        assertEquals(1, WorldManager.getInstance().size());
        WorldManager.getInstance().remove(Position.ORIGIN, Item.WALL);
        assertEquals(9, WorldManager.getInstance().size());
        assertTrue(WorldManager.getInstance().contains(Position.UP, Item.WALL));
        assertTrue(WorldManager.getInstance().contains(Position.DOWN, Item.WALL));
        assertTrue(WorldManager.getInstance().contains(Position.LEFT, Item.WALL));
        assertTrue(WorldManager.getInstance().contains(Position.RIGHT, Item.WALL));
        assertTrue(WorldManager.getInstance().contains(Position.UP.add(Position.LEFT), Item.WALL));
        assertTrue(WorldManager.getInstance().contains(Position.DOWN.add(Position.RIGHT), Item.WALL));
        assertTrue(WorldManager.getInstance().contains(Position.LEFT.add(Position.DOWN), Item.WALL));
        assertTrue(WorldManager.getInstance().contains(Position.RIGHT.add(Position.UP), Item.WALL));
        assertTrue(WorldManager.getInstance().contains(Position.ORIGIN, Item.AIR));
    }
    */
}
