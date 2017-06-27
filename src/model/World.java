package model;

import java.util.*;

import static model.Entity.EXIT;
/**
 * A 2d integer array of enumerable objects
 */
public class World extends Observable {
    private static final int MAX_LENGTH = 100000;
    private static final Random RANDOM = new Random();
    /**
     * The set of all walkable positions
     */
    private Set<Position> isWalkable;
    private Map<Position, Entity> world;

    /**
     * Create an empty world, with no walkable nodes
     */
    public World() {
        isWalkable = new HashSet<>();
        world = new HashMap<>();

        generate();
    }

    /**
     * The ultimate world generator
     */
    private void generate() {
        Position position = new Position(0,0);
        for (int i = 0; i < MAX_LENGTH; i++) {
            setWalkable(position, true);

            position = nextPosition(position);
        }

        put(position, EXIT);
    }

    private void setWalkable(Position position, boolean walkable) {
        if (walkable) {
            isWalkable.add(position);
        } else {
            isWalkable.remove(position);
        }
    }

    private Position nextPosition(Position position) {
        switch (RANDOM.nextInt(4)) {
            case 0:
                return new Position(position.getX() + 1, position.getY());
            case 1:
                return new Position(position.getX() - 1, position.getY());
            case 2:
                return new Position(position.getX(), position.getY() + 1);
            case 3:
                return new Position(position.getX(), position.getY() - 1);
            default:
                return position;
        }
    }

    /**
     * Put the given entity at the specified position
     * @param position position at which to store the entity
     * @param entity entity to be stored
     */
    void put(Position position, Entity entity) {
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
        return isWalkable;
    }

    public Optional<Entity> get(Position position) {
        if (world.containsKey(position)) {
            return Optional.of(world.get(position));
        } else {
            return Optional.empty();
        }
    }

    boolean isWalkable(Position position) {
        return isWalkable.contains(position);
    }
}
