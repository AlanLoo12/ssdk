package model;

import javafx.scene.paint.Color;

import java.util.function.Function;

/**
 * A thing in the world that does not occupy as much space as a whole block would
 */
public enum Item implements InventoryItem {
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

    Item(Color color, Function<Player, Boolean> applyFunc) {
        this.color = color;
        this.applyFunc = applyFunc;
    }

    @Override
    public void use(Player player) {
        applyFunc.apply(player);
    }

    @Override
    public Color getColor() {
        return color;
    }
}
