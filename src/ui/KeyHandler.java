package ui;

import javafx.scene.Scene;
import model.Player;

import static model.Position.*;

/**
 * Handles keys
 */
class KeyHandler {
    private final Player player;
    private final WorldRenderer worldRenderer;

    KeyHandler(Player player, WorldRenderer worldRenderer) {
        this.player = player;
        this.worldRenderer = worldRenderer;
    }

    /**
     * Add the key handler to the given scene
     * @param scene scene to which to add a key handler
     */
    void registerScene(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown()) {
                switch (event.getCode()) {
                    case UP:
                        player.look(UP);
                        break;
                    case LEFT:
                        player.look(LEFT);
                        break;
                    case DOWN:
                        player.look(DOWN);
                        break;
                    case RIGHT:
                        player.look(RIGHT);
                        break;
                    default:
                        break;
                }
            } else {
                switch (event.getCode()) {
                    case UP:
                        player.move(UP);
                        break;
                    case LEFT:
                        player.move(LEFT);
                        break;
                    case DOWN:
                        player.move(DOWN);
                        break;
                    case RIGHT:
                        player.move(RIGHT);
                        break;
                    case W:
                        player.look(UP);
                        break;
                    case A:
                        player.look(LEFT);
                        break;
                    case S:
                        player.look(DOWN);
                        break;
                    case D:
                        player.look(RIGHT);
                        break;



                    case T:
                        player.look(UP);
                        break;
                    case U:
                        player.look(UP.add(RIGHT));
                        break;
                    case Y:
                        player.look(UP.add(LEFT));
                        break;
                    case G:
                        player.look(LEFT);
                        break;
                    case F:
                        player.look(DOWN);
                        break;

                    case V:
                        player.look(DOWN.add(LEFT));
                        break;

                    case B:
                        player.look(DOWN.add(RIGHT));
                        break;
                    case H:
                        player.look(RIGHT);
                        break;

                    case C:
                        player.look(ORIGIN);
                        break;

                    case EQUALS:
                        worldRenderer.zoomIn();
                        break;
                    case MINUS:
                        worldRenderer.zoomOut();
                        break;
                    case Q:
                    case ESCAPE:
                        System.exit(0);
                        break;
                    case Z:
                        player.selectPreviousInventoryItem();
                        break;
                    case X:
                        player.selectNextInventoryItem();
                        break;
                    case ENTER:
                    case R:
                        player.useInventoryItem();
                    default:
                        break;
                }
            }
        });
    }
}
