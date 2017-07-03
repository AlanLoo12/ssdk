package model;

import java.util.*;

import static model.Item.FLOOR;
import static model.Item.WALL;
import static model.Position.*;

/**
 * A world generator
 */
public class WorldGenerator implements Observer {
    private static final Random RANDOM = new Random();
    private World world;
    private List<Position> randomWalkers;

    /**
     * Generates tunnels
     */
    private Set<Position> tunnelWalkers;
    /**
     * Entities that are randomly generated with the specified probability
     */
    private Map<Item, Float> randomFillGenerators;
    private boolean breedRandomWalkers;
    private float randomWalkersBirthChance;
    private int randomWalkersToTick;
    private float randomWalkersDeathChance;
    private boolean ignoreNotification;
    private static final Set<Position> NEIGHBOURS = new HashSet<>(Arrays.asList(
            new Position(1,0),
            new Position(-1,0),
            new Position(0,1),
            new Position(0,-1)
    ));

    public WorldGenerator(World world) {
        this.world = world;
        world.addObserver(this);
        randomFillGenerators = new HashMap<>();

        ignoreNotification = false;
        breedRandomWalkers = true;
        randomWalkersBirthChance = 0.0001f;
        randomWalkersDeathChance = randomWalkersBirthChance;
        randomWalkersToTick = 5;

        randomWalkers = new ArrayList<>();
        //randomWalkers.add(new Position(0,0));

        tunnelWalkers = new HashSet<>();
        tunnelWalkers.add(new Position(0,0));
    }

    public void tick() {
        if (randomWalkers.size() > 0) {
            for (int i = 0; i < randomWalkersToTick; i++) {
                int walkerIndex = getWalkerIndex();
                Position position = randomWalkers.get(walkerIndex);

                world.add(position, Item.FLOOR);
                world.remove(position, Item.WALL);

                addWalls(position);

                for (Item item : randomFillGenerators.keySet()) {
                    if (RANDOM.nextFloat() < randomFillGenerators.get(item)) {
                        world.add(position, item);
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

        // recognize
        // generate space
        // filter space
        // generate
        // shift

        Set<Position> toAdd = new HashSet<>();
        Set<Position> toRemove = new HashSet<>();

        for (Position tunnelWalker : tunnelWalkers) {
                Set<Position> walls = new HashSet<>();
                walls.add(tunnelWalker.add(UP).add(LEFT));
                walls.add(tunnelWalker.add(UP).add(RIGHT));
                walls.add(tunnelWalker.add(DOWN).add(LEFT));
                walls.add(tunnelWalker.add(DOWN).add(RIGHT));

                for (Position neighbour : NEIGHBOURS) {
                    if (world.contains(tunnelWalker.add(neighbour), WALL)) {
                        walls.add(tunnelWalker.add(neighbour));
                    } else if (RANDOM.nextInt(3) == 1 &&
                            !world.contains(tunnelWalker.add(neighbour), FLOOR)) {
                        world.add(tunnelWalker.add(neighbour), WALL);
                        walls.add(tunnelWalker.add(neighbour));
                    } else {
                        toAdd.add(tunnelWalker.add(neighbour.multiply(2)));
                    }
                }

                for (Position neighbour : NEIGHBOURS) {
                    ignoreNotification();
                    world.add(tunnelWalker.add(neighbour), FLOOR);
                }
                ignoreNotification();
                world.add(tunnelWalker, FLOOR);

                for (Position wall : walls) {
                    ignoreNotification();
                    world.add(wall, FLOOR);
                    ignoreNotification();
                    world.add(wall, WALL);
                }

                toRemove.add(tunnelWalker);
        }
        tunnelWalkers.removeAll(toRemove);
        tunnelWalkers.addAll(toAdd);
    }

    private int getWalkerIndex() {
        return RANDOM.nextInt(randomWalkers.size());
    }

    private void addWalls(Position position) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!world.contains(position.add(x, y), FLOOR)) {
                    ignoreNotification();

                    world.add(position.add(x, y), WALL);
                    world.add(position.add(x, y), FLOOR);
                }
            }
        }
    }

    private void ignoreNotification() {
        ignoreNotification = true;
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
        world.add(randomWalkers.get(getWalkerIndex()), item);
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

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && !ignoreNotification) {
            addWalls((Position) arg);
            ignoreNotification = false;
        }
    }
}
