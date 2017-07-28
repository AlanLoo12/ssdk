package model.world.area;

import model.Position;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RectangularAreaTest {
    private static final int MAX_RANDOM_POSITIONS = 100;
    private static final int BOUND = 20;

    //  ___________________________________________________________________
    // |                        _                   _                      |
    // |         _ __    _ __  (_) __   __   __ _  | |_    ___             |
    // |        | '_ \  | '__| | | \ \ / /  / _` | | __|  / _ \            |
    // |        | |_) | | |    | |  \ V /  | (_| | | |_  |  __/            |
    // |        | .__/  |_|    |_|   \_/    \__,_|  \__|  \___|            |
    // |        |_|                                                        |
    // |___________________________________________________________________|

    @FunctionalInterface
    private interface PositionGenerator {
        Position get();
    }

    private void testPositions(Set<Position> expectedToContain, Area area, PositionGenerator positionGenerator) {
        for (int i = 0; i < MAX_RANDOM_POSITIONS; i++) {
            Position randomPosition = positionGenerator.get();
            while (expectedToContain.contains(randomPosition)) {
                randomPosition = positionGenerator.get();
            }

            assertFalse(area.contains(randomPosition));
        }
    }

    private void testArea(Area area, Set<Position> expectedToContain) {
        for (Position position : expectedToContain) {
            assertTrue(area.contains(position));
        }

        testPositions(expectedToContain, area, Position::getRandomPosition);
        testPositions(expectedToContain, area, () -> Position.getRandomPosition(BOUND));
    }

    //  ___________________________________________________________________
    // |                        _       _   _                              |
    // |        _ __    _   _  | |__   | | (_)   ___                       |
    // |       | '_ \  | | | | | '_ \  | | | |  / __|                      |
    // |       | |_) | | |_| | | |_) | | | | | | (__                       |
    // |       | .__/   \__,_| |_.__/  |_| |_|  \___|                      |
    // |       |_|                                                         |
    // |___________________________________________________________________|

    @Test
    public void testSizeOne() {
        Area area = AreaFactory.getRectangle(Position.ORIGIN, 1, 1);
        Set<Position> expectedToContain = new HashSet<>();
        expectedToContain.add(new Position(0,0));

        testArea(area, expectedToContain);
    }

    @Test
    public void testSizeTwo() {
        Area area = AreaFactory.getRectangle(Position.ORIGIN, 2, 2);
        Set<Position> expectedToContain = new HashSet<>();
        expectedToContain.add(new Position(0,0));
        expectedToContain.add(new Position(0,1));
        expectedToContain.add(new Position(1,0));
        expectedToContain.add(new Position(1,1));

        testArea(area, expectedToContain);
    }

    @Test
    public void testSizeTwoNotOrigin() {
        Area area = AreaFactory.getRectangle(new Position(122, 300), 2, 2);
        Set<Position> expectedToContain = new HashSet<>();
        expectedToContain.add(new Position(122,300));
        expectedToContain.add(new Position(122,301));
        expectedToContain.add(new Position(123,300));
        expectedToContain.add(new Position(123,301));

        testArea(area, expectedToContain);
    }
}
