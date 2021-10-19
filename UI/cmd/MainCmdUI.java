package ui.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import main.FailureState;
import main.Island;
import main.IslandTrader;
import main.Player;
import main.PricedItem;
import main.RandomEvent;
import main.Route;
import main.Ship;
import main.Store;
import ui.IslandTraderUI;

/**
 * CMD Line UI for IslandTrader, implements the IslandTraderUI Interface
 */
public class MainCmdUI implements IslandTraderUI {

    // The scanner used to read input from the console
    private final Scanner scanner;

    // The islandtrader this ui interacts with
    private IslandTrader islandTrader;

    // The island currently being viewed in this gui
    private Island viewIsland;
    
	/**
	 * SubCMDUI to get a validated Play Name Choice
	 */    
	private class PlayerNameInput extends Option {	
		
		private PlayerNameInput(MainCmdUI ui) {
			super(ui, Player.NAME_REGEX);			
		}
		
		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("Please choose a trader name:\n(between 3-15 characters)");
		}		
		
	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			ui.getManager().setPlayer(new Player(option));			
			setMenuFinish();
		}
	
	}
	
	/**
	 * SubCMDUI to get a validated Game Length input from the user
	 */  
	private class GameLengthInput extends Option {	
		
		public GameLengthInput(MainCmdUI ui) {
			super(ui, IslandTrader.GAME_LENGTH_REGEX);
		}
		
		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("How many days do you want to play for?\n(between 20-50 days)");
		}				
		
	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);
			ui.getManager().setGameLength(intOption);
			setMenuFinish();
		}		
	}
	
	/**
	 * SubCMDUI to get a validated Ship choice from the user, 4 predefined ships are offered
	 */ 
	private class ShipChoiceInput extends Option {
		
		public ShipChoiceInput(MainCmdUI ui) {
			super(ui, IslandTrader.SHIP_REGEX);
		
		}
		
		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("Which Ship do you want to choose? ");
		}
		
	    /**
		 * Print the list of indexed options to choose from to the user
		 */			
		@Override
		protected void printOptions() {
			List<Ship> ships = ui.getManager().getWorld().getShips();
			
			for (int i=0; i < ships.size(); i++) {
				System.out.println("(" + (i+1) + ") " + ships.get(i).description());
			}
		}
		
	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);
			ui.getManager().selectShip(intOption-1);
			setMenuFinish();
		}
	}
	
	/**
	 * Class for Main Menu, presents the user options
	 * Loops until valid input is received
	 * Automatically adds an exit option with '-1' as the exit 
	 */ 
	private class MainMenu extends ListOption {		
				
		public MainMenu(MainCmdUI ui) {
			super(ui);
	    	
	    	//Set up Options
	    	String[] base_options = {	    	
				"Money & days remaining",
				"Ship properties",
				"Past purchases & sales",
				"Island properties",
				"Visit the island store",
				"Sail to another island"}; 
	        
	    	this.options = new ArrayList<String>(Arrays.asList(base_options));
	    	String exitOption = "*** Quit Game ***";
	    	this.options.add(0, exitOption);
		}
		
		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("You are at " + ui.getCurrentIsland() +"\nWhat do you want to do next?\n");
			if (getManager().isGameOver() == FailureState.GAMEOVER_SOFT) {
				System.out.println("*** You have no time / money to sail anywhere but you can trade ***");
			}
			
		}	

	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
	    protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);
	        switch (intOption) {
	        	case -1: //"Quit"
	        		ui.getManager().onFinish();
	        		break;  
	            case 1: //"Money & days remaining
	            	gameStatus();
	                break;
	            case 2: //"Ship status"
	            	shipProperties();
	                break;
	            case 3: //"View purchases"
	            	purchasesList();
	                break;
	            case 4: //"View island properties"
	            	ui.islandMenu.getUserOption(ui.scanner);
	                break;	                
	            case 5: //"Visit the island store"
	            	ui.storeMenu.getUserOption(ui.scanner);            	
	                break;      
	            case 6: //"Sail to another island"
	                ui.routeMenu.getUserOption(ui.scanner);	 	 
	                break;
	            default:
	                throw new IllegalStateException("Unexpected value: " + option);
	        }
	    }

	} 		
	
	/**
	 * Class for Store Menu, presents the user options they can do at the store
	 * Loops until valid input is received
	 * Automatically adds an exit option with '-1' as the exit 
	 */
	private class StoreMenu extends ListOption {		
		
		public StoreMenu(MainCmdUI ui) {
			super(ui);		    	
	    	
	    	//Set up Options
	    	String[] base_options = {	    	
				"See what we have for sale",
				"See what we are buying",
				"View your past purchases & sales",
				"Repair your ship"}; 
	    	
	    	this.options = new ArrayList<String>(Arrays.asList(base_options));
	    	String exitOption = "(leave store)";
	    	this.options.add(0, exitOption);
		}

		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("Welcome to "+ ui.getCurrentIsland().getStore() +" on " + ui.getCurrentIsland() +". How can we help?\n");
		}	
		
		/**
		 * Method to print a footer when finishing with menu
		 */			
		@Override
		public void oneFooter() {
			System.out.println("Thanks for shopping at "+ ui.getCurrentIsland().getStore() +".\n");
		}	
		
	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);
	        switch (intOption) {
		        case -1: //Exit the store menu
					setMenuFinish();
		            break;        
		        case 1: //User buys an item
		        	ui.buyMenu.getUserOption(ui.scanner);
		            break;
		        case 2: //User sells an item
		        	ui.sellMenu.getUserOption(ui.scanner);
		            break;
		        case 3: //Show the users's transaction list
		        	purchasesList();
		            break; 
		        case 4: // Repair the ship damage
		        	ui.repairShipInput.getUserOption(ui.scanner);
		            break; 		            
		        default:
		            throw new IllegalStateException("Unexpected value: " + option);
	        }		

		}

	}
	
	/**
	 * Class for Island Menu, presents the user option to choose island to view properties of
	 * Loops until valid input is received
	 * Automatically adds an exit option with '-1' as the exit 
	 */
	private class IslandMenu extends ListOption {		
		
		public IslandMenu(MainCmdUI ui) {
			super(ui);					
	    	this.options = stringList(ui.getManager().getWorld().getIslands(), false, false);
	    	String exitOption = "(go back)";
	    	this.options.add(0, exitOption);
		}

		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("Which Island do you want to know more about?");
		}			
		
	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);
			if (intOption == -1) {
				setMenuFinish();
			} else {
				ui.islandDetailMenu.setIsland(ui.getManager().getWorld().getIslands().get(intOption-1));
				ui.islandDetailMenu.getUserOption(ui.scanner);
			}
		}

	}

	/**
	 * Class for Island Detail Menu, lets the user see details about an island
	 * Loops until valid input is received
	 * Automatically adds an exit option with '-1' as the exit 
	 */
	private class IslandDetailMenu extends ListOption {		
		
		// The island this menu is currently based on
		private Island island;
		
		public IslandDetailMenu(MainCmdUI ui) {
			super(ui);		    	
	    	
	    	//Set up Options
	    	String[] base_options = {	    	
				"View routes to this island",
				"See what the island store is selling",
				"See what the island store is buying"}; 
	    	
	    	this.options = new ArrayList<String>(Arrays.asList(base_options));
	    	String exitOption = "(go back)";
	    	this.options.add(0, exitOption);
		}

		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("What do you want to know about " +island+"?");
		}			
		
	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);
	        switch (intOption) {
		        case -1: //"QUIT"
					setMenuFinish();
		            break;        
		        case 1: //"Routes"
		        	showRouteList(island);
		            break;
		        case 2: //"Selling"
		        	showBuyList(island.getStore());
		            break;
		        case 3: //"Buying"
		        	showSellList(island.getStore());
		            break;             
		        default:
		            throw new IllegalStateException("Unexpected value: " + option);
	        }		

		}
		
	    /**
		 * Menu takes an island as an input so the user can see what this island has
		 * @param island, the island to view properties about
		 */  		
		public void setIsland(Island island) {
			this.island = island;
		}

	}			
	
	/**
	 * Class for Store Buy Menu - lets the user buy an item at the curren store
	 * Loops until valid input is received
	 * Automatically adds an exit option with '-1' as the exit 
	 */
	private class BuyMenu extends ListOption {		
		
		public BuyMenu(MainCmdUI ui) {
			super(ui); 		  		    	
		}
		
		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("What do you want to buy?\n (* recommended for you)\n");
		}	
		
	    /**
		 * Print the list of indexed options to choose from to the user
		 */			
		@Override
		protected void printOptions() {
	    	this.options = stringList(ui.getCurrentIsland().getStore().getToSellList(), true, false); 
	    	String exitOption = "(back to store front)";
	    	this.options.add(0, exitOption);
			super.printOptions();
		}

	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);			
			if (intOption == -1) {
				setMenuFinish();
			} else { 
				ui.getManager().buyStoreItem(intOption-1);
			}	
		}

	}		

	/**
	 * Method to show items for sale at a store 
	 * 
	 * @param store, the store to view items at
	 */	
	private void showBuyList(Store store) {				
		System.out.println(store +" sells the following items?\n (* recommended for you)\n");
		
		ArrayList<String> options = stringList(store.getToSellList(), true, false);
		for (String option: options) {
			System.out.println(option);
		}
		if (options.size() == 0)
			System.out.println("We are not selling anything today");
		System.out.println("\n");
	}		 
	
	
	/**
	 * Class for Store Sell Menu - lets the user sell an item to a store
	 * Loops until valid input is received
	 * Automatically adds an exit option with '-1' as the exit 
	 */	
	private class SellMenu extends ListOption {
		
		public SellMenu(MainCmdUI ui) {
			super(ui);     	
		}

		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("What do you want to sell?\n (* recommended for you)\n");
		}		
		
	    /**
		 * Print the list of indexed options to choose from to the user
		 */			
		@Override
		protected void printOptions() {
	    	this.options = stringList(ui.getCurrentIsland().getStore().getToBuyList(), true, false);
	    	String exitOption = "(back to store front)";
	    	this.options.add(0, exitOption);
			super.printOptions();
		}		

	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);			
			if (intOption == -1) {
				setMenuFinish();
			} else {
				ui.getManager().sellStoreItem(intOption-1);
			}	
		}

	}	
	
	/**
	 * Method to show items for sale at a store 
	 * 
	 * @param store, the store to view items at 
	 */		
	private void showSellList(Store store) {			
		System.out.println(store +" buys the following items?\n (* recommended for you)\n");
		ArrayList<String> options = stringList(store.getToBuyList(), true, false);
		for (String option: options) {
			System.out.println(option);
		}
		if (store.getToBuyList().size() == 0)
			System.out.println("We are not buying anything today");		
		System.out.println("\n");
				
	}		
	
	/**
	 * Class for Route sailing menu - lets the user choose a sailing to a new island
	 * Loops until valid input is received
	 * Automatically adds an exit option with '-1' as the exit 
	 */		
	private class RouteMenu extends ListOption {		
		
		public RouteMenu(MainCmdUI ui) {
			super(ui); 		  		    	
		}
		
		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
			System.out.println("Where do you want to go?\n (* possible for you)\n");
			if (ui.getShip().getRepairCost() > 0) {
				System.out.println(" *** You can't sail anywhere until you repair your ship ***");
				System.out.println(" *** You can get it repaired at the Island store ***\n");
			}
		}				
		
	    /**
		 * Print the list of indexed options to choose from to the user
		 */			
		@Override
		protected void printOptions() {
			this.options = routeStringList(ui.getManager().getWorld().getRoutesFromCurrent(), true, false);
			String exitOption = "(back to main menu)";
			this.options.add(0, exitOption);
			super.printOptions();
		}

	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);			
			if (intOption == -1) {
				setMenuFinish();
			} else {
				//ui.sailRoute(intOption-1);				
				setMenuFinish();
				ui.getManager().sailRoute(intOption-1);				
			}	
		}

	}	
	
	/**
	 * Method to show routes to an island from current island 
	 * 
	 * @param store, the store to view items at 
	 */		
	private void showRouteList(Island island) {		
		
		System.out.println("Here are the routes available from your current island " +getCurrentIsland() + " to " + island);
		
		ArrayList<String> options = routeStringList(getManager().getWorld().getRoutes(getCurrentIsland(), island), true, false);
		for (String option: options) {
			System.out.println(option);
		}	
		System.out.println("(* recommended for you)\n");		
		System.out.println("\n");
	}		
	
	/**
	 * Class for Repair the ship, lets user repair ship or not
	 * Loops until valid input is received (ie 1 for yes to repair)
	 * Automatically adds an exit option with '-1' as the exit 
	 */	
	private class RepairShipInput extends ListOption {		
		
		public RepairShipInput(MainCmdUI ui) {
			super(ui);
		}
		
		/**
		 * Method to print a header every loop of menu
		 */			
		@Override
		protected void eachHeader() {
	    	int damage = ui.getShip().getDamageAmount();
	    	int repairCost = ui.getShip().getDamageAmount();
	    	if (damage > 0) {
	    		System.out.println("You have " +damage +" damage to your ship. This will cost " +repairCost +" dollars to repair.\n");
	    		if (ui.getManager().validateRepair(ui.getShip()) == FailureState.NOMONEY)
	    			System.out.println("However, you only have " +ui.getPlayer().getBalance() +". Trade to get more money");
	    	} else {
	    		System.out.println("You have no damage to your ship");
	    	}	
		}			
		
	    /**
		 * Print the list of indexed options to choose from to the user
		 */			
		@Override
		protected void printOptions() {
	    	this.options = new ArrayList<String>();
	    	int repairCost = ui.getShip().getDamageAmount();
	    	if (repairCost > 0 && ui.getManager().validateRepair(ui.getShip()) == FailureState.SUCCESS)
	    		this.options.add("Repair ship damage for " +repairCost);
	    	String exitOption = "(back to store front)";
	    	this.options.add(0, exitOption);
			super.printOptions();
		}			
		
	    /**
		 * Method to handle next steps once valid input has been received
		 * @param option, the regex validated string that the user entered
		 */  		
		@Override
		protected void handleOption(String option) {
			int intOption = Integer.parseInt(option);
			if (intOption == -1) {
				setMenuFinish();
			} else {
				ui.getManager().repairShip();
				System.out.println("Ship is repaired\n");
				setMenuFinish();
			}
		}

	}	
    
	// Instances of the various sub menus defined above
	private PlayerNameInput playerNameInput;
	private GameLengthInput gameLengthInput;
	private ShipChoiceInput shipChoiceInput;
	private MainMenu mainMenu;
	private StoreMenu storeMenu;	
	private BuyMenu buyMenu;
	private SellMenu sellMenu;
	private IslandMenu islandMenu;
	private IslandDetailMenu islandDetailMenu;
	private RouteMenu routeMenu;
	private RepairShipInput repairShipInput;
	
	/**
	 * Create a new CMD UI
	 * This is created by the game Manager (IslandTrader) which then calls the UI setup method
	 */
	public MainCmdUI() {
		scanner = new Scanner(System.in);
	}	

    /**
     * Initializes this UI and sets up the given IslandTrader, with the ships, islands, stores to be managed
     * Once setup is complete this UI must call {@link IslandTrader#onSetupFinished}.
     *
     * @param game, the {@link IslandTrader} game instance that this UI interacts with
     */	
	@Override
	public void setup(IslandTrader game) {
		// Set up game item
		this.islandTrader = game;
		
		// Set up player name input
		this.playerNameInput = new PlayerNameInput(this);
		
		// Set up game length input
		this.gameLengthInput = new GameLengthInput(this);
		
		// Set up ship choice input
		this.shipChoiceInput = new ShipChoiceInput(this);

		//Set up command menus
		this.mainMenu = new MainMenu(this);		
		this.storeMenu = new StoreMenu(this);		
		this.buyMenu = new BuyMenu(this);		
		this.sellMenu = new SellMenu(this);	
		this.islandMenu = new IslandMenu(this);
		this.islandDetailMenu = new IslandDetailMenu(this);
		this.routeMenu = new RouteMenu(this);
		this.repairShipInput = new RepairShipInput(this);
		
		this.setViewIsland(getCurrentIsland());
		// Start the game
		game.onSetupFinished();		
	}

    /**
     * Method used to start the game, where the user selects a ship, player name and game length
     * before triggering the main menu
     */		
	@Override
	public void start() {
		// Print Intro
		System.out.println("****************************************");
		System.out.println("Welcome to Island Trader V1.0");
		System.out.println("****************************************\n");		
		
		// Get the player name from the player 
		playerNameInput.getUserOption(scanner);
		System.out.println("Great name, " +getPlayer() +"\n");
		
		// Get the game length from the player
		gameLengthInput.getUserOption(scanner);
		System.out.println("Game will run for " +getManager().getGameLength() +" days\n");	
		
		// Get the ship choice
		shipChoiceInput.getUserOption(scanner);		
		System.out.println("Great choice, your ship is: " +getShip() +"\n");
		
		//Start the main menu
		mainMenu.getUserOption(this.scanner);						
	}	           

    /**
     * Confirms user wants to quit the game
     * @return true, this is dummy method for cmdline
     */
	@Override
	public boolean confirmQuit() {
		return true;
	}

    /**
     * Quits the game, showing the user their score and status
     */	
	@Override
	public void quit() {
		mainMenu.setMenuFinish();	
		System.out.println("****************************************");
		System.out.println("GAME OVER");
		System.out.println("****************************************\n");
		System.out.println(getPlayer());
		System.out.println("You played for " + getManager().getTime() +" days, out of " + getManager().getGameLength());
		System.out.println("You made " +getPlayer().getProfitValue()[0] +" dollars\n");
		System.out.println("Your score is:" +getManager().gameScore());
		System.out.println("\nThanks for playing");		
	}

    /**
     * Show the user an error message if what they were attempting to do failed
     */	
	@Override
	public void showError(String error) {
		System.out.println("!!!! " + error + " !!!!");
		
	}	
	
	/**
	 * Helper method to simplify code. Get the current island from the world class
	 * @return Island, the current island the user is on
	 */
	private Island getCurrentIsland() {
		return getManager().getWorld().getCurrentIsland();
	}
	
	/**
	 * Helper method to simplify code. Get the IslandManager
	 * @return IslandTrader, the game manager
	 */
	public IslandTrader getManager() {
		return this.islandTrader;
	}
	
	/**
	 * Helper method to allow easier testing. 
	 * @param islandTrader, the game manager to set
	 */
	public void setManager(IslandTrader islandTrader) {
		this.islandTrader = islandTrader;
	}	
	
	/**
	 * Helper method to simplify code. Get the player from the game
	 * @return Player, the player object
	 */
	public Player getPlayer() {
		return getManager().getPlayer();
	}	
	
	/**
	 * Helper method to simplify code. Get the ship the player has
	 * @return Ship, the ship the player is using
	 */
	private Ship getShip() {
		return getPlayer().getShip();
	}		
	
    /**
     * Show the user list of their purchases / transactions
     */	
	private void purchasesList() {
		System.out.println("****************************************");
		System.out.println("Here are all your purchases & sales.\n");
		System.out.println("You currently have " +getPlayer().getBalance() +" dollars.\n");
		
		ArrayList<String> options = stringList(getPlayer().getTransactions(), false, false);
		for (String option: options) {
			System.out.println(option);
		}		
		if(options.size() == 0) {
			System.out.println("You have no transactions yet\n");
		}	
		System.out.println("\n");
	}
	
    /**
     * Show the user properties of their ship. 2nd Main Menu Option
     */
	private void shipProperties() {
		System.out.println("****************************************");
		System.out.println("Ship Properties\n");
		System.out.println(getShip().description());
	}
	
    /**
     * Show the user the game status, their money, the time left, their score and profit
     */	
	private void gameStatus() {
		int[] profitvalue = getPlayer().getProfitValue();
		System.out.println("****************************************");
		System.out.println("Game Status\n");
		System.out.println("Hi " + getPlayer()+"\n");
		System.out.println("You currently have " +getPlayer().getBalance() +" dollars.");
		System.out.println("You made " +profitvalue[0] +" dollars profit and have "+profitvalue[1] +" dollars of cargo\n");
		System.out.println("You are on day " +getManager().getTime() +" of " +getManager().getGameLength() +". " +(getManager().getGameLength()-getManager().getTime()) +" days left.");
		System.out.println("Your score is:" +getManager().gameScore()+"\n");				
	}
	
	/**
	 * Helper method to take a list of objects and format them in list form to display to the user.
	 * Can add a 1 based counter and also can validate if this user can purchase / action the item
	 * @param list, the list of objects to convert into a string list (using toString method)
	 * @param validate, boolean do we want to validate the item and indicate to the user validation
	 * @param numbered, boolean should the output items have a 1 based index in front of them
	 * @return ArrayList<String> of the list input
	 */	
	private ArrayList<String> stringList(List<?> list, boolean validate, boolean numbered) {
		int counter = 1;
		String validPrefix;
		String numberPrefix;
		ArrayList<String> names = new ArrayList<String>();				
		for (Object obj : list) {
			// Add a prefix if the item is valid for the user
			if (validate && getManager().validate(obj) == FailureState.SUCCESS)
				validPrefix = "* ";
			else
				validPrefix = "";
			
			// Add a prefix if the item should be numbered in the list (1 based)
			if (numbered)
				numberPrefix = "(" +counter +") ";
			else
				numberPrefix = "";
			
			names.add(numberPrefix + validPrefix + obj.toString());
			counter++;
		}
		return names;
	}
	
	/**
	 * Helper method to take a list of Routes and format them in list form to display to the user.
	 * Can add a 1 based counter and also can validate if this user can purchase / action the item
	 * @param list, the list of objects to convert into a string list (using toString method)
	 * @param validate, boolean do we want to validate the item and indicate to the user validation
	 * @param numbered, boolean should the output items have a 1 based index in front of them 
	 * @return ArrayList<String> of the route list input 
	 */	
	private ArrayList<String> routeStringList(List<?> list, boolean validate, boolean numbered) {		
		ArrayList<String> routes = stringList(list, validate, numbered);
		String routeSuffix;
		Route route;
		for (int i = 0; i < routes.size(); i++) {
			route = (Route) list.get(i);
			// For each route add a suffix with more detail for the user
			routeSuffix = "\n  This route is " +route.getDistance() +"km. It will take you " +getShip().sailingDays(route) +" days\n"; 
			routes.set(i, routes.get(i) + routeSuffix);
		}
		return routes;		
	}	
	
	/**
	 * Show the user the details of the transaction, if successful
	 * @param transaction, the transaction we are showing the user
	 */	
	@Override
	public void processTransaction(PricedItem transaction) {
		System.out.println(transaction+"\n");		
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
		// GameStatus
		FailureState gameStatus = FailureState.SUCCESS;
		
		// Start the Journey
		System.out.println("*** Starting our journey ***");
		
		// Show the user the wages we paid
		this.processTransaction(wageRecord);
		
		// Call the game random event code
		for (RandomEvent event : route.getEvents()) {
			getManager().triggerRandomSailingEvent(event);
			gameStatus = getManager().isGameOver();
			if(gameStatus == FailureState.GAMEOVER_HARD)
				break;
		}
		
		if (gameStatus == FailureState.GAMEOVER_HARD) {
			getManager().setGameOver();
		} else if (gameStatus == FailureState.GAMEOVER_SOFT) {
			System.out.println("You made it, but you have no money or time left to go anywhere");
			System.out.println("You can trade if you want, else quit the game\n");
		} else {
			// Assume we made the next island (for now)
			System.out.println("Congrats on your journey, you made it\n");			
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
		System.out.println("*** You encountered bad weather ***");
		try { Thread.sleep(2000); } catch (InterruptedException e) {/*Doesn't matter}*/}
		System.out.println("Unfortunately the weather caused " +damage +" damage.");
		System.out.println("It will cost " +repairCost +" to repair\n");
		if (repairValidation == FailureState.NOMONEY)
			System.out.println("This is more money than you have you will have to trade before you can sail again\n");
	}
	
    /**
     * Reports details to the user of encounter with sailors who are rescued
     *
     * @param numRescuedSailors, the random number of sailors rescued, depends on ship size
     * @param rewardRecord, each sailor gives a random reward, this is the total
     */
    public void rescueSailors(int numRescuedSailors, PricedItem rewardRecord) {
    	System.out.println("*** You encountered sailors in distress ***");
    	try { Thread.sleep(2000); } catch (InterruptedException e) {/*Doesn't matter}*/}
    	System.out.println("There are " +numRescuedSailors +" sailors, who are very greatful for their rescue");
    	
		// Show the user their reward
		this.processTransaction(rewardRecord);    	
    }	
    
    /**
     * Reports details to the user of encounter with pirates
     * 
     * @param diceThrow, the random number that the user got to determine success when fighting the pirates
     * @param boardShip, the boolean result of the dicethrow if pirates boardded the ship
     * @param transactions, the record of items the pirates stole from the player
     * @param goodsSatisfy, boolean indicating if the goods were enough for the pirate, you lose game if false 
     */
    public void encounterPirates(int diceThrow, boolean boardShip, ArrayList<PricedItem> transactions, boolean goodsSatisfy) {
    	System.out.println("*** !!! You encountered Pirates !!! ***");
    	System.out.println("*** !!! They are trying to board your ship !!! ***\n");
    	
    	// Show dice game, result is predetermined
    	System.out.println("You must roll a die to stop them");
    	if (getShip().hasWeapons())
    		System.out.println("Because you have weapons you have to roll a 3 or above\n");    		
    	else
    		System.out.println("You have no weapons to fight them off, you must roll 5 or 6\n");    		
    	
    	// Pause for 2 seconds
    	try { Thread.sleep(2000); } catch (InterruptedException e) {/*Doesn't matter}*/}    
    	
    	// Show result of dice game
		if (diceThrow > 2 && getShip().hasWeapons())
			System.out.println("You rolled a " +diceThrow +" you fend off the pirates.\n");
		else if (diceThrow > 4)
			System.out.println("You rolled a " +diceThrow +" you fend off the pirates.\n");		
		else
			System.out.println("You rolled a " +diceThrow +" the pirates board your ship.\n");    	
    	
		//Board the ship, if we lost the dice game
    	if (boardShip) {    		
    		//Steal the goods
    		System.out.println("The pirates now steal all of your goods\n");
    		for (PricedItem transaction : transactions) {    			
    			//Show the user what was stolen
    			this.processTransaction(transaction);
    		}
    		
    		// Pause for 2 seconds
    		try { Thread.sleep(2000); } catch (InterruptedException e) {/*Doesn't matter}*/}
    		
    		// Does this satisfy them
    		if (goodsSatisfy) {
    			System.out.println("The pirates are happy with your cargo. You live another day\n");
    		} else {
    			System.out.println("Unfortunately that wasn't enough for them\n");
    			System.out.println("The pirates take everything and you are forced to walk the plank");
    			System.out.println("Hope you can swim\n");
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
     * Sets the current island we are viewing as
     * 
     * @return the current viewIsland
     */
	@Override	
    public Island getViewIsland() {
    	return this.viewIsland;
    }      
}
