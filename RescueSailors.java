package main;

/**
 * Class to model if a user encounters sailors to be rescued during a sailing
 */
public class RescueSailors implements RandomEvent {
	
	// Probability the event will be triggered during a sailing
	private int probability;	
	
	/**
	 * Creates a new RescueSailors instance
	 * @param probability, the probability sailors will be rescued
	 */		
	public RescueSailors(int probability) {
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
		// Calculate Random sailors rescued, upto twice the number of crew 
		int numRescuedSailors = game.getRandomInt(game.getPlayer().getShip().getNumberOfCrew()*2)+1;

		// Calc random reward per sailor 1-10$
		int rewardPerSailor = game.getRandomInt(10)+1;
		int reward = numRescuedSailors * rewardPerSailor; 
		// This was missing until I made a test #winning
		game.getPlayer().setBalance(game.getPlayer().getBalance() + reward);
		
		//Create a transaction record for the rescue funds
		PricedItem rescueRecord = new PricedItem(new Item("Sailor Rescue", "No Description", 0, ItemType.RESCUE), reward, PriceType.REWARD, game.getWorld().getCurrentIsland());
		game.getPlayer().addTransaction(rescueRecord);			
		
		// Call the rescueSailors UI method
		game.getUI().rescueSailors(numRescuedSailors, rescueRecord);				
	}

	/**
	 * Method to describe how likely and what the impact will be of the event
	 * @return description of the event
	 */	
	@Override
	public String riskDescription() {
		if (getProbability() >= 90) {
			return "I think you will get lucky and save sailors with a reward";
		} else if (getProbability() >= 25) {
			return "You might meet sailors and get a reward for their rescue";
		} else {
			return "You won't meet sailors this trip";
		}
	}
	
}
