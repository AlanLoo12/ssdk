package model.world.area;

import model.Position;

/**
 * A rectangular area
 */
class RectangularArea implements Area {
    private final Position topLeftCorner;
    private final Position bottomRightCorner;

    RectangularArea(Position position, int height, int width) {
        topLeftCorner = position;
        bottomRightCorner = position.add(width - 1, height - 1);
    }

    @Override
    public boolean contains(Position position) {
        return (topLeftCorner.getX() <= position.getX() &&
                position.getX() <= bottomRightCorner.getX()) &&
                (topLeftCorner.getY() <= position.getY() &&
                        position.getY() <= bottomRightCorner.getY());
    }
}
