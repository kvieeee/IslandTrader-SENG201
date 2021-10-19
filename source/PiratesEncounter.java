package main;

import java.util.ArrayList;

/**
 * Class to model if a user encounters pirates during sailing
 */
public class PiratesEncounter implements RandomEvent {
	
	// Probability the event will be triggered during a sailing
	private int probability;	
	
	/**
	 * Creates a new PiratesEncouter instance
	 * @param probability, the probability pirates will be encountered
	 */	
	public PiratesEncounter(int probability) {
		this.probability = probability;
	}
	
	/**
	 * @return the probability of the event being triggered during sailing
	 */		
	@Override
	public int getProbability() {
		return this.probability;
	}

	/**
	 * Method triggered if the event happens during sailing
	 * @param game, the IslandTrader object 
	 */			
	@Override
	public void eventTriggered(IslandTrader game) {
		Ship ship = game.getPlayer().getShip();
		boolean boardShip = true;
		boolean goodsSatisfy = true;
		ArrayList<PricedItem> transactions = new ArrayList<PricedItem>();
		
		// Randomize do pirates board ship //number game
		int diceThrow = game.getRandomInt(6)+1;
		if (ship.hasWeapons() == true && diceThrow > 2) // 2/3 chance of stopping boarding
			boardShip = false; 
		else if (ship.hasWeapons() == false && diceThrow > 4) // 1/3 chance of stopping boarding
			boardShip = false;	
		
		if (boardShip) {		
			//lose goods
			ArrayList<Item> stolenCargo = game.getPlayer().getShip().removeCargo();
			
			// Create transaction record for the stolen goods & count the value of the goods
			// Value is just the size for now, since we aren't saving purchase price attached to the item
			int stolenValue = 0; 
			PricedItem stolenRecord;
			for (Item cargo : stolenCargo) {
				stolenValue = stolenValue + cargo.getSize();
				stolenRecord = new PricedItem(cargo, 0, PriceType.STOLEN, game.getWorld().getCurrentIsland());
				game.getPlayer().addTransaction(stolenRecord);
				transactions.add(stolenRecord);
			}
			
			// do the goods satisfy the pirates, 50/50 chance
			goodsSatisfy = (game.getRandomInt(2) > 0);
			if (goodsSatisfy == false)
				game.getPlayer().setBalance(0);
		}
		
		// Call the UI, with the result, which will pretend to play the dice game
		game.getUI().encounterPirates(diceThrow, boardShip, transactions, goodsSatisfy);
		
	}	
	
	/**
	 * Method to describe how likely and what the impact will be of the event
	 * @return description of the event
	 */		
	@Override
	public String riskDescription() {
		if (getProbability() >= 90) {
			return "You will meet pirates, and they might take everything from you";
		} else if (getProbability() >= 50) {
			return "Pirates are likely, watch out";
		} else if (getProbability() >= 25) {
			return "You might get lucky and not meet pirates";
		} else {
			return "There are probably no pirates this route";
		}
	}	
	
}	