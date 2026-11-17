package simulation.entities.animals;

import simulation.Clock;
import simulation.common.Location;
import simulation.common.Randomizer;
import simulation.field.Field;
import simulation.weather.WeatherCondition;

import java.awt.*;
import java.util.Random;

/**
 * Class implementing the Leopard animal
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public class Leopard extends Animal {
    private static final Random rand = Randomizer.getRandom();
    private static final Color COLOR = new Color(0xbf9267);

    private static final int MAX_AGE = Clock.daysToSteps(100);

    // Nutrition
    private static final int PREY_FOOD_VALUE = 50;

    // Reproduction
    private static final int BREED_RANGE = 10;
    private static final int SEXUAL_MATURITY_AGE = 5;
    private static final int GESTATION_PERIOD = 3;
    private static final double BREEDING_PROBABILITY = 0.18;
    private static final int MAX_LITTER_SIZE = 4;


    public Leopard(Location location, int age, Gender gender) {
        super(location, age, MAX_AGE, SEXUAL_MATURITY_AGE, GESTATION_PERIOD, gender, COLOR);
        increaseFoodLevel(rand.nextInt(PREY_FOOD_VALUE));
    }

    public static Leopard random(Location location) {
        int age = rand.nextInt(MAX_AGE);
        Gender gender = Gender.random(rand);
        return new Leopard(location, age, gender);
    }

    public static Leopard newborn(Location location) {
        return new Leopard(location, 0, Gender.random(rand));
    }

    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     *
     * @param currentField     The field currently occupied.
     * @param nextFieldState   The updated field.
     * @param weatherCondition
     */
    @Override
    public void act(Field currentField, Field nextFieldState, float hour, WeatherCondition weatherCondition) {
        incrementAge();
        decrementHunger();
        if (isDead()) {
            return;
        }

        if (8 < hour && hour < 19) {
            // Eating
            findFoodOfTypes(currentField, 1, Pika.class, Goat.class).ifPresentOrElse(
                    prey -> {
                        // Kill prey and digest it
                        moveTo(nextFieldState, prey.getLocation());
                        prey.setDead();
                        increaseFoodLevel(PREY_FOOD_VALUE);
                    },
                    () -> nextFieldState.getFreeAdjacentLocation(getLocation(), 1).ifPresent(
                            location -> moveTo(nextFieldState, location))
            );

            // Reproduction
            switch (getGender()) {
                case MALE -> {
                    if (rand.nextDouble() <= BREEDING_PROBABILITY) {
                        meetMate(currentField, BREED_RANGE, this.getClass())
                                .ifPresent(mate -> {
                                    mate.impregnate(rand.nextInt(MAX_LITTER_SIZE));
                                });
                    }
                }
                case FEMALE -> updatePregnancy(nextFieldState, Leopard::newborn);
            }
        } else {
            // Sleep
            stay(nextFieldState);
        }
    }
}
