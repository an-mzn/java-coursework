/**
 * Represents a person in the game. People have a name, a description, and a
 * single line of dialogue.
 */
public class Person {
    private String name;          // The person's name
    private String description;   // A description of the person
    private String dialogue;   // A single line of dialogue spoken by the person


    /**
     * Creates a new Person object.
     * @param name The person's name.
     * @param description A description of the person.
     * @param dialogue The dialogue spoken by the person.
     */
    public Person(String name, String description, String dialogue) {
        this.name = name;
        this.description = description;
        this.dialogue = dialogue;
    }


    /**
     * Returns the person's name.
     * @return The person's name.
     */
    public String getName() {
        return name;
    }


    /**
     * Returns a description of the person.
     * @return The person's description.
     */
    public String getDescription() {
        return description;
    }


    /**
     * Returns the person's dialogue.
     * @return The person's dialogue.
     */
    public String getDialogue() {
        return dialogue;
    }
}