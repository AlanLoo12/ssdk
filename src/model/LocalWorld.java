package model;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static model.Item.AIR;
import static model.Item.WALL;

/**
 * A world located on this machine
 */
public class LocalWorld extends Observable implements World {
    private Map<Item, Set<Position>> worldLayers;
    private WorldGenerator generator;

    public LocalWorld() {
        worldLayers = new HashMap<>();
        generator = new WorldGenerator(this);

        wipeClean();
    }

    /**
     * Destroy the world and bring it to the original state
     */
    private void wipeClean() {
        worldLayers.clear();

        for (Item item : Item.values()) {
            worldLayers.put(item, new HashSet<>());
        }
    }

    /**
     * Put the given item at specified position
     *
     * @param position position at which to store the item
     * @param item     item to store
     */
    @Override
    public void put(@NotNull Position position, @NotNull Item item) {
        if (item.equals(WALL)) {
            worldLayers.get(AIR).remove(position);
        }
        if (item.equals(AIR)) {
            worldLayers.get(WALL).remove(position);
        }

        worldLayers.get(item).add(position);

        setChanged();
        notifyObservers(position);
    }

    /**
     * Remove the item from the specified position, if item exists.
     * Otherwise, do nothing
     *
     * @param position position at which to remove the item from
     * @param item     item to be removed
     */
    @Override
    public void remove(@NotNull Position position, @NotNull Item item) {
        if (item.equals(WALL)) {
            generator.addWalls(position);
            put(position, AIR);
        }

        if (worldLayers.get(item).remove(position)) {
            setChanged();
            notifyObservers(position);
        }
    }

    /**
     * Get all items at the given position
     *
     * @param position position from which to fetch the items
     * @return all items at the given position
     */
    @Override
    public @NotNull Set<Item> get(@NotNull Position position) {
        Set<Item> items = new HashSet<>();

        for (Item item : worldLayers.keySet()) {
            if (worldLayers.get(item).contains(position)) {
                items.add(item);
            }
        }

        return items;
    }

    /**
     * Produce true if specified position is walkable by player,
     * false otherwise
     *
     * @param position position to check
     * @return true if specified position is walkable by player,
     * false otherwise
     */
    @Override
    public boolean isWalkable(@NotNull Position position) {
        return (worldLayers.get(AIR).contains(position) &&
                !worldLayers.get(WALL).contains(position));
    }

    /**
     * Produce true if the given position contains any items at all,
     * false otherwise
     *
     * @param position position to check
     * @return true if the given position contains any items at all,
     * false otherwise
     */
    @Override
    public boolean contains(@NotNull Position position) {
        for (Set<Position> layer : worldLayers.values()) {
            if (layer.contains(position)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Produce true if given item is present at the specified position,
     * false otherwise
     *
     * @param position position to check
     * @param item     item to look for
     * @return true if given item is present at the specified position,
     * false otherwise
     */
    @Override
    public boolean contains(@NotNull Position position, @NotNull Item item) {
        return worldLayers.get(item).contains(position);
    }
}
