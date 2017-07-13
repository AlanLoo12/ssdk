package model;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A world where all the fun stuff happens
 */
public interface World {
    /**
     * Put the given item at specified position
     * @param position position at which to store the item
     * @param item item to store
     */
    void put(@NotNull Position position, @NotNull Item item);

    /**
     * Remove the item from the specified position, if item exists.
     * Otherwise, do nothing
     * @param position position at which to remove the item from
     * @param item item to be removed
     */
    void remove(@NotNull Position position, @NotNull Item item);

    /**
     * Get all items at the given position
     * @param position position from which to fetch the items
     * @return all items at the given position
     */
    @NotNull Set<Item> get(@NotNull Position position);

    /**
     * Produce true if specified position is walkable by player,
     * false otherwise
     * @param position position to check
     * @return true if specified position is walkable by player,
     * false otherwise
     */
    boolean isWalkable(@NotNull Position position);

    /**
     * Produce true if the given position contains any items at all,
     * false otherwise
     * @param position position to check
     * @return true if the given position contains any items at all,
     * false otherwise
     */
    boolean contains(@NotNull Position position);

    /**
     * Produce true if given item is present at the specified position,
     * false otherwise
     * @param position position to check
     * @param item item to look for
     * @return true if given item is present at the specified position,
     * false otherwise
     */
    boolean contains(@NotNull Position position, @NotNull Item item);

    WorldGenerator getGenerator();
}
