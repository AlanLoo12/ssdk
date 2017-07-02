package tests;

import model.Position;
import org.junit.Test;

import static model.Position.*;
import static org.junit.Assert.*;

/**
 *
 */
public class PositionTest {

    @Test
    public void testConstructor() {
        Position position = new Position(10,20);

        assertEquals(10, position.getX());
        assertEquals(20, position.getY());
    }

    @Test
    public void testGet() {
        Position position = new Position(0,0);

        assertEquals(new Position(0, -1), position.add(UP));
        assertEquals(new Position(0, 1), position.add(DOWN));
        assertEquals(new Position(1, 0), position.add(RIGHT));
        assertEquals(new Position(-1, 0), position.add(LEFT));
    }
}
