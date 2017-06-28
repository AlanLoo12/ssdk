package model;

import com.sun.javafx.scene.traversal.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;

/**
 * A player (yep, that's it).
 */
public class Player {
    private World world;
    private Position position;
    private Map<Entity, Integer> inventory;
    private int numberOfMoves;
    private long initialTime;

    public Player(World world) {
        this.world = world;
        inventory = new HashMap<>();
        numberOfMoves = 0;
        initialTime = System.currentTimeMillis();

        position = new Position(0,0);

        world.put(position, Entity.PLAYER);
    }

    public void move(Direction direction) {
        if (world.isWalkable(position.get(direction))) {
            Position nextPosition = position.get(direction);

            world.remove(position);

            Optional<Entity> entity = world.get(nextPosition);

            if (entity.isPresent()) {
                if (entity.get().equals(Entity.EXIT)) {
                    world.end(true);
                } else {
                    if (inventory.containsKey(entity.get())) {
                        inventory.put(entity.get(), inventory.get(entity.get()) + 1);
                    } else {
                        inventory.put(entity.get(), 1);
                    }
                }
            }

            world.put(nextPosition, Entity.PLAYER);

            position = nextPosition;

            numberOfMoves++;
        }
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Produce the number of seconds the player have played the game
     * @return the number of seconds the player have played the game
     */
    public int getTime() {
        return (int) ((System.currentTimeMillis() - initialTime) / 1000);
    }

    /**
     * Produce the number of moves the player had made
     * @return the number of moves the player had made
     */
    public int getMoves() {
        return numberOfMoves;
    }

    public int getCoins() {
        return inventory.getOrDefault(Entity.COIN, 0);
    }
}
