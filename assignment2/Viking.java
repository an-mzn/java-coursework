/**
 * Represents a Viking character in the game.  This class inherits from the
 * Person class.
 */
public class Viking extends Person {
    /**
     * Constructor for creating a new Viking character.
     * @param name The name of the Viking.
     * @param description A description of the Viking.
     * @param dialogue The dialogue spoken by the Viking.
     */
    public Viking(String name, String description, String dialogue) {
        super(name, description, dialogue); // Calls the constructor of the Person superclass
    }
}