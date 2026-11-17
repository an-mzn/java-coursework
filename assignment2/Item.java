/**
 * Represents an item in the game. Items have a name, description, value,
 * quantity, and weight.
 */
public class Item {
    private String name;          // The name of the item
    private String description;   // A description of the item
    private int value;            // The value (price or in-game worth) of the item
    private int quantity;         // The quantity of this item
    private double weight;        // The weight of a single unit of this item


    /**
     * Constructor for creating a new Item object.
     * @param name The name of the item.
     * @param description A description of the item.
     * @param value The value of the item.
     * @param quantity The quantity of the item.
     * @param weight The weight of the item.
     */
    public Item(String name, String description, int value, int quantity, double weight) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.quantity = quantity;
        this.weight = weight;
    }


    /**
     * Returns the name of the item.
     * @return The item's name.
     */
    public String getName() {
        return name;
    }


    /**
     * Returns a description of the item.
     * @return The item's description.
     */
    public String getDescription() {
        return description;
    }


    /**
     * Returns the value of the item.
     * @return The item's value.
     */
    public int getValue() {
        return value;
    }


    /**
     * Returns the quantity of the item.
     * @return The item's quantity.
     */
    public int getQuantity() {
        return quantity;
    }


    /**
     * Sets the quantity of the item.
     * @param quantity The new quantity of the item.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    /**
     * Returns the weight of the item.
     * @return The item's weight.
     */
    public double getWeight() {
        return weight;
    }


    /**
     * Provides a string representation of the item, including its name, description, and value.
     * @return A string representation of the item.
     */
    @Override
    public String toString() {
        return name + " - " + description + " (Value: " + value + ")";
    }
}