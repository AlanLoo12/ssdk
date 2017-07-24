package model.item;

import model.Position;

/**
 * A push behavior that doesn't do anything
 */
public class NoPush implements PushBehavior {
    @Override
    public void push(Position direction, int force) {
        // pass
    }
}
