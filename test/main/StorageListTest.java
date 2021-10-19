/**
 * 
 */
package test.main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Item;
import main.ItemType;
import main.StorageList;

/**
 * @author bmalthi
 *
 */
class StorageListTest {

	@Test
	void namingTest() {
		StorageList test1 = new StorageList("Test1", 10, ItemType.CARGO);
		StorageList test2 = new StorageList("Test2", 5, ItemType.WEAPON);
		
		//Test Naming
		assertTrue(test1.toString().equals("CARGO:Test1. Capacity is 10, 0 used"));
		assertTrue(test2.toString().equals("WEAPON:Test2. Capacity is 5, 0 used"));		
	}
	
	@Test
	void getItemsTest() {
		StorageList test1 = new StorageList("Test1", 10, ItemType.CARGO);
		Item testCargo = new Item("Test1", "", 5, ItemType.CARGO);
		test1.addItem(testCargo);
		
		//Test Naming
		assertTrue(test1.getItems().get(0) == testCargo);		
	}	
	
	@Test
	void addRemoveItemTest() {
		StorageList test1 = new StorageList("Test1", 10, ItemType.CARGO);
		StorageList test2 = new StorageList("Test2", 5, ItemType.WEAPON);
		
		// New StorageList should have capacity of 10
		assertTrue(test1.remainingSpace() == 10);
		
		Item testCargo = new Item("Test1", "", 5, ItemType.CARGO);
		Item testWeapon = new Item("Test1", "", 5, ItemType.WEAPON);
		Item notAdded = new Item("Test2", "", 5, ItemType.CARGO);
		
		// We should be able to add a Cargo Item of size 5
		assertTrue(test1.addItem(testCargo));
		
		// Remaining space should be 5
		assertTrue(test1.remainingSpace() == 5);		
		
		// The list should have the item we added
		assertTrue(test1.hasItem(testCargo));
		
		// We shouldn't be able to add a WEAPON Item of size 5 since its a CARGO STorageList
		assertFalse(test1.addItem(testWeapon));
		
		// Remaining space should still be 5
		assertTrue(test1.remainingSpace() == 5);
		
		//We shouldn't be able to remove testWeapon from the list since it was never added
		assertFalse(test1.removeItem(testWeapon));
		
		//We should be able to remove the testCargo from the list since it was added
		assertTrue(test1.removeItem(testCargo));
		
		//The storage list should have original space of 10 left
		assertTrue(test1.remainingSpace() == 10);		
				
		//We should not be able to remove the notAdded Item, since its not in the storageList
		assertFalse(test1.removeItem(notAdded));	
		
		// Test list removal
		assertTrue(test2.addItem(testWeapon));
		test2.setEmpty();
		assertTrue(test2.getSpaceUsed()==0);
		

	}

}
