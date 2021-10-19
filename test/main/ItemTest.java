/**
 * 
 */
package test.main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import main.Item;
import main.ItemType;

/**
 * @author bmalthi
 *
 */
class ItemTest {

	@Test
	void equalsTest() {
		Item test1 = new Item("Horse", "Description I don't use - test item 1", 10, ItemType.CARGO);
		Item test2 = new Item("Horse", "Description I don't use - test item 2", 10);
		Item test3 = new Item("Horse", "Description I don't use - test item 3", 5);
		Item test4 = new Item("Not Horse", "Description I don't use - test item 4", 10);
		Item test5 = new Item("Horse", "Description I don't use - test item 5", 10, ItemType.WEAPON);
		
		// Item1 & Item2 only differ by description & constructor method, there should be the same item
		assertTrue(test1.equals(test2));
		
		// Item3 is different its smaller than Item1, size = 5 instead of 10
		assertFalse(test1.equals(test3));
		
		// Item4 is different, its a "Not Horse" instead of "Horse"
		assertFalse(test1.equals(test4));
		
		// Item5 is different, its a Weapon type instead of Cargo
		assertFalse(test1.equals(test5));		
	}
	
	@Test
	void toStringTest() {
		Item test1 = new Item("Horse", "Description I don't use - test item 1", 10, ItemType.CARGO);
		Item test2 = new Item("Horse", "Description I don't use - test item 1", 10, ItemType.WEAPON);
		Item test3 = new Item("Horse", "Description I don't use - test item 3", 10, ItemType.CARGO);
		
		// Different types have different tostring output even if otherwise identical
		assertNotEquals(test1.toString(), test2.toString());
		
		// Description is not part of the tostring output
		assertEquals(test1.toString(), test3.toString());
		
		Item tests1 = new Item("Upgrade", "", 10, ItemType.UPGRADE);
		Item tests2 = new Item("Repair", "", 10, ItemType.REPAIR);
		Item tests3 = new Item("Wages", "", 10, ItemType.WAGES);
		Item tests4 = new Item("Rescue", "", 10, ItemType.RESCUE);
		
		assertTrue(tests1.toString().equals("Upgrade, UPGRADE"));
		assertTrue(tests2.toString().equals("Repair, REPAIR"));
		assertTrue(tests3.toString().equals("Wages, WAGES"));
		assertTrue(tests4.toString().equals("Rescue, RESCUE"));
	}	

}
