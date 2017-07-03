package model;

import model.Position;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void testHashCode() {
        Set<Position> positionSet = new HashSet<>();
        positionSet.add(new Position(0,0));
        positionSet.add(new Position(0,2));
        positionSet.add(new Position(3,0));

        assertTrue(positionSet.contains(new Position(0,0)));
        assertTrue(positionSet.contains(new Position(0,2)));
        assertTrue(positionSet.contains(new Position(3,0)));
    }
}
