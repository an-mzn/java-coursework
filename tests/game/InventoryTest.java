import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    @Test void addingAndRemovingTracksWeightAndNames() {
        var inventory = new Inventory();
        inventory.addItem(new Item("Sword", "", 1, 1, 3));
        assertTrue(inventory.containsItem("sword"));
        assertEquals(3, inventory.getCurrentWeight());
        assertTrue(inventory.removeItem("SWORD"));
        assertEquals(0, inventory.getCurrentWeight());
        assertFalse(inventory.removeItem("Sword"));
    }
    @Test void rejectsItemsOverCapacity() {
        var inventory = new Inventory();
        inventory.addItem(new Item("Heavy", "", 1, 1, 9));
        assertFalse(inventory.containsItem("Heavy"));
        assertEquals(0, inventory.getCurrentWeight());
    }
    @Test void cannotSpendMoreGoldThanAvailable() {
        var inventory = new Inventory();
        assertFalse(inventory.spendGold(351));
        assertTrue(inventory.spendGold(350));
        assertFalse(inventory.spendGold(1));
    }
}
