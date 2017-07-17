import network.Server;

import java.io.IOException;

/**
 * Starts a dedicated server
 */
public class MainServer {
    public static void main(String[] args) {
        try {
            Server server = new Server(3000);
        } catch (IOException e) {
            System.out.println("Failed to start server");
        }
    }
}
