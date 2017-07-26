package model.item;

import model.world.Area;

import java.util.Set;

/**
 * Something that can *look* at the world
 */
public interface WorldObserver {

    /**
     * Get all the visible items, limiting the set using the given parameters
     * @param height maximum height difference of the visible positions
     * @param width maximum width difference of the visible positions
     * @return visible items
     */
    Set<Item> getVisibleItems(int height, int width);
}
