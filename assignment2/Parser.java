import java.util.Scanner;

/**
 * This parser reads user input and attempts to interpret it as a game command.
 * It reads a line from the console and parses it into a Command object.  The
 * parser handles commands with up to three words and is case-insensitive.
 *
 * @author Michael Kölling, David J. Barnes, and Ayan Mamun
 * @version 2024.11.28
 */
public class Parser {
    private CommandWords commands; // Holds all valid command words


    /**
     * Creates a new Parser object.  Initializes the CommandWords object.
     */
    public Parser() {
        commands = new CommandWords();
    }


    /**
     * Obtains the next command from the user.
     * @param reader The Scanner object to read from (typically System.in).
     * @return A Command object representing the parsed command.
     */
    public Command getCommand(Scanner reader) {
        String inputLine = reader.nextLine();
        String commandWord = null;
        String secondWord = null;
        String thirdWord = null;

        if (inputLine != null && !inputLine.trim().isEmpty()) {
            String[] words = inputLine.trim().toLowerCase().split("\\s+", 3); // Split into at most 3 words (case-insensitive)
            if (words.length > 0) {
                commandWord = words[0];
                if (words.length > 1) {
                    secondWord = words[1];
                    if (words.length > 2) {
                        thirdWord = words[2];
                    }
                }
            }
        }

        return new Command(commands.isCommand(commandWord) ? commandWord : null, secondWord, thirdWord);
    }


    /**
     * Prints a list of valid command words to the console.
     */
    public void showCommands() {
        commands.showAll();
    }
}