package model;

import com.sun.javafx.scene.traversal.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;

/**
 * A player (yep, that's it).
 */
public class Player extends Observable {
    private World world;
    private Position position;
    private Inventory inventory;
    private int numberOfMoves;
    private long initialTime;
    private int selectedInventoryItem;
    private Direction lookDirection;

    public Player(World world) {
        this.world = world;
        inventory = new Inventory();
        numberOfMoves = 0;
        initialTime = System.currentTimeMillis();
        selectedInventoryItem = 0;
        lookDirection = Direction.UP;

        position = new Position(0,0);

        world.put(position, Entity.PLAYER);
    }

    public void move(Direction direction) {
        look(direction);

        if (world.isWalkable(position.get(direction))) {
            Position nextPosition = position.get(direction);

            world.remove(position);

            Optional<Entity> entity = world.get(nextPosition);

            if (entity.isPresent()) {
                if (entity.get().equals(Entity.EXIT)) {
                    world.end(true);
                } else {
                    inventory.add(entity.get());
                }
            }

            position = nextPosition;
            numberOfMoves++;

            world.put(nextPosition, Entity.PLAYER);
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

    public Inventory getInventory() {
        return inventory;
    }

    public void selectPreviousInventoryItem() {
        if (inventory.size() > 0) {
            selectedInventoryItem = selectedInventoryItem - 1;
            if (selectedInventoryItem < 0) {
                selectedInventoryItem = inventory.size() - 1;
            }

            setChanged();
            notifyObservers();
        }
    }

    public void selectNextInventoryItem() {
        if (inventory.size() > 0) {
            selectedInventoryItem = (selectedInventoryItem + 1) % inventory.size();

            setChanged();
            notifyObservers();
        }
    }

    public int getSelectedInventoryItem() {
        return selectedInventoryItem;
    }

    public void look(Direction lookDirection) {
        if (!lookDirection.equals(this.lookDirection)) {
            this.lookDirection = lookDirection;

            setChanged();
            notifyObservers();
        }
    }

    public Direction getLookDirection() {
        return lookDirection;
    }
}
