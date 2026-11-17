/**
 * Represents an Elf character in the game.  Inherits from the Person class.
 */
public class Elf extends Person {
    /**
     * Constructor for creating a new Elf object.
     * @param name The elf's name.
     * @param description A description of the elf.
     * @param dialogue The elf's dialogue.
     */
    public Elf(String name, String description, String dialogue) {
        super(name, description, dialogue); // Calls the Person class constructor
    }
}