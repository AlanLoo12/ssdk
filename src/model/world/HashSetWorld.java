package model.world;

import model.Position;
import model.item.Item;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * HashSet-based world
 */
public class HashSetWorld implements World {
    private Set<Item> world;

    public HashSetWorld() {
        world = new HashSet<>();
    }

    @Override
    public Set<Item> get(Position position) {
        return world.stream()
                .filter(item -> item.getPosition().equals(position))
                .collect(Collectors.toSet());
    }

    /**
     * Get all the items in the world inside the given area
     */
    @Override
    public Set<Item> get(Area area) {
        return world.stream()
                .filter(item -> area.contains(item.getPosition()))
                .collect(Collectors.toSet());
    }

    /**
     * Find an item according to the given search query near the given position
     */
    @Override
    public Item findNear(Position position, ItemSearchQuery itemSearchQuery) {
        return null; // TODO: finish
    }

    /**
     * Find items according to the given search query inside the given L1 radius
     */
    @Override
    public Set<Item> findInRadius(Position position, ItemSearchQuery itemSearchQuery, int radius) {
        return null; // TODO: finish
    }

    /**
     * Add the given item to the world
     */
    @Override
    public void add(Item item) {
        world.add(item);
    }

    /**
     * Remove the given item from the world
     */
    @Override
    public void remove(Item item) {
        world.remove(item);
    }
}
