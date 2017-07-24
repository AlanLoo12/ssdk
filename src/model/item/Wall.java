package model.item;

import javafx.scene.image.Image;
import model.Position;

import java.nio.file.Paths;

/**
 * A simple wall
 */
public class Wall extends Item {
    public Wall(Position position) {
        super(position);

        volume = 0.7f; // you need wall + floor to occupy the whole block

        image = new Image(Paths.get("assets", "tiles", "dc-dngn", "wall",
                "brick_brown0.png").toUri().toString());
    }
}
