package ui;

import java.util.ArrayList;

import main.FailureState;
import main.Island;
import main.IslandTrader;
import main.PricedItem;
import main.Route;

/**
 * Defines a user interface (UI) for a {@link IslandTrader}.
 */
public interface IslandTraderUI {
    /**
     * Initialises this UI and sets up the given IslandTrader, with the ships, islands, stores to be managed
     * Once setup is complete this UI must call {@link IslandTrader#onSetupFinished}.
     *
     * @param islandTrader, the islandTrader game instance that this UI interacts with
     */
    public void setup(IslandTrader islandTrader);

    /**
     * Starts this UI
     */
    public void start();

    /**
     * Confirms user wants to quit the game
     * @return boolean indicating user intention to quit
     */
	public boolean confirmQuit();
	
    /**
     * Quits the application.
     */
    public void quit();

    /**
     * Reports the given error to the user.
     *
     * @param error The error to display
     */
    public void showError(String error);
    
	/**
	 * Show the user the details of the transaction, if successful
	 * @param transaction the transaction
	 */	
    public void processTransaction(PricedItem transaction);
    
    /**
     * Show the user what happened on their sailing
     *
     * @param route, the route being sailed
     * @param wageRecord, the transaction record for our wage payment
     * @param sailingTime, days it took to sail the route
     */
    public void sailRoute(Route route, PricedItem wageRecord, int sailingTime);        
    
    /**
     * Show the user how bad weather impacted them on their sailing. If you hit bad weather it
     * damages your ship by 20% of its total endurance. This needs to be paid before another sailing
     *
     * @param damage, how much damage the weather caused
     * @param repairCost, the extra repair cost from the weather
     * @param repairValidation, indicates if the user can afford repair
     */
    public void encounterWeather(int damage, int repairCost, FailureState repairValidation);

    /**
     * Reports details to the user of encounter with sailors who are rescued
     *
     * @param numRescuedSailors, the random number of sailors rescued, depends on ship size
     * @param rewardRecord, each sailor gives a random reward, this is the total
     */
    public void rescueSailors(int numRescuedSailors, PricedItem rewardRecord);
    
    /**
     * Reports details to the user of encounter with pirates
     * 
     * @param diceThrow, the random number that the user got to determine success when fighting the pirates
     * @param boardShip, the boolean result of the dicethrow if pirates boardded the ship
     * @param transactions, the record of items the pirates stole from the player
     * @param goodsSatisfy, boolean indicating if the goods were enough for the pirate, you lose game if false
     */
    public void encounterPirates(int diceThrow, boolean boardShip, ArrayList<PricedItem> transactions, boolean goodsSatisfy);
    
    /**
     * Sets an view as the current viewIsland, so some ui's that want to view as another island can without
     * passing the island into the ui
     * 
     * @param viewIsland, the island to view the current gui as
     */
    public void setViewIsland(Island viewIsland);
    
    /**
     * Sets the current island we are viewing as
     * 
     * @return the current viewIsland
     */	
    public Island getViewIsland();    
  
}