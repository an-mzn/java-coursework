package simulation.entities;

import simulation.field.Field;
import simulation.common.Location;
import simulation.weather.WeatherCondition;

import java.awt.Color;

/**
 * Common elements of all Entities.
 *
 * @author David J. Barnes
 * @author Michael Kölling
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 1.0.0
 */
public abstract class Entity {
    /**
     * Whether the entity is dead/alive
     */
    private boolean isAlive;
    /**
     * Location of the entity
     */
    private Location location;
    /**
     * Age of the entity (in simulation steps)
     */
    private int age;
    /**
     * Maximum age of the entity/lifespan (in simulation steps)
     */
    private final int maxAge;

    /**
     * Color of the entity
     */
    private Color color;

    protected Entity(Location location, int age, int maxAge, Color color) {
        this.isAlive = true;
        this.location = location;
        this.age = age;
        this.maxAge = maxAge;
        this.color = color;
    }

    /**
     * Act.
     *
     * @param currentField     The current state of the field.
     * @param nextFieldState   The new state being built.
     * @param weatherCondition
     */
    public abstract void act(Field currentField, Field nextFieldState, float hour, WeatherCondition weatherCondition);

    /**
     * @return True when the animal is alive
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * @return True when the animal is dead.
     */
    public boolean isDead() {
        return !isAlive;
    }

    /**
     * Makes the animal dead/kills it.
     */
    public void setDead() {
        isAlive = false;
    }

    /**
     * @return Animal's age in number simulation steps.
     */
    public int getAge() {
        return age;
    }

    /**
     * Updates the age of the entity. Should be called in act.
     */
    protected void incrementAge() {
        age++;
        if (age > maxAge) {
            setDead();
        }
    }

    /**
     * @return Color of the entity.
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color New color.
     */
    protected void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return Current location of the entity.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Moves the entity into a new location.
     *
     * @param nextFieldState State of the next field.
     * @param nextLocation   Location to set the animal to go to.
     * @see Entity#act(Field, Field, float, WeatherCondition)
     */
    protected void moveTo(Field nextFieldState, Location nextLocation) {
        this.location = nextLocation;
        nextFieldState.placeEntity(this, nextLocation);
    }

    /**
     * Makes the animal stay in the same location.
     *
     * @param nextFieldState The next field state to update.
     */
    protected void stay(Field nextFieldState) {
        nextFieldState.placeEntity(this, location);
    }
}
