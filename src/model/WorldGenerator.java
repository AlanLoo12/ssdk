package model;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.*;

/**
 * A world generator
 */
public class WorldGenerator {
    private static final Random RANDOM = new Random();
    private World world;
    private List<Position> randomWalkers;
    /**
     * Entities that are randomly generated with the specified probability
     */
    private Map<Entity, Float> randomFillGenerators;
    private boolean breedRandomWalkers;
    private float randomWalkersBreedChance;
    private int randomWalkersToTick;

    public WorldGenerator(World world) {
        this.world = world;
        randomFillGenerators = new HashMap<>();

        breedRandomWalkers = true;
        randomWalkersBreedChance = 0.0001f;
        randomWalkersToTick = 5;

        randomWalkers = new ArrayList<>();
        randomWalkers.add(new Position(0,0));
    }

    public void tick() {

        if (randomWalkers.size() > 0) {
            for (int i = 0; i < randomWalkersToTick; i++) {
                int walkerIndex = getWalkerIndex();
                Position position = randomWalkers.get(walkerIndex);

                world.setActive(position, true);
                if (!world.isWalkable(position)) {
                    world.put(position, Entity.DUST);

                    for (Entity entity : randomFillGenerators.keySet()) {
                        if (RANDOM.nextFloat() < randomFillGenerators.get(entity)) {
                            world.put(position, entity);
                            break;
                        }
                    }
                }
                addWalls(position);
                randomWalkers.set(walkerIndex, nextPosition(position, 1));

                if (breedRandomWalkers) {
                    if (RANDOM.nextFloat() < randomWalkersBreedChance) {
                        randomWalkers.add(position);
                    }
                }
            }
        }
    }

    private int getWalkerIndex() {
        return RANDOM.nextInt(randomWalkers.size());
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

    public void generateRandomly(Entity entity, float density) throws IllegalArgumentException {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be between 0 and 1");
        }

        randomFillGenerators.put(entity, density);
    }

    public void putAtTheWalkerTip(Entity entity) {
        world.put(randomWalkers.get(getWalkerIndex()), entity);
    }

    public void tick(int initialMapSize) {
        for (int i = 0; i < initialMapSize; i++) {
            tick();
        }
    }

    public void setBreedRandomWalkers(boolean breedRandomWalkers) {
        this.breedRandomWalkers = breedRandomWalkers;
    }

    public void setRandomWalkersBreedChance(float randomWalkersBreedChance) {
        this.randomWalkersBreedChance = randomWalkersBreedChance;
    }

    public void setRandomWalkersToTick(int randomWalkersToTick) {
        this.randomWalkersToTick = randomWalkersToTick;
    }
}
