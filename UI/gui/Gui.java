	package ui.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import main.FailureState;
import main.Island;
import main.IslandTrader;
import main.PricedItem;
import main.RandomEvent;
import main.Route;
import ui.IslandTraderUI;

/**
 * A graphical user interface for a {@link IslandTrader}.
 */
public class Gui implements IslandTraderUI {

    // The game instance that this gui interacts with
	private IslandTrader islandTrader;

    // The currently active screen in this gui
    private Screen theScreen;
    
    // The island currently being viewed in this gui
    private Island viewIsland;

    /**
     * Initialises this UI and sets up the given IslandTrader, with the ships, islands, stores to be managed
     * Once setup is complete this UI must call {@link IslandTrader#onSetupFinished}.
     *
     * @param islandTrader, the islandTrader game instance that this UI interacts with
     */    
    @Override
    public void setup(IslandTrader islandTrader) {
        this.islandTrader = islandTrader;
        this.setViewIsland(this.islandTrader.getWorld().getCurrentIsland());
        theScreen = new SetupScreen(islandTrader);
        theScreen.show();
    }

    /**
     * Reports the given error to the user.
     *
     * @param error The error to display
     */    
    @Override
    public void showError(String error) {
        theScreen.showError(error);
    }

    /**
     * Starts this UI
     */    
    @Override
    public void start() {
        theScreen.quit();
        theScreen = new MainScreen(islandTrader);
        theScreen.show();
    }

    /**
     * Confirms user wants to quit the game
     * @return boolean indicating user intention
     */    
	@Override
    public boolean confirmQuit() {
    	return theScreen.confirmQuit();
    }

    /**
     * Quits the application.
     */	
    @Override
    public void quit() {
        theScreen.quit();
    }	
	
	/**
	 * Show the user the details of the transaction, if successful
	 * @param transaction,  the transaction we are showing details to the user about
	 */	
	@Override
	public void processTransaction(PricedItem transaction) {	
		JOptionPane.showMessageDialog(null, transaction.toString());
	}  
	
    /**
     * Show the user what happened on their sailing
     *
     * @param route, the route being sailed
     * @param wageRecord, the transaction record for our wage payment
     * @param sailingTime, days it took to sail the route
     */	
	@Override
	public void sailRoute(Route route, PricedItem wageRecord, int sailingTime) {
		// Set Up
		String title = "Sailing from " + getViewIsland() +" to " + route.otherIsland(getViewIsland());
        theScreen.quit();
        theScreen = new SailingScreen(islandTrader);
        theScreen.getFrame().setTitle(title);
		FailureState gameStatus = FailureState.SUCCESS;
		JTextArea detail = ((SailingScreen)theScreen).getDetailTextArea();
        
		// Show the UI / Start the Journey
        ((SailingScreen)theScreen).getHeaderTextArea().setText("Hello trader! Lets start our adventure to " + route.otherIsland(getViewIsland()));
        theScreen.show();       
		
		// Show the user the wages we paid
        detail.setText(wageRecord.toString()+"\n");
		
		// Call the game random event code
		for (RandomEvent event : route.getEvents()) {
			this.islandTrader.triggerRandomSailingEvent(event);
			gameStatus = this.islandTrader.isGameOver();
			if(gameStatus == FailureState.GAMEOVER_HARD)
				break;
		}
		
		if (gameStatus == FailureState.GAMEOVER_HARD) {
			this.islandTrader.setGameOver();
		} else if (gameStatus == FailureState.GAMEOVER_SOFT) {
			detail.setText(detail.getText() + "You made it, but you have no money or time left to go anywhere.\n");
			detail.setText(detail.getText() + "You can trade if you want, else quit the game\n");
		} else {
			// Assume we made the next island (for now)
			detail.setText(detail.getText() + "Congrats on your journey, you made it!\n");
		}        
        
	}
	
    /**
     * Show the user how bad weather impacted them on their sailing. If you hit bad weather it
     * damages your ship by 20% of its total endurance. This needs to be paid before another sailing
     *
     * @param damage, how much damage the weather caused
     * @param repairCost, the extra repair cost from the weather
     * @param repairValidation, indicates if the user can afford repair
     */
	@Override	
    public void encounterWeather(int damage, int repairCost, FailureState repairValidation) {
		JTextArea detail = ((SailingScreen)theScreen).getDetailTextArea();
		
		detail.setText(detail.getText() + "*** You encountered bad weather ***\n");
		//try { Thread.sleep(2000); } catch (InterruptedException e) {/*Doesn't matter}*/}
		detail.setText(detail.getText() + "Unfortunately the weather caused " + damage + " damage.");
		detail.setText(detail.getText() + " It will cost " + repairCost +" to repair.\n");
		if (repairValidation == FailureState.NOMONEY)
			detail.setText(detail.getText() + "This is more money than you have you will have to trade before you can sail again\n");
		
	}
	
    /**
     * Reports details to the user of encounter with sailors who are rescued
     *
     * @param numRescuedSailors, the random number of sailors rescued, depends on ship size
     * @param rewardRecord, each sailor gives a random reward, this is the total
     */
	@Override	
    public void rescueSailors(int numRescuedSailors, PricedItem rewardRecord) {
		JTextArea detail = ((SailingScreen)theScreen).getDetailTextArea();
		
		detail.setText(detail.getText() + "*** You encountered sailors in distress ***\n");
    	//try { Thread.sleep(2000); } catch (InterruptedException e) {/*Doesn't matter}*/}
    	detail.setText(detail.getText() + "There are " + numRescuedSailors + " sailors, who are very greatful for their rescue.");
    	
		// Show the user their reward 
		detail.setText(rewardRecord.toString() + "\n");
		
    }
    
    /**
     * Reports details to the user of encounter with pirates
     * 
     * @param diceThrow, the random number that the user got to determine success when fighting the pirates
     * @param boardShip, the boolean result of the dicethrow if pirates boardded the ship
     * @param transactions, the record of items the pirates stole from the player
     * @param goodsSatisfy, boolean indicating if the goods were enough for the pirate, you lose game if false
     */
	@Override	
    public void encounterPirates(int diceThrow, boolean boardShip, ArrayList<PricedItem> transactions, boolean goodsSatisfy) {
		JTextArea detail = ((SailingScreen)theScreen).getDetailTextArea();
		
		detail.setText(detail.getText() +"*** !!! You encountered Pirates !!! ***\n");
		detail.setText(detail.getText() +"*** !!! They are trying to board your ship !!! ***\n");
    	
    	// Show dice game, result is predetermined
		detail.setText(detail.getText() + "You must roll a die to stop them\n");
    	if (this.islandTrader.getPlayer().getShip().hasWeapons())
    		detail.setText(detail.getText() + " Because you have weapons you have to roll a 3 or above\n");    		
    	else
    		detail.setText(detail.getText() + "You have no weapons to fight them off, you must roll 5 or 6\n");    		
    	
    	// Pause for 2 seconds
    	//try { Thread.sleep(2000); } catch (InterruptedException e) {/*Doesn't matter}*/}    
    	
    	// Show result of dice game
		if (diceThrow > 2 && this.islandTrader.getPlayer().getShip().hasWeapons())
			detail.setText(detail.getText() + "You rolled a " + diceThrow +" you fend off the pirates.\n");
		else if (diceThrow > 4)
			detail.setText(detail.getText() + "You rolled a " + diceThrow +" you fend off the pirates.\n");		
		else
			detail.setText(detail.getText() + "You rolled a " + diceThrow +" the pirates board your ship.\n");    	
    	
		//Board the ship, if we lost the dice game
    	if (boardShip) {    		
    		//Steal the goods
    		detail.setText(detail.getText() + "The pirates now steal all of your goods\n");
    		for (PricedItem transaction : transactions) {    			
    			//Show the user what was stolen
    			detail.setText(detail.getText() + transaction.toString() + "\n");
    		}
    		
    		// Pause for 2 seconds
    		//try { Thread.sleep(2000); } catch (InterruptedException e) {/*Doesn't matter}*/}
    		
    		// Does this satisfy them
    		if (goodsSatisfy) {
    			detail.setText(detail.getText() + "The pirates are happy with your cargo. You live another day.\n");
    		} else {
    			detail.setText(detail.getText() + "Unfortunately that wasn't enough for them\n");
    			detail.setText(detail.getText() + " The pirates take everything and you are forced to walk the plank.");
    			detail.setText(detail.getText() + "Hope you can swim!\n");
    		}
    		
    	}
    }
    
    /**
     * Sets an view as the current viewIsland, so some ui's that want to view as another island can without
     * passing the island into the ui
     * 
     * @param viewIsland, the island to view the current gui as
     */
	@Override	
    public void setViewIsland(Island viewIsland) {
    	this.viewIsland = viewIsland;
    } 
    
    /**
     * Gets the current island we are viewing as
     * 
     * @return the current viewIsland
     */
	@Override	
    public Island getViewIsland() {
    	return this.viewIsland;
    } 
	
    /**
     * Sets an Screen as the current screen 
     * 
     * @param screen, the current screen to set
     */
    public void setScreen(Screen screen) {
    	this.theScreen = screen;
    } 
    
    /**
     * Gets the current screen the gui is on
     * 
     * @return the screen
     */
    public Screen getScreen() {
    	return this.theScreen;
    } 	
    
	/**
	 * Helper method to take a list of objects and format them in list form to display to the user.
	 * validate if this user can purchase / action the item
	 * @param list, the list of objects to convert into a string list (using toString method)
	 * @param validate, boolean do we want to validate the item and indicate to the user validation
	 * @return List of strings for the input list
	 */	
	public ArrayList<String> stringList(List<?> list, boolean validate) {
		String validPrefix;
		ArrayList<String> names = new ArrayList<String>();				
		for (Object obj : list) {
			// Add a prefix if the item is valid for the user
			if (validate && islandTrader.validate(obj) == FailureState.SUCCESS)
				validPrefix = "* ";
			else
				validPrefix = "";
			
			names.add(validPrefix + obj.toString());
		}
		return names;
	}    
	
	/**
	 * Helper method to take a list of Routes and format them in list form to display to the user.
	 * validate if this user can sail this route
	 * @param list, the list of objects to convert into a string list (using toString method)
	 * @param validate, boolean do we want to validate the item and indicate to the user validation 
	 * @return List of strings for the input list
	 */	
	public ArrayList<String> routeStringList(List<?> list, boolean validate) {		
		ArrayList<String> routes = stringList(list, validate);
		String routeSuffix;
		Route route;
		for (int i = 0; i < routes.size(); i++) {
			route = (Route) list.get(i);
			// For each route add a suffix with more detail for the user
			routeSuffix = "\n  This route is " +route.getDistance() +"km. It will take you " +islandTrader.getPlayer().getShip().sailingDays(route) +" days\n"; 
			routes.set(i, routes.get(i) + routeSuffix);
		}
		return routes;		
	}		
	
}