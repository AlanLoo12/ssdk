package model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A position in 2d integer space
 */
public final class Position implements Comparable {
    public static final Position ORIGIN = new Position(0, 0);
    public static final Position UP = new Position(0, -1);
    public static final Position DOWN = new Position(0, 1);
    public static final Position LEFT = new Position(-1, 0);
    public static final Position RIGHT = new Position(1, 0);
    public static final Position[] DIRECTIONS = new Position[]{UP, DOWN, LEFT, RIGHT};

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
    private boolean lessThan(Position that) {
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

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(@NotNull Object o) throws NullPointerException, ClassCastException {
        Position that = (Position) o;

        if (x != that.x) {
            return safeAdd(that.x, -x);
        } else {
            return safeAdd(that.y, -y);
        }
    }

    /**
     * Safely add a and b, avoiding the overflows
     * @return sum of a and b, or the maximum or minimum possible value of int,
     * if overflows happens
     */
    @Contract(pure = true)
    private int safeAdd(int a, int b) {
        if (a > 0 && b > 0 && a + b <= 0) {
            return Integer.MAX_VALUE;
        } else if (a < 0 && b < 0 && a + b >= 0) {
            return Integer.MIN_VALUE;
        } else {
            return a + b;
        }
    }

    /**
     * Convert this vector to the unit vector
     * @return unit vector of this vector
     */
    @NotNull
    public Position unitize() {
        if (Math.abs(x) >= Math.abs(y)) {
            return new Position(sign(x), 0);
        } else {
            return new Position(0, sign(y));
        }
    }

    @Contract(pure = true)
    private int sign(int i) {
        if (i < 0) {
            return -1;
        } else if (i > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Produce the Moore neighborhood of this position
     * @return Moore neighborhood of this position
     */
    public Collection<? extends Position> getNeighbours() {
        Set<Position> neighbours = new HashSet<>();
        neighbours.add(this.add(UP));
        neighbours.add(this.add(DOWN));
        neighbours.add(this.add(LEFT));
        neighbours.add(this.add(RIGHT));
        neighbours.add(this.add(UP).add(RIGHT));
        neighbours.add(this.add(DOWN).add(LEFT));
        neighbours.add(this.add(LEFT).add(UP));
        neighbours.add(this.add(RIGHT).add(DOWN));

        return neighbours;
    }

    public Position divide(int factor) {
        return new Position(x / factor, y / factor);
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
