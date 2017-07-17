package model.world.storage;

import model.Item;
import model.Position;
import model.world.generator.WorldGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 * A world where all the fun stuff happens
 */
public abstract class AbstractWorld extends Observable {
    /**
     * Put the given item at specified position
     * @param position position at which to store the item
     * @param item item to store
     * @return true if the world was modified, false otherwise
     */
    public abstract boolean put(@NotNull Position position, @NotNull Item item);

    /**
     * Remove the item from the specified position, if item exists.
     * Otherwise, do nothing
     * @param position position at which to remove the item from
     * @param item item to be removed
     */
    public abstract void remove(@NotNull Position position, @NotNull Item item);

    /**
     * Get all items at the given position
     * @param position position from which to fetch the items
     * @return all items at the given position
     */
    @NotNull
    public abstract Set<Item> get(@NotNull Position position);

    /**
     * Produce true if specified position is walkable by player,
     * false otherwise
     * @param position position to check
     * @return true if specified position is walkable by player,
     * false otherwise
     */
    public abstract boolean isWalkable(@NotNull Position position);

    /**
     * Produce true if given item is present at the specified position,
     * false otherwise
     * @param position position to check
     * @param item item to look for
     * @return true if given item is present at the specified position,
     * false otherwise
     */
    public abstract boolean contains(@NotNull Position position, @NotNull Item item);

    /**
     * Produce the world generator
     * @return world generator
     */
    public abstract WorldGenerator getGenerator();

    /**
     * Create a map of all items inside the rectangle specified by the given positions
     * @param from one of the corners
     * @param to another corner
     * @return a map of all items inside the rectangle specified by the given positions
     */
    public abstract Map<Position,Set<Item>> get(Position from, Position to);

    public abstract Set<Position> get(Item item);
}
