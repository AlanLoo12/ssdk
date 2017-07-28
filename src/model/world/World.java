package model.world;

import model.Position;
import model.item.Item;
import model.world.area.Area;

import java.util.Set;

/**
 * A world interface
 */
public interface World {

    //     ____                       _
    //    |  _ \    ___    __ _    __| |
    //    | |_) |  / _ \  / _` |  / _` |
    //    |  _ <  |  __/ | (_| | | (_| |
    //    |_| \_\  \___|  \__,_|  \__,_|

    /**
     * Get all the items in the world with the given position
     */
    Set<Item> get(Position position);

    /**
     * Get all the items in the world inside the given area
     */
    Set<Item> get(Area area);

    /**
     * Find an item according to the given search query near the given position
     */
    Item findNear(Position position, ItemSearchQuery itemSearchQuery);

    /**
     * Find items according to the given search query inside the given L1 radius
     */
    Set<Item> findInRadius(Position position, ItemSearchQuery itemSearchQuery, int radius);

    // __        __         _   _
    // \ \      / /  _ __  (_) | |_    ___
    //  \ \ /\ / /  | '__| | | | __|  / _ \
    //   \ V  V /   | |    | | | |_  |  __/
    //    \_/\_/    |_|    |_|  \__|  \___|

    /**
     * Add the given item to the world
     */
    void add(Item item);

    /**
     * Remove the given item from the world
     */
    void remove(Item item);
}
