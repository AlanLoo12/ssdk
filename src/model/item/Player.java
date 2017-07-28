package model.item;

import javafx.scene.image.Image;
import model.Inventory;
import model.Position;
import model.world.area.Area;
import model.world.area.AreaFactory;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * The entity controlled by the user. It can see the world.
 */
public class Player extends Item implements WorldObserver {
    private int numberOfMoves;
    @Deprecated // TODO: see the uml diagram
    private long initialTime;
    @Deprecated // TODO: move to inventory
    private int selectedItem;
    private Inventory inventory;
    private Position lookDirection;

    public Player(Position position) {
        super(position);

        initPlayer();
    }

    private void initPlayer() {
        inventory = new Inventory();
        inventory.add(new PickAxe(getPosition()));

        numberOfMoves = 0;
        initialTime = System.currentTimeMillis();
        selectedItem = 0;
        lookDirection = Position.ORIGIN;

        setImage(new Image(Paths.get("assets", "tiles", "simple_player.png").toUri().toString()));
        setVolume(0.6f);
    }

    //  ___________________________________________________________________
    // |                        _       _   _                              |
    // |        _ __    _   _  | |__   | | (_)   ___                       |
    // |       | '_ \  | | | | | '_ \  | | | |  / __|                      |
    // |       | |_) | | |_| | | |_) | | | | | | (__                       |
    // |       | .__/   \__,_| |_.__/  |_| |_|  \___|                      |
    // |       |_|                                                         |
    // |___________________________________________________________________|

    public void move(Position direction) {
        Position nextPosition = getPosition().add(direction);

        //if (WorldManager.getInstance().isWalkable(nextPosition)) {
            numberOfMoves++;

            setPosition(nextPosition);

            setChanged();
            notifyObservers();
        //}
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

    @Deprecated
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

    @Deprecated
    public void selectNextItem() {
        if (inventory.size() > 0) {
            selectedItem = (selectedItem + 1) % inventory.size();

            setChanged();
            notifyObservers();
        }
    }

    @Deprecated
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
    @Deprecated
    public void useItem() {
        //inventory.get(selectedItem).ifPresent(Item -> Item.use(this));
        // TODO: finish
    }

    /**
     * Get all the visible items, limiting the set using the given parameters
     *
     * @param height maximum height difference of the visible positions
     * @param width  maximum width difference of the visible positions
     * @return visible items
     */
    @Override
    public Set<Item> getVisibleItems(int height, int width) {
        return getWorld().get(AreaFactory.getRectangle(getPosition().sub(width / 2, height / 2), height, width));
    }
}
