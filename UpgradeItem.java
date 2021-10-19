package main;

/**
 * This class implements an Item that a player can buy to upgrade their ship
 */
public class UpgradeItem extends Item {
	
	private enum ShipUpgradeType {
	    ENGINE("Doubles your speed"),
	    CARGO10("Adds Cargo bay of capacity 10 to your ship"),
	    CARGO20("Adds Cargo bay of capacity 20 to your ship"),
	    WEAPONBAY("Adds a Weapons bay to your ship");

	    public final String name;

	    ShipUpgradeType(String name) {
	        this.name = name;
	    }
	}	
	
	// the enum this upgrade is linked to
	private ShipUpgradeType shipUpgradeType;
	
	/**
	 * @param shipUpgradeTypeString, the String value of the enum representing this upgrade
	 */
	public UpgradeItem(String shipUpgradeTypeString) {		
		super(ShipUpgradeType.valueOf(shipUpgradeTypeString).name, "", 0, ItemType.UPGRADE);
		this.shipUpgradeType = ShipUpgradeType.valueOf(shipUpgradeTypeString);
	}
	
	public void upgradeShip(Ship ship) {
        switch (this.shipUpgradeType) {
    		case ENGINE:
    			ship.upgradeSailSpeed();
    			break;
    		case CARGO10:
    			ship.getStorageBays().add(0, new StorageList("Extra Cargo", 10, ItemType.CARGO));
    			break;
    		case CARGO20:
    			ship.getStorageBays().add(0, new StorageList("Extra Cargo", 20, ItemType.CARGO));
    			break;
    		case WEAPONBAY:
    			ship.getStorageBays().add(0, new StorageList("Weapon Bay", 2, ItemType.WEAPON));
    			break;     			
	        default:
	        	break;	        	
        }	
	}	
	
	public static String[] upgrades() {
		int numEnumValues = ShipUpgradeType.values().length;
		String[] upgrades = new String[numEnumValues];
		for (int i = 0; i < numEnumValues; i++) {
			upgrades[i] = ShipUpgradeType.values()[i].toString();
		}
		return upgrades;
	}
}
