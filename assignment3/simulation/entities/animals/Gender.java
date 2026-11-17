package simulation.entities.animals;

import java.util.Random;

/**
 * Represents gender and provides a random method for randomly
 * selecting a gender.
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public enum Gender {
    MALE, FEMALE;

    /**
     * Gets a random gender.
     *
     * @param rand Reference to a random object.
     * @return Either male/female with a 50% chance of each.
     */
    public static Gender random(Random rand) {
        return rand.nextBoolean() ? MALE : FEMALE;
    }
}
