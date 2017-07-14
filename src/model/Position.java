package model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * A position in 2d integer space
 */
public final class Position {
    public static final Position ORIGIN = new Position(0, 0);
    public static final Position UP = new Position(0, -1);
    public static final Position DOWN = new Position(0, 1);
    public static final Position LEFT = new Position(-1, 0);
    public static final Position RIGHT = new Position(1, 0);

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Contract(pure = true)
    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return x == position.x && y == position.y;
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Contract(pure = true)
    public int getY() {
        return y;
    }

    @NotNull
    public Position add(Position position) {
        return this.add(position.x, position.y);
    }

    @NotNull
    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }

    @NotNull
    public Position multiply(int scale) {
        return new Position(x * scale, y * scale);
    }

    /**
     * Produce true if this position is less than or equal to that position
     *
     * @param that another position
     * @return true if this position is less than or equal to that position
     */
    @Contract(pure = true)
    boolean lessThanOrEqualTo(Position that) {
        return (x <= that.x && y <= that.y);
    }

    /**
     * Increment this position by one in L1 metric so that it becomes closer to max
     *
     * @param max position to approach
     */
    @NotNull Position add1(@NotNull Position max) {
        if (max.x > x) {
            return this.add(1, 0);
        } else if (max.x < x) {
            return this.add(-1, 0);
        } else if (max.y > y) {
            return this.add(0, 1);
        } else if (max.y < y) {
            return this.add(0, -1);
        } else {
            return this;
        }
    }

    /**
     * Increment this position by one in L1 metric so that it becomes closer to max,
     * <p>
     * all positions in the rectangle between min and max must be covered
     *
     * @param min starting position
     * @param max position to approach
     */
    public Position add1(Position min, Position max) {
        if (max.x > x) {
            return this.add(1, 0);
        } else if (max.x < x) {
            return this.add(-1, 0);
        }

        int newX = min.x;

        if (max.y > y) {
            return new Position(newX, y + 1);
        } else if (max.y < y) {
            return new Position(newX, y - 1);
        } else {
            return this;
        }
    }

    /**
     * Produce true if this position is less than that position
     *
     * @param that another position
     * @return true if this position is less than that position
     */
    @Contract(pure = true)
    public boolean lessThan(Position that) {
        return (x < that.x && y < that.y);
    }

    /**
     * Produce an iterator that would contain all the positions from this to that, inclusive
     *
     * @param that the end point
     * @return an iterator that would contain all the positions from min to max, inclusive
     */
    @NotNull
    @Contract(pure = true)
    public Iterable<Position> getIterator(Position that) {
        return () -> new PositionIterator(this, that);
    }

    private static class PositionIterator implements Iterator<Position> {
        Position min;
        Position max;
        Position next;
        boolean hasNext;

        PositionIterator(Position min, Position max) {
            this.min = min;
            this.max = max;
            next = min;
            hasNext = true;
        }

        @Override
        public boolean hasNext() {
            boolean current = hasNext;
            if (next.equals(max)) {
                hasNext = false;
            }
            return current;
        }

        @Override
        public Position next() {
            Position current = next;
            next = next.add1(min, max);

            return current;
        }
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
