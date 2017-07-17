package model.world.generator;

import java.util.Observable;

/**
 * A world generator actor that somehow can modify world
 */
abstract class Actor extends Observable {
    /**
     * Tick the actor once
     */
    abstract void tick();
}
