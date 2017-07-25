package model.world.physics;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles world physics
 */
public class WorldPhysics {
    private Set<Actor> actors;

    public WorldPhysics() {
        actors = new HashSet<>();

        actors.add(new Water());
        actors.add(new Plants());
    }

    public void start() {
        for (Actor actor : actors) {
            actor.start();
        }
    }
}
