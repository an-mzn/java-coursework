package simulation.entities.plants;

import simulation.field.Field;
import simulation.common.Location;
import simulation.entities.EntityFactory;
import simulation.entities.Entity;

import java.awt.Color;
import java.util.List;

/**
 * Common elements of all Entities.
 *
 * @author David J. Barnes
 * @author Michael Kölling
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public abstract class Plant extends Entity {

    protected Plant(Location location, int age, int maxAge, Color color) {
        super(location, age, maxAge, color);
    }

    /**
     * Surrounds the plant with a random number of new plants if space is available.
     *
     * @param nextFieldState Next field state to place plants in.
     * @param distance       Maximum distance to spawn new plants in.
     * @param maxSprouts     Maximum number of plants to randomly place.
     * @param entityFactory  A function/method creating a new plant at a given location.
     */
    protected void sprout(Field nextFieldState, int distance, int maxSprouts, EntityFactory<Plant> entityFactory) {
        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation(), distance).toList();
        for (int i = 0; i < Math.max(0, Math.min(freeLocations.size(), maxSprouts)); i++) {
            // Get one of the fre locations
            Location loc = freeLocations.get(i);

            // Place new plant at location
            Plant child = entityFactory.create(loc);
            nextFieldState.placeEntity(child, loc);
        }
    }
}
