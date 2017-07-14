package model.world;

import model.Item;
import model.Position;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Manages different world implementations
 *
 * The actual world data could be stored locally or on another machine
 */
public class WorldManager extends Observable {
    private static WorldManager instance;
    private World world;

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
    }

    /**
     * Put the given item at the specified position
     * @param position position at which to store the item
     * @param item item to be stored
     */
    public void put(Position position, Item item) {
        world.put(position, item);
    }

    /**
     * Remove the given entity at the specified position
     * @param position position at which to remove the entity
     */
    public void remove(Position position, Item item) {
        world.remove(position, item);
    }

    public Set<Item> get(Position position) {
        return world.get(position);
    }

    public boolean isWalkable(Position position) {
        return world.isWalkable(position);
    }

    public boolean contains(Position position) {
        return world.contains(position);
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
    public void connect(InetAddress address) throws IOException {
        world = new RemoteWorld(address);
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
}
