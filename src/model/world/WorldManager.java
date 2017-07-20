package model.world;

import model.Item;
import model.Position;
import model.world.generator.WorldGenerator;
import model.world.storage.AbstractWorld;
import model.world.storage.LocalWorld;
import model.world.storage.RemoteWorld;
import network.Change;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Manages different world implementations
 *
 * The actual world data could be stored locally or on another machine
 */
public class WorldManager extends Observable implements Observer {
    private static WorldManager instance;
    private AbstractWorld world;
    private Change change;

    /**
     * Return an instance of the world, if there is one
     * Othrewise create one and return it
     * @return an instance of the world
     */
    public static WorldManager getInstance() {
        if (instance == null) {
            instance = new WorldManager();
        }

        return instance;
    }

    /**
     * Create an empty world
     */
    private WorldManager() {
        world = new LocalWorld();
        world.addObserver(this);
    }

    /**
     * Put the given item at the specified position
     * @param position position at which to store the item
     * @param item item to be stored
     */
    public synchronized boolean put(Position position, Item item) {
        change = Change.getAddChange(position, item);
        return world.put(position, item);
    }

    /**
     * Remove the given entity at the specified position
     * @param position position at which to remove the entity
     */
    public synchronized void remove(Position position, Item item) {
        change = Change.getRemoveChange(position, item);
        world.remove(position, item);
    }

    public Set<Item> get(Position position) {
        return world.get(position);
    }

    public boolean isWalkable(Position position) {
        return world.isWalkable(position);
    }

    @Deprecated
    public int size() {
        return 0;
    }

    public boolean contains(@NotNull Position position, Item item) {
        return world.contains(position, item);
    }

    // TODO: should we be able to access remote generator?
    public WorldGenerator getGenerator() {
        return world.getGenerator();
    }

    /**
     * Connect to the server at the specified location
     * @param address address of the server
     */
    public void connect(InetAddress address, int port) throws IOException {
        world = new RemoteWorld(address, port);
    }

    /**
     * Create a map of all items inside the rectangle specified by the given positions
     * @param from one of the corners
     * @param to another corner
     * @return a map of all items inside the rectangle specified by the given positions
     */
    public Map<Position, Set<Item>> get(Position from, Position to) {
        return world.get(from, to);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    /**
     * Produce the positions of all items with the given type
     * @param item items to search for
     * @return positions of all items with the given type
     */
    public Set<Position> get(Item item) {
        return world.get(item);
    }

    /**
     * Find the specified item near the given position and
     * maybe produce the position of the found item
     * @param position position around which to search for
     * @param item item to search for
     * @param maxActiveDistance maximum L1 distance to be searched for
     * @return requested item
     */
    @NotNull
    public Optional<Position> findNear(Position position,
                                       Item item,
                                       int maxActiveDistance,
                                       boolean ignoreCenter) {
        Set<Position> visited = new HashSet<>();
        Queue<Position> todo = new LinkedList<>();

        if (ignoreCenter) {
            todo.addAll(position.getNeighbours());
            visited.add(position);
        } else {
            todo.add(position);
        }

        while (!todo.isEmpty()) {
            Position currentPosition = todo.poll();

            if (!visited.contains(currentPosition)) {
                if (world.contains(currentPosition, item)) {
                    return Optional.of(currentPosition);
                }

                todo.addAll(currentPosition.getNeighbours());
                visited.add(currentPosition);
            }
        }

        return Optional.empty();
    }

    /**
     * Same as put, but only places the item if the givne position is walkable
     * @param position position at which to store the item
     * @param item item to be stored
     * @return true if the item was stored, false otherwise
     */
    public boolean safePut(@NotNull Position position, Item item) {
        return isWalkable(position) && put(position, item);
    }

    public Optional<Position> findNear(Position position, Item item, int distance) {
        return findNear(position, item, distance, false);
    }

    public Change getChange() {
        return change;
    }

    /**
     * Commits given change to this world
     * @param change change to be done
     */
    public void commitChange(Change change) {
        switch (change.getType()) {
            case ADD:
                put(change.getPosition(), change.getItem());
                break;
            case REMOVE:
                remove(change.getPosition(), change.getItem());
                break;
        }
    }
}
