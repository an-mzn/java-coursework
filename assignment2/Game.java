import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**
 *  This class is the main class of "The Lost Sword of Lunareth" application. 
 *  "The Lost Sword of Lunareth" is a simple, text based adventure game.  Users 
 *  can traverse around a continent.The player
 * explores a world, battles monsters, collects items, and ultimately strives to achieve a
 * specific goal (e.g., defeat a powerful enemy, find a lost artifact).
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  Main class for the "Lost Sword of Lunareth" text adventure game.
 *  Initializes the game world, handles player input, and manages game logic.
 * 
 * @author  Michael Kölling, David J. Barnes and Ayan Mamun
 * @version 2024.11.28
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Inventory inventory;
    private Scanner scanner;
    private String playerName;
    private Dragon dragon;
    private String dragonName;
    private Playerstats playerStats;
    private Stack<Room> roomHistory = new Stack<>();
    private List<Room> rooms;

    private Room caelthorne;
    private Room aeloria;
    private Room ignirath;
    private Room eryndale;
    private Room eldralis;
    private Room noriven;
    private Room lunareth;

    /**
     * Create the game. Initializes game components.
     */
    public Game()
    {
        rooms = new ArrayList<>(); // Initialize the list in the constructor
        createRooms();
        parser = new Parser();
        inventory = new Inventory();     
        scanner = new Scanner(System.in); 
        playerStats = new Playerstats(100);
    }

    /**
    * Method to initialise all aspects of the game's rooms.
    */
    private void createRooms() {
        createBasicRooms();
        setRoomExits();
        addShopItems();
        addMonsters();
        addPeopleToRooms();
        currentRoom = caelthorne; // Start game in Caelthorne

    }

    /**
     * Method to add people as parts of those rooms,
     */
    private void addPeopleToRooms() {
        // Add people to rooms using subclasses
        Villager villager1 = new Villager("Elara", "a friendly villager.", "Hello there, traveller! I hear Lunareth is on the furthest part of this continent");
        caelthorne.addPerson(villager1);

        Elf elf1 = new Elf("Lindir", "a wise elven senior.", "Greetings, friend!  You should look in the marketplace - I hear they have just what you need.");
        aeloria.addPerson(elf1);

        Viking viking1 = new Viking("Bjorn", "a strong Viking warrior.", "We're alike...Fighting beasts and seeking glory! I'll take any weapons you have to sell me?");
        noriven.addPerson(viking1);
    }
    
    /**
     * Creates the game's rooms and adds them to the `rooms` list. text
     * blocks for multi-line room descriptions.
     */
    private void createBasicRooms() {
        caelthorne = new Room("""
                              the village of Caelthorne.
                              \tThere isn't much to do here but the villagers are nice 
                              \tand there's a marketplace.
                              """, rooms);
        aeloria = new Room("""
                           the town of Aeloria.
                           \tIts Elven heritage is much of the reason it is considered
                           \ta good place to buy spells and potions!
                           """, rooms);
        ignirath = new Room("""
                            the midst of a dragon realm, Ignirath.
                            \tWhat was once filled with dragons is now overcome with
                            \tits apparent extinction. 
                            """, rooms);
        eryndale = new Room("""
                            the Woods of Eryndale.
                            \tCareful, this place is rumoured to be home to beasts...
                            """, rooms);
        eldralis = new Room("""
                            the new city of Eldralis.
                            \tThese elves have gotten together and recently 
                            \tdeveloped a fascinating magic portal.
                            """, rooms);
        noriven = new Room("""
                           the old town of Noriven.
                           \tHome to giant vikings and their brethren.
                           """, rooms);
        lunareth = new Room("""
                            the rumoured mountains of Lunareth.
                            \tBeware, peril awaits those who purely seek glory 
                            \tand triumph to those with nobility.
                            """, rooms);
                    rooms.addAll(List.of(caelthorne, aeloria, ignirath, eryndale, eldralis, noriven, lunareth));
    }
    
    /**
     * Creates exits to those rooms
     */
    private void setRoomExits() {
        caelthorne.setExit("south", ignirath);
        caelthorne.setExit("west", aeloria);
        caelthorne.setExit("northeast", noriven);
        caelthorne.setExit("southeast", eryndale);
    
        aeloria.setExit("east", caelthorne);
        aeloria.setExit("south", eldralis);
        aeloria.setExit("southeast", ignirath);
    
        ignirath.setExit("north", caelthorne);
        ignirath.setExit("southwest", eldralis);
        ignirath.setExit("southeast", eryndale);
        ignirath.setExit("northwest", aeloria);
    
        eryndale.setExit("westsouthwest", eldralis);
        eryndale.setExit("northeast", lunareth);
        eryndale.setExit("northwest", ignirath);
        eryndale.setExit("north", noriven);
    
        noriven.setExit("south", eryndale);
        noriven.setExit("southeast", lunareth);
        noriven.setExit("southwest", caelthorne);
    
        dragon = new Dragon("Ignis", "A magnificent, friendly dragon.");
        ignirath.setDragon(dragon);
    }
    
    /**
     * Puts items in the shop available for the player in certain rooms
     */
    private void addShopItems() {
        caelthorne.addToShop(new Item("rustyAxe", "A dull but heavy axe.", 50, 3, 4));
        caelthorne.addToShop(new Item("healthPotion", "Restores health.", 45, 5, 1));
        aeloria.addToShop(new Item("beastEnhancingSpell", "A spell to enhance your beast.", 200, 1, 0));
        aeloria.addToShop(new Item("attackingSpell", "A spell to mash up monsters (400 damage)", 100, 4, 1));
        noriven.addToShop(new Armour("leatherArmour", "Not the best but good", 200, 1, 275, 0));
        noriven.addToShop(new Armour("ironArmour", "A true warrior's attire", 300, 1, 400, 0));
        noriven.addToShop(new Item("ironSword", "The real deal", 300, 1, 2));
    }
    
    /**
     * Creates monsters in the rooms
     */
    private void addMonsters() {
        Monster vouivre = new Monster("Vouivre", 1000, 40, "          _/          ,          .                                          \n" + //
                "       , -' )         ( \\-------.,')            (\\_________________________  \n" + //
                "     , ,-/ |          /\\_) )     \\/            ,' _.----------------------,\\ \n" + //
                "   ,',  /, |         /      >--. ,)           / /\\\\                          \n" + //
                "  / ,  //|,'        /'     '\\--'\\\\)          /,'  \\\\     `         `   ,     \n" + //
                " / ,  // ||       ,'     (.--^( `')         //     \\\\                \\       \n" + //
                "( ,  //  ||,___,-'    (___\\\\  '^^^'        //       \\\\              ,        \n" + //
                " \\  //   ||--.__     (     \\`^--)  _____.-'/         \\\\   `                  \n" + //
                "  >'/    ||,        (       \\|_(\\-'      ,'           \\\\         \\,          \n" + //
                " /,'     ||          \\           \\      /              \\\\                    \n" + //
                "(/       ||           \\           )  ,'(     `     `    \\\\      ,            \n" + //
                " `       ||\\           \\      ) ,'  /_  )                \\\\    \\             \n" + //
                "         || `.          `.    ,'   /( `.\\  \\ , \\ \\,       \\\\   ,             \n" + //
                "   `     || (_`.          ` .'   .'  )  `)'            ,   \\\\                \n" + //
                "         ||  (_ `-v-------  ^--v' , )                      '\\\\,              \n" + //
                "         ||    (    , _,-  /  -./ )'                         `)              \n" + //
                "     `   '|     ),  ,'    '     )'                                           \n" + //
                "        ' ;    /  ,'          ,'                                             \n" + //
                "       /,'    /  /      '    /     , - --- .                                 \n" + //
                "       \\|    /  (          ,'   '           `.                               \n" + //
                "       ('  ,'    `.    \"  / ,'                \\                              \n" + //
                "         ,'        \\    ,/,'        '`)   (_   )                             \n" + //
                "        /           \\ , /'          ,      /  /                              \n" + //
                "       .             )  ,       ,         '  /                               \n" + //
                "                      )      ,              /                                \n" + //
                "       .            ' `|   ,'              /                                 \n" + //
                "                    '  |  /              ,'                                  \n" + //
                "        |\\             | <    ______,---'                                    \n" + //
                "        ` \\            ','   (                                               \n" + //
                "         \\ '          /(____ ,`-._,-.                                        \n" + //
                "          `.         /      `._, )---)                                       \n" + //
                "            `-------'\\         `/ \\                                          \n" + //
                "               )   )  \\          ` \\                                         \n" + //
                "              /  '(    `.         `                                          \n" + //
                "         ___,' _, /      `.         .                                        \n" + //
                "        ('.---/ \\(          .      '|                                        \n" + //
                "        /'    `|^'           .     ,                                         \n" + //
                "                             .     /                                         \n" + //
                "                                  '                                          \n" + //
                "  \t       \t            '    '                                           \n" + //
                "    \t\t          ,'  ,                                              \n" + //
                "                         (_ '                \n");
        lunareth.setMonster(vouivre);
    
        Monster leshen = new Monster("Leshen", 200, 20, "\n" + //
                "                                                             / /\n" + //
                "                                                          | | |  /\n" + //
                "                                                           \\|_|_/\n" + //
                "                                                         ,--/.__/--'\n" + //
                "                         _.-/   _   \\-._                    .'|\n" + //
                "                       .'::(_,-' `-._)::`.                  |:|\n" + //
                "                      (:::::::::::::::::::)                .':|\n" + //
                "                       \\_:::;;;::::;;;:::/    /            |::|\n" + //
                "               \\        ,---'..\\::/..`-.'    /             |::|\n" + //
                "                \\       \\_;:....|'...:_ )   /             .'=||\n" + //
                "                 \\.       )---. )_.--< (   /'             ||=||\n" + //
                "                  \\\\     //|:: /--\\:::\\\\\\ //             _||= |\n" + //
                "                   \\\\   ||::\\:|----|:/:||/--.______,--==' \\ - /\n" + //
                "            -._     \\`.  \\\\:|:|-- -|:\\:/-.,,\\\\  .----'//'_.`-'\n" + //
                "        \\.     `-.   \\ \\ _|:|:|-- -|::||::\\,,||-'////---' |/'\n" + //
                "         \\\\       `._)\\ / |\\/:|-/|--\\:/|. :\\_,'---'       /\n" + //
                "          \\\\_      /,,\\/:.'\\\\/-.'`-.-//  \\ |\n" + //
                "          /`\\-    //,,,' |-.\\-'\\--/|-/ ./| |             /\n" + //
                "           /||-   ||, /| |\\. |.-==-.| . /| |            | /\n" + //
                "   __  |    /||-  ||,,\\- | .  \\;;;;/ .  .':/         _,-=/-'\n" + //
                "  /  \\//    /||-  ' `,-|::\\ . \\,..,/   /: /         /.-'\n" + //
                "  ,--||      /||-/.-.'  \\  `._ `--' _.' .'|        //\n" + //
                "  .--||`.    /||//\\ '   |-'.___`___' _,'|//       /;\n" + //
                "    /\\| \\     ///\\ /     \\\\_`-.`--`-'_==|'       /;'\n" + //
                "   / |'\\ \\.   //\\ /       \\_\\__)\\`==-_'_|       / /\n" + //
                "    .'  \\.=`./|\\ /          \\`-- \\--._/_/------( /\n" + //
                "         \\.=| `_/|-          |\\`-| -/| `--------'\n" + //
                "          \\\\` ./|-/-         |\\`-| |-|     ________\n" + //
                "           `--\\ |=|'        _|\\`-| |-|----'.-._ ..\\`-.\n" + //
                "               -|-|-     .-':`-;-| |./.-.-( | ||..|=-\\\\\n" + //
                "               `'= /'   / ,--.:|-| ||_|_|_|_|-'__ .`-._)\n" + //
                "                /|-|- .' /\\ \\ \\|-` \\\\ ____,---'  `-. ..|\n" + //
                "                 /\\=\\/..'\\ \\_>-'`-\\ \\'              \\ .|\n" + //
                "                 `,-':/\\`.>' |\\/ \\/\\ \\              `- |\n" + //
                "                 //::/\\ \\/_/|-' \\/| \\ `.            / ||\n" + //
                "                / (:|\\ \\/) \\ \\|.'-'  `-\\\\          |;:|\\_\n" + //
                "               || |:`-/:.'-|-' \\|       \\\\          `;_\\-`-._\n" + //
                "               \\\\=/:_/::/\\| \\|          |\\\\            `-._ =`-._\n" + //
                "                \\_)' |:|                | //               `--.__`-.\n" + //
                "                     |:|                                         )\\|\n" + //
                "                     /;/                                         / (\\_\n" + //
                "                    / /                                         |\\\\;;_`-.\n" + //
                "                  _/ /                                          ' `---\\.-\\\n" + //
                "                 /::||\n" + //
                "                /:::/\n" + //
                "               //;;'\n" + //
                "              `-'\n");
        eryndale.setMonster(leshen);
    }



    
    /**
     *  Main play routine.
     */
    public void play() {
        Scanner scanner = new Scanner(System.in);
        playerName = getPlayerName(scanner);
        printWelcome();
        runGameLoop(scanner);
        scanner.close();
    }
    
    /** 
     * Prompt for username
     */
    private String getPlayerName(Scanner scanner) {
        System.out.print("Tell me your name witcher\n");
        scanner.nextLine(); //Consume the first (rejected) name
        System.out.println("That's boring, pick a better name");
        System.out.print("Try again: ");
        return scanner.nextLine();
    }


    /**
     * Print out the opening message for the player.
     * 
     * ASCII Art (2024) Text to ASCII Art.
     * Available at: https://www.asciiart.eu/text-to-ascii-art 
     * (Accessed: 27 November 2024).
     */
    private void printWelcome()
    {
        System.out.println("Greetings " + playerName + ", welcome to...");
        System.out.println(" _____ _            _              _       \n" + //
                        "|_   _| |__   ___  | |    ___  ___| |_     \n" + //
                        "  | | | '_ \\ / _ \\ | |   / _ \\/ __| __|    \n" + //
                        "  | | | | | |  __/ | |__| (_) \\__ \\ |_     \n" + //
                        " _|_| |_| |_|\\___| |_____\\___/|___/\\__|__  \n" + //
                        "/ ___|_      _____  _ __ __| |   ___  / _| \n" + //
                        "\\___ \\ \\ /\\ / / _ \\| '__/ _` |  / _ \\| |_  \n" + //
                        " ___) \\ V  V / (_) | | | (_| | | (_) |  _| \n" + //
                        "|____/ \\_/\\_/ \\___/|_|  \\__,_|  \\___/|_|   \n" + //
                        "| |   _   _ _ __   __ _ _ __ ___| |_| |__  \n" + //
                        "| |  | | | | '_ \\ / _` | '__/ _ \\ __| '_ \\ \n" + //
                        "| |__| |_| | | | | (_| | | |  __/ |_| | | |\n" + //
                        "|_____\\__,_|_| |_|\\__,_|_|  \\___|\\__|_| |_|\n" + //
                        "");
        System.out.println("An Epic Fantasy Adventure");
        System.out.println();
        System.out.println("Type 'help' if you need help.");
        System.out.println(currentRoom.getLongDescription());
    }
    
    /**
     * Main game loop.  Continues until the player quits the game.
     * @param scanner The Scanner object to read player input.
     */
    private void runGameLoop(Scanner scanner) {
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand(scanner);
            finished = processCommand(command);
        }
    }
    

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
    if (command.isUnknown()) {
        System.out.println("I don't know what you mean...");
        return false;
    }

    String commandWord = command.getCommandWord();
    return executeCommand(commandWord, command);
    }

    /**
     * Executes the specified command. Use a HashMap to map command words to
     * corresponding actions.
     * @param commandWord The command word entered by the player.
     * @param command The Command object representing the player's input.
     * @return True if the command was "quit", false otherwise.
     */
    private boolean executeCommand(String commandWord, Command command) {
        // I used ChatGPT to simplify some of the code and learnt about new syntax
        Map<String, Runnable> commandMap = new HashMap<>();
        commandMap.put("help", this::printHelp);
        commandMap.put("go", () -> goRoom(command));
        commandMap.put("map", this::printMap);
        commandMap.put("inventory", this::printInventory);
        commandMap.put("buy", () -> buyItem(command));
        commandMap.put("exits", this::printRoute);
        commandMap.put("marketplace", () -> currentRoom.displayShopInventory());
        commandMap.put("fight", this::initiateFight);
        commandMap.put("explore", this::explore);
        commandMap.put("stats", this::printStats);
        commandMap.put("back", this::goBack);
        commandMap.put("quit", () -> quit(command));
        commandMap.put("drop", () -> dropItem(command));
        commandMap.put("sell", () -> sellItem(command));

        Runnable action = commandMap.get(commandWord);
        if(action != null){
            action.run();
            return commandWord.equals("quit"); //End the game if quit is selected
        } else{
            System.out.println("I don't know what you mean...");
            return false; //Command was not found
        }
    }

    /**
     * Initiates a fight with a monster if one is present in the current room.
     */
    private void initiateFight() {
        Monster monster = currentRoom.getMonster();
        if (monster != null) {
            if (fightMonster(monster)) {
                quit(null); // Call quit to end game
            }
            currentRoom.setMonster(null);
        } else {
            System.out.println("There is no monster to fight here!");
        }
    }

    /**
    * Simulates a fight between the player and a monster.
    * @param monster The Monster object to fight.
    * @return True if the player loses, false if the player wins or flees.
    */
    private boolean fightMonster(Monster monster) {
        int playerHealth = playerStats.getHealth();
        int monsterHealth = monster.getHealth();

        while (monster.isAlive() && playerHealth > 0) {
            displayCombatStatus(playerHealth, monster.getHealth(), monster);
            int choice = getAttackChoice();

            if (choice == -1) { // Player chose to flee
                return false; // Player flees, fight ends
            }

            int damage = calculateDamage(choice);
            applyDamage(damage, monster);

            if (monster.isAlive()) {
                playerHealth = applyMonsterAttack(monster, playerHealth);
            }

            if (playerHealth <= 0) {
                gameOver(monster);
                currentRoom.setMonster(null); // Remove monster if player dies
                return true; // Player died
            }
        }

        // Determine outcome AFTER the loop
        boolean playerWon = monster.getHealth() <= 0;
        if (playerWon) {
            victory(monster); // Call victory method FIRST
            currentRoom.setMonster(null); // Remove monster AFTER victory
        }
        playerStats.setHealth(playerHealth); 

        return !playerWon; // Return true if player lost (false if player won)
    }

    /**
     * Displays the current combat status, including monster name, player health, and monster health.
     * @param playerHealth The player's current health points.
     * @param monsterHealth The monster's current health points.
     * @param monster The Monster object currently being fought.
     */
    private void displayCombatStatus(int playerHealth, int monsterHealth, Monster monster) {
        System.out.println("\n\t" + monster.getName() + " attacks! You are in combat!");
        System.out.println("\tYour health: " + playerHealth);
        System.out.println("\t" + monster.getName() + "'s health: " + monsterHealth);
    }


    /**
    * Prompts the player to choose an attack and returns their choice.
    * @return The player's attack choice (1-4), or -1 if the input is invalid or the player chooses to flee.
    */
    private int getAttackChoice() {
        Scanner scanner = this.scanner;
        System.out.println("\nChoose your attack:");
        System.out.println("1. Bare fists");
        System.out.println("2. Weapon");
        System.out.println("3. Health Potion");
        System.out.println("4. Attack Spell");
        System.out.print("Enter your choice (1-4 or 'flee'): ");
        String input = scanner.nextLine();
    
        if (input.equalsIgnoreCase("flee")) {
            System.out.println("This game is not for the faint hearted");
            System.exit(0);
        }
    
        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > 4) {
                System.out.println("Invalid choice (1-4). Try again or type 'flee'.");
                return -1; // Invalid choice: return -1, will loop again in fightMonster
            }
            return choice;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1-4) or type 'flee'.");
            return -1;
        }
    }


    /**
     * Calculates the damage dealt based on the player's attack choice.
     * @param choice The player's attack choice (1-4).
     * @return The amount of damage dealt.
     */
    private int calculateDamage(int choice) {
        int damage = 0;
        switch (choice) {
            case 1:
                damage = 70;
                System.out.println("\t\nYou punch the monster.");
                break;
            case 2:
                damage = inventory.containsItem("ironSword") ? 400 : (inventory.containsItem("rustyAxe") ? 150 : 0);
                System.out.println("\t\nYou swing your " + (damage > 0 ? (inventory.containsItem("ironSword") ? "iron sword" : "rusty axe") : "bare fists"));
                break;
            case 3:
                if (inventory.containsItem("healthPotion")) {
                    playerStats.setHealth(Math.min(100, playerStats.getHealth() + 20));
                    System.out.println("\n\tYou drink a potion and feel stronger!");
                    inventory.removeItem("healthPotion");
                } else {
                    System.out.println("\n\tYou don't have any health potions!");
                }
                break;
            case 4:
                if (inventory.containsItem("attackingSpell")) {
                    damage = 150;
                    System.out.println("\n\tYou cast an attacking spell!");
                    inventory.removeItem("attackingSpell");
                } else {
                    System.out.println("\n\tYou don't have any attacking spells!");
                }
                break;
            default:
                System.out.println("\t\nInvalid choice!");
        }
        return damage;
    }
    
    /**
     * Applies damage to the monster.
     * @param damage The amount of damage to apply.
     * @param monster The monster to apply damage to.
     */
    private void applyDamage(int damage, Monster monster) {
        if (damage > 0) {
            monster.setHealth(monster.getHealth() - damage);
            System.out.println("You dealt " + damage + " damage!");
        }
    }
    
    /**
     * Applies the monster's attack to the player's health.
     * @param monster The attacking monster.
     * @param playerHealth The player's current health.
     * @return The player's health after the monster's attack.
     */
    private int applyMonsterAttack(Monster monster, int playerHealth) {
        playerHealth -= monster.getAttack();
        System.out.println("The " + monster.getName() + " hits you for " + monster.getAttack() + " damage!");
        return playerHealth;
    }
    
    /**
     * Ends the game because the player has been defeated.
     * @param monster The monster that defeated the player.
     */
    private void gameOver(Monster monster) {
        System.out.println("\tYou have been defeated by the " + monster.getName() + "!");
        System.exit(0);
    }
    
    /**
     * Handles actions after the player defeats a monster.  Awards gold for
     * defeating the Leshen and triggers a transformation sequence for
     * defeating the Vouivre.
     * @param monster The defeated monster.
     */
    private void victory(Monster monster) {
        System.out.println("\tYou have defeated the " + monster.getName() + "!");
        if (monster.getName().equals("Leshen")) {
            inventory.addGold(150);
            System.out.println("\tThe surrounding villagers gave you 150 gold coins!");
        } else if (monster.getName().equals("Vouivre")) {
            vouivreTransformation();
        }
    }
    
    /**
     * Handles the transformation sequence when the Vouivre monster is defeated.
     * Displays a text-based animation of the transformation.
     */
    private void vouivreTransformation() {
        System.out.println("The vouivre... it's transforming");
        System.out.println("                     ...%@-..                      \n" + //
                        "                   ....:+-%....                    \n" + //
                        "                   ....@=-:@...                    \n" + //
                        "                   ....%**=*.... .                 \n" + //
                        "                   ..@@#+=-+#@....                 \n" + //
                        "                   ..@@##=--@#..                   \n" + //
                        "                   ..+++==::@...                   \n" + //
                        "                   ...*%%##%...                    \n" + //
                        "                   ....%%##@....                   \n" + //
                        "                   ....%%##%....                   \n" + //
                        "                   ....%%*#@....                   \n" + //
                        "                   ....%%*%#*..                    \n" + //
                        "                    ...@%*#%...                    \n" + //
                        "                   ...+%%##*-..                    \n" + //
                        "                   ....%%*#@. ..                   \n" + //
                        "                   ....@%##%...                    \n" + //
                        "                   ....%%%#@.                      \n" + //
                        "                   ...-#%##%....                   \n" + //
                        "                   ....%%*#%...                    \n" + //
                        "                   ...-#%##%....                   \n" + //
                        "                   ....@%##%....                   \n" + //
                        "                   ....%%*#@....                   \n" + //
                        "                   ....%%##%....                   \n" + //
                        "                   ....@@*#%...                    \n" + //
                        "      ... .............%%##@..........-.....       \n" + //
                        "       .:=...*#-.@+...:%%%%%....=@.%++..-%:.       \n" + //
                        "     .  .=##*@%##@##:.*#+=:+..++=@==@*==#...       \n" + //
                        "  .........+@%#*%@@@@@#%#=#+#@##*##**#@=....... ...\n" + //
                        ".....+@#*#*####+=+#@@##=++=---@%+--====-:::=@=.....\n" + //
                        "...@++====*###*@@##%#@#%%%-%-%+==#%=:-----==*++@...\n" + //
                        "..:@*+=====+@%@#%-=@=#@#@#-#%-@=.+%%*==-::..:::@:..\n" + //
                        ".@*=*%+#+=====@+====-=*#%--*--....-@-::::..#.#+:.@.\n" + //
                        "**...-*.....@#@*====---@%+@-......-@@=@.....+.. .=*\n" + //
                        " .  ... ...%=@..@##%%+==+==-::.---@..@.+... .......\n" + //
                        "       . ..@-*@...*@@==*%%%+-.@%....@=.@..         \n" + //
                        "         ..@=*+@.....@*==:-:.@....=#=..@.          \n" + //
                        "          ..++*+++@@%#==#%#=-.@@@**. .#...         \n" + //
                        "          ..%-++-+++**=*#:-+=-++=-:...+..          \n" + //
                        "            .@===-=**#+==::-=-==-..:.@.            \n" + //
                        "            ..%-==*+@#=-:@=...@-.:.-+..            \n" + //
                        "            . .*==*+@=*.%%%+..%:..:-.              \n" + //
                        "            . ..@==+++=#*#*%.*-...@..              \n" + //
                        "              ..:+=+++@%*#*%%--..=..               \n" + //
                        "              ...@=+++*@#*%@=-...@..               \n" + //
                        "            ....@=+++*+#%%**-....-@....            \n" + //
                        "            ...@-=*++=+*%*#-:...-=-@ ..            \n" + //
                        "              ..#-+-+*++%#%--=--=.*..              \n" + //
                        "            . ..%==++=**%%%#**+...%.               \n" + //
                        "            . ..-=-=+++#%**#+:....=.               \n" + //
                        "            . ..:=-+*++@#**@.....::.               \n" + //
                        "               ..+-*++**#*#......-..               \n" + //
                        "               ..%-*+**=+%=......+..               \n" + //
                        "               ..@-**=--+%.......%...              \n" + //
                        "               ..@=*=-==+%:::....@..               \n" + //
                        "               ..@===-=*#*#=.....@..               \n" + //
                        "                .@=-==++@*%... ..@                 \n" + //
                        "                 @-==*+++#...   .@                 \n" + //
                        "                 %=-+*=+*@..    .@                 \n" + //
                        "                .+===+++++.......%                 \n" + //
                        "                 ---=+++**.......#                 \n" + //
                        "                 .==**++*+.......+                 \n" + //
                        "                 .*-*++++*.......=                 \n" + //
                        "                 .*-++++++.......=                 \n" + //
                        "                 .+=++++++.......+                 \n" + //
                        "                 :-=+*++**.......#                 \n" + //
                        "                 *=+++++*#.......@                 \n" + //
                        "                 @-++++++@*......@..               \n" + //
                        "              ...==+++*++%@=.:...=:..              \n" + //
                        "              ..*-++++++@*%+=:-.-=@                \n" + //
                        "              ...:+=++=+@*%%===:.% ..              \n" + //
                        "                 .@---++%#%++ ..@                  \n" + //
                        "                 .:=--=+=%%..   @                  \n" + //
                        "                 ..%-=*==@.... .*.                 \n" + //
                        "                 ..%-+---@......=.                 \n" + //
                        "                 ..%=----@......-.                 \n" + //
                        "                 ..%--==@@.....-:.                 \n" + //
                        "                 ..@-==+*@.....=..                 \n" + //
                        "                 ..@=-*=*@.....+.                  \n" + //
                        "                 ..@=+=+*@.....#..                 \n" + //
                        "                 ..@=-++*@.....#..                 \n" + //
                        "                 ..@-*+**@.....%..                 \n" + //
                        "                 ..@=++++@.::..%..                 \n" + //
                        "                 ..%-+++*@.:...%..                 \n" + //
                        "                 ..#-++++@..-:=%..                 \n" + //
                        "                 ..*-+*++@..--.@.                  \n" + //
                        "                 . ==++++@.....@..                 \n" + //
                        "                 ..-=*@--@.  ..@..                 \n" + //
                        "                 . :=@=-=@.  ..@..                 \n" + //
                        "                 . :+=--=@.  ..@..                 \n" + //
                        "                   :+----@.  ..@..                 \n" + //
                        "                   .+=--=%.  ..@..                 \n" + //
                        "                 . .+==-+#.  ..%.                  \n" + //
                        "                 . .*-=++*.  ..#.                  \n" + //
                        "                   .#==+++.  ..*                   \n" + //
                        "                   .@=++*=.  ..* .                 \n" + //
                        "                   .@=+-=-.....+..                 \n" + //
                        "                    @----- ....=..                 \n" + //
                        "                    @---==.....-.                  \n" + //
                        "                    @=-=-@... ::                   \n" + //
                        "                    @-=+++  .:::                   \n" + //
                        "                    @-++++....::                   \n" + //
                        "                   .@-****....::                   \n" + //
                        "                   .%=-++* ..:-:                   \n" + //
                        "                   .#-=+**..:-=.                   \n" + //
                        "                   .-==+*+.:=.@.                   \n" + //
                        "                 . ..@-+*+:-=.@.                   \n" + //
                        "                   ..@-++*--..=.                   \n" + //
                        "                   . .==*%--.@..                   \n" + //
                        "                   ...@==@-..:..                   \n" + //
                        "                   .  .@=@..#..                    \n" + //
                        "                      ..%@.%.                      \n" + //
                        "                        .@+..                      ");
        System.out.println("THE VOUIVRE *WAS* THE SWORD!?");
        System.out.println("Well done " + playerName + ". Use it well.");
        System.exit(0);
    }

    

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("\n\tYou are on a journey. Your journey to become a hero.");
        System.out.println("""
                           \tYou need to look for clues about lunareth in order to find the mighty sword of Geralt of Rivia
                           \tand prepare for battle in faraway lands.""" );
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }



    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }
    
        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);
    
        if (nextRoom == null) {
            System.out.println("There is no route!");
        } else {
            // Check for restricted locations requiring the dragon
            String[] restrictedLocations = {"eldralis", "lunareth", "eryndale", "noriven"};
            boolean needsDragon = false;
            for (String location : restrictedLocations) {
                if (nextRoom.getShortDescription().toLowerCase().contains(location)) {
                    needsDragon = true;
                    break;
                }
            }
    
            // Dragon check for restricted areas
            if (needsDragon && (!dragon.isAcquired() || !dragon.canFly())) {
                System.out.println("You cannot get there on foot!");
                return;
            }
    
            // Move to the new room and handle the portal automatically
            if (nextRoom != null) {
                roomHistory.push(currentRoom);
                currentRoom = nextRoom; 
                currentRoom.onEnter(this); 
                System.out.println(currentRoom.getLongDescription());
    
                // Check for a monster in the NEW room
                Monster monster = currentRoom.getMonster();
                if (monster != null) {
                    String answer = ""; 
                    while (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("no")) { 
                        System.out.println("\n\tYou encounter a " + monster.getName() + "!\n" + monster.getImage());
                        System.out.println("\tDo you want to fight? (yes/no)");
                        answer = scanner.nextLine();
                        if (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("no")) {
                            System.out.println("\tInvalid input. Please enter 'yes' or 'no'.");
                        }
                    }
                    if (answer.equalsIgnoreCase("yes")) {
                        if (fightMonster(monster)) {
                            return; 
                        }
                        currentRoom.setMonster(null); 
                    } else { 
                        System.out.println("\tPerhaps a legendary sword requires a legendary battle...");
                    }
                }
            }
        }
    }


    /**
     * Goes back to previous room until first room.
     */
    private void goBack() {
        if (roomHistory.isEmpty()) {
            System.out.println("You are already in the starting room.");
        } else {
            currentRoom = roomHistory.pop();
            System.out.println("\n\tYou retrace your steps...\n");
            System.out.println(currentRoom.getLongDescription());
        }
    }

    

    /**
     * Print out the map of the continent.
     * Includes all the playable locations excluding Lunareth.
     */
    private void printMap() 
    {
        System.out.println(
        "\n\n\t\t\t\t\t\tNoriven\n\n\n\n\n\n" +
        "\tAeloria \tCaelthorne \t\t\t\t\t\t\n\n\n\n\t\t\t\n" +
        "\t\t\t Ignirath\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\tEryndale\n\n\n\n" +
        " Eldralis \n\n" +
        "Hmm...Lunareth isn't on it \n"
        );
    }



    /**
     * Print out items in player inventory.
     * If there is nothing, we say its empty.
     */
    private void printInventory() 
    {
        inventory.displayInventory();
        if(dragon.isAcquired()){
            System.out.println("\tFamiliar: " + dragonName + " the Dragon");
        }
    }

    /**
     * Prints location and possible routes.
     */
    private void printRoute() {
        System.out.println(currentRoom.getRoomAndRouteInfo());
    }


    /**
     * Handles the "buy" command, allowing the player to purchase items from a shop.
     * @param command The Command object representing the player's buy command.
     */
    private void buyItem(Command command) 
    {
        if (!command.hasSecondWord()) {
            System.out.println("Buy what?");
            return;
        }
        String itemName = command.getSecondWord();
        Item itemToBuy = currentRoom.findItemInShop(itemName);

        if (itemToBuy instanceof Armour) {
            useArmor((Armour) itemToBuy);
        }
        
        if (itemToBuy == null) {
            System.out.println("That item is not available for sale here.");
            return;
        }

        if (!dragon.isAcquired() && itemName.equals("beastEnhancingSpell")) {
            System.out.println("You need a dragon to use this spell!");
            return; // Prevent purchase if no dragon
        }

        if (inventory.spendGold(itemToBuy.getValue())) {
            currentRoom.removeFromShop(itemName);
            if (itemName.equalsIgnoreCase("beastenhancingspell")) { // Case-insensitive check
                System.out.println("\tYou bought the " + itemToBuy.getName() + ".\n"); // Message AFTER spell effect
                useBeastEnhancingSpell();
            } else {
                System.out.println("\tYou bought the " + itemToBuy.getName() + ".\n");
                inventory.addItem(itemToBuy);
            }
        }
    }

    /**
     * Explores what is in the area.
     */
    private void explore() {
        if (currentRoom.hasPeople()) {
            printPersonDialogue(); // Call printPersonDialogue directly
        } else if (currentRoom.getShortDescription().toLowerCase().contains("ignirath")) {
            findDragonEgg();
        } else {
            System.out.println("\tThere is nothing of interest to explore here.");
        }
    }

    /**
     * Extension of the explore - if the player meets someone, they talk.
     */
    private void printPersonDialogue() {
        System.out.println("\tYou come across " + currentRoom.getPersonDescription());
        for (Person person : currentRoom.getPeople()) {
            System.out.println(person.getName() + ": " + person.getDialogue());
        }
    }
    
    /**
     * Simulates finding a dragon egg in Ignirath.  There's a 55% chance of
     * finding an egg; otherwise, nothing is found.
     */
    private void findDragonEgg() {
        if (new Random().nextDouble() < 0.55) { 
            System.out.println("\tYou stumble upon a hidden cave!");
            System.out.println("\tInside, nestled amongst ancient stones, you find a dragon egg!");
            System.out.println(
                "           .----.\n" + //
                                        "                 .'  \\__ '.\n" + //
                                        "                |   __|  _ :\n" + //
                                        "                V\\/(0I0\\/ |/\n" + //
                                        "                   \\ : /  '\n" + //
                                        "                   (oYo)\n" + //
                                        "                   /` `\\__\n" + //
                                        "                 _|  _ Vuu: ,\n" + //
                                        "                |uV\\/ \\| \\_/|\n" + //
                                        "                :      /\\   :\n" + //
                                        "            snd  \\   -'    /\n" + //
                                        "                  '-.___.-'  <>\n" + //
                                        "                 =        <;");
            dragon.setAcquired(true); 
            System.out.println("\tYou now have a dragon!");
            System.out.print("What will you name your dragon? ");
            dragonName = scanner.nextLine();
            dragon.setName(dragonName); 
            dragon.setAcquired(true);
            System.out.println("\tYou now have a dragon named " + dragonName + "!");
        } else {
            System.out.println("\tYou explore the area, but find nothing of interest.");
        }
    }


    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        System.out.println("See you soon, intrepid witcher");
        return true;
    }

    /**
     * Applies the beast enhancing spell to the player's dragon, enabling it to fly.
     */
    private void useBeastEnhancingSpell() {
        if (dragon.isAcquired() && !dragon.canFly()) { // Check dragon is acquired and can't fly
            dragon.setCanFly(true); // Add canFly field and method to your Dragon class.
            System.out.println("\n\t" + dragonName + " has grown and learned to fly!");
            System.out.println("\n" + //
                                "                                                 __----~~~~~~~~~~~------___\r\n" + //
                                "                                      .  .   ~~//====......          __--~ ~~\r\n" + //
                                "                      -.            \\_|//     |||\\\\  ~~~~~~::::... /~\r\n" + //
                                "                   ___-==_       _-~o~  \\/    |||  \\\\            _/~~-\r\n" + //
                                "           __---~~~.==~||\\=_    -_--~/_-~|-   |\\\\   \\\\        _/~\r\n" + //
                                "       _-~~     .=~    |  \\\\-_    '-~7  /-   /  ||    \\      /\r\n" + //
                                "     .~       .~       |   \\\\ -_    /></-   /   ||      \\   /\r\n" + //
                                "    /  ____  /         |     \\\\ ~-_/></|- _/   .||       \\ /\r\n" + //
                                "    |~~    ~~|--~~~~--_ \\     ~==-/>-<| \\~--===~~        .\\\r\n" + //
                                "             '         ~-|      /|>--<|-~\\~~       __--~~\r\n" + //
                                "                         |-~~-_/ |>--<|   ~\\_   _-~            /\\\r\n" + //
                                "                              /  \\>---<\\__   \\/~                \\__\r\n" + //
                                "                          _--~ _/ | .-~~____--~-/                  ~~==.\r\n" + //
                                "                         ((->/~   '.|||' -_|    ~~-/ ,              . _||\r\n" + //
                                "                                    -_>-<__~\\      ~~---l__i__i__i--~~_/\r\n" + //
                                "                                    _-~-__   ~)  \\--______________--~~\r\n" + //
                                "                                  //.-~~~-~_--~- |-------~~~~~~~~\r\n" + //
                                "                                         //.-~~~--\\");    
        } else if (!dragon.isAcquired()){
            System.out.println("You need a beast to use this spell.");
        } else {
            System.out.println("Your dragon is already in prime");
        }
    }

    /**
     * Handles the "drop" command, removing an item from the player's inventory.
     * @param command The Command object containing the item to drop.
     */
    private void dropItem(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }
    
        String itemName = command.getSecondWord().toLowerCase(); // Convert to lowercase for case-insensitive check
        if (inventory.removeItem(itemName)) {
            System.out.println("You dropped the " + itemName + ".");
        }
    }

    /**
     * Sets the player's current room to the specified room.
     * @param room The Room object to set as the current room.
     */
    public void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    /**
     * Applies the health increase from a piece of armour to the player's stats.
     * @param armor The Armour object to apply.
     */
    private void useArmor(Armour armor) {
        playerStats.increaseHealth(armor.getHealthIncrease());
        System.out.println("You put on the armor, and feel stronger! Your health has increased by " + armor.getHealthIncrease() + "!");
        System.out.println(playerStats);
    }

    /**
     * Prints the player's current stats (health).
     */
    private void printStats() {
        System.out.println(playerStats);
    }

    /**
     * Returns a list of all rooms in the game.
     * @return A list of all Room objects.
     */
    private List<Room> getAllRooms() {
    return List.of(caelthorne, aeloria, ignirath, eryndale, eldralis, noriven, lunareth);
    }

    /**
     * Handles the "sell" command, allowing the player to sell an item to a specific person.
     * @param command The Command object representing the sell command.
     */
    private void sellItem(Command command) {
        if (!command.hasSecondWord() || !command.hasThirdWord()) {
            System.out.println("Sell what to whom?");
            return;
        }
    
        String itemName = command.getSecondWord().toLowerCase();
        String personName = command.getThirdWord();
    
        Item itemToSell = inventory.findItem(itemName);
    
        if (itemToSell != null) {
            // Find the person
            Person person = findPerson(personName);
            if (person != null) {
                double originalValue = itemToSell.getValue(); // Get original value
                double sellValue = originalValue * 0.8; // Calculate 80% of the value
                int goldEarned = (int) sellValue; // Convert to integer for gold
                inventory.addGold(goldEarned);
                inventory.removeItem(itemName);
                System.out.println("You sold the " + itemName + " to " + personName + " for " + goldEarned + " gold.");
            } else {
                System.out.println("I cannot find " + personName + " here.");
            }
        } else {
            System.out.println("You don't have that item.");
        }
    }
    
    /**
     * Finds a person in the current room by name (case-insensitive).
     * @param personName The name of the person to find.
     * @return The Person object if found, null otherwise.
     */
    private Person findPerson(String personName) {
        for (Person p : currentRoom.getPeople()) {
            if (p.getName().equalsIgnoreCase(personName)) {
                return p;
            }
        }
        return null;
    }
    
}
