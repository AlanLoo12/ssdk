package model;

import java.util.*;

/**
 * An inventory that player can hold
 */
public class Inventory extends Observable {
    private Map<InventoryItem, Integer> inventory;
    private List<InventoryItem> itemsOrder;

    /**
     * Create a new empty inventory
     */
    Inventory() {
        inventory = new HashMap<>();
        itemsOrder = new ArrayList<>();
    }

    /**
     * Add the given inventory item to this inventory
     * @param inventoryItem inventory item to be added
     */
    void add(InventoryItem inventoryItem) {
        add(inventoryItem, 1);
    }

    /**
     * Produce the quantity of the given inventory item currently present
     * @param inventoryItem inventory item to count
     * @return quantity of the given inventory item currently present
     */
    public int get(InventoryItem inventoryItem) {
        return inventory.getOrDefault(inventoryItem, 0);
    }

    public Set<InventoryItem> keySet() {
        return inventory.keySet();
    }

    int size() {
        return inventory.size();
    }

    Optional<InventoryItem> get(int selectedInventoryItem) {
        if (itemsOrder.size() <= selectedInventoryItem) {
            return Optional.empty();
        } else {
            return Optional.of(itemsOrder.get(selectedInventoryItem));
        }
    }

    /**
     * Remove the specified quantity of the given inventory item from this inventory
     *
     * <p>
     *     If given item is not present in the inventory, do nothing.
     *
     *     If the given quantity is larger than the amount of item in the invenory,
     *     nullify the amount present
     * </p>
     * @param inventoryItem item to be reduced
     * @param quantity how many items should be taken
     */
    void take(InventoryItem inventoryItem, int quantity) {
        if (inventory.containsKey(inventoryItem)) {
            if (inventory.get(inventoryItem) - quantity > 0) {
                inventory.put(inventoryItem, inventory.get(inventoryItem) - quantity);
            } else {
                inventory.remove(inventoryItem);
                itemsOrder.remove(inventoryItem);
            }
            setChanged();
            notifyObservers();
        }
    }

    public int getPosition(InventoryItem inventoryItem) {
        return itemsOrder.indexOf(inventoryItem);
    }

    public void add(InventoryItem inventoryItem, int quantity) {
        if (inventory.containsKey(inventoryItem)) {
            inventory.put(inventoryItem, inventory.get(inventoryItem) + quantity);
        } else {
            inventory.put(inventoryItem, quantity);
            itemsOrder.add(inventoryItem);
        }

        setChanged();
        notifyObservers();
    }
}
