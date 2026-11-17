import java.util.ArrayList;
import java.util.List;

/**
 * Manages the player's inventory, including items and gold.  The inventory
 * has a maximum weight capacity.
 */
public class Inventory {
    private List<Item> items;       // List of items in the inventory
    private int gold;              // Amount of gold the player possesses
    private double maxWeight;      // Maximum weight capacity of the inventory
    private double currentWeight; // Current weight of items in the inventory


    /**
     * Constructor for the Inventory class.  Initializes the inventory with
     * a starting amount of gold and a maximum weight capacity.
     */
    public Inventory() {
        this.items = new ArrayList<>();
        this.gold = 350; // Player's starting gold
        this.maxWeight = 8; // Maximum weight the player can carry
        this.currentWeight = 0;
    }


    /**
     * Adds an item to the inventory if there is enough weight capacity.
     * @param item The item to add.
     */
    public void addItem(Item item) {
        double newWeight = currentWeight + item.getWeight();
        if (newWeight <= maxWeight) {
            items.add(item);
            currentWeight = newWeight;
            System.out.println("\t" + item.getName() + " has been added to your inventory.\n");
        } else {
            System.out.println("\n\tYour inventory is too full to carry " + item.getName() + "!");
        }
    }


    /**
     * Subtracts a specified amount of gold from the player's gold.
     * @param amount The amount of gold to subtract.
     * @return True if the player had enough gold, false otherwise.
     */
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        } else {
            System.out.println("\tYou don't have enough gold!");
            return false;
        }
    }


    /**
     * Adds a specified amount of gold to the player's gold.
     * @param amount The amount of gold to add.
     */
    public void addGold(int amount) {
        gold += amount;
    }


    /**
     * Removes an item from the inventory by name.
     * @param itemName The name of the item to remove.
     * @return True if the item was found and removed, false otherwise.
     */
    public boolean removeItem(String itemName) {
        Item toRemove = null;
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                toRemove = item;
                break;
            }
        }
        if (toRemove != null) {
            items.remove(toRemove);
            currentWeight -= toRemove.getWeight();
            return true; 
        } else {
            System.out.println("\tYou don't have " + itemName + " in your inventory!");
            return false; 
        }
    }


    /**
     * Checks if the inventory contains an item with the given name.
     * @param itemName The name of the item to check for.
     * @return True if the item is found, false otherwise.
     */
    public boolean containsItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Displays the contents of the player's inventory, including items and gold.
     */
    public void displayInventory() {
        if (items.isEmpty()) {
            System.out.println("\n\tYour inventory is empty.");
        } else {
            System.out.println("\n\tYour inventory contains:");
            for (Item item : items) {
                System.out.println("- " + item + " (Weight: " + item.getWeight() + ")");
            }
        }
        System.out.println("\tGold: " + gold);
        System.out.println("\tCurrent Weight: " + currentWeight + "/" + maxWeight);
    }


    /**
     * Sets the maximum weight capacity of the inventory.
     * @param maxWeight The new maximum weight.
     */
    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }


    /**
     * Returns the maximum weight capacity of the inventory.
     * @return The maximum weight.
     */
    public double getMaxWeight() {
        return maxWeight;
    }


    /**
     * Returns the current weight of items in the inventory.
     * @return The current weight.
     */
    public double getCurrentWeight() {
        return currentWeight;
    }

    /**
     * Finds an item in the inventory by name (case-insensitive).
     * @param itemName The name of the item to search for.
     * @return The Item object if found, null otherwise.
     */
    public Item findItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }
}