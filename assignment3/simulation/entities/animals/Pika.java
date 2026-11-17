package simulation.entities.animals;

import simulation.Clock;
import simulation.common.Location;
import simulation.common.Randomizer;
import simulation.entities.plants.Grass;
import simulation.field.Field;
import simulation.weather.WeatherCondition;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Class implementing the Pika animal
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public class Pika extends Animal {
    private static final Random rand = Randomizer.getRandom();
    private static final Color COLOR = new Color(0xA48855);

    private static final int MAX_AGE = Clock.daysToSteps(100);

    // Nutrition
    private static final int GRASS_FOOD_VALUE = 7;
    private static final int GRAZING_RANGE = 3;

    // Reproduction
    private static final int SEXUAL_MATURITY_AGE = 3;
    private static final int GESTATION_PERIOD = 3;
    private static final double BREEDING_PROBABILITY = 0.4;
    private static final int BREED_RANGE = 7;
    private static final int MAX_LITTER_SIZE = 13;

    // Disease
    private static final double RABIES_CONTAGION = 0.08;
    private static final double RABIES_ENVIRONMENTAL = 0.005;

    public Pika(Location location, int age, Gender gender) {
        super(location, age, MAX_AGE, SEXUAL_MATURITY_AGE, GESTATION_PERIOD, gender, COLOR);
        increaseFoodLevel(5);
    }

    public static Pika newborn(Location location) {
        return new Pika(location, 0, Gender.random(rand));
    }

    public static Pika random(Location location) {
        int age = rand.nextInt(MAX_AGE);
        Gender gender = Gender.random(rand);
        return new Pika(location, age, gender);
    }

    @Override
    public void act(Field currentField, Field nextFieldState, float hour, WeatherCondition weatherCondition) {
        incrementAge();
        if (!isAlive()) {
            return;
        }

        if (6 < hour && hour < 20) {
            // Eating
            decrementHunger();

            currentField.getAdjacentOfType(GRAZING_RANGE, getLocation(), Grass.class).findFirst().ifPresent(entity -> {
                increaseFoodLevel(GRASS_FOOD_VALUE * 3);  // plants are un-affected when eating plants
            });

            // Reproduction
            switch (getGender()) {
                case MALE -> {
                    if (rand.nextDouble() <= BREEDING_PROBABILITY) {
                        meetMate(currentField, BREED_RANGE, this.getClass()).ifPresent(mate -> {
                            mate.impregnate(rand.nextInt(MAX_LITTER_SIZE));
                        });
                    }
                }
                case FEMALE -> updatePregnancy(nextFieldState, Pika::newborn);
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

            // Movement
            nextFieldState.getFreeAdjacentLocation(getLocation(), 1).ifPresentOrElse(
                    location -> moveTo(nextFieldState, location),
                    this::setDead);
        } else {
            // Asleep
            stay(nextFieldState);
        }
    }
}
