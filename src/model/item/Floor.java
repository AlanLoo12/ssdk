package model.item;

import javafx.scene.image.Image;
import model.Position;

import java.nio.file.Paths;

/**
 *
 */
public class Floor extends Item {
    public Floor(Position position) {
        super(position);

        setImage(new Image(Paths.get("assets", "tiles", "dc-dngn", "floor", "floor_sand_stone0.png").toUri().toString()));
    }
}
