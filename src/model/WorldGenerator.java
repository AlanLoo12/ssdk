package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class WorldGenerator {
    private static final Random RANDOM = new Random();
    private static final int RANDOM_WALKER_SPAWN_CHANCE = 10000;
    private static final int CORRIDOR_BUILDER_SPAWN_CHANCE = 100000;
    private World world;
    private List<Position> randomWalkers;
    private List<Position> corridorBuilders;

    public WorldGenerator(World world) {
        this.world = world;

        randomWalkers = new ArrayList<>();
        randomWalkers.add(new Position(0,0));

        corridorBuilders = new ArrayList<>();
    }

    public void tick() {
        for (int index = 0; index < randomWalkers.size(); index++) {
            Position position = randomWalkers.get(index);

            world.setWalkable(position, true);
            randomWalkers.set(index, nextPosition(position, 1));

            if (RANDOM.nextInt(RANDOM_WALKER_SPAWN_CHANCE) == 0) {
                randomWalkers.add(position);
            }

            if (RANDOM.nextInt(CORRIDOR_BUILDER_SPAWN_CHANCE) == 0) {
                corridorBuilders.add(position);
            }
        }

        for (int index = 0; index < corridorBuilders.size(); index++) {
            Position position = corridorBuilders.get(index);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    world.setWalkable(position.add(x, y), true);
                }
            }

            corridorBuilders.set(index, nextPosition(position, 3));
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
