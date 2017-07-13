package use;

import model.Position;
import model.RemoteWorld;
import model.World;
import model.WorldManager;

import java.io.IOException;
import java.net.InetAddress;

/**
 *
 */
public class UsePlay {
    private static final int MAX = 10;

    public static void main(String[] args) throws IOException {
        WorldManager.getInstance().connect(InetAddress.getByName("127.0.0.1"));

        for (int x = 0; x < MAX; x++) {
            for (int y = 0; y < MAX; y++) {
                System.out.println(x + " " + y + ":");
                System.out.println(WorldManager.getInstance().get(new Position(x, y)));
            }
        }
    }
}
