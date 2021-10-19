package main;

/**
 * Interface for classes that represent a random event a player might face while sailing a route
 *
 */
public interface RandomEvent {

	/**
	 * Method for getting the probability of an event being triggered 
	 * 
	 * @return probability of event happening during the route
	 */
	public int getProbability();	

	/**
	 * Method called if the event is triggered, actions the random event 
	 * @param game, the IslandTrader object
	 */
	public void eventTriggered(IslandTrader game);

	/**
	 * Method to describe riskyness of the event 
	 * 
	 * @return String, a friendly name for how likely the event is to happen
	 */	
	public String riskDescription();
}
