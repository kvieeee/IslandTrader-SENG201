package main;

/**
 * Class that models an island in the game. Each island has a name and a store
 */
public class Island {

	// The Name of the Island
	private final String name;
	
	// Link to the store on the island
	private final Store store;
	
	/**
	 * Create and Island with a name and a store
	 * @param name The name of the island
	 * @param store The store that is on the island
	 */
	public Island(String name, Store store) {
		this.name = name;
		this.store = store;
	}
	
	/**
	 * @return the name of this Island
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the store on the island
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * @return a string representation of the island, the name
	 */	
	@Override
	public String toString() {		
		return getName();
	}

}