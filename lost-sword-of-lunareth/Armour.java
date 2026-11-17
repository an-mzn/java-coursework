/**
 * Represents a piece of armour in the game.  Armour increases the player's health.
 */
public class Armour extends Item {
    private int healthIncrease; // Represents the amount of health the armour increases

    /**
     * Constructor for the Armour class.
     * @param name The name of the armour.
     * @param description A description of the armour.
     * @param value The value (price or worth) of the armour.
     * @param quantity The number of armour pieces available.
     * @param healthIncrease The amount of health this armour increases.
     * @param weight The weight of the armour.
     */
    public Armour(String name, String description, int value, int quantity, int healthIncrease, double weight) {
        super(name, description, value, quantity, weight); // Call the Item class constructor
        this.healthIncrease = healthIncrease;
    }

    /**
     * Returns the amount of health this armour increases.
     * @return The health increase value.
     */
    public int getHealthIncrease() {
        return healthIncrease;
    }

    /**
     * Overrides the toString method to provide a detailed description of the armour.
     * @return A string representation of the armour, including its name, description, value, and health increase.
     */
    @Override
    public String toString() {
        return getName() + " - " + getDescription() + " (Value: " + getValue() + ", Health Increase: " + healthIncrease + ")";
    }
}