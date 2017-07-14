package model.world;

/**
 * A world generator actor that somehow can modify world
 */
interface Actor {
    /**
     * Tick the actor once
     */
    void tick();
}
