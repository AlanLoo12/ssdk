package network;

import model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import static model.Item.PLANT;

/**
 * A simple server class
 */
public class Server {
    private static ServerSocket serverSocket;
    public static final int PORT = 3000;

    public static void main(String[] args) throws IOException {
        setUpWorld();
        startServer();
    }

    private static void startServer() throws IOException {
        serverSocket = new ServerSocket(PORT);

        System.out.println("Server listening on " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Got a client with ip " + clientSocket.getInetAddress());

            serviceClient(clientSocket);
            clientSocket.close();

            System.out.println("Client is disconnected.");
        }
    }

    private static void setUpWorld() {
        WorldManager abstractWorld = WorldManager.getInstance();
        WorldGenerator worldGenerator = abstractWorld.getGenerator();

        // Configure the generator
        worldGenerator.generateRandomly(PLANT, 0.001f);
        worldGenerator.setBreedRandomWalkers(true);
        worldGenerator.setRandomWalkersBirthChance(0.0001f);
        worldGenerator.setRandomWalkersDeathChance(0.00005f);
        worldGenerator.setRandomWalkersToTick(4);

        // Generate
        worldGenerator.tick(3000);

        //Thread worldGeneratorThread
    }

    private static void serviceClient(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        try {
            String msg = in.readLine();

            if (msg == null) {
                clientSocket.close();
            } else {
                if (msg.matches("GET -?\\d+ -?\\d+")) {
                    int x = Integer.parseInt(msg.split(" ")[1]);
                    int y = Integer.parseInt(msg.split(" ")[2]);

                    Set<Item> items = WorldManager.getInstance().get(new Position(x, y));
                    out.println(Protocol.encodeItems(items));

                    System.out.println("Client requested x = " + x + ", y = " + y);
                    System.out.println("Sending " + items);
                } else if (msg.matches("PUT -?\\d+ -?\\d+ [A-Z]+")) {
                    // TODO: move the try part to regex
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
                } else if (msg.matches("CONTAINS -?\\d+ -?\\d+ [A-Z]+")) {
                    try {
                        int x = Integer.parseInt(msg.split(" ")[1]);
                        int y = Integer.parseInt(msg.split(" ")[2]);

                        Item item = Item.valueOf(msg.split(" ")[3]);

                        Position position = new Position(x, y);
                        out.println(Protocol.encodeBoolean(WorldManager.getInstance().contains(position, item)));

                        System.out.println("Client requested x = " + x + ", y = " + y);
                        System.out.println("Returning " + WorldManager.getInstance().contains(position, item));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: unknown material");
                    }
                } else if (msg.matches("IS_WALKABLE -?\\d+ -?\\d+")) {
                    try {
                        int x = Integer.parseInt(msg.split(" ")[1]);
                        int y = Integer.parseInt(msg.split(" ")[2]);

                        Position position = new Position(x, y);
                        out.println(Protocol.encodeBoolean(WorldManager.getInstance().isWalkable(position)));

                        System.out.println("Client requested x = " + x + ", y = " + y);
                        System.out.println("Returning " + WorldManager.getInstance().isWalkable(position));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: unknown material");
                    }
                } else {
                    System.out.print("Error, Unrecognized message: ");
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection reset.");
        }
    }
}
