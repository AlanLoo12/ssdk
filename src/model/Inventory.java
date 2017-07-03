package model;

import java.util.*;

/**
 * An inventory that player can hold
 */
public class Inventory extends Observable {
    private Map<Item, Integer> inventory;
    private List<Item> itemsOrder;

    /**
     * Create a new empty inventory
     */
    Inventory() {
        inventory = new HashMap<>();
        itemsOrder = new ArrayList<>();
    }

    /**
     * Add the given inventory item to this inventory
     * @param item inventory item to be added
     */
    public void add(Item item) {
        add(item, 1);
    }

    /**
     * Produce the quantity of the given inventory item currently present
     * @param item inventory item to count
     * @return quantity of the given inventory item currently present
     */
    public int get(Item item) {
        return inventory.getOrDefault(item, 0);
    }

    public Set<Item> keySet() {
        return inventory.keySet();
    }

    int size() {
        return inventory.size();
    }

    Optional<Item> get(int selectedItem) {
        if (itemsOrder.size() <= selectedItem) {
            return Optional.empty();
        } else {
            return Optional.of(itemsOrder.get(selectedItem));
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
     * @param item item to be reduced
     * @param quantity how many items should be taken
     */
    void take(Item item, int quantity) {
        if (inventory.containsKey(item)) {
            if (inventory.get(item) - quantity > 0) {
                inventory.put(item, inventory.get(item) - quantity);
            } else {
                inventory.remove(item);
                itemsOrder.remove(item);
            }
            setChanged();
            notifyObservers();
        }
    }

    public int getPosition(Item item) {
        return itemsOrder.indexOf(item);
    }

    public void add(Item item, int quantity) {
        if (inventory.containsKey(item)) {
            inventory.put(item, inventory.get(item) + quantity);
        } else {
            inventory.put(item, quantity);
            itemsOrder.add(item);
        }

        setChanged();
        notifyObservers();
    }
}
