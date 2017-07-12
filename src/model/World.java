package model;

import network.Client;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import static model.Item.AIR;
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
 *      <p>
 *          Better idea: for each Item, generate a level and simply store positions which have that item
 *      </p>
 */
public class World extends Observable {
    private Map<Item, Set<Position>> worldLayers;
    private boolean isEnded;
    private boolean win;

    private static World instance;
    private WorldGenerator generator;
    private Client client;

    /**
     * Return an instance of the world, if there is one
     * Othrewise create one and return it
     * @return an instance of the world
     */
    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }

        return instance;
    }

    /**
     * Create an empty world
     */
    private World() {
        worldLayers = new HashMap<>();
        generator = new WorldGenerator(this);

        wipeClean();
    }

    /**
     * Put the given item at the specified position
     * @param position position at which to store the item
     * @param item item to be stored
     */
    void add(Position position, Item item) {

        if (item.equals(WALL)) {
            worldLayers.get(AIR).remove(position);
        }
        if (item.equals(AIR)) {
            worldLayers.get(WALL).remove(position);
        }

        worldLayers.get(item).add(position);

        setChanged();
        notifyObservers(position);
    }

    /**
     * Remove the given entity at the specified position
     * @param position position at which to remove the entity
     */
    void remove(Position position, Item item) {
        if (item.equals(WALL)) {
            generator.addWalls(position);
            add(position, AIR);
        }

        if (worldLayers.get(item).remove(position)) {
            setChanged();
            notifyObservers(position);
        }
    }

    public Set<Item> get(Position position) {
        if (client == null) {
            return localGet(position);
        } else {
            return client.get(position);
        }
    }

    @NotNull
    private Set<Item> localGet(Position position) {
        Set<Item> items = new HashSet<>();

        for (Item item : worldLayers.keySet()) {
            if (worldLayers.get(item).contains(position)) {
                items.add(item);
            }
        }

        return items;
    }

    boolean isWalkable(Position position) {
        return (worldLayers.get(AIR).contains(position) &&
                !worldLayers.get(WALL).contains(position));
    }

    public boolean isEnded() {
        return isEnded;
    }

    public boolean isWin() {
        return win;
    }

    public boolean contains(Position position) {
        if (client == null) {
            return localContains(position);
        } else {
            return client.contains(position);
        }
    }

    private boolean localContains(Position position) {
        for (Set<Position> layer : worldLayers.values()) {
            if (layer.contains(position)) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        int sizeSoFar = 0;
        for (Set<Position> layer : worldLayers.values()) {
            sizeSoFar += layer.size();
        }

        return sizeSoFar;
    }

    boolean contains(@NotNull Position position, Item wall) {
        return worldLayers.get(wall).contains(position);
    }

    /**
     * Destroy the world and bring it to the original state
     */
    void wipeClean() {
        worldLayers.clear();

        for (Item item : Item.values()) {
            worldLayers.put(item, new HashSet<>());
        }
    }

    /**
     * Produces true if all layers contain no positions, false otherwise
     * @return true if all layers contain no positions, false otherwise
     */
    boolean isEmpty() {
        for (Set<Position> positions : worldLayers.values()) {
            if (!positions.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public WorldGenerator getGenerator() {
        return generator;
    }

    /**
     * Connect to the server at the specified location
     * @param address address of the server
     */
    public void connect(InetAddress address) throws IOException {
        client = new Client(address);
    }
}
