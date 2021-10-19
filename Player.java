package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Player in the game. A player has money and transactions, 
 * a player has a ship
 *
 */
public class Player {

	//The Name of the player
	private String name;
	
	//The current balance of the the player	
	private int balance;
	
	//The transaction list of the player, everything they have bought / sold / upgraded
	private ArrayList<PricedItem> transactions;

	//The Ship the player is using for the game 
	private Ship ship;
	
	// The players starting balance of money
	private final int STARTING_BALANCE = 100;
	
	// Regex limiting the players name, has to be 3-15 letters or space
	public static final String NAME_REGEX = "^[a-z A-Z]{3,15}$";

	/**
	 * Create a player
	 * 
	 * @param name the name of the player
	 */
	public Player(String name) {
		this.name = name;
		this.balance = STARTING_BALANCE;
		this.transactions = new ArrayList<PricedItem>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the balance
	 */
	public int getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}

	/**
	 * Gets the transactions the player has made 
	 * @return the transactions
	 */
	public List<PricedItem> getTransactions() {
		return Collections.unmodifiableList(transactions);
	}		
	
	/**
	 * Adds a transaction to the list of the transactions the player has made
	 * 
	 * @param transaction, the PricedItem representing a new transaction
	 */
	public void addTransaction(PricedItem transaction) {
		transactions.add(transaction);
	}			

	/**
	 * @return the ship
	 */
	public Ship getShip() {
		return ship;
	}

	/**
	 * @param ship, the ship the player has chosen
	 */
	public void setShip(Ship ship) {
		this.ship = ship;
	}			
	
	/**
	 * Checks if the player has enough money to make a particular purchase
	 * 
	 * @param purchase, the PricedItem the player is wanting to purchase
	 * @return boolean indicating if the player has enough money for the purchase
	 */	
	public boolean hasMoney(PricedItem purchase) {
		return (getBalance() >= purchase.getPrice());
	}
	
	/**
	 * Gets the profit the user has made, change balance - starting balance
	 * How much extra cash the player has compared to when they started
	 * 
	 * @return The current profit of the player 
	 */		
	public int[] getProfitValue() {
		//This is a bit hacky to calc value of goods since I'm only storing items in cargo not their value
		ArrayList<PricedItem> purchases = new ArrayList<PricedItem>();
		ArrayList<PricedItem> sales = new ArrayList<PricedItem>();
		ArrayList<PricedItem> stolen = new ArrayList<PricedItem>();		
				
		for (PricedItem item : getTransactions()) {
			if (item.getItem().getType() == ItemType.CARGO) {
		        switch (item.getType()) {
		    		case PURCHASED:
		    			purchases.add(item);
		    			break;
		    		case SOLD:
		    			sales.add(item);
	    				break;
		    		case STOLEN:
		    			stolen.add(item);
		    			break;     			
			        default:
			        	break;
		        }				
			}
		}
		
		int profit = 0;
		int value = 0;
        try {
    		for (PricedItem sale : sales) {
    			for (int i = purchases.size()-1; i >= 0; i--) {
    				PricedItem purchase = purchases.get(i);
	    			if (purchase.getItem().equals(sale.getItem())) {
		    			profit = sale.getPrice() - purchase.getPrice();
		    			purchases.remove(purchase);
	    			}
    			}
    		}
    		for (PricedItem steal : stolen) {  
    			for (int j = purchases.size()-1; j >= 0; j--) {
    				PricedItem purchase = purchases.get(j);
	    			if (purchase.getItem().equals(steal.getItem())) {
		    			profit = 0 - purchase.getPrice();
		    			purchases.remove(purchase);
	    			}
    			}
    		}
    		for (PricedItem item : purchases) {      			
    			value = value + item.getPrice();
    		}    		
        } catch (Exception e) {
        	System.out.println("OPPS:\n" +e.getMessage());
        } 		
        		
		return (new int[]{profit, value});
	}		

	/**
	 * Method to action the purchase of an item by the player.
	 * It adds the item to the users cargo, it deducts money from the user
	 * and it adds it to the users list of transactions 
	 * 
	 * @param purchase, the PricedItem the player is purchasing
	 * @return transaction, the record of the transaction from player pov	 * 
	 */		
	public PricedItem buyItem(PricedItem purchase) {
		PricedItem transaction = new PricedItem(purchase.getItem(), purchase.getPrice(), PriceType.PURCHASED, purchase.getIsland());
		addTransaction(transaction);
		setBalance(getBalance() - purchase.getPrice());
		getShip().addItem(purchase.getItem());
		
		// if the item is an upgrade item, then upgrade the ship
		Item item = purchase.getItem();
		if (item.getType() ==  ItemType.UPGRADE) {
			((UpgradeItem)item).upgradeShip(getShip());
		}
		
		return transaction;
	}	
	
	/**
	 * Method to action the sale of an item by the player to a store
	 * It removes the item to the users cargo, it adds money to the user
	 * and it adds it to the users list of transactions 
	 * 
	 * @param sale, the PricedItem the player just sold
	 * @return transaction, the record of the transaction from player pov
	 */		
	public PricedItem sellItem(PricedItem sale) {
		PricedItem transaction = new PricedItem(sale.getItem(), sale.getPrice(), PriceType.SOLD, sale.getIsland());
		addTransaction(transaction);
		setBalance(getBalance() + sale.getPrice());
		getShip().removeItem(sale.getItem());	
		return transaction;		
	}
	
	/**
	 * Checks if the user has enough money to sail a route,
	 * given their ship and the length of the route, and repairs needed
	 * 
	 * @param route, the route the user wishes to sail
	 * @param includeCargo, boolean to indicate if we should include cargo value in hasMoney calculation	 * 
	 * @return boolean indicating true / false if they have enough money
	 */			
	public boolean hasMoney(Route route, boolean includeCargo) {
		int fundsNeeded = getShip().costOfRoute(route) + getShip().getRepairCost();		
		int fundsHave = getBalance();
		// This is a bit of a hack, its possible the local store won't buy these items
		// or the price the local store will pay is more / less
		if (includeCargo)
			fundsHave = fundsHave + getProfitValue()[1];
		return (fundsHave >= fundsNeeded);
	}
	
	/**
	 * Updates the players balance money to pay for the sailing
	 * 
	 * @param route, the route the user wishes to sail
	 * @return int, how much wages were deducted
	 */			
	public int deductRouteWages(Route route) {
		int wages = getShip().costOfRoute(route);
		setBalance(getBalance() - wages);
		return wages;
	}
	
	/**
	 * @return a string representation of the player, the name
	 */	
	@Override
	public String toString() {		
		return getName();
	}	
}
