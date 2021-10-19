package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a store on an island, a store has a name and a list of items that can be bought and sold
 */
public class Store {
	
	// List of items the store sells
	private ArrayList<PricedItem> toSell;
	
	// List of items the store buys
	private ArrayList<PricedItem> toBuy;
	
	// Name of the store
	private String name;

	/**
	 * Creates a new store
	 * @param name, the name of the store
	 */
	public Store(String name) {
		this.name = name;
		this.toSell = new ArrayList<PricedItem>();
		this.toBuy = new ArrayList<PricedItem>();
	}

	/**
	 * @return the name of the store
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the toSell list
	 */
	public List<PricedItem> getToSellList() {
		return Collections.unmodifiableList(toSell);
	}
	
	/**
	 * Add an item to the stores list of things to sell, eg if the store sells to the player
	 * @param item, the PricedItem to be added to the stores list of things to sell
	 */	
	public void addToSell(PricedItem item) {
		toSell.add(item);		
	}
	
	/**
	 * Remove an item to the stores list of things to sell, eg if a player buys something 
	 * @param item, the PricedItem to be removed from the stores list of things to sell
	 */		
	private void removeFromSell(PricedItem item) {
		toSell.remove(item);		
	}	
	
	/**
	 * @return the List of PricedItems the store is willing to buy 
	 */
	public List<PricedItem> getToBuyList() {
		return Collections.unmodifiableList(toBuy);
	}
	
	/**
	 * Add an item to the stores list of things to buy
	 * @param item, the PricedItem to be added to the stores list of things to buy
	 */	
	public void addToBuy(PricedItem item) {
		toBuy.add(item);		
	}	
	
	/**
	 * Remove an item to the stores list of things to buy, eg if a player sells to the store 
	 * @param item, the PricedItem to be removed from the stores list of things to buy
	 */		
	private void removeFromBuy(PricedItem item) {
		toBuy.remove(item);		
	}			
	
	/**
	 * Method called when the store is selling an item
	 * 
	 * @param item, the item that is being sold by the store
	 */	
	public void sellItem(PricedItem item) {
		removeFromSell(item);
	}	
	
	/**
	 * Method called when the store is buying an item
	 * 
	 * @param item, the item that is being bought by the store
	 */		
	public void buyItem(PricedItem item) {
		removeFromBuy(item);
		// Ugly hard coding, store of that name doesn't sell anything
		if (this.name != "Hotel California")
			addToSell(new PricedItem(item.getItem(), item.getPrice(), PriceType.FORSALE, item.getIsland()));
	}		
	
	/**
	 * @return a string representation of the store, the name
	 */	
	@Override
	public String toString() {		
		return getName();
	}	
	
}