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

        setVolume(0.7f);
        setImage(new Image(Paths.get("assets", "tiles", "dc-dngn", "wall",
                "brick_brown0.png").toUri().toString()));
    }
}
