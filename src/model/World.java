package model;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A 2d integer array of enumerable objects
 */
public class World extends Observable {
    /**
     * The set of all walkable positions
     */
    private Set<Position> activePositions;
    private Map<Position, Entity> world;
    private boolean isEnded;
    private boolean win;

    /**
     * Create an empty world, with no walkable nodes
     */
    public World() {
        activePositions = new HashSet<>();
        world = new HashMap<>();
    }

    void setActive(Position position, boolean active) {
        if (active) {
            activePositions.add(position);
        } else {
            activePositions.remove(position);
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Put the given entity at the specified position
     * @param position position at which to store the entity
     * @param entity entity to be stored
     */
    public void put(Position position, Entity entity) {
        world.put(position, entity);

        setChanged();
        notifyObservers();
    }

    /**
     * Remove the given entity at the specified position
     * @param position position at which to remove the entity
     */
    void remove(Position position) {
        if (world.containsKey(position)) {
            world.remove(position);

            setChanged();
            notifyObservers();
        }
    }

    /**
     * Produce the set of all occupied positions
     * @return the set of all occupied positions
     */
    public Set<Position> getActivePositions() {
        return activePositions;
    }

    public Optional<Entity> get(Position position) {
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
}
