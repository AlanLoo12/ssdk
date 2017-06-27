package model;

import java.util.*;

import static model.Entity.EXIT;
import static model.Entity.FLOOR;

/**
 * A 2d integer array of enumerable objects
 */
public class World extends Observable {
    private static final int MAX_ANGLE = (int) (40 * Math.PI);
    private static final int MAX_LENGTH = 100000;
    private static final Random RANDOM = new Random();
    private Map<Position, Entity> world;

    /**
     * Create an empty world, with no walkable nodes
     */
    public World() {
        world = new HashMap<>();

        generate();
        //generateSpiral();
    }

    /**
     * The ultimate world generator
     */
    private void generate() {
        Position position = new Position(0,0);
        for (int i = 0; i < MAX_LENGTH; i++) {
            put(position, FLOOR);

            position = nextPosition(position);
        }

        put(position, EXIT);
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

    private void generateSpiral() {
        for (float i = 0; i < MAX_ANGLE; i = i + 0.1f) {
            put(new Position(
                    (int)((i + 2)*Math.cos(i)),
                    (int)((i + 2)*Math.sin(i))
            ), FLOOR);
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
        return world.keySet();
    }

    public Optional<Entity> get(Position position) {
        return Optional.of(world.get(position));
    }

    boolean isWalkable(Position position) {
        return world.containsKey(position) && world.get(position).isWalkable();
    }
}
