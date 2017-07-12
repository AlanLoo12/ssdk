package model;

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
    }

    /**
     * Put the given item at the specified position
     * @param position position at which to store the item
     * @param item item to be stored
     */
    public void put(Position position, Item item) {
        // stub
    }

    /**
     * Remove the given entity at the specified position
     * @param position position at which to remove the entity
     */
    void remove(Position position, Item item) {
        // stub
    }

    public Set<Item> get(Position position) {
        // stub
        return null;
    }

    public boolean isWalkable(Position position) {
        return false; // stub
    }

    public boolean contains(Position position) {
        return false; // stub
    }

    public int size() {
        return 0; // stub
    }

    public boolean contains(@NotNull Position position, Item item) {
        return false; // stub
    }

    public WorldGenerator getGenerator() {
        return null; // stub
    }

    /**
     * Connect to the server at the specified location
     * @param address address of the server
     */
    public void connect(InetAddress address) throws IOException {
        // stub
    }
}
