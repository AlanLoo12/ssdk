package model.world;

import model.Item;
import model.Position;

import java.util.*;

import static model.Item.AIR;
import static model.Item.WALL;
import static model.Position.*;

/**
 * A world generator
 */
public class WorldGenerator implements Observer {
    private static final Random RANDOM = new Random();
    private AbstractWorld world;
    private List<Actor> actors;

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
    private Thread worldGeneratorThread;

    WorldGenerator(AbstractWorld world) {
        this.world = world;
        worldGeneratorThread = new WorldGeneratorThread();

        randomFillGenerators = new HashMap<>();

        actors = new LinkedList<>();
        actors.add(new RandomWalker(new Position(0, 0)));

        breedRandomWalkers = true;
        randomWalkersBirthChance = 0.0001f;
        randomWalkersDeathChance = randomWalkersBirthChance;
        randomWalkersToTick = 5;
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

    static void addWalls(Position position) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!WorldManager.getInstance().contains(position.add(x, y), AIR)) {
                    WorldManager.getInstance().put(position.add(x, y), WALL);
                }
            }
        }
    }

    @Deprecated
    private Position nextPosition(Position position) {
        return position;
    }

    public void generateRandomly(Item item, float density) throws IllegalArgumentException {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be between 0 and 1");
        }

        randomFillGenerators.put(item, density);
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
        // TODO: do something
    }
}