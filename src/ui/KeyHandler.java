package ui;

import javafx.scene.Group;
import javafx.scene.Scene;
import model.Player;

import static com.sun.javafx.scene.traversal.Direction.*;

/**
 * Handles keys
 */
public class KeyHandler {
    private final Player player;

    public KeyHandler(Player player) {
        this.player = player;
    }

    /**
     * Add the key handler to the given scene
     * @param scene scene to which to add a key handler
     */
    public void registerScene(Scene scene) {
        scene.setOnKeyTyped(event -> {
            switch (event.getCharacter()) {
                case "w":
                    player.move(UP);
                    break;
                case "a":
                    player.move(LEFT);
                    break;
                case "s":
                    player.move(DOWN);
                    break;
                case "d":
                    player.move(RIGHT);
                    break;
                default:
                    break;
            }
        });
    }
}
