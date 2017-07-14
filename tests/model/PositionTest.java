package model;

import model.Position;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static model.Position.*;
import static org.junit.Assert.*;

/**
 *
 */
public class PositionTest {
    private static final int X_MAX = 4;
    private static final int Y_MAX = 3;

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

    // lessThanOrEqualTo tests

    @Test
    public void testLessThanOrEqualToCaseEqual() {
        Position positionA = new Position(10,-30);
        Position positionB = new Position(10,-30);

        assertTrue(positionA.lessThanOrEqualTo(positionB));
        assertTrue(positionB.lessThanOrEqualTo(positionA));
    }

    @Test
    public void testLessThanOrEqualToCaseLessThanX() {
        Position positionA = new Position(10,-30);
        Position positionB = new Position(0,-30);

        assertFalse(positionA.lessThanOrEqualTo(positionB));
        assertTrue(positionB.lessThanOrEqualTo(positionA));
    }

    @Test
    public void testLessThanOrEqualToCaseLessThanY() {
        Position positionA = new Position(10,-30);
        Position positionB = new Position(10,-31);

        assertFalse(positionA.lessThanOrEqualTo(positionB));
        assertTrue(positionB.lessThanOrEqualTo(positionA));
    }

    @Test
    public void testLessThanOrEqualToCaseLessThanYX() {
        Position positionA = new Position(10,-30);
        Position positionB = new Position(-12,-320);

        assertFalse(positionA.lessThanOrEqualTo(positionB));
        assertTrue(positionB.lessThanOrEqualTo(positionA));
    }

    // add1

    @Test
    public void testAdd1BaseCase() {
        Position positionA = new Position(10,-30);

        Position positionC = positionA.add1(positionA);

        assertEquals(positionA, positionC);
    }

    @Test
    public void testAdd1CaseX() {
        Position positionA = new Position(10,-30);
        Position positionB = new Position(100,-30);

        assertEquals(positionA.add(1,0), positionA.add1(positionB));
        assertEquals(positionB.add(-1, 0), positionB.add1(positionA));
    }

    @Test
    public void testAdd1CaseY() {
        Position positionA = new Position(10,-30);
        Position positionB = new Position(10,-10);

        assertEquals(positionA.add(0,1), positionA.add1(positionB));
        assertEquals(positionB.add(0, -1), positionB.add1(positionA));
    }

    @Test
    public void testAdd1CaseXY() {
        Position positionA = new Position(0,0);
        Position positionB = new Position(100,-30);

        assertEquals(positionA.add(1,0), positionA.add1(positionB));
        assertEquals(positionB.add(-1, 0), positionB.add1(positionA));
    }

    // multiply

    @Test
    public void testMultiply() {
        Position position = new Position(1, 3);
        assertEquals(position.add(position), position.multiply(2));
        assertEquals(position.add(position).add(position), position.multiply(3));
        assertEquals(position.add(position).add(position).add(position), position.multiply(4));

        position = new Position(-32, 0);
        assertEquals(position.add(position), position.multiply(2));
        assertEquals(position.add(position).add(position), position.multiply(3));
        assertEquals(position.add(position).add(position).add(position), position.multiply(4));
    }

    // add1(min,max)

    @Test
    public void testAdd1MinBaseCase() {
        Position positionA = new Position(10,-30);
        assertEquals(positionA, positionA.add1(positionA, positionA));
    }

    @Test
    public void testAdd1MinCaseX() {
        Position positionA = new Position(10,-30);
        Position positionB = new Position(100,-30);

        assertEquals(positionA.add(1,0), positionA.add1(positionA, positionB));
        assertEquals(positionB.add(-1, 0), positionB.add1(positionB, positionA));
    }

    @Test
    public void testAdd1MinCaseY() {
        Position positionA = new Position(10,-30);
        Position positionB = new Position(10,-10);

        assertEquals(positionA.add(0,1), positionA.add1(positionA, positionB));
        assertEquals(positionB.add(0, -1), positionB.add1(positionB, positionA));
    }

    @Test
    public void testAdd1MinCaseXY() {
        Position positionA = new Position(0,0);
        Position positionB = new Position(100,-30);

        assertEquals(positionA.add(1,0), positionA.add1(positionA, positionB));
        assertEquals(positionB.add(-1, 0), positionB.add1(positionB, positionA));
    }

    @Test
    public void testAdd1MinCaseXYSwitch() {
        Position min = new Position(0,0);
        Position max = new Position(100,-30);
        Position current = new Position(100,0);

        assertEquals(new Position(0, -1), current.add1(min, max));
        assertEquals(new Position(1, -1), current.add1(min, max).add1(min, max));
    }

    @Test
    public void testAdd1MinCaseXYExhaustive() {
        Set<Position> expected = getPositions(ORIGIN, new Position(X_MAX, Y_MAX));

        Set<Position> actual = new HashSet<>();

        Position max = new Position(X_MAX, Y_MAX).add(-1, -1);

        boolean hitOnce = false;
        for (Position i = ORIGIN; !hitOnce; i = i.add1(ORIGIN, max)) {
            actual.add(i);

            if (i.equals(max)) {
                hitOnce = true;
            }
        }

        assertEquals(expected, actual);
    }

    @NotNull
    private Set<Position> getPositions(Position origin, Position max) {
        Set<Position> expected = new HashSet<>();

        for (int x = origin.getX(); x < max.getX(); x++) {
            for (int y = origin.getY(); y < max.getY(); y++) {
                expected.add(new Position(x, y));
            }
        }
        return expected;
    }

    // iterator

    @Test
    public void iteratorTest() {
        Position min = new Position(-2, -3);
        Position max = new Position(5, 2);

        Set<Position> expected = getPositions(min, max);

        Set<Position> actual = new HashSet<>();
        for (Position position : min.getIterator(max.add(-1, -1))) {
            actual.add(position);
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        assertEquals("0 0", ORIGIN.toString());
        assertEquals("-100 200", new Position(-100, 200).toString());
    }
}
