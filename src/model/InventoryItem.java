package model;

import javafx.scene.paint.Color;

/**
 * Anything that can be stored in the player's inventory
 */
public interface InventoryItem {
    void use(Player player);
    Color getColor();
}
