package model.world;

import model.Item;
import model.Position;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A world located on some other machine
 */
public class RemoteWorld implements World {
    private final InetAddress address;
    private PrintWriter out;
    private BufferedReader in;

    RemoteWorld(InetAddress address) {
        this.address = address;
    }

    /**
     * Connect to the server and open the IO streams
     * @throws IOException if connection failed
     */
    private void connect() throws IOException {
        Socket serverSocket = new Socket(address, Server.PORT);
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
    public void put(@NotNull Position position, @NotNull Item item) {
        try {
            connect();
            out.println("PUT " + position + " " + item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the item from the specified position, if item exists.
     * Otherwise, do nothing
     *
     * @param position position at which to remove the item from
     * @param item     item to be removed
     */
    @Override
    public void remove(@NotNull Position position, @NotNull Item item) {

    }

    /**
     * Get all items at the given position
     *
     * @param position position from which to fetch the items
     * @return all items at the given position
     */
    @Override
    public @NotNull Set<Item> get(@NotNull Position position) {
        Set<Item> items = new HashSet<>();

        try {
            connect();
            out.println("GET " + position);
            items.addAll(Protocol.decodeItems(in.readLine()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return items;
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
    public boolean isWalkable(@NotNull Position position) {
        try {
            connect();
            out.println("IS_WALKABLE " + position);
            return Protocol.decodeBoolean(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Produce true if the given position contains any items at all,
     * false otherwise
     *
     * @param position position to check
     * @return true if the given position contains any items at all,
     * false otherwise
     */
    @Override
    public boolean contains(@NotNull Position position) {
        return !get(position).isEmpty();
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
    public boolean contains(@NotNull Position position, @NotNull Item item) {
        try {
            connect();
            out.println("CONTAINS " + position + " " + item);
            return Protocol.decodeBoolean(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public WorldGenerator getGenerator() {
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
    public Map<Position, Set<Item>> get(Position from, Position to) {
        try {
            connect();
            out.println("GET " + from + " " + to);
            return Protocol.decodeMap(from, to, in.readLine());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null; // stub
    }
}
