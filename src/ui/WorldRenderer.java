package ui;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.*;
import model.item.Item;
import model.item.Player;
import model.world.WorldManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

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
    private Position localWorldCenter;
    private Point2D center;
    private Group group;
    private boolean changed;

    /**
     * Connect this renderer to the given world
     *
     */
    WorldRenderer(double width, double height, Player player) {
        group = new Group();
        worldCanvas = new Canvas(width, height);
        group.getChildren().add(worldCanvas);
        this.center = new Point2D(width / 2, height / 2);
        this.player = player;
        this.localWorldCenter = player.getPosition();

        WorldManager.getInstance().addObserver(this);
        player.addObserver(this);
        changed = true;
        render();
    }

    void render() {
        if (changed) {
            changed = false;

            GraphicsContext graphicsContext = worldCanvas.getGraphicsContext2D();
            clearCanvas(graphicsContext);

            //updateLocalWorldCenter();
            localWorldCenter = player.getPosition();

            Position from = localWorldCenter.add(
                    (int) (-center.getX() / scale - 1),
                    (int) (-center.getY() / scale - 1));
            Position to = localWorldCenter.add(
                    (int) (center.getX() / scale),
                    (int) (center.getY() / scale));

            Map<Position, Set<Item>> visiblePositions = WorldManager.getInstance().get(from, to);

            for (Position position : visiblePositions.keySet()) {
                double x = getScreenX(position);
                double y = getScreenY(position);

                List<Image> imageSequence = getImageSequence(visiblePositions.get(position));
                for (int i = 0; i < imageSequence.size(); i++) {
                    Image image = imageSequence.get(i);

                    graphicsContext.drawImage(image, x, y, scale, scale);
                }
            }

            graphicsContext.setFill(Color.rgb(100, 100, 100, 0.5));
            Position lookPosition = player.getPosition().add(player.getLookDirection());
            graphicsContext.strokeRect(getScreenX(lookPosition), getScreenY(lookPosition), scale, scale);
        }
    }

    private List<Image> getImageSequence(Set<Item> items) {
        List<Item> result = new ArrayList<>(items);
        result.sort((o1, o2) -> sign(o1.getVolume() - o2.getVolume()));

        return result.stream().map(Item::getImage).collect(Collectors.toList());
    }

    private int sign(float v) {
        if (v == 0) {
            return 0;
        } else if (v < 0) {
            return -1;
        } else {
            return 1;
        }
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
        if (arg == null || isOnScreen((Position) arg)) {
            changed = true;
        }
    }

    void zoomIn() {
        scale++;
        changed = true;
        render();
    }

    void zoomOut() {
        scale = Math.max(scale - 1, MIN_SCALE);
        changed = true;
        render();
    }

    Node getGroup() {
        return group;
    }
}
