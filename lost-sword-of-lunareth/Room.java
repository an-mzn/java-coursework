import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Represents a room in the game.  Rooms have a description, exits to other
 * rooms, a shop inventory, a monster (optional), and a list of people
 * present.
 */
public class Room {
    private String description;        // A description of the room
    private HashMap<String, Room> exits; // A map of exits (direction -> Room)
    private List<Item> shopInventory; // List of items for sale in this room
    private Monster monster;           // The monster in this room (can be null)
    private int initialMonsterHealth; // The monster's initial health (for respawning)
    private Dragon dragon;             // A dragon present in this room (can be null)
    private List<Room> allRooms;       // A list of all rooms in the game (for portal)
    private List<Person> people;      // People present in this room


    /**
     * Creates a new Room object.
     * @param description A short description of the room.
     * @param allRooms A list of all rooms in the game (for portal teleportation).
     */
    public Room(String description, List<Room> allRooms) {
        this.description = description;
        this.exits = new HashMap<>();
        this.shopInventory = new ArrayList<>();
        this.monster = null; 
        this.dragon = null;
        this.allRooms = allRooms;
        this.people = new ArrayList<>(); 
    }


    /**
     * Checks if there are any people in this room.
     * @return True if people are present, false otherwise.
     */
    public boolean hasPeople() {
        return !people.isEmpty(); 
    }


    /**
     * Adds a person to the room.
     * @param person The Person object to add.
     */
    public void addPerson(Person person) {
        people.add(person);
    }


    /**
     * Returns a list of Person objects in this room.
     * @return A list of people in the room.
     */
    public List<Person> getPeople() { 
        return people;
    }


    /**
     * Returns a string describing the people in this room.
     * @return A string describing the people, or an empty string if no people are present.
     */
    public String getPersonDescription() {
        if (people.isEmpty()) {
            return ""; 
        }
        StringBuilder sb = new StringBuilder();
        for (Person person : people) {
            sb.append(person.getDescription()).append("\n");
        }
        return sb.toString();
    }


    private List<Room> getPossibleDestinations() {
        List<Room> destinations = new ArrayList<>();
        for (Room room : exits.values()) {
            if (room != null && room != this) {
                destinations.add(room);
            }
        }
        return destinations;
    }


    /**
     * Handles actions performed when the player enters this room.  Currently
     * only handles the Eldralis portal.
     * @param game The Game object.
     */
    public void onEnter(Game game) {
        if (this.getShortDescription().toLowerCase().contains("eldralis")) {
            System.out.println("\n\tYou are in Eldralis. The shimmering portal activates...\n");
            activatePortal(game);
        }
    }


    /**
     * Activates the portal in Eldralis, teleporting the player to a random room.
     * @param game The Game object.
     */
    public void activatePortal(Game game) {
        if (allRooms == null || allRooms.isEmpty()) {
            System.out.println("The portal is malfunctioning! No rooms defined.");
            return;
        }
        allRooms.remove(this); 
        int randomIndex = (int) (Math.random() * allRooms.size());
        Room destinationRoom = allRooms.get(randomIndex);
        game.setCurrentRoom(destinationRoom);
    }


    /**
     * Sets an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor The room the exit leads to.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }


    /**
     * Returns a short description of the room.
     * @return The short description.
     */
    public String getShortDescription() {
        return description;
    }


    /**
     * Returns a long description of the room, including exits.
     * @return The long description.
     */
    public String getLongDescription() {
        return "\n\tYou are in " + description + getExitString();
    }


    /**
     * Returns a string describing the exits from this room.
     * @return The exit string.
     */
    private String getExitString() {
        String returnString = "\n\tRoutes:";
        Set<String> keys = exits.keySet();
        for (String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }


    /**
     * Returns the room reached by going in a specific direction.
     * @param direction The direction to move.
     * @return The Room object in that direction, or null if no exit exists.
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }


    /**
     * Adds an item to the shop inventory of this room.
     * @param item The item to add.
     */
    public void addToShop(Item item) {
        shopInventory.add(item);
    }


    /**
     * Removes an item from the shop inventory.
     * @param itemName The name of the item to remove.
     * @return The removed Item object, or null if not found.
     */
    public Item removeFromShop(String itemName) {
        for (Item item : shopInventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (item.getQuantity() > 0) {
                    item.setQuantity(item.getQuantity() - 1);
                    return item;
                } else {
                    System.out.println("We're all out of " + itemName + "!");
                    return null;
                }
            }
        }
        return null; 
    }


    /**
     * Finds an item in the shop inventory.
     * @param itemName The name of the item to find.
     * @return The Item object, or null if not found.
     */
    public Item findItemInShop(String itemName) {
        for (Item item : shopInventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null; 
    }


    /**
     * Returns the shop inventory of this room.
     * @return The shop inventory.
     */
    public List<Item> getShopInventory() {
        return shopInventory;
    }


    /**
     * Displays the items available for sale in this room.
     */
    public void displayShopInventory() {
        if (shopInventory.isEmpty()) {
            System.out.println("\nThis shop is empty.");
        } else {
            System.out.println("\nItems for sale:");
            for (Item item : shopInventory) {
                System.out.println("- " + item.getName() + ": " + item.getDescription() + " (Value: " + item.getValue() + ", Quantity: " + item.getQuantity() + ")");
            }
        }
    }


    /**
     * Sets the monster for this room.
     * @param monster The Monster object to set.
     */
    public void setMonster(Monster monster) {
        if (monster == null) {
            return; 
        }
        this.monster = monster;
        this.initialMonsterHealth = monster.getHealth();
    }


    /**
     * Returns the monster in this room.
     * @return The Monster object, or null if no monster is present.
     */
    public Monster getMonster() {
        return monster;
    }


    /**
     * Respawns the monster in this room if the room is Eryndale and the monster is dead or absent.
     */
    public void respawnMonster() {
        if (this.getShortDescription().toLowerCase().contains("eryndale")) {
            if (monster == null || !monster.isAlive()) {
                if (monster == null) {
                    monster = new Monster("Leshen", 400, 25,
                        "\n" + //
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
                                                        "              `-'");
                }
                monster.setHealth(initialMonsterHealth); 
                System.out.println("Another Leshen has approached you!");
            }
        }
    }

    /**
     * Sets the dragon for this room.
     * @param dragon The Dragon object.
     */
    public void setDragon(Dragon dragon) {
        this.dragon = dragon;
    }

    /**
     * Gets the dragon in this room.
     * @return The Dragon object, or null.
     */
    public Dragon getDragon() {
        return dragon;
    }

    /**
     * Returns room description and exits.
     * @return Formatted description and exits.
     */
    public String getRoomAndRouteInfo() {
        String routeString = getExitString(); 
        return "\tYou are " + description + "\n" + routeString; 
    }
    
}