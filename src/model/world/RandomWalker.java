package model.world;

import model.Item;
import model.Position;

import java.util.Observable;
import java.util.Random;

/**
 * A random walker actor
 */
class RandomWalker extends Observable implements Actor {
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

        /*
        for (Item item : WorldGenerator.getRandomFillGenerators().keySet()) {
            if (RANDOM.nextFloat() < randomFillGenerators.get(item)) {
                world.put(position, item);
                break;
            }
        }*/

        position = nextPosition(position);

        /*
        if (breedRandomWalkers) {
            if (RANDOM.nextFloat() < randomWalkersBirthChance) {
                randomWalkers.add(position);
            }
            if (randomWalkers.size() > 1) {
                if (RANDOM.nextFloat() < randomWalkersDeathChance) {
                    randomWalkers.remove(RANDOM.nextInt(randomWalkers.size()));
                }
            }
        }*/
    }

    private Position nextPosition(Position position) {
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
