package simulation.entities.animals;

import simulation.Clock;
import simulation.common.Location;
import simulation.common.Randomizer;
import simulation.field.Field;
import simulation.weather.WeatherCondition;

import java.awt.*;
import java.util.Random;

/**
 * Class implementing the Wolf animal
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public class Wolf extends Animal {
    private static final Random rand = Randomizer.getRandom();
    private static final Color COLOR = new Color(0x5b5c56);

    private static final int MAX_AGE = Clock.daysToSteps(100);

    // Nutrition
    private static final int RABBIT_FOOD_VALUE = 15;

    // Reproduction
    private static final int BREED_RANGE = 5;
    private static final int SEXUAL_MATURITY_AGE = 5;
    private static final int GESTATION_PERIOD = 3;
    private static final double BREEDING_PROBABILITY = 0.2;
    private static final int MAX_LITTER_SIZE = 4;

    // Disease
    private static final double RABIES_CONTAGION = 0.08;
    private static final double RABIES_ENVIRONMENTAL = 0.0001;

    public Wolf(Location location, int age, Gender gender) {
        super(location, age, MAX_AGE, SEXUAL_MATURITY_AGE, GESTATION_PERIOD, gender, COLOR);
        increaseFoodLevel(rand.nextInt(RABBIT_FOOD_VALUE * 5));
    }

    public static Wolf random(Location location) {
        int age = rand.nextInt(MAX_AGE);
        Gender gender = Gender.random(rand);
        return new Wolf(location, age, gender);
    }

    public static Wolf newborn(Location location) {
        return new Wolf(location, 0, Gender.random(rand));
    }

    @Override
    public void act(Field currentField, Field nextFieldState, float hour, WeatherCondition weatherCondition) {
        incrementAge();
        decrementHunger();
        if (isDead()) {
            return;
        }

        if (12 < hour && hour < 23) {
            // Eating
            findFoodOfTypes(currentField, 3, Goat.class).ifPresentOrElse(
                    prey -> {
                        increaseFoodLevel(RABBIT_FOOD_VALUE);
                        prey.setDead();
                        moveTo(nextFieldState, prey.getLocation());
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
                case FEMALE -> updatePregnancy(nextFieldState, Wolf::newborn);
            }

            // Disease stuff
            if (getDiseases().contains(Disease.RABIES)) {
                if (rand.nextDouble() <= RABIES_CONTAGION) {
                    currentField.getAdjacentOfType(1, getLocation(), Goat.class).findFirst().ifPresent(other -> {
                        other.infect(Disease.RABIES);
                    });
                }

                // Engage in cannibalism >:D
                if (rand.nextDouble() <= 0.1) {
                    findSingleFood(currentField, 1, Pika.class).ifPresent(Pika::setDead);
                }
            } else {
                if (rand.nextDouble() <= RABIES_ENVIRONMENTAL) {
                    this.infect(Disease.RABIES);
                }
            }
        } else {
            stay(nextFieldState);
        }

    }
}
