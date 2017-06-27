package ui;

import com.sun.istack.internal.NotNull;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    private static final int SIZE = 20;
    private static final int BOUNDING_BOX_X = 10;
    private static final int BOUNDING_BOX_Y = 5;
    private static final Paint FLOOR_COLOR = Color.BEIGE;
    private final Player player;

    private Group worldGroup;
    private World world;
    private Position localWorldCenter;
    private Point2D center;

    /**
     * Connect this renderer to the given world
     * @param world world to render
     */
    WorldRenderer(World world, Point2D center, Player player) {
        worldGroup = new Group();
        this.world = world;
        this.center = center;
        this.player = player;
        this.localWorldCenter = player.getPosition();

        world.addObserver(this);
    }

    /**
     * Produce the javafx group to put on the screen
     * @return javafx group to put on the screen
     */
    @NotNull
    Group getGroup() {
        updateGroup();

        return worldGroup;
    }

    private void updateGroup() {
        for (Position position : world.getActivePositions()) {

            if (localWorldCenter.getX() - player.getPosition().getX() > BOUNDING_BOX_X) {
                localWorldCenter = localWorldCenter.add(-1,0);
            } else if (localWorldCenter.getX() - player.getPosition().getX() < - BOUNDING_BOX_X) {
                localWorldCenter = localWorldCenter.add(1,0);
            }

            if (localWorldCenter.getY() - player.getPosition().getY() > BOUNDING_BOX_Y) {
                localWorldCenter = localWorldCenter.add(0,-1);
            } else if (localWorldCenter.getY() - player.getPosition().getY() < - BOUNDING_BOX_Y) {
                localWorldCenter = localWorldCenter.add(0,1);
            }

            double x = (position.getX() - localWorldCenter.getX()) * SIZE + center.getX();
            double y = (position.getY() - localWorldCenter.getY()) * SIZE + center.getY();

            if (isOnScreen(x, y)) {

                Rectangle rectangle = new Rectangle(x, y, SIZE, SIZE);

                if (world.get(position).isPresent()) {
                    rectangle.setFill(world.get(position).get().getColor());
                } else {
                    rectangle.setFill(FLOOR_COLOR);
                }

                worldGroup.getChildren().add(rectangle);
            }
        }
    }

    /**
     * Produce true if the given position is on the screen
     */
    private boolean isOnScreen(double x, double y) {
        return ((0 <= x && x <= (center.getX() * 2)) &&
                (0 <= y && y <= (center.getY() * 2)));
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
