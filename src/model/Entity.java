package model;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.Contract;

/**
 * An entity that can occupy world position
 */
public enum Entity {
    PLAYER(true, Color.DARKGOLDENROD),
    FLOOR(true, Color.BEIGE);

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
