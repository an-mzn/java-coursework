package simulation.field;

import simulation.common.Location;
import simulation.common.Randomizer;
import simulation.entities.EntityFactory;
import simulation.entities.Entity;

import java.util.*;

/**
 * Used for defining a spawning procedure with different {@link Entity} types each having their
 * own stage with its own probability.
 * <p>
 * First use {@link Spawner#register(double, EntityFactory)} to add stages to the spawn procedure,
 * then use the {@link Spawner#spawn(Field)} method to populate a given field refernce according to the
 * spawn procedure.
 *
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public class Spawner {
    private static final Random rand = Randomizer.getRandom();

    /**
     * Record for storing information about a spawn stage.
     *
     * @param spawner     spawner function for entity at a given location.
     * @param probability probability of that stage spawning an animal.
     */
    private record SpawnStage(EntityFactory<Entity> spawner, double probability) {
    }

    /**
     * List starting with first spawn stages.
     */
    private final List<SpawnStage> spawnStages = new ArrayList<>();

    /**
     * Register a spawn stage with the spawner.
     *
     * @param spawnProbability probability of the entity spawning at that stage.
     * @param spawner          A function/method reference used to create the entity.
     * @return The spawner reference (enables method chaining)
     */
    public Spawner register(double spawnProbability, EntityFactory<Entity> spawner) {
        spawnStages.add(new SpawnStage(spawner, spawnProbability));
        return this;
    }

    /**
     * Populates a field according to the defined spawn stages of the Spawner.
     * @param field Field to populate.
     */
    public void spawn(Field field) {
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                for (SpawnStage spawnStage : spawnStages) {
                    if (rand.nextDouble() <= spawnStage.probability) {
                        Location loc = new Location(row, col);
                        Entity entity = spawnStage.spawner.create(loc);
                        field.placeEntity(entity, loc);
                        break;
                    }
                }
            }
        }
    }
}