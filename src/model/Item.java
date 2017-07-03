package model;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

/**
 * An entity that can occupy world position
 */
public enum Item {
    FLOOR(Color.DARKGREY),

    COIN(Color.GOLD),

    WATER(Color.DARKBLUE),
    PLANT(Color.GREEN),

    MOUSE(Color.SANDYBROWN),
    CAT(Color.AZURE),

    PLAYER(Color.DARKGOLDENROD),

    EXIT(Color.BLACK),

    WALL(Color.DARKRED),
    PICK_AXE(Color.rgb(183, 124, 99),
    player -> {
        Position selectedPosition = player.getPosition().add(player.getLookDirection());

        if (World.getInstance().contains(selectedPosition, WALL)) {
            World.getInstance().remove(selectedPosition, WALL);
            player.getInventory().add(WALL);
        }

        return true; // stub
    });

    private Color color;
    private Function<Player, Boolean> applyFunc;

    Item(Color color) {
        this.color = color;

        applyFunc = (player -> {
            Position selectedPosition = player.getPosition().add(player.getLookDirection());
            if (World.getInstance().contains(selectedPosition, this)) {
                return false;
            } else {
                player.getInventory().take(this, 1);
                World.getInstance().add(selectedPosition, this);
                return true;
            }
        });
    }

    Item(Color color, Function<Player, Boolean> applyFunc) {
        this.color = color;
        this.applyFunc = applyFunc;
    }

    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    public void use(Player player) {
        applyFunc.apply(player);
    }

    @Contract(pure = true)
    public boolean isWalkable() {
        return !this.equals(WALL);
    }

    public int getIndex() {
        int indexSoFar = 0;
        for (Item item : Item.values()) {
            if (this.equals(item)) {
                return indexSoFar;
            } else {
                indexSoFar++;
            }
        }

        return -1;
    }
}
