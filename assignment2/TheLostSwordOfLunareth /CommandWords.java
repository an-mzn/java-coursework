/**
 * This class defines an enumeration of all valid command words for the game.
 * It is used to recognize commands as they are typed in.  The commands are
 * case-insensitive.
 *
 * @author Michael Kölling, David J. Barnes, and Ayan Mamun
 * @version 2024.11.28
 */
public class CommandWords {
    // A constant array holding all valid command words.
    private static final String[] validCommands = {
        "go", "quit", "help", "map", "inventory", "buy", "marketplace", "fight", "explore", "stats",
        "exits", "back", "drop", "sell"
    };


    /**
     * Constructor - initializes the command words.  No initialization is needed here.
     */
    public CommandWords() {
        // Nothing to do at the moment...
    }


    /**
     * Check whether a given String is a valid command word.
     * @param aWord The word to check.
     * @return true if the word is a valid command, false otherwise.
     */
    public boolean isCommand(String aWord) {
        for (int i = 0; i < validCommands.length; i++) {
            if (validCommands[i].equalsIgnoreCase(aWord)) { // Case-insensitive check
                return true;
            }
        }
        return false; // Word not found
    }


    /**
     * Print all valid commands to the console.
     */
    public void showAll() {
        System.out.print("Valid commands are: ");
        for (String command : validCommands) {
            System.out.print(command + ", ");
        }
        System.out.println();
    }
}