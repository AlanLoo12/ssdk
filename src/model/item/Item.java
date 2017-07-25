package model.item;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import model.Position;

import java.util.Observable;
import java.util.Random;

/**
 * A world item
 *
 * Builder pattern,
 * Strategy Pattern
 */
public abstract class Item extends Observable {
    Position position;
    Image image;
    PushBehavior pushBehavior;
    float volume;
    TickBehavior tickBehavior;

    public Item(Position position) {
        this.position = position;
        image = randomImage();
        pushBehavior = new NoPush();
        tickBehavior = new NoTick();
    }

    public Item() {
        this.position = Position.ORIGIN;
        image = randomImage();
        pushBehavior = new NoPush();
        tickBehavior = new NoTick();
    }

    private Image randomImage() {
        WritableImage image = new WritableImage(1, 1);
        image.getPixelWriter().setColor(0,0, randomColor());

        return image;
    }

    private Color randomColor() {
        Random random = new Random();

        return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public Position getPosition() {
        return position;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getVolume() {
        return volume;
    }

    public void push(Position direction, int force) {
        pushBehavior.push(direction, force);
    }

    public void tick() {
        tickBehavior.tick();
    }

    public static Item valueOf(String word) {
        return null; // TODO: finish
    }

    public static Item[] values() {
        return new Item[0]; // TODO: finish
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
