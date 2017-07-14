package use;

import model.Position;
import model.world.WorldManager;

import java.io.IOException;
import java.net.InetAddress;

/**
 *
 */
public class UsePlay {
    private static final int MAX = 2000;

    public static void main(String[] args) throws IOException {
        /*WorldManager.getInstance().connect(InetAddress.getByName("127.0.0.1"));

        for (int i = 200; i < MAX; i += 100) {
            long startTime = System.currentTimeMillis();
            for (int x = 0; x < i; x++) {
                for (int y = 0; y < i; y++) {
                    //System.out.println(x + " " + y + ":");
                    WorldManager.getInstance().get(new Position(x, y));
                }
            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println(i + " " + elapsedTime);
        }*/

        //System.out.println(new Position(100, -200));

        WorldManager.getInstance().connect(InetAddress.getByName("127.0.0.1"));

        for (int i = 1; i < MAX; i++) {
            long startTime = System.currentTimeMillis();

            WorldManager.getInstance().get(new Position(0, 0), new Position(i, i));

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println(i + " " + elapsedTime);
        }
    }
}
