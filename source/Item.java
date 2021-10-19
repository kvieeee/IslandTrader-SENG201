package main;

/**
 * This class implements an Item that a player can buy and sell at stores
 * and have in their cargo bay/s
 */
public class Item {
	
	// The name of the item
	private String name;
	
	// A description of the item
	private String description;
	
	// The size of the item	
	private int size;
	
	// The type of the item, eg Cargo or a Weapon
	private ItemType type;
	
	/**
	 * Create a new item
	 * @param name, The item name
	 * @param description, the description of the item
	 * @param size, The size of item, how much ship space it takes up
	 * @param type, the type of item eg Cargo of Weapon or Upgrade
	 */	
	public Item(String name, String description, int size, ItemType type) {
		this.name = name;
		this.description = description;
		this.size = size;
		this.type = type;
	}	
	
	/**
	 * Create a new item of type Cargo
	 * @param name, The item name
	 * @param description, the description of the item
	 * @param size, The size of item, how much ship space it takes up	 * 
	 */	
	public Item(String name, String description, int size) {
		this(name, description, size, ItemType.CARGO);
	}		

	/**
	 * @return the name of the Item
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description of the Item
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the size of Item, e.g. Cargo space it takes up
	 */
	public int getSize() {
		return size;
	}		
	
	/**
	 * @return enum ItemType which is the type of item
	 */	
	public ItemType getType() {
		return type;
	}

	/**
	 * @return a string representation of the Item
	 */
	@Override
	public String toString() {
        switch (this.type) {
    		case CARGO:	
    			return name +", " +type.name() +":"+ size;
    		case WEAPON:
    			return name +", " +type.name() +":"+ size;
    		case UPGRADE:
    			return name +", " +type.name();
    		case REPAIR:
    			return name +", " +type.name();
    		case WAGES:
    			return name +", " +type.name(); 
    		case RESCUE:
    			return name +", " +type.name();     			
	        default:
	        	return name +", " +type.name();
        }
	}		
	
	/**
	 * Method to test of Item equality, ignores item description but items
	 * must be the same name, type and size
	 * @param item, the item being compared to this item
	 * @return a boolean representing equality with another Item
	 */	
	@Override	
	public boolean equals(Object item) {
		return (	
			   (this.getName() == ((Item) item).getName())
			&& (this.getType() == ((Item) item).getType())
			&& (this.getSize() == ((Item) item).getSize())
		);		
	}

	
}