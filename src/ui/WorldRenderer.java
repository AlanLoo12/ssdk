package ui;

import com.sun.istack.internal.NotNull;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import model.Player;
import model.Position;
import model.World;

import java.util.Observable;
import java.util.Observer;

/**
 * Renders the world on the scene
 */
public class WorldRenderer implements Observer {
    public static final int SIZE = 30;
    private final Player player;

    private Group worldGroup;
    private World world;
    private Point2D center;

    /**
     * Connect this renderer to the given world
     * @param world world to render
     */
    public WorldRenderer(World world, Point2D center, Player player) {
        worldGroup = new Group();
        this.world = world;
        this.center = center;
        this.player = player;

        world.addObserver(this);
    }

    /**
     * Produce the javafx group to put on the screen
     * @return javafx group to put on the screen
     */
    @NotNull
    public Group getGroup() {
        updateGroup();

        return worldGroup;
    }

    private void updateGroup() {
        for (Position position : world.getActivePositions()) {
            if (!world.get(position).isEmpty()) {
                int x = (int) ((position.getX() - player.getPosition().getX()) * SIZE + center.getX());
                int y = (int) ((position.getY() - player.getPosition().getY()) * SIZE + center.getY());

                Rectangle rectangle = new Rectangle(x, y, SIZE, SIZE);
                rectangle.setFill(world.get(position).get(0).getColor());

                worldGroup.getChildren().add(rectangle);
            }
        }
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
        worldGroup.getChildren().clear();
        updateGroup();
    }
}
