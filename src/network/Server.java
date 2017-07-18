package network;

import model.*;
import model.world.generator.WorldGenerator;
import model.world.WorldManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static model.Item.MUSHROOM;

/**
 * A simple server class
 */
public class Server implements Observer {
    private ServerSocket serverSocket;
    private int port;
    private Set<Client> clients;
    private Thread serverThread;

    public Server(int port) throws IOException {
        System.out.print("Starting server... ");

        setUpWorld();

        this.port = port;
        clients = new HashSet<>();
        serverSocket = new ServerSocket(port);

        serverThread = new ServerThread();
        serverThread.setDaemon(true);
        serverThread.start();

        System.out.println("OK");
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        //noinspection SuspiciousMethodCalls
        clients.remove(arg);
        System.out.print("Client disconnected. ");
        System.out.println("Clients left: " + clients.size());
    }

    private class ServerThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                acceptConnections();
            }
        }
    }

    private void acceptConnections() {
        try {
            Socket clientSocket = serverSocket.accept();
            Client client = new Client(clientSocket);
            client.addObserver(this);

            clients.add(client);

            System.out.println("Client connected from " + clientSocket.getInetAddress());
            System.out.println("Total number of clients: " + clients.size());
        } catch (IOException ignored) {}
    }

    private void setUpWorld() {
        WorldManager abstractWorld = WorldManager.getInstance();
        WorldGenerator worldGenerator = abstractWorld.getGenerator();
        worldGenerator.generateRandomly(MUSHROOM, 0.001f);
        worldGenerator.setBreedRandomWalkers(true);

        // Configure the generator
        worldGenerator.setRandomWalkersBirthChance(0.1f);
        worldGenerator.setRandomWalkersDeathChance(0.05f);
        worldGenerator.setRandomWalkersToTick(4);

        // Generate
        worldGenerator.tick(30000);
        worldGenerator.start();

        //Thread worldGeneratorThread
    }
}
