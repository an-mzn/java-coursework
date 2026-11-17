/**
 * Tracks the player's health.
 */
public class Playerstats {
    private int health; // The player's current health


    /**
     * Creates a new PlayerStats object with a specified starting health.
     * @param startingHealth The player's initial health.
     */
    public Playerstats(int startingHealth) {
        this.health = startingHealth;
    }


    /**
     * Returns the player's current health.
     * @return The player's health.
     */
    public int getHealth() {
        return health;
    }


    /**
     * Sets the player's health to a new value.
     * @param health The new health value.
     */
    public void setHealth(int health) {
        this.health = health;
    }


    /**
     * Increases the player's health by a specified amount.
     * @param amount The amount to increase health by.
     */
    public void increaseHealth(int amount) {
        health += amount;
    }


    /**
     * Decreases the player's health by a specified amount.  Health cannot
     * drop below 0.
     * @param amount The amount to decrease health by.
     */
    public void decreaseHealth(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }


    /**
     * Returns a string representation of the player's health.
     * @return A string showing the player's health.
     */
    @Override
    public String toString() {
        return "Health: " + health;
    }
}