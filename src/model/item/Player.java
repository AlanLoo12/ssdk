package model.item;

import javafx.scene.image.Image;
import model.Inventory;
import model.Position;
import model.world.WorldManager;

import java.nio.file.Paths;
import java.util.Set;

/**
 *
 */
public class Player extends Item {
    private Inventory inventory;
    private int numberOfMoves;
    private long initialTime;
    private int selectedItem;
    private Position lookDirection;

    public Player(Position position) {
        super(position);

        initPlayer();
    }

    public Player() {
        super();

        initPlayer();
    }

    private void initPlayer() {
        inventory = new Inventory();
        inventory.add(ItemEnum.PICK_AXE);

        numberOfMoves = 0;
        initialTime = System.currentTimeMillis();
        selectedItem = 0;
        lookDirection = Position.ORIGIN;

        setImage(new Image(Paths.get("assets", "tiles", "simple_player.png").toUri().toString()));
        setVolume(0.6f);
    }

    public void move(Position direction) {
        Position nextPosition = position.add(direction);

        if (WorldManager.getInstance().isWalkable(nextPosition)) {

            WorldManager.getInstance().remove(this);

            Set<Item> items = WorldManager.getInstance().get(nextPosition);
            if (items.contains(ItemEnum.MUSHROOM)) {
                WorldManager.getInstance().remove(ItemEnum.MUSHROOM);
                inventory.add(ItemEnum.MUSHROOM);
            }

            position = nextPosition;
            numberOfMoves++;

            setPosition(nextPosition);
            WorldManager.getInstance().add(this);

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
        //inventory.get(selectedItem).ifPresent(Item -> Item.use(this));
        // TODO: finish
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
