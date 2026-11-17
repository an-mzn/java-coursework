package simulation.entities.animals;

import simulation.entities.Entity;
import simulation.entities.EntityFactory;
import simulation.field.Field;
import simulation.common.Location;

import java.awt.Color;
import java.util.*;
import java.util.stream.Stream;

/**
 * Common elements of all Animals.
 *
 * @author David J. Barnes
 * @author Michael Kölling
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 7.1
 */
public abstract class Animal extends Entity {

    private int foodLevel;

    private boolean isPregnant;
    private int timePregnant;
    private int litterSize;

    private final Gender gender;
    private final int gestationPeriod;
    private final int sexualMaturityAge;

    private final Set<Disease> diseases;


    protected Animal(Location location, int age, int maxAge, int gestationPeriod, int sexualMaturityAge, Gender gender, Color color) {
        super(location, age, maxAge, color);
        this.gender = gender;
        this.sexualMaturityAge = sexualMaturityAge;
        this.gestationPeriod = gestationPeriod;
        this.isPregnant = false;
        this.diseases = new HashSet<>();
    }

    /**
     * Increase hunger.
     */
    protected void decrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Increase hunger.
     *
     * @param amount number of food points.
     */
    protected void increaseFoodLevel(int amount) {
        foodLevel += amount;
    }

    /**
     * @return Gender of the animal.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @return True when female.
     */
    private boolean isFemale() {
        return getGender().equals(Gender.FEMALE);
    }

    /**
     * Predicate for checking if an animal is able to breed/become impregnated.
     *
     * @return True if instance can breed.
     */
    private boolean canBreed() {
        return isAlive() && isFemale() && !isPregnant && getAge() >= sexualMaturityAge;
    }

    private <T extends Animal> Stream<T> findMates(Field currentField, int distance, Class<T> type) {
        return currentField.getAdjacentOfType(distance, getLocation(), type)
                .filter(Animal::canBreed);
    }

    protected <T extends Animal> Optional<T> meetMate(Field currentField, int distance, Class<T> type) {
        return findMates(currentField, distance, type)
                .findFirst();
    }

    /**
     * Impregnate a female instance with a particular number of fetuses. This method will throw a
     * runtime exception if called on a male animal.
     *
     * @param litterSize number of children the female will be pregnant with.
     */
    public void impregnate(int litterSize) {
        // intended to be called from male animal
        if (canBreed()) {
            isPregnant = true;
            timePregnant = 0;
            this.litterSize = litterSize;

        } else {
            throw new RuntimeException("Cannot call impregnate method!");
        }
    }

    /**
     * Updates pregnancy for female instances. Should be called in {@link Animal#act}.
     *
     * @param nextFieldState Next field used for birthing children into.
     * @param entityFactory  Method/reference or function that creates an animal, taking the location as a parameter.
     */
    protected void updatePregnancy(Field nextFieldState, EntityFactory<Animal> entityFactory) {
        if (isFemale()) {
            if (isPregnant) {
                timePregnant++;
            }
            if (timePregnant >= gestationPeriod) {
                birthChildren(nextFieldState, 1, entityFactory);
            }
        }
    }

    /**
     * Births children currently being carried by the female instance. Called from {@link Animal#birthChildren}
     *
     * @param nextFieldState Next field to put children into.
     * @param distance       Maximum distance to put children.
     * @param entityFactory  Child creation method/function.
     */
    private void birthChildren(Field nextFieldState, int distance, EntityFactory<Animal> entityFactory) {
        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation(), distance).toList();
        for (int i = 0; i < Math.max(0, Math.min(litterSize, freeLocations.size())); i++) {
            Location loc = freeLocations.get(i);
            Animal child = entityFactory.create(loc);
            nextFieldState.placeEntity(child, loc);
        }
        isPregnant = false;
    }

    private <T extends Entity> Stream<T> findFood(Field currentField, int distance, Class<T> type) {
        return currentField.getAdjacentOfType(distance, getLocation(), type)
                .filter(T::isAlive);
    }

    protected <T extends Entity> Optional<T> findSingleFood(Field currentLocation, int distance, Class<T> entityClass) {
        return findFood(currentLocation, distance, entityClass).findFirst();
    }

    protected Optional<Entity> findFoodOfTypes(Field currentField, int distance, Class<? extends Entity>... types) {
        return currentField.getAdjacentOfTypes(distance, getLocation(), types)
                .filter(Entity::isAlive)
                .findFirst();
    }

    /**
     * @return The current food level of the animal.
     */
    public int getFoodLevel() {
        return foodLevel;
    }

    public void infect(Disease disease) {
        diseases.add(disease);
        setColor(Color.red);
    }

    /**
     * @return Set of diseases this instance has. (Note this set is unmodifiable)
     */
    public Set<Disease> getDiseases() {
        return Collections.unmodifiableSet(diseases);  // Unmodifiable since this is supposed to be an accessor
    }

    /**
     * Cures the disease from the animal instance.
     *
     * @param disease Disease to remove from this animal.
     */
    protected void cureDisease(Disease disease) {
        diseases.remove(disease);
    }
}
