package use;

import model.Item;
import model.Position;
import network.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 */
public class UsePlay {
    private static final int MAX = 10;

    public static void main(String[] args) throws IOException {
        Client client = new Client(InetAddress.getByName("127.0.0.1"));

        for (int x = 0; x < MAX; x++) {
            for (int y = 0; y < MAX; y++) {
                System.out.println(x + " " + y + ":");
                System.out.println(client.get(new Position(x, y)));
            }
        }
    }
}
