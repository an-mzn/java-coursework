package simulation.weather;

import java.util.ArrayList;
import java.util.List;


/**
 * Class representing a weather cycle consisting of at least one stage.
 * This class uses the builder pattern to streamline cycle configuration.\
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 * @see
 */
public final class WeatherCycle {

    /**
     * List of stages being used to create the cycle
     */
    private final List<WeatherCycleStage> elementList;

    public static Builder builder() {
        return new Builder();
    }

    private WeatherCycle(Builder builder) {
        this.elementList = builder.elementList;
    }

    /**
     * Accessor for the weather cycle's stages.
     *
     * @return list of cycle stages
     */
    public List<WeatherCycleStage> getCycle() {
        return elementList;
    }

    public static class Builder {
        private final List<WeatherCycleStage> elementList = new ArrayList<>();

        /**
         * Adds a new stage to the weather cycle being built.
         *
         * @param weatherCondition Weather condition for the cycle
         * @param minHours         Minimum duration of the stage in hours.
         * @param maxHours         Maximum duration of the stage in hours.
         * @return The instance of this builder (makes it possible to do method chaining)
         */
        public Builder add(WeatherCondition weatherCondition, int minHours, int maxHours) {
            elementList.add(new WeatherCycleStage(weatherCondition, minHours, maxHours));
            return this;
        }

        /**
         * @return A valid instance of the weather cycle.
         */
        public WeatherCycle build() {
            if (elementList.isEmpty()) {
                throw new RuntimeException("Cannot build empty weather cycle!");
            }
            return new WeatherCycle(this);
        }
    }
}
