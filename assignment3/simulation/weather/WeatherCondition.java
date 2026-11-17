package simulation.weather;

/**
 * Enum class representing different weather conditions.
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public enum WeatherCondition {
    NORMAL("Normal"),
    FOG("Fog"),
    RAIN("Rain");

    private final String name;

    WeatherCondition(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
