package model;

import javafx.scene.paint.Color;
import model.world.WorldManager;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

/**
 * An entity that can occupy world position
 */
public enum Item {
    AIR(Color.DARKGREY),

    WATER(Color.ROYALBLUE),

    PLAYER(Color.DARKGOLDENROD),

    MUSHROOM(Color.MEDIUMVIOLETRED),

    WALL(Color.DARKRED),
    PICK_AXE(Color.rgb(183, 124, 99),
    player -> {
        Position selectedPosition = player.getPosition().add(player.getLookDirection());

        if (WorldManager.getInstance().contains(selectedPosition, WALL)) {
            WorldManager.getInstance().remove(selectedPosition, WALL);
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
            if (WorldManager.getInstance().contains(selectedPosition, this)) {
                return false;
            } else {
                player.getInventory().take(this, 1);
                WorldManager.getInstance().put(selectedPosition, this);
                return true;
            }
        });
    }

    Item(Color color, Function<Player, Boolean> applyFunc) {
        this.color = color;
        this.applyFunc = applyFunc;
    }

    /**
     * Produce regular expression that would match all the valid item names
     * @return regular expression that would match all the valid item names
     */
    public static String getValidNamesRegex() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");

        for (Item item : Item.values()) {
            stringBuilder.append(item);
            stringBuilder.append("|");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    public void use(Player player) {
        applyFunc.apply(player);
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
