package model;

import java.util.*;

import static model.Entity.WALL;

/**
 * A 2d integer array of enumerable objects
 */
public class World extends Observable {
    private static final int MAX_ANGLE = (int) (40 * Math.PI);
    private Map<Position, List<Entity>> world;

    /**
     * Create an empty world, with no walkable nodes
     */
    public World() {
        world = new HashMap<>();

        generateSpiral();
    }

    private void generateSpiral() {
        for (float i = 0; i < MAX_ANGLE; i = i + 0.1f) {
            put(new Position(
                    (int)((i + 2)*Math.cos(i)),
                    (int)((i + 2)*Math.sin(i))
            ), WALL);
        }
    }

    /**
     * Put the given entity at the specified position
     * @param position position at which to store the entity
     * @param entity entity to be stored
     */
    public void put(Position position, Entity entity) {
        if (world.containsKey(position)) {
            world.get(position).add(entity);
        } else {
            List<Entity> entities = new ArrayList<>();
            entities.add(entity);

            world.put(position, entities);
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Remove the given entity at the specified position
     * @param position position at which to remove the entity
     * @param entity entity to be removed
     */
    public void remove(Position position, Entity entity) {
        if (world.containsKey(position)) {
            List<Entity> entities = world.get(position);

            entities.remove(entity);

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

    public List<Entity> get(Position position) {
        return world.get(position);
    }

    public boolean isWalkable(Position position) {
        if (!world.containsKey(position)) {
            return true;
        } else {
            for (Entity entity : world.get(position)) {
                if (!entity.isWalkable()) {
                    return false;
                }
            }
            return true;
        }
    }
}
