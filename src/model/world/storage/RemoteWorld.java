package model.world.storage;

import model.Item;
import model.Position;
import model.world.generator.WorldGenerator;
import network.Protocol;
import network.Server;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A world located on some other machine
 */
public class RemoteWorld extends AbstractWorld {
    private final InetAddress address;
    private PrintWriter out;
    private BufferedReader in;
    private Socket serverSocket;
    private int port;

    // Caching
    private LocalWorld cacheWorld;
    private static final int CHUNK_SIZE = 30;
    private Set<Position> cachedChunks;

    public RemoteWorld(InetAddress address, int port) throws IOException {
        this.address = address;
        this.port = port;

        serverSocket = new Socket(address, port);
        out = new PrintWriter(serverSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        cacheWorld = new LocalWorld();
        cachedChunks = new HashSet<>();
        cacheChunk(new Position(0,0));
    }

    /**
     * Cache the given chunk
     * @param position position of the chunk in chunk coordinates
     */
    private void cacheChunk(Position position) {
        if (!cachedChunks.contains(position)) {
            Position from = position.add(-1, -1).multiply(CHUNK_SIZE);
            Position to = position.add(1, 1).multiply(CHUNK_SIZE);

            cacheWorld.addAll(getChunk(from, to));
            cachedChunks.add(position);
        }
    }

    /**
     * Put the given item at specified position
     *
     * @param position position at which to store the item
     * @param item     item to store
     */
    @Override
    public synchronized boolean put(@NotNull Position position, @NotNull Item item) {
        updateChunks(position);

        if (!cacheWorld.contains(position, item)) {
            out.println("PUT " + position + " " + item);
            cacheWorld.put(position, item);
        }

        return true; // TODO: get boolean from the server
    }

    /**
     * Remove the item from the specified position, if item exists.
     * Otherwise, do nothing
     *
     * @param position position at which to remove the item from
     * @param item     item to be removed
     */
    @Override
    public synchronized void remove(@NotNull Position position, @NotNull Item item) {
        updateChunks(position);

        if (cacheWorld.contains(position, item)) {
            out.println("REMOVE " + position + " " + item);
            cacheWorld.remove(position, item);
        }
    }

    /**
     * Get all items at the given position
     *
     * @param position position from which to fetch the items
     * @return all items at the given position
     */
    @Override
    public synchronized @NotNull Set<Item> get(@NotNull Position position) {
        updateChunks(position);

        assert  (cacheWorld.contains(position));
        return cacheWorld.get(position);
    }

    /**
     * Produce true if specified position is walkable by player,
     * false otherwise
     *
     * @param position position to check
     * @return true if specified position is walkable by player,
     * false otherwise
     */
    @Override
    public synchronized boolean isWalkable(@NotNull Position position) {
        updateChunks(position);

        assert (cacheWorld.contains(position));
        return cacheWorld.isWalkable(position);
    }

    /**
     * Produce true if given item is present at the specified position,
     * false otherwise
     *
     * @param position position to check
     * @param item     item to look for
     * @return true if given item is present at the specified position,
     * false otherwise
     */
    @Override
    public synchronized boolean contains(@NotNull Position position, @NotNull Item item) {
        updateChunks(position);

        assert  (cacheWorld.contains(position));
        return cacheWorld.contains(position, item);
    }

    @Override
    public synchronized WorldGenerator getGenerator() {
        // TODO: finish
        return null; // TODO: what do we do here?
    }

    /**
     * Create a map of all items inside the rectangle specified by the given positions
     *
     * @param from one of the corners
     * @param to   another corner
     * @return a map of all items inside the rectangle specified by the given positions
     */
    @Override
    public synchronized Map<Position, Set<Item>> get(Position from, Position to) {
        for (int x = from.getX(); x <= to.getX(); x += CHUNK_SIZE / 2) {
            for (int y = from.getX(); y <= to.getX(); y += CHUNK_SIZE / 2) {
                updateChunks(new Position(x, y));
            }
        }

        return cacheWorld.get(from, to);
    }

    private Map<Position, Set<Item>> getChunk(Position from, Position to) {
        Map<Position, Set<Item>> map = new HashMap<>();
        try {
            out.println("GET " + from + " " + to);
            map.putAll(Protocol.decodeMap(from, to, in.readLine()));
        } catch (IOException | ParseException ignored) {}

        cacheWorld.addAll(map);
        return map;
    }

    private void updateChunks(Position position) {
        Position chunkPosition = worldCoordinatesToChunkCoordinates(position);

        cacheChunk(chunkPosition);
        for (Position neighbour : chunkPosition.getNeighbours()) {
            cacheChunk(neighbour);
        }
    }

    private Position worldCoordinatesToChunkCoordinates(Position position) {
        return position.divide(CHUNK_SIZE);
    }

    @Override
    public synchronized Set<Position> get(Item item) {
        return new HashSet<>();
        // TODO: what do we return here??? not enough access rights? enough? why? who?
    }

    /**
     * Produce true if given position was initialized
     *
     * @param position position to check for
     * @return true if given position was initialized
     */
    @Override
    public boolean contains(@NotNull Position position) {
        return false; //TODO: finish
    }

    @Override
    public void addAll(@NotNull Position position, Set<Item> items) {
        for (Item item : items) {
            put(position, item);
            // TODO: write a faster implementation
        }
    }

    @Override
    public void addAll(Map<Position, Set<Item>> map) {
        // TODO: implement
    }
}
