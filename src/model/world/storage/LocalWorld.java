package model.world.storage;

import model.item.Air;
import model.item.Item;
import model.Position;
import model.item.Wall;
import model.world.generator.WorldGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static model.item.ItemEnum.AIR;
import static model.item.ItemEnum.WALL;

/**
 * A world located on this machine
 *
 * INVARIANT: Either a wall or air occupies every active position
 * // TODO: check invariant
 */
public class LocalWorld extends AbstractWorld {
    private Set<Item> world;

    public LocalWorld() {
        world = new HashSet<>();

        wipeClean();
    }

    /**
     * Destroy the world and bring it to the original state
     */
    private void wipeClean() {
        world.clear();
    }

    /**
     * Put the given item at specified position
     *
     * @param item     item to store
     */
    @Override
    public boolean add(@NotNull Item item) {
        if (item instanceof Wall) {
            world.remove(new Air(item.getPosition()));
        }
        if (item instanceof Air) {
            world.remove(new Wall(item.getPosition()));
        }

        boolean returnVal = world.add(item);

        setChanged();
        notifyObservers(item);

        return returnVal;
    }

    /**
     * Remove the item from the specified position, if item exists.
     * Otherwise, do nothing
     *
     * @param item     item to be removed
     */
    @Override
    public void remove(@NotNull Item item) {
        if (item.equals(WALL)) {
            WorldGenerator.addWalls(item.getPosition());
            add(AIR);
        }

        if (world.remove(item)) {
            setChanged();
            notifyObservers(item);
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

        for (Item item : world) {
            if (item.getPosition().equals(position)) {
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
        return (world.contains(new Air(position)) &&
                !world.contains(new Wall(position)));
    }

    /**
     * Produce true if given item is present at the specified position,
     * false otherwise
     *
     * @param item     item to look for
     * @return true if given item is present at the specified position,
     * false otherwise
     */
    @Override
    public boolean contains(@NotNull Item item) {
        return world.contains(item);
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

    /**
     * Produce true if given position was initialized
     *
     * @param position position to check for
     * @return true if given position was initialized
     */
    @Override
    public boolean contains(@NotNull Position position) {
        for (Item item : world) {
            if (item.getPosition().equals(position)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addAll(Set<Item> items) {
        world.addAll(items);
    }
}
