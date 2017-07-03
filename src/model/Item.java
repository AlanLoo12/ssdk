package model;

import javafx.scene.paint.Color;
import model.Player;
import model.Position;
import model.World;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

/**
 * An entity that can occupy world position
 */
public enum Item {
    PLAYER(Color.DARKGOLDENROD),
    MOUSE(Color.SANDYBROWN),
    CAT(Color.AZURE),
    COIN(Color.GOLD),
    DUST(Color.DARKGREY),
    WALL(Color.RED),
    PLANT(Color.GREEN),
    WATER(Color.DARKBLUE),
    EXIT(Color.BLACK),
    PICK_AXE(Color.rgb(183, 124, 99),
            player -> {
                Position selectedPosition = player.getPosition().add(player.getLookDirection());

                World.getInstance()
                        .get(selectedPosition)
                        .filter(entity -> !entity.isWalkable())
                        .ifPresent(entity -> {
                            World.getInstance().remove(selectedPosition);
                            player.getInventory().add(entity);
                        });

                return true; // stub
            });

    private Color color;
    private Function<Player, Boolean> applyFunc;

    Item(Color color) {
        this.color = color;

        applyFunc = (player -> {
            Position selectedPosition = player.getPosition().add(player.getLookDirection());
            if (World.getInstance().contains(selectedPosition)) {
                return false;
            } else {
                player.getInventory().take(this, 1);
                World.getInstance().put(selectedPosition, this);
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
}
