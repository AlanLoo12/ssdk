package model.world;

import model.Item;
import model.Position;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static model.Item.AIR;
import static model.Item.WALL;

/**
 * A world located on this machine
 */
public class LocalWorld extends AbstractWorld {
    private Map<Item, Set<Position>> worldLayers;

    LocalWorld() {
        worldLayers = new ConcurrentHashMap<>();

        wipeClean();
    }

    /**
     * Destroy the world and bring it to the original state
     */
    private void wipeClean() {
        worldLayers.clear();

        for (Item item : Item.values()) {
            worldLayers.put(item, new ConcurrentSkipListSet<>());
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
            WorldGenerator.addWalls(position);
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

    @Override
    public WorldGenerator getGenerator() {
        return WorldGenerator.getInstance();
    }

    /**
     * Create a map of all items inside the rectangle specified by the given positions
     *
     * @param from one of the corners
     * @param to   another corner
     * @return a map of all items inside the rectangle specified by the given positions
     */
    @Override
    public Map<Position, Set<Item>> get(Position from, Position to) {
        Map<Position, Set<Item>> map = new HashMap<>();

        for (Position position : from.getIterator(to)) {
            map.put(position, get(position));
        }

        return map;
    }
}
