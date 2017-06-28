package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A world generator
 */
public class WorldGenerator {
    private static final Random RANDOM = new Random();
    private World world;
    private List<Position> randomWalkers;

    public WorldGenerator(World world) {
        this.world = world;

        randomWalkers = new ArrayList<>();
        randomWalkers.add(new Position(0,0));
        randomWalkers.add(new Position(0,0));
        randomWalkers.add(new Position(0,0));
    }

    public void tick() {

        if (randomWalkers.size() > 0) {
            int index = RANDOM.nextInt(randomWalkers.size());
            Position position = randomWalkers.get(index);

            world.setActive(position, true);
            if (!world.isWalkable(position)) {
                world.put(position, Entity.DUST);
            }
            addWalls(position);
            randomWalkers.set(index, nextPosition(position, 1));
        }
    }

    private void addWalls(Position position) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!world.isActive(position.add(x, y))) {
                    world.setActive(position.add(x, y), true);
                    world.put(position.add(x, y), Entity.WALL);
                }
            }
        }
    }

    private Position nextPosition(Position position, int step) {
        switch (RANDOM.nextInt(4)) {
            case 0:
                return new Position(position.getX() + step, position.getY());
            case 1:
                return new Position(position.getX() - step, position.getY());
            case 2:
                return new Position(position.getX(), position.getY() + step);
            case 3:
                return new Position(position.getX(), position.getY() - step);
            default:
                return position;
        }
    }
}
