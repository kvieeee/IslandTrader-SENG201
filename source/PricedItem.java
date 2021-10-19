package main;

/**
 * Represent a {@link Item} linked to a price and an {@link PriceType} type and the
 * island the Item was transacted on
 */
public class PricedItem {

	//The Item the record represents
	private Item item;
	
	//The price of the transaction
	private int price;
	
	//The transaction type
	private PriceType priceType;
	
	//The Island the Item was transacted on
	private Island island;

	/**
	 * Creates a new PricedItem
	 * @param item, the Item the record represents
	 * @param price, the price of the transaction
	 * @param type, the transaction type
	 * @param island, the island the transaction happened on 
	 */
	public PricedItem(Item item, int price, PriceType type, Island island) {
		this.item = item;
		this.price = price;
		this.priceType = type;
		this.island = island;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the type
	 */
	public PriceType getType() {
		return priceType;
	}
	
	/**
	 * @return the island
	 */
	public Island getIsland() {
		return island;
	}	

	/**
	 * @return a string representation of the Item
	 */
	@Override
	public String toString() {
        switch (this.priceType) {
    		case FORSALE:	
    			return item +", is for sale for $" +price;
    		case FORBUY:
    			return item +", we will buy for $" +price;
    		case PURCHASED:
    			return item +", was purchased for $" +price +" at " +getIsland();
    		case SOLD:
    			return item +", was sold for $" +price +" at " +getIsland();
    		case REWARD:
    			return item.getName() +" Reward of $" +price +", was given onroute to " +getIsland();
    		case STOLEN:
    			return item.getName() +" was stolen by pirates onroute to " +getIsland();    			
	        default:
	            return item +", is $" +price;
        }
	}

}
