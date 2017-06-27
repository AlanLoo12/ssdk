package tests;

import model.Position;
import org.junit.Test;

import static com.sun.javafx.scene.traversal.Direction.*;
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

        assertEquals(new Position(0, -1), position.get(UP));
        assertEquals(new Position(0, 1), position.get(DOWN));
        assertEquals(new Position(1, 0), position.get(RIGHT));
        assertEquals(new Position(-1, 0), position.get(LEFT));
    }
}
