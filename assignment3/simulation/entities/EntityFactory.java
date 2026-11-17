package simulation.entities;

import simulation.common.Location;

/**
 * Functional interface for creating an entity at a specific location.
 *
 * @param <T> Specific entity type
 */
@FunctionalInterface
public interface EntityFactory<T extends Entity> {
    T create(Location location);
}
