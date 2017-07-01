package model;

import com.sun.org.apache.xpath.internal.functions.FunctionOneArg;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

/**
 * An entity that can occupy world position
 */
public enum Entity implements InventoryItem {
    PLAYER(true, Color.DARKGOLDENROD),
    COIN(true, Color.DARKBLUE),
    DUST(true, Color.DARKGREY),
    WALL(false, Color.RED),
    EXIT(true, Color.INDIGO);

    private boolean isWalkable;
    private Color color;
    private Function<Player, Boolean> applyFunc;

    Entity(boolean isWalkable, Color color) {
        this.isWalkable = isWalkable;
        this.color = color;

        applyFunc = (player -> {
            //Position selectedPosition = player.getSelectedPosition();
            return false; // stub
        });
    }

    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    @Contract(pure = true)
    public boolean isWalkable() {
        return isWalkable;
    }

    @Override
    public void use(Player player) {
        applyFunc.apply(player);
    }
}
