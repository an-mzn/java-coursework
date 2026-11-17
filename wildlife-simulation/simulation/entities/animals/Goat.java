package simulation.entities.animals;

import simulation.Clock;
import simulation.common.Location;
import simulation.common.Randomizer;
import simulation.entities.plants.Grass;
import simulation.entities.plants.Shrub;
import simulation.field.Field;
import simulation.weather.WeatherCondition;

import java.awt.Color;
import java.util.Random;

/**
 * Class implementing the Goat animal
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public class Goat extends Animal {
    private static final Random rand = Randomizer.getRandom();
    private static final Color COLOR = new Color(0x977c6f);

    private static final int MAX_AGE = Clock.daysToSteps(25);

    // Nutrition
    private static final int PLANT_FOOD_VALUE = 6;
    private static final int GRAZING_DISTANCE = 3;

    // Reproduction
    private static final int SEXUAL_MATURITY_AGE = 6;
    private static final int GESTATION_PERIOD = 2;
    private static final double BREEDING_PROBABILITY = 0.4;
    private static final int BREED_RANGE = 15;
    private static final int MAX_LITTER_SIZE = 4;

    public Goat(Location location, int age, Gender gender) {
        super(location, age, MAX_AGE, SEXUAL_MATURITY_AGE, GESTATION_PERIOD, gender, COLOR);
        increaseFoodLevel(15);
    }

    public static Goat newborn(Location location) {
        return new Goat(location, 0, Gender.random(rand));
    }

    public static Goat random(Location location) {
        int age = rand.nextInt(MAX_AGE);
        Gender gender = Gender.random(rand);
        return new Goat(location, age, gender);
    }

    @Override
    public void act(Field currentField, Field nextFieldState, float hour, WeatherCondition weatherCondition) {
        incrementAge();
        if (!isAlive()) {
            return;
        }

        if (6 < hour && hour < 20) {
            decrementHunger();
            // Eating
            currentField.getAdjacentOfTypes(GRAZING_DISTANCE, getLocation(), Grass.class, Shrub.class)
                    .findFirst().ifPresent(entity -> increaseFoodLevel(PLANT_FOOD_VALUE));  // plants are un-affected when eating plants

            // Reproduction
            switch (getGender()) {
                case MALE -> {
                    if (rand.nextDouble() <= BREEDING_PROBABILITY)
                        meetMate(currentField, BREED_RANGE, this.getClass()).ifPresent(mate -> mate.impregnate(rand.nextInt(MAX_LITTER_SIZE)));
                }
                case FEMALE -> updatePregnancy(nextFieldState, Goat::newborn);
            }

            // Movement
            nextFieldState.getFreeAdjacentLocation(getLocation(), 1).ifPresentOrElse(
                    location -> moveTo(nextFieldState, location),
                    this::setDead);
        } else {
            stay(nextFieldState);
        }
    }
}
