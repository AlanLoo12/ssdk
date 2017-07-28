package model.world.area;

import model.Position;

/**
 * Produces areas
 */
public class AreaFactory {
    private AreaFactory() {}

    /**
     * Get rectangle area with the given corner position
     * @param position top left corner of the area
     * @param height height of the rectangle
     * @param width width of the rectangle
     * @return rectangle area with the given corner position
     */
    public static Area getRectangle(Position position, int height, int width) {
        // TODO: add exceptions
        return new RectangularArea(position, height, width);
    }
}
