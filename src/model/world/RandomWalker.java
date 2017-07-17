package model.world;

import model.Item;
import model.Position;

import java.util.Random;

/**
 * A random walker actor
 */
class RandomWalker extends Actor {
    private static final Random RANDOM = new Random();
    private Position position;

    RandomWalker(Position position) {
        super();
        this.position = position;
    }

    /**
     * Tick the actor once
     */
    @Override
    public void tick() {
        WorldManager.getInstance().put(position, Item.AIR);
        WorldGenerator.addWalls(position);

        for (RandomFillGenerator randomFillGenerator : WorldGenerator.getInstance().getRandomFillGenerators()) {
            if (randomFillGenerator.generate(position)) {
                break;
            }
        }

        position = nextPosition(position);

        if (WorldGenerator.getInstance().getBreedRandomWalkers()) {
            if (RANDOM.nextFloat() < WorldGenerator.getInstance().getRandomWalkersBirthChance()) {
                setChanged();
                notifyObservers(new RandomWalker(position));
            }
        }
    }

    static Position nextPosition(Position position) {
        switch (RANDOM.nextInt(4)) {
            case 0:
                return new Position(position.getX() + 1, position.getY());
            case 1:
                return new Position(position.getX() - 1, position.getY());
            case 2:
                return new Position(position.getX(), position.getY() + 1);
            case 3:
                return new Position(position.getX(), position.getY() - 1);
            default:
                return position;
        }
    }
}
