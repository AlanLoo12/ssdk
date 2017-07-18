package network;

import model.Item;
import model.Position;
import model.world.WorldManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 * Represents a remote client
 *
 * Handles all the IO with the client
 */
class Client extends Observable {
    private final PrintWriter out;
    private final BufferedReader in;
    private Socket socket;
    private Thread clientThread;

    Client(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        clientThread = new ClientThread();
        clientThread.start();
    }

    private class ClientThread extends Thread {
        @Override
        public void run() {
            while (socket.isConnected() && !Thread.interrupted()) {
                try {
                    serviceClient();
                } catch (IOException e) {
                    closeConnection();
                }
            }
            closeConnection();
        }
    }

    private void closeConnection() {
        clientThread.interrupt();

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error: can't close socket");
        }
        setChanged();
        notifyObservers(this);
    }

    private void serviceClient() throws IOException {
        try {
            String msg = in.readLine();

            if (msg == null) {
                closeConnection();
            } else {
                 if (msg.matches("GET -?\\d+ -?\\d+ -?\\d+ -?\\d+")) {
                    int x0 = Integer.parseInt(msg.split(" ")[1]);
                    int y0 = Integer.parseInt(msg.split(" ")[2]);

                    int x1 = Integer.parseInt(msg.split(" ")[3]);
                    int y1 = Integer.parseInt(msg.split(" ")[4]);

                    Position from = new Position(x0, y0);
                    Position to = new Position(x1, y1);
                    Map<Position, Set<Item>> map =
                            WorldManager.getInstance().get(from, to);

                    out.println(Protocol.encodeMap(from, to, map));

                    System.out.println(msg);
                    System.out.println("Sending " + map);
                } else if (msg.matches("PUT -?\\d+ -?\\d+ " + Item.getValidNamesRegex())) {
                    try {
                        int x = Integer.parseInt(msg.split(" ")[1]);
                        int y = Integer.parseInt(msg.split(" ")[2]);

                        Item item = Item.valueOf(msg.split(" ")[3]);
                        WorldManager.getInstance().put(new Position(x, y), item);

                        System.out.println("Client requested x = " + x + ", y = " + y);
                        System.out.println("Putting " + item);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: unknown material");
                    }
                } else if (msg.matches("REMOVE -?\\d+ -?\\d+ " + Item.getValidNamesRegex())) {
                    try {
                        int x = Integer.parseInt(msg.split(" ")[1]);
                        int y = Integer.parseInt(msg.split(" ")[2]);

                        Item item = Item.valueOf(msg.split(" ")[3]);
                        WorldManager.getInstance().remove(new Position(x, y), item);

                        System.out.println("Client requested x = " + x + ", y = " + y);
                        System.out.println("Removing " + item);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: unknown material");
                    }
                } else {
                    System.out.print("Error, Unrecognized message: ");
                    System.out.println(msg);
                    closeConnection();
                }
            }
        } catch (IOException e) {
            System.out.println("Connection reset.");
            closeConnection();
        }
    }
}
