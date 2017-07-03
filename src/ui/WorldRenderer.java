package ui;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.*;
import model.Item;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import static javafx.scene.paint.Color.BLACK;

/**
 * Renders the world on the scene
 */
public class WorldRenderer implements Observer {
    private static final int MIN_SCALE = 5;
    private int scale = 20;
    private static final int BOUNDING_BOX_X = 10;
    private static final int BOUNDING_BOX_Y = 5;
    private static final Paint FLOOR_COLOR = Color.BEIGE;
    private final Player player;

    private Canvas worldCanvas;
    private World world;
    private Position localWorldCenter;
    private Point2D center;
    private Group group;

    /**
     * Connect this renderer to the given world
     * @param world world to render
     *
     */
    WorldRenderer(World world, double width, double height, Player player) {
        group = new Group();
        worldCanvas = new Canvas(width, height);
        group.getChildren().add(worldCanvas);
        this.world = world;
        this.center = new Point2D(width / 2, height / 2);
        this.player = player;
        this.localWorldCenter = player.getPosition();

        world.addObserver(this);
        player.addObserver(this);
        updateGroup();
    }

    private void updateGroup() {
        GraphicsContext graphicsContext = worldCanvas.getGraphicsContext2D();
        clearCanvas(graphicsContext);

        //updateLocalWorldCenter();
        localWorldCenter = player.getPosition();

        for (Position position : computeVisiblePositions()) {
            if (world.contains(position)) {
                double x = getScreenX(position);
                double y = getScreenY(position);

                if (world.contains(position)) {
                    graphicsContext.setFill(getColor(position));
                } else {
                    graphicsContext.setFill(FLOOR_COLOR);
                }

                graphicsContext.fillRect(x, y, scale, scale);
            }
        }

        graphicsContext.setFill(Color.rgb(100,100,100,0.5));
        Position lookPosition = player.getPosition().add(player.getLookDirection());
        graphicsContext.strokeRect(getScreenX(lookPosition), getScreenY(lookPosition), scale, scale);
    }

    private Paint getColor(Position position) {
        int highestItemSoFar = -1;
        Color highestColorSoFar = BLACK;

        for (Item item : World.getInstance().get(position)) {
            if (item.getIndex() > highestItemSoFar) {
                highestColorSoFar = item.getColor();
                highestItemSoFar = item.getIndex();
            }
        }

        return highestColorSoFar;
    }

    private double getScreenY(Position position) {
        return (position.getY() - localWorldCenter.getY()) * scale + center.getY();
    }

    private double getScreenX(Position position) {
        return (position.getX() - localWorldCenter.getX()) * scale + center.getX();
    }

    private void updateLocalWorldCenter() {
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
    }

    private Set<Position> computeVisiblePositions() {
        Set<Position> visiblePositions = new HashSet<>();
        for (int x = (int) (-center.getX() / scale - 1); x < center.getX() / scale; x++) {
            for (int y = (int) (-center.getY() / scale - 1); y < center.getY() / scale; y++) {
                Position visiblePosition = new Position(
                        x + localWorldCenter.getX(),
                        y + localWorldCenter.getY());

                visiblePositions.add(visiblePosition);
            }
        }


        return visiblePositions;
    }

    private void clearCanvas(GraphicsContext graphicsContext) {
        graphicsContext.setFill(BLACK);
        graphicsContext.fillRect(0,0, worldCanvas.getWidth(), worldCanvas.getHeight());
    }

    /**
     * Produce true if the given position is on the screen
     */
    private boolean isOnScreen(@NotNull Position position) {
        int x = (position.getX() - localWorldCenter.getX());
        int y = (position.getY() - localWorldCenter.getY());

        return ((- center.getX() / scale <= x && x <= center.getX() / scale) &&
                (- center.getY() / scale <= y && y <= center.getY() / scale));
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
        if (!world.isEnded()) {
            if (arg == null || isOnScreen((Position) arg)) {
                updateGroup();
            }
        } else {
            GraphicsContext graphicsContext = worldCanvas.getGraphicsContext2D();
            clearCanvas(graphicsContext);

            Text endText = new Text(center.getX(), center.getY(),
                    "Well done!" +
                            "\nYou " + (world.isWin()? "won" : "lost") +
                            "\nTime spent playing: " + player.getTime() + " s" +
                            "\nTotal number of moves made: " + player.getMoves());
            endText.setFill(BLACK);

            group.getChildren().clear();
            group.getChildren().add(endText);
        }
    }

    void zoomIn() {
        scale++;
        updateGroup();
    }

    void zoomOut() {
        scale = Math.max(scale - 1, MIN_SCALE);
        updateGroup();
    }

    Node getGroup() {
        return group;
    }
}
