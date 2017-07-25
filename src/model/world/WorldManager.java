package model.world;

import model.item.Item;
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
public class WorldManager extends AbstractWorld implements Observer {
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
     * @param item item to be stored
     */
    @Override
    public synchronized boolean add(Item item) {
        change = Change.getAddChange(item.getPosition(), item);
        return world.add(item);
    }

    /**
     * Remove the given entity at the specified position
     */
    @Override
    public synchronized void remove(Item item) {
        change = Change.getRemoveChange(item.getPosition(), item);
        world.remove(item);
    }

    @Override
    public Set<Item> get(Position position) {
        return world.get(position);
    }

    @Override
    public boolean isWalkable(Position position) {
        return world.isWalkable(position);
    }

    @Deprecated
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(Item item) {
        return world.contains(item);
    }

    // TODO: should we be able to access remote generator?
    @Override
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
    @Override
    public Map<Position, Set<Item>> get(Position from, Position to) {
        return world.get(from, to);
    }

    /**
     * Produce true if given position was initialized
     *
     * @param position position to check for
     * @return true if given position was initialized
     */
    @Override
    public boolean contains(@NotNull Position position) {
        return world.contains(position);
    }

    @Override
    public void addAll(Set<Item> items) {
        world.addAll(items);
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
                if (world.contains(item)) {
                    return Optional.of(currentPosition);
                }

                todo.addAll(currentPosition.getNeighbours());
                visited.add(currentPosition);
            }
        }

        return Optional.empty();
    }

    /**
     * Same as add, but only places the item if the givne position is walkable
     * @param position position at which to store the item
     * @param item item to be stored
     * @return true if the item was stored, false otherwise
     */
    public boolean safePut(@NotNull Position position, Item item) {
        return isWalkable(position) && add(item);
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
                add(change.getItem());
                break;
            case REMOVE:
                remove(change.getItem());
                break;
        }
    }

    public void put(Item item) {
        add(item);
    }
}
