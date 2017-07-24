package model.item;

import model.Position;

/**
 * Represents the push behaviour on an item
 */
public interface PushBehavior {
    void push(Position direction, int force);
}
