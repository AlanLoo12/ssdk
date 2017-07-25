package model.world.physics;

import model.item.ItemEnum;
import model.Position;
import model.world.WorldManager;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Handles plants growth
 */
public class Plants implements Actor {
    private static final int MIN_DELAY = 5000;
    private static final Random RANDOM = new Random();
    private static final int MAX_WATER_DISTANCE = 10;
    private Thread plantsThread;

    Plants() {
        plantsThread = new PlantsThread();
    }

    @Override
    public void start() {
        plantsThread.start();
    }

    @Override
    public void stop() {
        plantsThread.interrupt();
    }

    private class PlantsThread extends Thread {
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
        Set<Position> toAdd = new HashSet<>();
        Set<Position> toRemove = new HashSet<>();

        /*for (Position plant : WorldManager.getInstance().get(ItemEnum.MUSHROOM)) {
            if (kill(plant)) {
                toRemove.add(plant);
            }
            toAdd.addAll(newNeighbours(plant));
        }*/

        for (Position position : toAdd) {
            WorldManager.getInstance().safePut(position, ItemEnum.MUSHROOM);
        }
        for (Position position : toRemove) {
            WorldManager.getInstance().remove(ItemEnum.MUSHROOM);
        }
    }

    /**
     * Produce new neighbours of the plant
     * @param plant plant near which to search for neighbours
     * @return new neighbours of the plant
     */
    private Collection<? extends Position> newNeighbours(Position plant) {
        Set<Position> newNeighbours = new HashSet<>();
        for (Position neighbour : plant.getNeighbours()) {
            if (getNeighboursSize(neighbour) == 3) {
                newNeighbours.add(neighbour);
            }
        }

        return newNeighbours;
    }

    /**
     * Kill the plant if it's lonely or if there is not enough nutrition for it
     * @param plant plant to check for
     * @return true means that the plant will be killed, false otherwise
     */
    @Contract(pure = true)
    private boolean kill(Position plant) {
        int neighboursSize = getNeighboursSize(plant);

        return neighboursSize > 3 || neighboursSize < 2;
    }

    /**
     * Produce the number of alive neighbours of this plant
     * @param plant plant near which to search for neighbours
     * @return number of alive neighbours
     */
    private int getNeighboursSize(Position plant) {
        return (int) plant.getNeighbours().stream()
                .filter(position -> WorldManager.getInstance().contains(ItemEnum.MUSHROOM))
                .count();
    }

}
