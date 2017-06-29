package ui;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.Entity;
import model.Player;
import model.Position;
import model.World;

import java.util.Observable;
import java.util.Observer;

/**
 * Renders the world on the scene
 */
public class WorldRenderer implements Observer {
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
    }

    private void updateGroup() {
        GraphicsContext graphicsContext = worldCanvas.getGraphicsContext2D();
        clearCanvas(graphicsContext);

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

            double x = (position.getX() - localWorldCenter.getX()) * scale + center.getX();
            double y = (position.getY() - localWorldCenter.getY()) * scale + center.getY();

            if (isOnScreen(x, y)) {
                if (world.get(position).isPresent()) {
                    graphicsContext.setFill(world.get(position).get().getColor());
                } else {
                    graphicsContext.setFill(FLOOR_COLOR);
                }

                graphicsContext.fillRect(x, y, scale, scale);
            }
        }
    }

    private void clearCanvas(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0,0, worldCanvas.getWidth(), worldCanvas.getHeight());
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
        if (!world.isEnded()) {
            updateGroup();
        } else {
            GraphicsContext graphicsContext = worldCanvas.getGraphicsContext2D();
            clearCanvas(graphicsContext);

            Text endText = new Text(center.getX(), center.getY(),
                    "Well done!" +
                            "\nYou " + (world.isWin()? "won" : "lost") +
                            "\nTime spent playing: " + player.getTime() + " s" +
                            "\nTotal number of moves made: " + player.getMoves() +
                            "\nNumber of coins collected: " + player.getCoins() +
                            "\nDust piles collected: " + player.getInventory().getOrDefault(Entity.DUST, 0));
            endText.setFill(Color.WHITE);

            group.getChildren().clear();
            group.getChildren().add(endText);
        }
    }

    void zoomIn() {
        scale++;
        updateGroup();
    }

    void zoomOut() {
        scale = Math.max(scale - 1, 1);
        updateGroup();
    }

    Node getGroup() {
        return group;
    }
}
