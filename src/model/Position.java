package model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A position in 2d integer space
 */
public final class Position {
    public static final Position ORIGIN = new Position(0,0);
    public static final Position UP = new Position(0,-1);
    public static final Position DOWN = new Position(0,1);
    public static final Position LEFT = new Position(-1,0);
    public static final Position RIGHT = new Position(1,0);

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

    public Position add(Position position) {
        return this.add(position.x, position.y);
    }

    @NotNull
    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }
}
