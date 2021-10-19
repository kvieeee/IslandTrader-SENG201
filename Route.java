package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * This class represents a route from an island to another. It is undirected
 */ 
public class Route {	
	
	// The length of the route
	private int distance;
	
	// one of the islands attached to the route
	private Island island1;
	
	// The other island attached to the route
	private Island island2;
	
	// List of random events that might happen while sailing the route
	private ArrayList<RandomEvent> events;
	
	// The world the route is part of
	private World world;

	/**
	 * Creates a new route
	 * @param distance - the length of the route
	 * @param island1 - one of the islands attached to the route
	 * @param island2 - one of the islands attached to the route
	 * @param world - the world the island is attached to
	 */
	public Route(int distance, Island island1, Island island2, World world) {
		this.distance = distance;
		this.island1 = island1;
		this.island2 = island2;
		this.events = new ArrayList<RandomEvent>();
		this.world = world;
	}
	
	/**
	 * Adds a random event to the route
	 * 
	 * @param randomEvent a randomEvent a user could encounter on the route
	 */	
	public void addEvent(RandomEvent randomEvent) {
		events.add(randomEvent);
	}

	/**
	 * Get the length of the route. Note this is not sailing time, sailing time is determined by
	 * route distance and the ship's sailing speed
	 * 
	 * @return the route distance
	 */
	public int getDistance() {
		return distance;
	}
	
	/**
	 * @return island1, one of the islands attached to the route
	 */
	public Island getIsland1() {
		return island1;
	}
	
	/**
	 * @return island2, one of the islands attached to the route
	 */
	public Island getIsland2() {	
		return island2;
	}
	
	/**
	 * @return the List of Events that might happen as you sail this route 
	 */
	public List<RandomEvent> getEvents() {
		return Collections.unmodifiableList(events);
	}	
	
	/**
	 * Get a description of the route
	 * @return String description of the route
	 */		
	private String description() {
		String output = "";	
		if (getIsland1() == this.world.getCurrentIsland())
			output = output + getIsland1() + " to " + getIsland2();	
		else
			output = output + getIsland2() + " to " + getIsland1();
		for (RandomEvent event : events) {
			output = output + "\n  " +event.riskDescription();
		}
		return output;
	}
	
	/**
	 * @return string description of the route, including its safety 
	 */	
	@Override
	public String toString() {
		return description();
	}
	
	/**
	 * This method gets the otherIsland linked to a route is we know one, 
	 * helps us to treat the routes as undirected
	 * 
	 * @param island, the island we know
	 * @return the other island attached to a route given one island that we don't know
	 */
	public Island otherIsland(Island island) {
		if (getIsland1() == island)
			return getIsland2();
		else
			return getIsland1();
	}
		
}