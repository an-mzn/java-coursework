package simulation.weather;

import java.util.Random;

/**
 * Record class for storing weather stage parameters.
 * @param weatherCondition Weather condition for the stage.
 * @param minHours Minimum length of the stage in hours.
 * @param maxHours Maximum length of the stage in hours.
 */
public record WeatherCycleStage(WeatherCondition weatherCondition, int minHours, int maxHours) {
    /**
     * @param rand Reference to a random object to use.
     * @return A random number of hours in between the specified range.
     */
    public int generateRandomHour(Random rand) {
        return rand.nextInt(minHours, maxHours + 1);
    }
}
