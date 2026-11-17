package simulation.weather;

import simulation.Clock;
import simulation.common.Randomizer;

import java.util.List;
import java.util.Random;

/**
 * Manages the weather cycle throughout a simulation. The Cycle is ordered, but
 * the length of each stage is randomised within a specified range.
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public class WeatherManager {
    /**
     * Shared reference to random number generator
     */
    private static final Random rand = Randomizer.getRandom();

    /**
     * Ordered list of weather cycle stages.
     */
    private final List<WeatherCycleStage> weatherCycleStageList;
    /**
     * An index of the current weather cycle stage.
     */
    private int currentWeatherCycleIndex;

    /**
     * The next hour count to generate the next cycle wait time.
     */
    private int nextCycleGenerationHour;

    /**
     * Reference to clock used in the simulation.
     */
    private final Clock simulationClock;

    /**
     * @param clock        Reference to the clock used in the simulation
     * @param cycleBuilder A weather cycle builder describing the stages to add to the cycle.
     *                     The builder will be built in this constructor.
     */
    public WeatherManager(Clock clock, WeatherCycle weatherCycle) {
        this.simulationClock = clock;
        this.weatherCycleStageList = weatherCycle.getCycle();
        reset();
    }

    /**
     * @param index new index for the current stage. (must be a valid index)
     */
    private void setWeatherCycle(int index) {
        // Set index for current condition
        currentWeatherCycleIndex = index;

        // Generate new hour count for next cycle generation
        WeatherCycleStage condition = weatherCycleStageList.get(currentWeatherCycleIndex);
        nextCycleGenerationHour = simulationClock.getTotalHoursPassed() + condition.generateRandomHour(rand);
    }

    /**
     * Updates the state of the weather manager. Must be called every step of the simulation.
     * After calling this method, getCurrentWeather() can be used to get the updated weather.
     */
    public void updateWeather() {
        // Check if exceeded time, if so go to the next stage
        if (simulationClock.getTotalHoursPassed() >= nextCycleGenerationHour) {
            int wrappedIndex = (currentWeatherCycleIndex + 1) % weatherCycleStageList.size();
            setWeatherCycle(wrappedIndex);
        }
    }

    /**
     * Gets the current weather condition given the current time in the simulation clock.
     *
     * @return The current weather condition.
     */
    public WeatherCondition getCurrentWeather() {
        return weatherCycleStageList.get(currentWeatherCycleIndex).weatherCondition();
    }

    public void reset() {
        currentWeatherCycleIndex = 0;
        nextCycleGenerationHour = 0;
        this.setWeatherCycle(0);
    }
}
