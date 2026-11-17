package simulation;

/**
 * Helps with calculations involving time and provides a way for entities to
 * find the time in 24-hour format.
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public class Clock {
    /**
     * Number of steps that correspond to one hour.
     */
    private static final int STEPS_PER_HOUR = 4;  // i.e. 1 step = 15 minutes
    /**
     * Number of steps that correspond to 1 day (24 h)
     */
    private static final int STEPS_PER_DAY = 24 * STEPS_PER_HOUR;

    /**
     * Number of steps passed in the simulation
     */
    private int stepCounter;

    public Clock() {
        stepCounter = 0;
    }

    /**
     * Advance the time by one step.
     */
    public void advanceTime() {
        stepCounter++;
    }

    /**
     * Accessor for the current hour in 24-hour format
     *
     * @return 24-hour in decimal format (e.g. 0.5 = 30 mins, so 07:30 is 7.5)
     */
    public float getHour() { // 24H
        // Number of steps passed during the current day
        long currentDaySteps = stepCounter % STEPS_PER_DAY;
        // Return the current hour as a decimal (.5 = 30 mins, so 07:30 -> 7.5)
        return (float) currentDaySteps / (float) STEPS_PER_HOUR;
    }

    /**
     * Calculates the number of in-simulation hours that have passed since the simulation started.
     *
     * @return number of hours since start.
     */
    public int getTotalHoursPassed() {
        return stepCounter / STEPS_PER_HOUR;
    }

    /**
     * Gets the current step of the simulation. This starts at 0 and increases by 1 each step.
     *
     * @return The current step count.
     */
    public int getStep() {
        return stepCounter;
    }

    /**
     * Resets the clock to the zero. Should be called when the simulation is reset.
     */
    public void reset() {
        stepCounter = 0;
    }

    /**
     * Gets the formatted time for the current simulated day.
     *
     * @return A 24-hour formatted string.
     */
    public String getTimeString() {
        int currentDaySteps = stepCounter % STEPS_PER_DAY;
        int hour = currentDaySteps / STEPS_PER_HOUR;
        int minute = (int) (((float) (currentDaySteps % STEPS_PER_HOUR) / (float) STEPS_PER_HOUR) * 60);
        return String.format("%02d:%02d", hour, minute);
    }

    /**
     * Calculates the number of simulation steps given a desired number of in-simulation hours.
     *
     * @param hours Number of hours.
     * @return Number of simulation steps.
     */
    public static int hoursToSteps(int hours) {
        return hours * STEPS_PER_HOUR;
    }

    /**
     * Calculates the number of simulation steps given a desired number of in-simulation days.
     *
     * @param days Number of days.
     * @return Number of simulation steps.
     */
    public static int daysToSteps(int days) {
        return days * STEPS_PER_DAY;
    }
}
