package main;

import java.util.ArrayList;
import java.util.Random;
import ui.IslandTraderUI;

/**
 * Manages the IslandTrader game, allowing the {@link Player} to travel to {@link Island}s and trade
 * goods in {@link Store}s
 */
public class IslandTrader {

    // The user interface to be used by this manager
	private final IslandTraderUI ui;

	// The player playing the game
	private Player player;
	
	// The world. An object to hold the Islands and Routes 
	private World world;
	
	// The length of the game, set from user input on initialization
	private int gameLength;
	
	// The current position of the game in time
	private int time = 1;
	
	// The game can be between 20 and 50 days, this regex matches valid input for this period
	public static final String GAME_LENGTH_REGEX = "^[2-4][0-9]|50$";	
	public static final String SHIP_REGEX = "[1-4]";
	
	// Random object to use for game options like do we encounter pirates
	private Random random;	
	
	// Keep tracking of islands visited for scoring purposes
	private ArrayList<Island> visitedIslands;
	
	/**
	 * Creates a IslandManager with the given user interface. Then initializes the world objects
	 * such as Stores, Islands and the Player.
	 * 
	 * @param ui The user interface that this manager should use
	 */
	public IslandTrader(IslandTraderUI ui) {
		this.ui = ui;
		this.random = new Random();		
		this.world = new World(random);
		this.visitedIslands = new ArrayList<Island>();
		this.visitedIslands.add(this.world.getCurrentIsland());
	}

	/**
	 * Starts this IslandTrader. 
	 * This method calls {@link IslandTraderUI#setup(IslandTrader)} to initiate setup of the user interface.
	 */
	public void start() {
		ui.setup(this);
	}	

	/**
	 * This method should be called by the user interface when {@link IslandTraderUI#setup(IslandTrader)}
	 * has been completed. This method calls {@link IslandTraderUI#start()} to tell the user interface to start.	 
	 */
	public void onSetupFinished() {
		ui.start();
	}
	
	/**
	 * This method should be called by the {@link IslandTraderUI} when the user has requested
	 * to quit the application. This method calls {@link IslandTraderUI#quit()} after first confirming the
	 * user really wants to quit.
	 */
	public void onFinish() {
		if (ui.confirmQuit())
			setGameOver();
	}	
	
	/**
	 * This method is called when the game is over, eg if the player loses to pirates
	 */	
	public void setGameOver() {
		// If we had any clean up to do before quitting we should do it here before telling
		// the ui to quit.
		ui.quit();		
	}
	
	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the player of the game
	 * 
	 * @return the player of the game
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Set the player of the game
	 * 
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the length of the game
	 * 
	 * @return the length of the game
	 */
	public int getGameLength() {
		return gameLength;
	}
	
	/**
	 * Gets a random int
	 * 
	 * @param maxInt, determines the biggest random int that could be generate
	 * @return the length of the game
	 */
	public int getRandomInt(int maxInt) {
		return random.nextInt(maxInt);
	}	

	/**
	 * Sets the length of the game
	 * 
	 * @param gameLength, the length of the game to set
	 */
	public void setGameLength(int gameLength) {
		this.gameLength = gameLength;
	}
	
	/**
	 * Get the current time in the game
	 * 
	 * @return the time of the game
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Sets the current time in the game
	 * 
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}
	
	/**
	 * Gets the ui attached to the game
	 *
	 * @return the ui
	 */
	public IslandTraderUI getUI() {
		return this.ui;
	}	

	/**
	 * Gets the game score illustrating how well the player has done. 
	 * Points are awarded for profit and visiting lots of all islands 
	 * Points are deducted for not finishing the game
	 * Points are also linked to your remaining balance
	 * 
	 * @return a score integer, can be negative or positive 
	 */	
	public int gameScore() {		
		int score = player.getBalance();
		
		// Get extra points for profit. 10$ per profit
		score = score + player.getProfitValue()[0] * 10;
		
		// Get points for value in storage still
		score = score + player.getProfitValue()[1];
		
		//lost points if you didn't finish the game, or get close
		if (time < (gameLength-5)) {
			score = score - 30;
		}
		
		//bonus points if you went to all islands
		score = score + visitedIslands.size()*10;
		
		return score;
	}	
	
	/**
	 *  Sets the ship the player has chosen.
	 *  @param option, is the int corresponding to the user ship choice
	 */
	public void selectShip(int option) {
		//Option should be already validated by the calling code
		getPlayer().setShip(getWorld().getShips().get(option));		
	}
	
	/**
	 * This method validates if the player can purchase an item given their money,
	 * given the storage on this ship
	 * 
	 * @param purchase, the priced item that player is attempting to purchase
	 * @return FailureState, Enum representing outcome of the validation
	 */	
	public FailureState validatePurchase(PricedItem purchase) {
		if (player.hasMoney(purchase) == false)
			return FailureState.NOMONEY;
		else if (player.getShip().hasSpace(purchase.getItem()) == false)
			return FailureState.NOSPACE;
		else
			return FailureState.SUCCESS;
	}	
	
	/**
	 * This method transacts a purchase of an item in a store. Returns the purchased item if successful
	 * 
	 * @param option The 0 based index of the item in the store's {@link Store#getToSellList()} list
	 */		
	public void buyStoreItem(int option) {
		//Get the chosen item
		Store store = getWorld().getCurrentIsland().getStore();
		PricedItem purchase = store.getToSellList().get(option);
		//Validate the user can do this
		FailureState validationResult = validatePurchase(purchase);
		if (validationResult == FailureState.SUCCESS) {
			store.sellItem(purchase);
			PricedItem transaction = player.buyItem(purchase);
			ui.processTransaction(transaction);
		} else {
			ui.showError("The purchase failed: " + validationResult.name);
		}
	}
	
	/**
	 * This method validates if the player can sell an item to a store given them
	 * having the item
	 * 
	 * @param sale, the priced item that player is attempting to sale
	 * @return FailureState, Enum representing outcome of the validation
	 */	
	public FailureState validateSale(PricedItem sale) {
		if (player.getShip().hasItem(sale.getItem()) == false)
			return FailureState.NOITEM;
		else
			return FailureState.SUCCESS;
	}		
	
	/**
	 * This method transacts a sale of an item to a store. Returns the sold item if successful
	 * 
	 * @param option The 0 based index of the item in the store's {@link Store#getToBuyList()} list
	 */	
	public void sellStoreItem(int option) {
		//Get the chosen item
		Store store = getWorld().getCurrentIsland().getStore();
		PricedItem sale = store.getToBuyList().get(option);
		//Validate the user can do this
		FailureState validationResult = validateSale(sale);
		if (validationResult == FailureState.SUCCESS) {
			store.buyItem(sale);
			PricedItem transaction = player.sellItem(sale);
			ui.processTransaction(transaction);
		} else {
			ui.showError("The sale failed: " + validationResult.name);
		}
	}	
	
	/**
	 * This method returns a boolean indicating if the user has enough game time left to sail a route
	 * 
	 * @param route, the route the user wishes to sale on
	 * @return boolean indicating if they have enough time
	 */		
	private boolean hasTime(Route route) {
		if (gameLength-time >= player.getShip().sailingDays(route)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Method to determine if the user can trade at all, do they have any money
	 * or anything to sell (CARGO)
	 * 
	 * @return boolean indicating if the user can trade
	 */		
	private boolean canTrade() {		
		return (
					//Player has money and store is selling
					(
						getPlayer().getBalance() > 0
						&& getWorld().getCurrentIsland().getStore().getToSellList().size() > 0
					) ||
					//Player has cargo & store is buying 
					(
						getPlayer().getShip().hasCargo()
						&& getWorld().getCurrentIsland().getStore().getToBuyList().size() > 0
					)
				);
	}
	
	/**
	 * Method determines if it is game over, ie the user does not have time/money to sail any route
	 * and the value of the items in their cargo is not enough to save them
	 * @return FailureState indicating whether the game should end
	 */
	public FailureState isGameOver() {
		for (Route route : getWorld().getRoutesFromCurrent()) {
			FailureState lastFailure = validateRoute(route, true);
			if (lastFailure == FailureState.SUCCESS || lastFailure == FailureState.MUSTREPAIR) {
				return FailureState.SUCCESS;
			}			
		}
		if (canTrade()) {
			// The user can't sail anywhere, even if they trade but they can buy and sell some stuff for fun
			return FailureState.GAMEOVER_SOFT;
		} else {
			// The user doesn't have money and anything to sell, its hard game over
			// Also true if pirates make you walk the plank
			return FailureState.GAMEOVER_HARD;
		}
	}
	
	/**
	 * This method returns a FailureState enum indicating if the user can sail a route
	 * given their money and remaining gametime. User may fail multiple criteria but only
	 * one is returned in order of NOTIME, NOMONEY, MUSTREPAIR
	 * 
	 * @param route, the route the user wishes to sale on
	 * @param includeCargo, boolean to indicate if we should include cargo value in hasMoney calculation
	 * @return FailureState, Enum representing outcome of the validation
	 */	
	public FailureState validateRoute(Route route, boolean includeCargo) {
		if (hasTime(route) == false)
			return FailureState.NOTIME;		
		else if (player.hasMoney(route, includeCargo) == false)
			return FailureState.NOMONEY;		
		else if (player.getShip().getRepairCost() > 0)
			return FailureState.MUSTREPAIR;		
		else
			return FailureState.SUCCESS;
	}
	
	/**
	 * This method returns a enum indicating if the user has
	 * enough money to repair their ship
	 * 
	 * @param ship, the ship to repair
	 * @return FailureState, Enum representing outcome of the validation
	 */	
	public FailureState validateRepair(Ship ship) {
		if (player.getShip().getRepairCost() > this.getPlayer().getBalance())
			return FailureState.NOMONEY;
		else
			return FailureState.SUCCESS;
	}	
	
	/**
	 * This method validates if the user can buy / sell / travel an Item or route. Helper method
	 * to enable the ui to highlight better options for the user 
	 * 
	 * @param obj, the object being validated as a option for the user
	 * @return FailureState, Enum representing outcome of the validation
	 */		
	public FailureState validate(Object obj) {
		if (obj instanceof Route) {
			return validateRoute((Route)obj, false);
		} else if (obj instanceof PricedItem) {
			if (((PricedItem)obj).getType() == PriceType.FORBUY) {
				return validateSale((PricedItem)obj);
			} else {
				return validatePurchase((PricedItem)obj);
			}
		} else if (obj instanceof Ship) {
			return validateRepair((Ship) obj);
		} else {
			return FailureState.UNKNOWN;
		}
	}	
	
	/**
	 * Sails the route
	 * @param option, the route index chosen by the user in the route list from the current island
	 */
	public void sailRoute(int option) {
		// Get the route the user choose
		Route route = getWorld().getRoutesFromCurrent().get(option);
		
		// Validate the route (money to sail, time in game)
		FailureState validationResult = validateRoute(route, false);
		if (validationResult == FailureState.SUCCESS) {			
			//Get the wages for the route, they are paid upfront
			int wages = getPlayer().deductRouteWages(route);
			String name = "Crew to " +route.otherIsland(getWorld().getCurrentIsland());
			PricedItem wageRecord = new PricedItem(new Item(name, "No Description", 0, ItemType.WAGES), wages, PriceType.PURCHASED, getWorld().getCurrentIsland());
			getPlayer().addTransaction(wageRecord);
			
			//Move player to the new island
			Island newIsland = route.otherIsland(getWorld().getCurrentIsland());
			if(visitedIslands.contains(newIsland) == false) {
				visitedIslands.add(newIsland);
			}
			getWorld().setCurrentIsland(newIsland);
			
			// Assume the time passed, even if we meet pirates etc,
			// you sail the route and you effectively get all the bad effects at the end
			int sailingTime = getPlayer().getShip().sailingDays(route);
			setTime(getTime() + sailingTime);
			
			// Tell the user about it
			ui.sailRoute(route, wageRecord, sailingTime);
		} else {
			ui.showError("Sailing Failed: " + validationResult.name);
		}
	}
	
	/**
	 * Triggers the random event attached to the route if it is randomly called. 
	 * A random number between 1-100 is created, if the event probability is lower 
	 * than this then trigger the event
	 * @param event, the event to potentially trigger randomly
	 */	
	public void triggerRandomSailingEvent(RandomEvent event) {
		int probabilityOutcome = getRandomInt(101);
		if (probabilityOutcome < event.getProbability()) {
			event.eventTriggered(this);
		}
	}
	
	/**
	 * Method called to repair this ship. Deducts money from the player and creates a transaction record
	 */		
	public void repairShip() {
		//Repair the ship & deduct funds
		int repairCost = getPlayer().getShip().getRepairCost();
		getPlayer().setBalance(getPlayer().getBalance()-repairCost);
		getPlayer().getShip().setDamageAmount(0);
		
		//Create a transaction record for the repair
		String name = "Repair ship " + getWorld().getCurrentIsland();
		PricedItem repairRecord = new PricedItem(new Item(name, "No Description", 0, ItemType.REPAIR), repairCost, PriceType.PURCHASED, getWorld().getCurrentIsland());
		this.getPlayer().addTransaction(repairRecord);		
	}

}
