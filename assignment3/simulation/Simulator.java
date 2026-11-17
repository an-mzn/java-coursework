package simulation;

import simulation.entities.*;
import simulation.entities.animals.*;
import simulation.entities.plants.*;
import simulation.field.*;
import simulation.weather.*;
import simulation.gui.SimulatorView;

/**
 * A more advanced predator-prey simulator, based on a rectangular field containing
 * a variety of animals.
 *
 * @author David J. Barnes
 * @author Michael Kölling
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 7.2
 */
public class Simulator {
    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_DEPTH = 80;

    /**
     * Keeps track of simulation steps
     */
    private final Clock clock = new Clock();

    /**
     * Keeps track of the weather cycle (depends on clock)
     */
    private final WeatherManager weatherManager = new WeatherManager(
            clock, // Inject clock dependency
            WeatherCycle.builder()
                    .add(WeatherCondition.RAIN, 6, 12)
                    .add(WeatherCondition.NORMAL, 12, 24)
                    .add(WeatherCondition.FOG, 6, 12)
                    .add(WeatherCondition.NORMAL, 20, 24)
                    .build()
    );

    /**
     * Population spawner used when populating the field with entities.
     */
    private static final Spawner populationSpawner = new Spawner()
            // Flora
            .register(0.05, Grass::random)
            .register(0.05, Shrub::random)
            // Fauna
            .register(0.08, Goat::random)
            .register(0.08, Pika::random)
            // Predators
            .register(0.03, Leopard::random)
            .register(0.02, Wolf::random);

    /**
     * Spawner to be used for each cycle. Particularly plants (represents seeding)
     */
    private static final Spawner perStepSpawner = new Spawner()
            .register(0.001, Grass::random)
            .register(0.001, Shrub::random);

    /**
     * The current state of the field.
     */
    private Field field;
    /**
     * Used for calculating field statistics.
     */
    private final FieldStats stats = new FieldStats();
    /**
     * A graphical view of the simulation.
     */
    private final SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        field = new Field(depth, width);
        view = new SimulatorView(depth, width, stats);
        reset();
    }

    /**
     * Run the simulation for the given number of steps.
     *
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        reportStats();
        for (int n = 1; n <= numSteps; n++) {
            simulateOneStep();
            delay(50);         // adjust this to change execution speed
        }
    }

    /**
     * Run the simulation from its current stage for a single step, update its clock and weather conditions,
     * and iterate over the whole field updating the state of entity.
     */
    public void simulateOneStep() {
        // Update time and weather
        clock.advanceTime();
        weatherManager.updateWeather();

        // Use a separate Field to store the starting state of the next step.
        final Field nextFieldState = new Field(field.getDepth(), field.getWidth());
        final float currentHour = clock.getHour();  // precompute to improve performance in loop
        final WeatherCondition currentWeather = weatherManager.getCurrentWeather();  // same here

        // Iterate over the field
        for (Entity entity : field.getEntities()) {
            entity.act(field, nextFieldState, currentHour, currentWeather);
        }

        // Do random flora seeding
        perStepSpawner.spawn(nextFieldState);

        // Reset/recalculate field stats
        stats.reset();

        // Replace the old state with the new one.
        field = nextFieldState;

        // Output stats and update GUI
        reportStats();
        view.showStatus(clock, weatherManager.getCurrentWeather(), field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        clock.reset();
        weatherManager.reset();
        populate();
        view.showStatus(clock, weatherManager.getCurrentWeather(), field);
    }

    /**
     * Randomly populate the current field using the {@link Simulator#populationSpawner}.
     */
    private void populate() {
        field.clear();
        populationSpawner.spawn(field);
    }

    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats() {
        System.out.printf("Step: %05d\t\tTime: %s\t\t%s%n",
                clock.getStep(), clock.getTimeString(), stats.getPopulationDetails(field));
    }

    /**
     * Pause for a given time.
     *
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
