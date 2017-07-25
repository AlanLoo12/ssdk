package model.world.physics;

import model.item.ItemEnum;
import model.Position;
import model.world.WorldManager;
import model.world.generator.RandomWalker;

import java.util.Random;

/**
 * Handles water physics
 */
public class Water implements Actor {
    private static final int MIN_DELAY = 5000;
    private static final int MAX_ACTIVE_DISTANCE = 20;
    private static final Random RANDOM = new Random();
    private Thread waterThread;

    Water() {
        waterThread = new WaterThread();
    }

    @Override
    public void start() {
        waterThread.start();
    }

    @Override
    public void stop() {
        waterThread.interrupt();
    }

    private class WaterThread extends Thread {
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
        /*
        for (Position water : WorldManager.getInstance().get(ItemEnum.WATER)) {
            WorldManager.getInstance().findNear(water,
                    ItemEnum.WATER,
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

                        if (!WorldManager.getInstance().contains(nextPosition, ItemEnum.WATER) &&
                                WorldManager.getInstance().isWalkable(nextPosition)) {
                            WorldManager.getInstance().remove(water, ItemEnum.WATER);
                            WorldManager.getInstance().put(nextPosition, ItemEnum.WATER);
                        }
                    });
        }*/
    }
}
