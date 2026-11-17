/**
 * The `PollutantFilter` class allows filtering of pollutant data points based on their values
 * relative to thresholds defined by a `PollutantColour` object.  It provides the functionality
 * to show only high, mid, or low pollutant values, or to show all data points without filtering.
 * 
 * @author Karim He
 * @version 1.0
 */
public class PollutantFilter {
    private String filterType;

    /**
     * Constructor for the PollutantFilter class.
     * Initializes the filterType to "All" by default. This means no filtering
     * is applied initially.
     */
    public PollutantFilter() {
        this.filterType = "All"; // Default filter: Show all data points
    }

    /**
     * Sets the filter type to a specific value. This determines which data points
     * will be displayed based on their pollutant value relative to thresholds
     * defined by the PollutantColour object.
     *
     * @param filterType The type of filter to apply.  Valid values are "High", "Mid", "Low", or "All".
     *                   Any other value is treated as "All".
     */
    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    /**
     * Determines whether a given data point should be drawn based on the filter type
     * and the pollutant value.  It compares the pollutant value to the high, mid, and low
     * thresholds associated with the given PollutantColour object.
     *
     * @param value           The pollutant value of the data point.
     * @param pollutantColour The PollutantColour object that provides the threshold values (lowValue, midValue)
     *                        for different pollutant levels.
     * @return true if the data point should be drawn (visible) based on the current filter;
     *         false otherwise.
     */
    public boolean shouldDrawDataPoint(double value, PollutantColour pollutantColour) {
        switch (filterType) {
            case "High":
                // Show only data points with values greater than the mid value.
                return value > pollutantColour.getMidValue();
            case "Mid":
                // Show only data points with values between the low value (exclusive) and the mid value (inclusive).
                return value > pollutantColour.getLowValue() && value <= pollutantColour.getMidValue();
            case "Low":
                // Show only data points with values less than or equal to the low value.
                return value <= pollutantColour.getLowValue();
            default: // "All"
                // Show all data points regardless of their values.
                return true;
        }
    }

    /**
     * Gets the current filter type.
     *
     * @return The current filter type (e.g., "High", "Mid", "Low", "All").
     */
    public String getFilterType() {
        return filterType;
    }
}