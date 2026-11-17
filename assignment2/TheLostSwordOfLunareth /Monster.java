/**
 * Represents a monster in the game. Monsters have a name, health points,
 * attack strength, and an image representation (for text-based display).
 */
public class Monster {
    private String name;    // The monster's name
    private int health;     // The monster's health points
    private int attack;    // The monster's attack strength
    private String image;   // A text-based image of the monster


    /**
     * Creates a new Monster object.
     * @param name The monster's name.
     * @param health The monster's initial health points.
     * @param attack The monster's attack strength.
     * @param image A text-based image representing the monster.
     */
    public Monster(String name, int health, int attack, String image) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.image = image;
    }


    /**
     * Returns the monster's name.
     * @return The monster's name.
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the monster's image (text-based representation).
     * @return The monster's image.
     */
    public String getImage() {
        return image;
    }


    /**
     * Returns the monster's current health points.
     * @return The monster's health.
     */
    public int getHealth() {
        return health;
    }


    /**
     * Sets the monster's health points.
     * @param health The new health points for the monster.
     */
    public void setHealth(int health) {
        this.health = health;
    }


    /**
     * Returns the monster's attack strength.
     * @return The monster's attack strength.
     */
    public int getAttack() {
        return attack;
    }


    /**
     * Checks if the monster is still alive (health > 0).
     * @return True if the monster is alive, false otherwise.
     */
    public boolean isAlive() {
        return health > 0;
    }
}