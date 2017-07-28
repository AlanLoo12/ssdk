package model.world.area;

import model.Position;

/**
 * Area is a set of positions
 */
public interface Area {
    boolean contains(Position position);
}
