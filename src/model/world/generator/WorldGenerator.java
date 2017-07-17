package model.world.generator;

import model.Item;
import model.Position;
import model.world.WorldManager;

import java.util.*;

import static model.Item.AIR;
import static model.Item.WALL;

/**
 * A world generator
 */
public class WorldGenerator implements Observer {
    private static final Random RANDOM = new Random();
    private List<Actor> actors;
    private List<Actor> actorsToAdd;

    /**
     * Entities that are randomly generated with the specified probability
     */
    private Set<RandomFillGenerator> randomFillGenerators;
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
    private Thread worldGeneratorThread;
    private static WorldGenerator instance;
    private List<Actor> actorsToRemove;

    private WorldGenerator() {
        worldGeneratorThread = new WorldGeneratorThread();

        randomFillGenerators = new HashSet<>();

        actors = new LinkedList<>();
        actorsToAdd = new LinkedList<>();
        actorsToRemove = new LinkedList<>();

        add(new RandomWalker(new Position(0, 0)));

        breedRandomWalkers = true;
        randomWalkersBirthChance = 0.0001f;
        randomWalkersDeathChance = randomWalkersBirthChance;
        randomWalkersToTick = 5;
    }

    public static WorldGenerator getInstance() {
        if (instance == null) {
            instance = new WorldGenerator();
        }
        return instance;
    }

    private void add(Actor actor) {
        actors.add(actor);
        actor.addObserver(this);
    }

    Set<RandomFillGenerator> getRandomFillGenerators() {
        return randomFillGenerators;
    }

    boolean getBreedRandomWalkers() {
        return breedRandomWalkers;
    }

    float getRandomWalkersBirthChance() {
        return randomWalkersBirthChance;
    }

    private class WorldGeneratorThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                tick();
            }
        }
    }

    public void start() {
        worldGeneratorThread.start();
    }

    public void stop() {
        worldGeneratorThread.interrupt();
    }

    private void tick() {
        for (Actor actor : actors) {
            actor.tick();
        }

        for (Actor actor : actorsToAdd) {
            add(actor);
        }
        actorsToAdd.clear();

        actors.removeAll(actorsToRemove);
        actorsToRemove.clear();
    }

    /*private void tickTunnelWalkers() {
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
    }*/

    private void buildTunnelBlock(Position tunnelWalker, Set<Position> walls) {
        for (Position neighbour : NEIGHBOURS) {
            WorldManager.getInstance().put(tunnelWalker.add(neighbour), AIR);
        }
        WorldManager.getInstance().put(tunnelWalker, AIR);

        for (Position wall : walls) {
            WorldManager.getInstance().put(wall, WALL);
        }
    }

    private void generateTunnelBlock(Set<Position> toAdd, Position tunnelWalker, Set<Position> walls) {
        for (Position neighbour : NEIGHBOURS) {
            Position friend = tunnelWalker.add(neighbour);
            if (WorldManager.getInstance().contains(friend, WALL)) {
                walls.add(friend);
            } else if (!WorldManager.getInstance().contains(friend, AIR)) {
                if (RANDOM.nextInt(2) == 1 &&
                        !WorldManager.getInstance().contains(friend, AIR)
                        && walls.size() <= 6) {
                    WorldManager.getInstance().put(tunnelWalker.add(neighbour), WALL);
                    walls.add(tunnelWalker.add(neighbour));
                } else {
                    toAdd.add(tunnelWalker.add(neighbour.multiply(2)));
                }
            }
        }
    }

    /*private void tickRandomWalkers() {
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
    }*/

    public static void addWalls(Position position) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!WorldManager.getInstance().contains(position.add(x, y), AIR)) {
                    WorldManager.getInstance().put(position.add(x, y), WALL);
                }
            }
        }
    }

    /**
     * Generate given item randomly with the given uniform density
     * @param item item to be generated
     * @param density density
     * @param radius l1 radius in which to generate the item
     * @throws IllegalArgumentException if density is less than 0 or greater than 1
     */
    public void generateRandomly(Item item, float density, int radius) {
        randomFillGenerators.add(new RandomFillGenerator(item, density, radius));
    }

    public void generateRandomly(Item item, float density, int radius, float fillChance) {
        randomFillGenerators.add(new RandomFillGenerator(item, density, radius, fillChance));
    }


    /**
     * Generate given item randomly with the given uniform density
     * @param item item to be generated
     * @param density density
     * @throws IllegalArgumentException if density is less than 0 or greater than 1
     */
    public void generateRandomly(Item item, float density) throws IllegalArgumentException {
        randomFillGenerators.add(new RandomFillGenerator(item, density));
    }

    /*public void putAtTheWalkerTip(Item item) {
        world.put(randomWalkers.get(getWalkerIndex()), item);
    }*/

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
        actorsToAdd.add((Actor) arg);
        actorsToRemove.add(actors.get(RANDOM.nextInt(actors.size())));
    }
}
