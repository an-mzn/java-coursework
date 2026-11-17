package simulation.entities.plants;

import simulation.Clock;
import simulation.common.Location;
import simulation.common.Randomizer;
import simulation.field.Field;
import simulation.weather.WeatherCondition;

import java.awt.Color;
import java.util.Map;
import java.util.Random;

public class Shrub extends Plant {

    private static final Color COLOR = new Color(0x4C6A54);

    private static final int MAX_AGE = Clock.daysToSteps(50);
    private static final int OVERCROWDING_DISTANCE = 2;

    private static final int SPROUT_RANGE = 1;
    private static final int SPROUTS = 1;

    private static final Map<WeatherCondition, Double> SPROUT_PROBABILITY = Map.of(
            WeatherCondition.NORMAL, 0.01,
            WeatherCondition.RAIN, 0.03,
            WeatherCondition.FOG, 0.01
    );

    private static final Random rand = Randomizer.getRandom();

    public Shrub(Location location, int age) {
        super(location, age, MAX_AGE, COLOR);
    }

    public static Shrub random(Location location) {
        int age = rand.nextInt(MAX_AGE);
        return new Shrub(location, age);
    }

    public static Shrub sprout(Location location) {
        return new Shrub(location, 0);
    }

    @Override
    public void act(Field currentField, Field nextFieldState, float hour, WeatherCondition weatherCondition) {
        incrementAge();
        if (isAlive()) {
            // Reserve place in next field (required due to the way fields work)
            nextFieldState.placeEntity(this, this.getLocation());

            // Sprouting/reproduction
            if (rand.nextDouble() <= SPROUT_PROBABILITY.getOrDefault(weatherCondition, 0d)) {
                sprout(nextFieldState, SPROUT_RANGE, SPROUTS, Shrub::sprout);
            }

            // Overcrowding
            if(currentField.getFreeAdjacentLocation(getLocation(), OVERCROWDING_DISTANCE).isEmpty()) {
                setDead();
            }
        } else {
            stay(nextFieldState);
        }
    }
}
