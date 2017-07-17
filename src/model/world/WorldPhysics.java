package model.world;

import model.Item;
import model.Position;

import java.util.Random;

/**
 * Handles world physics:
 *
 *  * Liquid flow
 */
public class WorldPhysics {
    private static final int MAX_ACTIVE_DISTANCE = 20;
    private static final Random RANDOM = new Random();
    private WorldPhysicsThread worldPhysicsThread;

    public WorldPhysics() {
        worldPhysicsThread = new WorldPhysicsThread();
    }

    public void start() {
        worldPhysicsThread.start();
    }

    private class WorldPhysicsThread extends Thread {
        private static final int MIN_DELAY = 5000;
        private long prevTime = 0;

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                if (System.currentTimeMillis() - prevTime > MIN_DELAY) {
                    tick();
                    prevTime = System.currentTimeMillis();
                }
            }
        }
    }

    private void tick() {
        tickWater();
    }

    private void tickWater() {
        for (Position water : WorldManager.getInstance().get(Item.WATER)) {
            WorldManager.getInstance().findNear(water,
                    Item.WATER,
                    MAX_ACTIVE_DISTANCE,
                    true).ifPresent(
                    position -> {
                        Position shift = position.add(water.multiply(-1)).unitize();

                        Position nextPosition;
                        if (RANDOM.nextBoolean()) {
                            nextPosition = RandomWalker.nextPosition(water);
                        } else {
                            nextPosition = water.add(shift);
                        }

                        if (!WorldManager.getInstance().contains(nextPosition, Item.WATER) &&
                                WorldManager.getInstance().isWalkable(nextPosition)) {
                            WorldManager.getInstance().remove(water, Item.WATER);
                            WorldManager.getInstance().put(nextPosition, Item.WATER);
                        }
                    });
        }
    }
}
