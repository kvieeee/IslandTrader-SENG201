package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class represents the physical world in our game, the islands, the routes and methods to 
 * survey the world. It contains private methods to initialize the world with fixed 
 * islands / routes but random Items to buy at the islands stores
 * 
 * Also creates 4 ships for the user to choose from when the game starts
 */
public class World {
	
	// ArrayList of Islands
	private ArrayList<Island> islands; 	

	// ArrayList of Routes between Islands	
	private ArrayList<Route> routes;

	//ArrayList of ships that contain 4 ships
	private ArrayList<Ship> ships; 
	
	// Random object to use for creating random items for the stores
	private Random random;
	
	// Current island the user is on
	private Island currentIsland;

	/**
	 * Creates a new world object. Currently with fixed islands and routes. 
	 * Stores within islands will have random selection of objects to buy or sell
	 * 
	 * @param random, the random object we use for creating random world objects
	 */
	public World(Random random) {
		this.islands = new ArrayList<Island>();
		this.routes = new ArrayList<Route>();
		this.ships = new ArrayList<Ship>();		
		this.random = random;
		setUpWorld();
		this.setCurrentIsland(getIslands().get(0));
	}
	
	/**
	 * Method to create the world 
	 * Creates 5 islands
	 * Creates a store on each island
	 * Adds items to the store according to some store criteria (hard coded)
	 * Adds routes between each island pair (undirected), including random events that might be encountered
	 */	
	private void setUpWorld() {		
		
		PricedItem pricedItem;
		String[] rawCargoItems = {"Burger", "Fries", "Coke", "IceCream", "Chairs", "Dog", "Bananas", "Beer", "Water"};
		String[] rawWeaponItems = {"Rifle", "Cannon", "Shield"};		
		String[] rawUpgradeItems = UpgradeItem.upgrades();
		
		/*
		 * Home Island #1
		 * Friendly relaxed place, has food and drink and grocery type items
		 * no weapons / upgrades 
		 */
		Store store1 = new Store("Bob's Burgers");
		Island island1 = new Island("Home Island", store1);
		islands.add(island1);
		//Add items to the store	
		// CARGO
		for (int i = 0; i < 10; i++) {
			pricedItem = createRandomPricedItem(store1, rawCargoItems, ItemType.CARGO, 8, PriceType.FORSALE, island1);
			store1.addToSell(pricedItem);
			pricedItem = createRandomPricedItem(store1, rawCargoItems, ItemType.CARGO, 6, PriceType.FORBUY, island1);
			store1.addToBuy(pricedItem);
		}		
		
		/*
		 * All Sell No Buy #2
		 * Island has everything for sale, but doesn't buy anything 
		 */
		Store store2 = new Store("All Sell All Day");
		Island island2 = new Island("Everything Island", store2);
		islands.add(island2);		
		//Add items to the store
		// CARGO
		for (int i = 0; i < 10; i++) {
			pricedItem = createRandomPricedItem(store2, rawCargoItems, ItemType.CARGO, 8, PriceType.FORSALE, island2);
			store2.addToSell(pricedItem);
		}
		// WEAPONS
		for (int i = 0; i < 3; i++) {
			pricedItem = createRandomPricedItem(store2, rawWeaponItems, ItemType.WEAPON, 15, PriceType.FORSALE, island2);
			store2.addToSell(pricedItem);
		}	
		// UPGRADES
		for (int i = 0; i < 3; i++) {
			pricedItem = createRandomPricedItem(store2, rawUpgradeItems, ItemType.UPGRADE, 15, PriceType.FORSALE, island2);
			store2.addToSell(pricedItem);
		}			
		
		/*
		 * Mechanical Island #3
		 * No Grocery (well water)
		 * Lots of Upgrades and Weapons 
		 */
		Store store3 = new Store("Guns City");
		Island island3 = new Island("Mechanical Island", store3);
		islands.add(island3);		
		//Add items to the store
		// WATER
		pricedItem = createRandomPricedItem(store3, new String[] {"WATER"}, ItemType.CARGO, 5, PriceType.FORSALE, island3);
		store3.addToSell(pricedItem);
		// WEAPONS
		for (int i = 0; i < 3; i++) {
			pricedItem = createRandomPricedItem(store3, rawWeaponItems, ItemType.WEAPON, 10, PriceType.FORSALE, island3);
			store3.addToSell(pricedItem);
			pricedItem = createRandomPricedItem(store3, rawWeaponItems, ItemType.WEAPON, 15, PriceType.FORBUY, island3);
			store3.addToBuy(pricedItem);			
		}	
		// UPGRADES
		for (int i = 0; i < 4; i++) {
			pricedItem = createRandomPricedItem(store3, rawUpgradeItems, ItemType.UPGRADE, 12, PriceType.FORSALE, island3);
			store3.addToSell(pricedItem);
		}		
		
		/*
		 * Hoarder Island #4
		 * All Buy no Sell
		 * Island buys almost everything but does not sell anything 
		 */
		Store store4 = new Store("Hotel California");
		Island island4 = new Island("Hoarder Island", store4);
		islands.add(island4);		
		//Add items to the store
		// CARGO
		for (int i = 0; i < 10; i++) {
			pricedItem = createRandomPricedItem(store4, rawCargoItems, ItemType.CARGO, 8, PriceType.FORBUY, island4);
			store4.addToBuy(pricedItem);
		}
		// WEAPONS
		for (int i = 0; i < 3; i++) {
			pricedItem = createRandomPricedItem(store4, rawWeaponItems, ItemType.WEAPON, 15, PriceType.FORBUY, island4);
			store4.addToBuy(pricedItem);
		}	
		
		/*
		 * Danger Island #5
		 * All routes to this island should be dangerous but
		 * Prices to sell are cheap, prices to buy are high
		 */	
		Store store5 = new Store("You Made It Emporium");
		Island island5 = new Island("Danger Island", store5);
		islands.add(island5);		
		//Add items to the store
		// CARGO
		for (int i = 0; i < 10; i++) {
			pricedItem = createRandomPricedItem(store5, rawCargoItems, ItemType.CARGO, 5, PriceType.FORSALE, island5);
			store5.addToSell(pricedItem);
			pricedItem = createRandomPricedItem(store5, rawCargoItems, ItemType.CARGO, 9, PriceType.FORBUY, island5);
			store5.addToBuy(pricedItem);			
		}
		// WEAPONS
		for (int i = 0; i < 3; i++) {
			pricedItem = createRandomPricedItem(store5, rawWeaponItems, ItemType.WEAPON, 10, PriceType.FORSALE, island5);
			store5.addToSell(pricedItem);
			pricedItem = createRandomPricedItem(store5, rawWeaponItems, ItemType.WEAPON, 15, PriceType.FORBUY, island5);
			store5.addToBuy(pricedItem);			
		}	
		// UPGRADES
		for (int i = 0; i < 3; i++) {
			pricedItem = createRandomPricedItem(store5, rawUpgradeItems, ItemType.UPGRADE, 10, PriceType.FORSALE, island5);
			store5.addToSell(pricedItem);
		}	
		
		/*
		 * Routes - create routes from an island to another 
		 * with some events that may occur (depend on the probability) 
		 */		
		Route route12 = new Route(4, island1, island2, this);
		route12.addEvent(new RescueSailors(20));
		route12.addEvent(new UnfortunateWeather(70));
		routes.add(route12);		
		
		Route route13 = new Route(7, island1, island3, this);
		route13.addEvent(new RescueSailors(80));
		routes.add(route13);
		
		Route route14 = new Route(7, island1, island4, this);
		route14.addEvent(new UnfortunateWeather(60));
		routes.add(route14);
		
		Route route15 = new Route(10, island1, island5, this);
		route15.addEvent(new RescueSailors(30));
		route15.addEvent(new PiratesEncounter(40));	
		routes.add(route15);
		
		Route route15B = new Route(20, island1, island5, this);
		route15B.addEvent(new RescueSailors(30));
		route15B.addEvent(new PiratesEncounter(80));	
		routes.add(route15B);		
		
		Route route23= new Route(7, island2, island3, this);
		route23.addEvent(new UnfortunateWeather(80));
		routes.add(route23);
		
		Route route24 = new Route(7, island2, island4, this);
		route24.addEvent(new UnfortunateWeather(100));
		routes.add(route24);
		
		Route route25 = new Route(7, island2, island5, this);
		route25.addEvent(new UnfortunateWeather(20));
		route25.addEvent(new RescueSailors(30));
		route25.addEvent(new PiratesEncounter(60));
		routes.add(route25);
		
		Route route25B = new Route(25, island2, island5, this);
		route25B.addEvent(new UnfortunateWeather(40));
		route25B.addEvent(new RescueSailors(80));
		routes.add(route25B);		
		
		Route route34 = new Route(5, island3, island4, this);
		route34.addEvent(new UnfortunateWeather(20));
		routes.add(route34);
		
		Route route35 = new Route(8, island3, island5, this);
		route35.addEvent(new UnfortunateWeather(90));
		route35.addEvent(new RescueSailors(10));
		route35.addEvent(new PiratesEncounter(50));
		routes.add(route35);
		
		Route route45 = new Route(15, island4, island5, this);
		route45.addEvent(new UnfortunateWeather(90));
		route45.addEvent(new PiratesEncounter(30));
		routes.add(route45);				
		
		/*
		 * Ship
		 */		
		Ship ship1 = new Ship("Speedy Soul", 4, 3, 10);
		ship1.getStorageBays().add(new StorageList("Cargo Hold 1", 10, ItemType.CARGO));
		ship1.getStorageBays().add(new StorageList("Upgradable", 1, ItemType.UPGRADE));	
		ships.add(ship1);
		
		Ship ship2 = new Ship("Sudden Storm", 8, 2, 20);
		ship2.getStorageBays().add(new StorageList("Cargo Hold 1", 30, ItemType.CARGO));
		ship2.getStorageBays().add(new StorageList("Upgradable", 1, ItemType.UPGRADE));		
		ships.add(ship2);
		
		Ship ship3 = new Ship("Steel Skull", 6, 2, 20);
		ship3.getStorageBays().add(new StorageList("Cargo Hold 1", 20, ItemType.CARGO));	
		ship3.getStorageBays().add(new StorageList("Cannon Bay 1", 2, ItemType.WEAPON));
		ship3.getStorageBays().add(new StorageList("Upgradable", 1, ItemType.UPGRADE));		
		ships.add(ship3);
		
		Ship ship4 = new Ship("Savage Sloop", 8, 2, 40);
		ship4.getStorageBays().add(new StorageList("Cargo Hold 1", 20, ItemType.CARGO));	
		ship4.getStorageBays().add(new StorageList("Cannon Bay 1", 2, ItemType.WEAPON));
		ship4.getStorageBays().add(new StorageList("Missle Bay 1", 2, ItemType.WEAPON));
		ships.add(ship4);		
		
	}
	
	/**
	 * Method to create a new random priced item
	 * 
	 *  @param store, the Store the Item will be on
	 *  @param itemNames, a string List of names to randomly choose from
	 *  @param itemType, the ItemType (eg Cargo / Update) that is being created
	 *  @param maxPrice, the max price of the item, will be a random int under this value
	 *  @param priceType, the type of priced Item being made eg FORSALE or FORBUY
	 *  @param island, the Island that the item is being sold / bought on
	 *  @return PricedItem, a newly created random PricedItem for a store   
	 */		
	private PricedItem createRandomPricedItem(Store store, String[] itemNames, ItemType itemType, int maxPrice, PriceType priceType, Island island) {
		Item item;
		if (itemType == ItemType.UPGRADE) {
			item = createRandomUpgradeItem(itemNames);
		} else {
			item = createRandomItem(itemNames, itemType, 2);
		}	
						
		int price = random.nextInt(maxPrice *2) + 1; //*2 is dirty balance hack for now				
		PricedItem pricedItem = new PricedItem(item, price, priceType, island);	
		return pricedItem;
	}	
	
	/**
	 * Method to create a new random
	 * 
	 *  @param itemNames, a string List of names to randomly choose from
	 *  @param itemType, the ItemType (eg Cargo / Update) that is being created
	 *  @param maxsize, how big the item is in terms of cargo space
	 *  @return Item, a newly created random Item for a store
	 */		
	private Item createRandomItem(String[] itemNames, ItemType itemType, int maxSize) {
		String newName = itemNames[random.nextInt(itemNames.length)];
		int newSize = 0;
		if (maxSize != 0)
			newSize = random.nextInt(maxSize) + 1;
		Item newItem = new Item(newName, "Dumb Description", newSize, itemType);
		return newItem;
	}
	
	/**
	 * Method to create a new random UpgradeItem
	 * 
	 *  @param itemNames, a string List of names to randomly choose from
	 */		
	private UpgradeItem createRandomUpgradeItem(String[] itemNames) {
		return new UpgradeItem(itemNames[random.nextInt(itemNames.length)]);
	}	
	
	/**
	 * @return the ships the user can choose from
	 */	
	public List<Ship> getShips() {
		return Collections.unmodifiableList(ships);
	}
	
	/**
	 * @return the islands in the game
	 */
	public List<Island> getIslands() {
		return Collections.unmodifiableList(islands);
	}

	/**
	 * Gets list of routes that exist in the game
	 * @return all the routes in the game
	 */
	public List<Route> getRoutes() {
		return Collections.unmodifiableList(routes);
	}
	
	/**
	 * Gets list of routes that exist in the game, limited to routes that touch island1 and island2
	 * @param island1, the first island the route must go between
	 * @param island2, the second island the route must go between 
	 * @return the routes that start and finish at certain island
	 */	
	public List<Route> getRoutes(Island island1, Island island2) {
		ArrayList<Route> validRoutes = new ArrayList<Route>();
		for (Route route : routes) {
			if ((route.getIsland1() == island1 && route.getIsland2() == island2)
				|| (route.getIsland1() == island2 && route.getIsland2() == island1))
				validRoutes.add(route);
		}
		return Collections.unmodifiableList(validRoutes);
	}
	
	/**
	 * Gets list of routes that exist in the game, limited to routes that touch island
	 * @param island, the island we want routes from / to 
	 * @return the routes that start OR finish at a certain island
	 */		
	public List<Route> getRoutes(Island island) {
		ArrayList<Route> validRoutes = new ArrayList<Route>();
		for (Route route : routes) {
			if (route.getIsland1() == island || route.getIsland2() == island)
				validRoutes.add(route);
		}
		return Collections.unmodifiableList(validRoutes);
	}
	
	/**
	 * Gets list of routes from the current island 
	 * @return the routes
	 */		
	public List<Route> getRoutesFromCurrent() {
		ArrayList<Route> validRoutes = new ArrayList<Route>();
		for (Route route : routes) {
			if (route.getIsland1() == getCurrentIsland() || route.getIsland2() == getCurrentIsland())
				validRoutes.add(route);
		}
		return Collections.unmodifiableList(validRoutes);
	}	

	/**
	 * @return the currentIsland
	 */
	public Island getCurrentIsland() {
		return currentIsland;
	}

	/**
	 * @param currentIsland the currentIsland to set
	 */
	public void setCurrentIsland(Island currentIsland) {
		this.currentIsland = currentIsland;
	}		
	
	/**
	 * Method to help me test
	 * @param route, the route to add at position 0
	 */
	public void addRouteIndZero(Route route) {
		this.routes.add(0, route);
	}

}