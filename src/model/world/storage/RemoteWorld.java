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

    public RemoteWorld(InetAddress address, int port) throws IOException {
        this.address = address;
        this.port = port;

        serverSocket = new Socket(address, port);
        out = new PrintWriter(serverSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }

    /**
     * Put the given item at specified position
     *
     * @param position position at which to store the item
     * @param item     item to store
     */
    @Override
    public synchronized boolean put(@NotNull Position position, @NotNull Item item) {
        out.println("PUT " + position + " " + item);

        return true; // TODO: finish. It can't always be true
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
        out.println("REMOVE " + position + " " + item);
    }

    /**
     * Get all items at the given position
     *
     * @param position position from which to fetch the items
     * @return all items at the given position
     */
    @Override
    public synchronized @NotNull Set<Item> get(@NotNull Position position) {
        try {
            out.println("GET " + position);
            return Protocol.decodeItems(in.readLine());
        } catch (IOException | ParseException ignored) {}

        return new HashSet<>();
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
        try {
            out.println("IS_WALKABLE " + position);
            return Protocol.decodeBoolean(in.readLine());
        } catch (IOException ignored) {}

        return false;
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
        try {
            out.println("CONTAINS " + position + " " + item);
            return Protocol.decodeBoolean(in.readLine());
        } catch (IOException ignored) {}

        return false;
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
        try {
            out.println("GET " + from + " " + to);
            return Protocol.decodeMap(from, to, in.readLine());
        } catch (IOException | ParseException ignored) {}

        return new HashMap<>();
    }

    @Override
    public synchronized Set<Position> get(Item item) {
        return new HashSet<>();
        // TODO: what do we return here??? not enough access rights? enough? why? who?
    }
}
