package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 * An inventory that player can hold
 */
public class Inventory extends Observable {
    private Map<InventoryItem, Integer> inventory;

    /**
     * Create a new empty inventory
     */
    Inventory() {
        inventory = new HashMap<>();
    }

    /**
     * Add the given inventory item to this inventory
     * @param inventoryItem inventory item to be added
     */
    void add(InventoryItem inventoryItem) {
        if (inventory.containsKey(inventoryItem)) {
            inventory.put(inventoryItem, inventory.get(inventoryItem) + 1);
        } else {
            inventory.put(inventoryItem, 1);
        }

        setChanged();
        notifyObservers();
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
}
