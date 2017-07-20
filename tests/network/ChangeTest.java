package network;

import model.Item;
import model.Position;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ChangeTest {
    @Test
    public void testGetAddChange() {
        assertEquals("ADD " + new Position(0, 2) + " " + Item.WALL,
                Change.getAddChange(new Position(0, 2), Item.WALL).toString());

        assertEquals("ADD " + new Position(3, 2) + " " + Item.PICK_AXE,
                Change.getAddChange(new Position(3, 2), Item.PICK_AXE).toString());
    }

    @Test
    public void testGetRemoveChange() {
        assertEquals("REMOVE " + new Position(0, 2) + " " + Item.WALL,
                Change.getRemoveChange(new Position(0, 2), Item.WALL).toString());

        assertEquals("REMOVE " + new Position(3, 2) + " " + Item.PICK_AXE,
                Change.getRemoveChange(new Position(3, 2), Item.PICK_AXE).toString());
    }

    @Test
    public void testValueOfAdd() {
        assertEquals(Change.valueOf("ADD " + new Position(0, 2) + " " + Item.WALL),
                Change.getAddChange(new Position(0, 2), Item.WALL));

        assertEquals(Change.valueOf("ADD " + new Position(3, 2) + " " + Item.PICK_AXE),
                Change.getAddChange(new Position(3, 2), Item.PICK_AXE));
    }

    @Test
    public void testValueOfRemove() {
        assertEquals(Change.valueOf("REMOVE " + new Position(0, 2) + " " + Item.WALL),
                Change.getRemoveChange(new Position(0, 2), Item.WALL));

        assertEquals(Change.valueOf("REMOVE " + new Position(3, 2) + " " + Item.PICK_AXE),
                Change.getRemoveChange(new Position(3, 2), Item.PICK_AXE));
    }
}
