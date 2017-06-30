package model;

/**
 * Anything that can be stored in the player's inventory
 */
public interface InventoryItem {
    void use(Player player);
}
