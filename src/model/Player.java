package model;

import java.util.Observable;
import java.util.Set;

/**
 * A player (yep, that's it).
 */
public class Player extends Observable {
    private WorldManager world;
    private Position position;
    private Inventory inventory;
    private int numberOfMoves;
    private long initialTime;
    private int selectedItem;
    private Position lookDirection;

    public Player(WorldManager world) {
        this.world = world;
        inventory = new Inventory();
        inventory.add(Item.PICK_AXE);

        numberOfMoves = 0;
        initialTime = System.currentTimeMillis();
        selectedItem = 0;
        lookDirection = Position.ORIGIN;

        position = new Position(0,0);

        world.put(position, Item.PLAYER);
    }

    public void move(Position direction) {
        if (world.isWalkable(position.add(direction))) {
            Position nextPosition = position.add(direction);

            world.remove(position, Item.PLAYER);

            Set<Item> items = world.get(nextPosition);
            if (items.contains(Item.PLANT)) {
                world.remove(nextPosition, Item.PLANT);
                inventory.add(Item.PLANT);
            }

            position = nextPosition;
            numberOfMoves++;

            world.put(nextPosition, Item.PLAYER);

            setChanged();
            notifyObservers();
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

    public void selectPreviousItem() {
        if (inventory.size() > 0) {
            selectedItem = selectedItem - 1;
            if (selectedItem < 0) {
                selectedItem = inventory.size() - 1;
            }

            setChanged();
            notifyObservers();
        }
    }

    public void selectNextItem() {
        if (inventory.size() > 0) {
            selectedItem = (selectedItem + 1) % inventory.size();

            setChanged();
            notifyObservers();
        }
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void look(Position lookDirection) {
        if (!lookDirection.equals(this.lookDirection)) {
            this.lookDirection = lookDirection;

            setChanged();
            notifyObservers();
        }
    }

    public Position getLookDirection() {
        return lookDirection;
    }

    /**
     * Uses the selected inventory item
     */
    public void useItem() {
        inventory.get(selectedItem).ifPresent(Item -> Item.use(this));
    }
}
