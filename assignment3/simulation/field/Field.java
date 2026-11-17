package simulation.field;

import simulation.common.Location;
import simulation.common.Randomizer;
import simulation.entities.Entity;
import simulation.entities.plants.Plant;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal/object.
 *
 * @author David J. Barnes
 * @author Michael Kölling
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 7.1
 */
public class Field {
    private static final Random rand = Randomizer.getRandom();

    // The dimensions of the field.
    private final int depth, width;
    // Animals mapped by location.
    private final Map<Location, Entity> field = new HashMap<>();
    // The entities.
    private final List<Entity> entities = new ArrayList<>();

    /**
     * Represent a field of the given dimensions.
     *
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Predicate for checking if the location is free on the field.
     * Locations are considered free when there is no entity, it's dead,
     * or it's a plant (plants can be walked through/over).
     *
     * @param location Location to check.
     * @return True if location is free.
     */
    public boolean isFreeLocation(Location location) {
        Entity entity = field.get(location);
        return entity == null || entity.isDead() || entity instanceof Plant;
    }

    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will
     * be lost.
     *
     * @param anEntity The animal to be placed.
     * @param location Where to place the animal.
     */
    public void placeEntity(Entity anEntity, Location location) {
        Objects.requireNonNull(location);
        Object other = field.get(location);
        if (other != null) {
            entities.remove(other);
        }
        if (location.col() < 0 || width <= location.col() || location.row() < 0 || depth <= location.row()) {
            throw new RuntimeException("Invalid location!");
        }
        field.put(location, anEntity);
        entities.add(anEntity);
    }

    /**
     * Return the animal at the given location, if any.
     *
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Entity getEntityAt(Location location) {
        return field.get(location);
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     *
     * @param location The location from which to generate adjacencies.
     * @param distance Distance in cells from the {@code location}
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location, int distance) {
        List<Location> locations = new ArrayList<>();
        if (location != null) {
            int row = location.row();
            int col = location.col();
            for (int roffset = -distance; roffset <= distance; roffset++) {
                int nextRow = row + roffset;
                if (nextRow >= 0 && nextRow < depth) {
                    for (int coffset = -distance; coffset <= distance; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }

        return locations;
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     *
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public Stream<Location> getFreeAdjacentLocations(Location location, int distance) {
        return getAdjacentLocations(location, distance).stream()
                .filter(this::isFreeLocation);
    }

    /**
     * Gets a (random) free location if one is present.
     *
     * @param location Get locations adjacent to this.
     * @return An option wrapped instance of a {@link Location} instance.
     */
    public Optional<Location> getFreeAdjacentLocation(Location location, int distance) {
        return getFreeAdjacentLocations(location, distance).findFirst();
    }

    /**
     * Finds entities of a specific type surrounding the current cell.
     *
     * @param distance Distance or range in number of cells.
     * @param type
     * @param <T> A subtype of Entity.
     * @return Stream of instances of type T
     */
    public <T extends Entity> Stream<T> getAdjacentOfType(int distance, Location location, Class<T> type) {
        return getAdjacentLocations(location, distance).stream()
                .map(field::get)
                .filter(type::isInstance)
                .map(type::cast);
    }

    /**
     * Finds entities of specific types surrounding the current cell.
     *
     * @param distance Distance or range in number of cells.
     * @param location Location of to search from.
     * @param types    List of types to return instances of (must extend {@link Entity})
     * @return A stream of instances of {@link Entity} that match the criteria.
     */
    public Stream<Entity> getAdjacentOfTypes(int distance, Location location, Class<? extends Entity>... types) {
        return getAdjacentLocations(location, distance).stream()
                .map(field::get)
                .filter(Predicate.not(Objects::isNull))
                .filter(entity -> {
                    Class<? extends Entity> entityClass = entity.getClass();
                    for (Class<? extends Entity> type : types) {
                        if (entityClass.equals(type)) { return true; }
                    }
                    return false;
                });
    }

    /**
     * Empty the field.
     */
    public void clear() {
        entities.clear();
        field.clear();
    }

    /**
     * Get the shuffled list of animals.
     */
    public List<Entity> getEntities() {
        // Use shuffling to make entites act in random order.
        Collections.shuffle(entities, Randomizer.getRandom());
        return entities;
    }

    /**
     * Return the depth of the field.
     *
     * @return The depth of the field.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Return the width of the field.
     *
     * @return The width of the field.
     */
    public int getWidth() {
        return width;
    }
}
