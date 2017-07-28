package model.item;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import model.Inventory;
import model.Position;
import model.world.HashSetWorld;
import model.world.World;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * A world item
 *
 * Builder pattern,
 * Strategy Pattern
 */
public abstract class Item extends Observable {
    private static World world;

    private float volume;
    private Position position;
    private Image image;
    private PushBehavior pushBehavior;
    private TickBehavior tickBehavior;

    /**
     * Construct the item with the given position and add it to the world
     *
     * Fill the fields with default values:
     *  * random image
     *  * NoPush as pushBehavior
     *  * NoTick as tickBehavior
     *
     * @param position position of the item
     */
    public Item(Position position) {
        this.position = position;
        image = randomImage();
        pushBehavior = new NoPush();
        tickBehavior = new NoTick();

        getWorld().add(this);
    }

    //  ___________________________________________________________________
    // |                        _                   _                      |
    // |         _ __    _ __  (_) __   __   __ _  | |_    ___             |
    // |        | '_ \  | '__| | | \ \ / /  / _` | | __|  / _ \            |
    // |        | |_) | | |    | |  \ V /  | (_| | | |_  |  __/            |
    // |        | .__/  |_|    |_|   \_/    \__,_|  \__|  \___|            |
    // |        |_|                                                        |
    // |___________________________________________________________________|

    private Image randomImage() {
        WritableImage image = new WritableImage(1, 1);
        image.getPixelWriter().setColor(0,0, randomColor());

        return image;
    }

    private Color randomColor() {
        Random random = new Random();

        return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    //  ___________________________________________________________________
    // |                          _                   _                _   |
    // |   _ __    _ __    ___   | |_    ___    ___  | |_    ___    __| |  |
    // |  | '_ \  | '__|  / _ \  | __|  / _ \  / __| | __|  / _ \  / _` |  |
    // |  | |_) | | |    | (_) | | |_  |  __/ | (__  | |_  |  __/ | (_| |  |
    // |  | .__/  |_|     \___/   \__|  \___|  \___|  \__|  \___|  \__,_|  |
    // |  |_|                                                              |
    // |___________________________________________________________________|

    protected static World getWorld() {
        if (world == null) {
            world = new HashSetWorld();
        }

        return world;
    }

    protected void setVolume(float volume) {
        this.volume = volume;
    }

    protected void setPosition(Position position) {
        this.position = position;
    }

    protected void setImage(Image image) {
        this.image = image;
    }

    protected void setPushBehavior(PushBehavior pushBehavior) {
        this.pushBehavior = pushBehavior;
    }

    protected void setTickBehavior(TickBehavior tickBehavior) {
        this.tickBehavior = tickBehavior;
    }


    //  ___________________________________________________________________
    // |                        _       _   _                              |
    // |        _ __    _   _  | |__   | | (_)   ___                       |
    // |       | '_ \  | | | | | '_ \  | | | |  / __|                      |
    // |       | |_) | | |_| | | |_) | | | | | | (__                       |
    // |       | .__/   \__,_| |_.__/  |_| |_|  \___|                      |
    // |       |_|                                                         |
    // |___________________________________________________________________|

    public float getVolume() {
        return volume;
    }

    public Position getPosition() {
        return position;
    }

    public Image getImage() {
        return image;
    }

    public void push(Position direction, int force) {
        pushBehavior.push(direction, force);
    }

    public void tick() {
        tickBehavior.tick();
    }

    public void store(Inventory inventory) {
        // TODO: finish
    }
}
