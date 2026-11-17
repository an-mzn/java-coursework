/**
 * Represents a Villager character in the game.  Inherits from the Person class.
 */
public class Villager extends Person {
    /**
     * Constructor for creating a new Villager object.
     * @param name The villager's name.
     * @param description A description of the villager.
     * @param dialogue The villager's dialogue.
     */
    public Villager(String name, String description, String dialogue) {
        super(name, description, dialogue); // Calls the Person class constructor
    }
}