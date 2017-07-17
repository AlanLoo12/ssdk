package model.world.generator;

import model.Item;
import model.Position;
import model.world.WorldManager;

import java.util.Random;

/**
 * Generates stuff on the random walker's path
 */
class RandomFillGenerator {
    private static final Random RANDOM = new Random();
    private final int radius;
    private float fillChance = 1;
    private float density;
    private Item item;

    RandomFillGenerator(Item item, float density) throws IllegalArgumentException {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be between 0 and 1 (inclusive)");
        }

        this.item = item;
        this.density = density;
        radius = 0;
    }

    RandomFillGenerator(Item item, float density, int radius) throws IllegalArgumentException {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be between 0 and 1 (inclusive)");
        }

        this.item = item;
        this.density = density;
        this.radius = radius;
    }

    RandomFillGenerator(Item item, float density, int radius, float fillChance) {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be between 0 and 1 (inclusive)");
        }


        if (fillChance < 0 || fillChance > 1) {
            throw new IllegalArgumentException("Fill chance must be between 0 and 1 (inclusive)");
        }

        this.item = item;
        this.density = density;
        this.radius = radius;
        this.fillChance = fillChance;
    }

    /**
     * Try to generate the given item near the given position
     * @param position position near which to generate
     * @return true if the world was modified, false otherwise
     */
    boolean generate(Position position) {
        if (RANDOM.nextFloat() < density) {
            if (radius == 0) {
                return WorldManager.getInstance().put(position, item);
            } else {
                return generateInRadius(position);
            }
        }

        return false;
    }

    private boolean generateInRadius(Position position) {
        boolean returnVal = false;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                if (RANDOM.nextFloat() < fillChance) { // TODO: finish fillChance logic
                    if (WorldManager.getInstance().safePut(position.add(x, y), item)) {
                        returnVal = true;
                    }
                }
            }
        }

        return returnVal;
    }
}
