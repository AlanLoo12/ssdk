package model;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static model.Item.WALL;

/**
 * A 2d integer array of enumerable objects
 *
 * World consists of several layers:
 *      * floor
 *      * wall
 *      * items
 *      * creatures
 *      * plants
 *      * environment
 *
 *      Both floor and wall can contain nodes of type Item (they must be not passable)
 *      I.e. player should not fall under the floor and should not pass through the walls
 *
 *      Items can contain nodes of type Item (things like food, bones, socks, ...)
 *
 *      Plants can contain things of type Plant
 *
 *      Creatures can contain things of type Creature (player, mobs, NPC, ...)
 *
 *      Environment can contain things of type Environment (gas, water, lava, ...)
 *
 *      Invariant: at each moment, one position at each layer can contain only one thing.
 *      I.e. at maximum, there could be 6 things of different types at each position
 */
public class World extends Observable {
    /**
     * The set of all walkable positions
     */
    private Set<Position> activePositions;
    private Map<Position, Item> world;
    private boolean isEnded;
    private boolean win;

    private static World instance;

    /**
     * Create an empty world, with no walkable nodes
     */
    private World() {
        activePositions = new HashSet<>();
        world = new HashMap<>();
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }

        return instance;
    }

    void setActive(Position position, boolean active) {
        if (active) {
            activePositions.add(position);
        } else {
            activePositions.remove(position);
        }

        setChanged();
        notifyObservers(position);
    }

    /**
     * Put the given item at the specified position
     * @param position position at which to store the item
     * @param item item to be stored
     */
    void put(Position position, Item item) {
        activateNeighbours(position);
        world.put(position, item);

        setChanged();
        notifyObservers(position);
    }

    private void activateNeighbours(Position position) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Position positionTry = position.add(x,y);
                if (!activePositions.contains(positionTry)) {
                    activePositions.add(positionTry);
                    world.put(positionTry, WALL);
                }
            }
        }
    }

    /**
     * Remove the given entity at the specified position
     * @param position position at which to remove the entity
     */
    void remove(Position position) {
        if (world.containsKey(position)) {
            activateNeighbours(position);
            world.remove(position);

            setChanged();
            notifyObservers(position);
        }
    }

    /**
     * Produce the set of all occupied positions
     * @return the set of all occupied positions
     */
    public Set<Position> getActivePositions() {
        return activePositions;
    }

    public Optional<Item> get(Position position) {
        if (world.containsKey(position)) {
            return Optional.of(world.get(position));
        } else {
            return Optional.empty();
        }
    }

    boolean isWalkable(Position position) {
        return activePositions.contains(position) &&
                (!world.containsKey(position) ||
                        world.get(position).isWalkable());
    }

    /**
     * Ends the world
     * @param win if true, then it's a win, else its a lost
     */
    void end(boolean win) {
        isEnded = true;
        this.win = win;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public boolean isWin() {
        return win;
    }

    boolean isActive(@NotNull Position position) {
        return activePositions.contains(position);
    }

    public Collection<? extends Position> getWalkablePositions() {
        return activePositions.stream().filter(this::isWalkable).collect(Collectors.toSet());
    }

    boolean contains(Position position) {
        return world.containsKey(position);
    }

    public int size() {
        return world.size();
    }
}
