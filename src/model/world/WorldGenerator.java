package model.world;

import model.Item;
import model.Position;
import model.world.World;
import model.world.WorldManager;

import java.util.*;

import static model.Item.AIR;
import static model.Item.WALL;
import static model.Position.*;

/**
 * A world generator
 */
public class WorldGenerator {
    private static final Random RANDOM = new Random();
    private World world;
    private List<Position> randomWalkers;

    /**
     * Generates tunnels
     */
    private List<Position> tunnelWalkers;
    /**
     * Entities that are randomly generated with the specified probability
     */
    private Map<Item, Float> randomFillGenerators;
    private boolean breedRandomWalkers;
    private float randomWalkersBirthChance;
    private int randomWalkersToTick;
    private float randomWalkersDeathChance;
    private static final Set<Position> NEIGHBOURS = new HashSet<>(Arrays.asList(
            new Position(1,0),
            new Position(-1,0),
            new Position(0,1),
            new Position(0,-1)
    ));

    WorldGenerator(World world) {
        this.world = world;
        randomFillGenerators = new HashMap<>();

        breedRandomWalkers = true;
        randomWalkersBirthChance = 0.0001f;
        randomWalkersDeathChance = randomWalkersBirthChance;
        randomWalkersToTick = 5;

        randomWalkers = new ArrayList<>();
        randomWalkers.add(new Position(0,0));

        tunnelWalkers = new ArrayList<>();
    }

    public void tick() {
        tickRandomWalkers();
        tickTunnelWalkers();
    }

    private void tickTunnelWalkers() {
        // recognize
        // generate space
        // filter space
        // generate
        // shift

        Set<Position> toAdd = new HashSet<>();
        Set<Position> toRemove = new HashSet<>();

        if (tunnelWalkers.size() > 0) {
            Position tunnelWalker = tunnelWalkers.get(RANDOM.nextInt(tunnelWalkers.size()));

            Set<Position> walls = new HashSet<>();
            walls.add(tunnelWalker.add(UP).add(LEFT));
            walls.add(tunnelWalker.add(UP).add(RIGHT));
            walls.add(tunnelWalker.add(DOWN).add(LEFT));
            walls.add(tunnelWalker.add(DOWN).add(RIGHT));

            generateTunnelBlock(toAdd, tunnelWalker, walls);
            buildTunnelBlock(tunnelWalker, walls);

            toRemove.add(tunnelWalker);
        }

        tunnelWalkers.removeAll(toRemove);
        tunnelWalkers.addAll(toAdd);
    }

    private void buildTunnelBlock(Position tunnelWalker, Set<Position> walls) {
        for (Position neighbour : NEIGHBOURS) {
            world.put(tunnelWalker.add(neighbour), AIR);
        }
        world.put(tunnelWalker, AIR);

        for (Position wall : walls) {
            world.put(wall, WALL);
        }
    }

    private void generateTunnelBlock(Set<Position> toAdd, Position tunnelWalker, Set<Position> walls) {
        for (Position neighbour : NEIGHBOURS) {
            Position friend = tunnelWalker.add(neighbour);
            if (world.contains(friend, WALL)) {
                walls.add(friend);
            } else if (!world.contains(friend, AIR)) {
                if (RANDOM.nextInt(2) == 1 &&
                        !world.contains(friend, AIR)
                        && walls.size() <= 6) {
                    world.put(tunnelWalker.add(neighbour), WALL);
                    walls.add(tunnelWalker.add(neighbour));
                } else {
                    toAdd.add(tunnelWalker.add(neighbour.multiply(2)));
                }
            }
        }
    }

    private void tickRandomWalkers() {
        if (randomWalkers.size() > 0) {
            for (int i = 0; i < randomWalkersToTick; i++) {
                int walkerIndex = getWalkerIndex();
                Position position = randomWalkers.get(walkerIndex);

                world.put(position, Item.AIR);

                addWalls(position);

                for (Item item : randomFillGenerators.keySet()) {
                    if (RANDOM.nextFloat() < randomFillGenerators.get(item)) {
                        world.put(position, item);
                        break;
                    }
                }

                randomWalkers.set(walkerIndex, nextPosition(position));

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

    void addWalls(Position position) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!WorldManager.getInstance().contains(position.add(x, y), AIR)) {
                    WorldManager.getInstance().put(position.add(x, y), WALL);
                }
            }
        }
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
}
