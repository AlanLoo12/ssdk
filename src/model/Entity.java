package model;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.Contract;

/**
 * An entity that can occupy world position
 */
public enum Entity {
    PLAYER(true, Color.DARKGOLDENROD),
    COIN(true, Color.DARKBLUE),
    DUST(true, Color.DARKGREY),
    WALL(false, Color.RED),
    EXIT(true, Color.INDIGO);

    private boolean isWalkable;
    private Color color;

    Entity(boolean isWalkable, Color color) {
        this.isWalkable = isWalkable;
        this.color = color;
    }

    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    @Contract(pure = true)
    public boolean isWalkable() {
        return isWalkable;
    }
}
