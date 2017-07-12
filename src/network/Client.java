package network;

import model.Item;
import model.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple client
 */
public class Client {
    public static final int PORT = 3001;

    private final InetAddress address;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Create a new client
     */
    public Client(InetAddress address) throws IOException {
        this.address = address;
    }

    public Set<Item> get(Position position) {
        Set<Item> items = new HashSet<>();

        try {
            connect();
            out.println("GET " + position.getX() + " " + position.getY());
            items.addAll(Protocol.decodeItems(in.readLine()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return items;
    }

    private void connect() throws IOException {
        Socket serverSocket = new Socket(address, Server.PORT);
        out = new PrintWriter(serverSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }

    /**
     * Produce true if the server contains something at the given position
     * @param position position to check
     * @return true if the server contains something at the given position,
     * false otherwise
     */
    public boolean contains(Position position) {
        return !get(position).isEmpty();
    }
}
