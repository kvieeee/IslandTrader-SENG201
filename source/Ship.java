package main;
import java.util.ArrayList;

/**
 * This class represents a ship that the player uses on their quest 
 */
public class Ship {
	
	//The name of the ship
	private String name; 
	
	//The number of crew the ship has
	private int numberOfCrew; 
	
	// How fast the ship can sail, distance per day
	private int sailSpeed;  
	
	// The current damage status of the ship
	private int damageAmount;
	
	//How much damage the ship can take
	private int endurance;
	
	// List of storageLists the ship has
	// Different lists indicate differing ability to store cargo
	private ArrayList<StorageList> storage;
	
	/**
	 * Creates a new ship
	 * @param name, the name of the ship
	 * @param numberOfCrew, the number of crew the ship takes
	 * @param sailSpeed, the speed of the ship
	 * @param endurance, how much damage the ship can take
	 */	
	public Ship(String name, int numberOfCrew, int sailSpeed, int endurance) {
		this.name = name;
		this.numberOfCrew = numberOfCrew;
		this.sailSpeed = sailSpeed;
		this.endurance = endurance;
		this.damageAmount = 0;
		this.storage = new ArrayList<StorageList>();
		
	}
	
	/** 
	 * @return the name of the ship
	 */	
	public String getName() {
		return this.name;
	}
	
	/** 
	 * @return the number of crew on the ship
	 */		
	public int getNumberOfCrew() {
		return this.numberOfCrew;
	}

	/**
	 * @return the sailing speed of the ship
	 */	
	public int getSailSpeed() {
		return this.sailSpeed;
	}
	
	/**
	 * Doubles the sail speed of the ship, used after buying a certain upgrade
	 */	
	public void upgradeSailSpeed() {
		this.sailSpeed = this.sailSpeed * 2; 
	}	
	
	/**
	 * @return the current damage level of the ship
	 */		
	public int getDamageAmount() {
		return this.damageAmount;
	}
	
	/**
	 * @param damageAmount, the new amount of damage for the ship
	 */		
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}	
	
	/**
	 * @return the endurance, ie how much damage the ship can take
	 */
	public int getEndurance() {
		return this.endurance;
	}
	
	/**
	 * @return the list of storage bays on the ship
	 */	
	public ArrayList<StorageList> getStorageBays() {
		return this.storage;
	}
	
	/**
	 * Checks of the ship has enough space on in any relevant storage bay to add the item.
	 * @param item, the Item we are checking if there is enough space for
	 * @return boolean indicating if the item can be added to the ship of not
	 */	
	public boolean hasSpace(Item item) {
		for (StorageList storagebay : storage) {
			if (storagebay.validateAdd(item)) {
 					return true;
 			}

 		}
 		return false;
 	}

	/**
	 * Adds the item to the ship's storage if it can
	 * @param item, the item to be added
	 * @return boolean indicating success
	 */
	public boolean addItem(Item item) {
		for (StorageList storagebay : storage) {
			if (storagebay.validateAdd(item)) {
				//this is a bit ugly by this point we have triple checked, could be refactored
				return storagebay.addItem(item);
 			}

 		}
 		return false;
 	}

	/**
	 * Checks if the item is on the ship
	 * @param item, the item to be added
	 * @return boolean indicating if the item is on the ship or not
	 */	
	public boolean hasItem(Item item) {
		for (StorageList storagebay : storage) {
			if (storagebay.hasItem(item)) {
				return true;
			}
		}
		return false;
 	}

	/**
	 * Remove the item from storage if the ship has the item
	 * @param item, the item to be removed
	 * @return boolean indicating if the item was removed or not
	 */		
	public boolean removeItem(Item item) {
		for (StorageList storagebay : storage) {
			if(storagebay.removeItem(item)) {
				return true;
			}
		}
		return false;
 	}	
	
	/**
	 * Gets the sailing cost per day for the ship.
	 * Calculated as the number of crew on the ship 
	 * @return the cost per day
	 */
	public int getCostPerDay() {
		return 1 * getNumberOfCrew();
	}
	
	/**
	 * Gets the repair cost for the ship. 
	 * Cost is 1 for unit of damage
	 * @return cost of repair
	 */	
	public int getRepairCost() {
		return 1 * getDamageAmount();
	}
	
	/**
	 * Returns a description of the ship
	 * @return the string description
	 */		
	public String description() {		
		String output = "***  " + getName() +"  ***\n";
		output = output + "  It has " +getNumberOfCrew() + " crew and speed of " + getSailSpeed() +" at cost of " +getCostPerDay() +" per day wages.\n";
		output = output + "  It can take " +getEndurance() +" damage. Currently " +getDamageAmount() +" damage.";
		if (getDamageAmount() > 0)
			output = output + " It will cost " +getRepairCost() +" to repair.\n";
		else
			output = output + "\n";
		output = output + "  It has the following storage:\n";
		for (StorageList list : storage) {
			output = output +"    " + list.description() +"\n";
		}
		ArrayList<Item> upgrades = getUpgrades();
		if (upgrades.size() > 0) {
			output = output +"  It has been upgraded:\n";
			for (Item upgrade : upgrades) {
				output = output +"    " + upgrade +"\n";
			}			
		}
		return output;						
	}
	
	/**
	 * Returns core ship details (needed at start of game in gui)
	 * @return the string description
	 */		
	public String details() {		
		String output = "";
		output = output + "  It has " +getNumberOfCrew() + " crew and speed of " + getSailSpeed() +" at cost of " +getCostPerDay() +" per day wages. It can take " +getEndurance() +" damage\n";
		output = output + "  It has the following storage:\n";
		for (StorageList list : storage) {
			output = output +"    " + list.description() +"\n";
		}
		return output;						
	}	
	
	/**
	 * Returns a string representation of the ship
	 */	
	@Override
	public String toString() {
		return getName();						
	}
	
	/**
	 * This method returns a the time it will take the player to sail a route, given their ship
	 * 
	 * @param route, the route the user wishes to sale on
	 * @return int the number of sailing days
	 */		
	public int sailingDays(Route route) {
		return  (int) Math.ceil( (double) route.getDistance() / (double) getSailSpeed());
	}
	
	/**
	 * This method returns a the cost for a player to sail a route
	 * 
	 * @param route, the route the user wishes to sale on
	 * @return int the cost of the route
	 */		
	public int costOfRoute(Route route) {
		return getCostPerDay() * sailingDays(route);		
	}
	
	/**
	 * Gets any upgrades
	 * @return the upgrades
	 */
	public ArrayList<Item> getUpgrades() {
		ArrayList<Item> upgrades = new ArrayList<Item>();
		for (StorageList list : storage) {
			if (list.getType() == ItemType.UPGRADE) {
				for (Item item: list.getItems()) {
					upgrades.add(item);
				}
			}
		}
		return upgrades;
	}
	
	/**
	 * Removes all the user's cargo (Ignores weapons)
	 * @return the stolen items
	 */
	public ArrayList<Item> removeCargo() {
		ArrayList<Item> stolenCargo = new ArrayList<Item>();
		for (StorageList storageBay : storage) {			
			if (storageBay.getType() == ItemType.CARGO) {								
				for (Item item: storageBay.getItems()) {
					stolenCargo.add(item);
				}
				storageBay.setEmpty();
			}
		}
		return stolenCargo;
	}
	
	/**
	 * Method to check if the ship has weapons
	 * @return boolean indicating if the ship has weapons or not
	 */
	public boolean hasWeapons() {
		for (StorageList list : storage) {
			if (list.getType() == ItemType.WEAPON && list.getSpaceUsed() > 0)
				return true;
		}
		return false;
	}
	
	/**
	 * Method to check if the ship has any cargo
	 * @return boolean indicating if the ship has cargo or not
	 */
	public boolean hasCargo() {
		for (StorageList list : storage) {
			if (list.getType() == ItemType.CARGO && list.getSpaceUsed() > 0)
				return true;
		}
		return false;
	}	
	
}

