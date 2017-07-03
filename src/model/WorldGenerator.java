package model;

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
    private Map<Item, Float> randomFillGenerators;
    private boolean breedRandomWalkers;
    private float randomWalkersBirthChance;
    private int randomWalkersToTick;
    private float randomWalkersDeathChance;
    private boolean generateDust;
    private boolean generateWalls;

    public WorldGenerator(World world) {
        this.world = world;
        randomFillGenerators = new HashMap<>();

        breedRandomWalkers = true;
        generateDust = true;
        generateWalls = true;
        randomWalkersBirthChance = 0.0001f;
        randomWalkersDeathChance = randomWalkersBirthChance;
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
                    if (generateDust) {
                        world.put(position, Item.DUST);
                    } else {
                        world.remove(position);
                    }

                    for (Item item : randomFillGenerators.keySet()) {
                        if (RANDOM.nextFloat() < randomFillGenerators.get(item)) {
                            world.put(position, item);
                            break;
                        }
                    }
                }
                if (generateWalls) {
                    addWalls(position);
                }
                randomWalkers.set(walkerIndex, nextPosition(position, 1));

                if (breedRandomWalkers) {
                    if (RANDOM.nextFloat() < randomWalkersBirthChance) {
                        randomWalkers.add(position);
                    }
                    if (randomWalkers.size() > 1) {
                        if (RANDOM.nextFloat() < randomWalkersDeathChance) {
                            randomWalkers.remove(RANDOM.nextInt(randomWalkers.size()));
                        }
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
                    world.put(position.add(x, y), Item.WALL);
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

    public void generateRandomly(Item item, float density) throws IllegalArgumentException {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be between 0 and 1");
        }

        randomFillGenerators.put(item, density);
    }

    public void putAtTheWalkerTip(Item item) {
        world.put(randomWalkers.get(getWalkerIndex()), item);
    }

    public void tick(int initialMapSize) {
        for (int i = 0; i < initialMapSize; i++) {
            tick();
        }
    }

    public void setBreedRandomWalkers(boolean breedRandomWalkers) {
        this.breedRandomWalkers = breedRandomWalkers;
    }

    public void setRandomWalkersBirthChance(float randomWalkersBirthChance) {
        this.randomWalkersBirthChance = randomWalkersBirthChance;
    }

    public void setRandomWalkersToTick(int randomWalkersToTick) {
        this.randomWalkersToTick = randomWalkersToTick;
    }

    public void setRandomWalkersDeathChance(float randomWalkersDeathChance) {
        this.randomWalkersDeathChance = randomWalkersDeathChance;
    }

    public void setGenerateDust(boolean generateDust) {
        this.generateDust = generateDust;
    }

    public void setGenerateWalls(boolean generateWalls) {
        this.generateWalls = generateWalls;
    }
}
