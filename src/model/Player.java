package model;

import com.sun.javafx.scene.traversal.Direction;

import java.util.Optional;

/**
 * A player (yep, that's it).
 */
public class Player {
    private World world;
    private Position position;

    public Player(World world) {
        this.world = world;

        position = new Position(0,0);

        world.put(position, Entity.PLAYER);
    }

    public void move(Direction direction) {
        if (world.isWalkable(position.get(direction))) {
            Position nextPosition = position.get(direction);

            world.remove(position);

            Optional<Entity> entity = world.get(nextPosition);
            entity.ifPresent(e -> world.put(position, e));
            world.put(nextPosition, Entity.PLAYER);

            position = nextPosition;
        }
    }

    public Position getPosition() {
        return position;
    }
}
