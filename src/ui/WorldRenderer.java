package ui;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.*;
import model.item.Item;
import model.item.WorldObserver;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static javafx.scene.paint.Color.BLACK;

/**
 * Renders the world on the scene
 */
class WorldRenderer {
    private static final int MIN_SCALE = 5;
    private static final int BOUNDING_BOX_X = 10;
    private static final int BOUNDING_BOX_Y = 5;

    private int scale = 20;
    private final WorldObserver worldObserver;

    private Canvas worldCanvas;
    private Point2D center;
    private Group group;
    private boolean changed;
    private double height;
    private double width;

    /**
     * Connect this renderer to the given world
     *
     */
    WorldRenderer(double width, double height, WorldObserver worldObserver) {
        this.width = width;
        this.height = height;

        group = new Group();
        worldCanvas = new Canvas(width, height);
        group.getChildren().add(worldCanvas);
        this.center = new Point2D(width / 2, height / 2);
        this.worldObserver = worldObserver;

        render();
    }

    void render() {
        GraphicsContext graphicsContext = worldCanvas.getGraphicsContext2D();
        clearCanvas(graphicsContext);

        Set<Item> visibleItems = worldObserver.getVisibleItems((int) (height / scale + 1), (int)(width / scale + 1));

            for (Item item : visibleItems) {
                Position position = item.getPosition();

                double x = getScreenX(position);
                double y = getScreenY(position);

                Image image = item.getImage();
                graphicsContext.drawImage(image, x, y, scale, scale);

                // TODO: fix image sequence stuff
                /*List<Image> imageSequence = getImageSequence(item.);
                for (int i = 0; i < imageSequence.size(); i++) {
                    Image image = imageSequence.get(i);

                    graphicsContext.drawImage(image, x, y, scale, scale);
                }*/
            }

            //graphicsContext.setFill(Color.rgb(100, 100, 100, 0.5));
            //Position lookPosition = worldObserver.getPosition().add(worldObserver.getLookDirection());
            //graphicsContext.strokeRect(getScreenX(lookPosition), getScreenY(lookPosition), scale, scale);
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
        return (position.getY() - worldObserver.getPosition().getY()) * scale + center.getY();
    }

    private double getScreenX(Position position) {
        return (position.getX() - worldObserver.getPosition().getX()) * scale + center.getX();
    }

    private void clearCanvas(GraphicsContext graphicsContext) {
        graphicsContext.setFill(BLACK);
        graphicsContext.fillRect(0,0, worldCanvas.getWidth(), worldCanvas.getHeight());
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
