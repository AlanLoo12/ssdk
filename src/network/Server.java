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
    static final int PORT = 3000;

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
        World abstractWorld = World.getInstance();
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
                if (msg.contains("GET")) {
                    try {
                        int x = Integer.parseInt(msg.split(" ")[1]);
                        int y = Integer.parseInt(msg.split(" ")[2]);

                        Set<Item> items = World.getInstance().get(new Position(x, y));
                        out.println(Protocol.encodeItems(items));

                        System.out.println("Client requested x = " + x + ", y = " + y);
                        System.out.println("Sending " + items);
                    } catch (NumberFormatException e) {
                        System.out.println("Wrong number format requested");
                    }
                } else {
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection reset.");
        }
    }
}
