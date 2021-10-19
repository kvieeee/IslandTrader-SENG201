/**
 * 
 */
package test.main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.FailureState;
import main.Island;
import main.IslandTrader;
import main.Item;
import main.PiratesEncounter;
import main.Player;
import main.PriceType;
import main.PricedItem;
import main.RandomEvent;
import main.RescueSailors;
import main.Route;
import main.StorageList;
import main.Store;
import main.UnfortunateWeather;
import ui.cmd.MainCmdUI;

/**
 * @author bmalthi
 *
 */
class IslandTraderTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void setupTest() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Test buying an item
        assertTrue(islandTrader.getPlayer().getBalance() == 100);
        assertTrue(islandTrader.getPlayer().getShip() == islandTrader.getWorld().getShips().get(0)); 
	}
	@Test
	void purchaseTest() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Test buying an item
        Item firstInList = islandTrader.getWorld().getCurrentIsland().getStore().getToSellList().get(0).getItem();
        islandTrader.buyStoreItem(0);
        assertTrue(islandTrader.getPlayer().getShip().hasItem(firstInList));
	}
	
	@Test
	void saleTest() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Test buying an item
        Item firstInList = islandTrader.getWorld().getCurrentIsland().getStore().getToBuyList().get(0).getItem();
        islandTrader.getPlayer().getShip().getStorageBays().add(new StorageList("Test", 10, firstInList.getType()));
        islandTrader.getPlayer().getShip().addItem(firstInList);
        assertTrue(islandTrader.getPlayer().getShip().hasItem(firstInList));
        islandTrader.sellStoreItem(0);
        assertFalse(islandTrader.getPlayer().getShip().hasItem(firstInList));
	}	
	
	@Test
	void gameOverTest() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Test is game is over
        islandTrader.buyStoreItem(0);
        assertTrue(islandTrader.isGameOver() == FailureState.SUCCESS);
        
        //Remove all money and goods and retest
        islandTrader.getPlayer().setBalance(0);
        Item stolen = islandTrader.getPlayer().getShip().removeCargo().get(0);
        islandTrader.getPlayer().addTransaction(new PricedItem(stolen, 0, PriceType.STOLEN, islandTrader.getWorld().getCurrentIsland())); 
        assertTrue(islandTrader.isGameOver() == FailureState.GAMEOVER_HARD);
	}	
	
	@Test
	void sailTest() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Create route and sail it, should end up at Ben Island
        Island endIsland = new Island("ben", new Store("ben"));
        Route testRoute = new Route(10, islandTrader.getWorld().getCurrentIsland(), endIsland, islandTrader.getWorld());
        
        int startTime = islandTrader.getTime();
        int startBalance = islandTrader.getPlayer().getBalance();
        //No transactions yet
        assertTrue(islandTrader.getPlayer().getTransactions().size() == 0);        
        islandTrader.getWorld().addRouteIndZero(testRoute);
        islandTrader.sailRoute(0);
        int endTime = islandTrader.getTime();
        int endBalance = islandTrader.getPlayer().getBalance();
        
        //Time should have passed
        assertTrue(endTime > startTime);
        
        // Crew wages shoudl be deducted with a transaction record        
        assertTrue(endBalance < startBalance);
        assertTrue(islandTrader.getPlayer().getTransactions().size() == 1);
        
        //we shoud have moved islands
        assertTrue(islandTrader.getWorld().getCurrentIsland() == endIsland);
	}	
	
	@Test
	void sailTestBadWeather() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Create Bad Weather
        RandomEvent weather = new UnfortunateWeather(100);
        int startDamage = islandTrader.getPlayer().getShip().getDamageAmount();
        islandTrader.triggerRandomSailingEvent(weather);
        int endDamage = islandTrader.getPlayer().getShip().getDamageAmount();
        assertTrue(endDamage > startDamage);
        
        // Create Bad Weather - zero prop of happening
        weather = new UnfortunateWeather(0);
        startDamage = islandTrader.getPlayer().getShip().getDamageAmount();
        islandTrader.triggerRandomSailingEvent(weather);
        endDamage = islandTrader.getPlayer().getShip().getDamageAmount();
        assertTrue(endDamage == startDamage);        
	}	
	
	@Test
	void sailTestSailors() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Create Sailors
        RandomEvent sailors = new RescueSailors(100);
        int startBalance = islandTrader.getPlayer().getBalance();
        islandTrader.triggerRandomSailingEvent(sailors);
        int endBalance = islandTrader.getPlayer().getBalance();
        assertTrue(endBalance > startBalance);
        
        // Create Sailors - zero prop of happening
        sailors = new UnfortunateWeather(0);
        startBalance = islandTrader.getPlayer().getBalance();
        islandTrader.triggerRandomSailingEvent(sailors);
        endBalance = islandTrader.getPlayer().getBalance();
        assertTrue(endBalance == startBalance);      
	}
	
	@Test
	void sailTestPirates() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Create Bad Weather
        // Flakey test because #probability, but no fear, we will do it 5 times 1/3^4 is small ~1%
        
        //Start with 1 item & lots of money
        PricedItem firstInList = islandTrader.getWorld().getCurrentIsland().getStore().getToSellList().get(0);
        islandTrader.buyStoreItem(0);
        int startBalance = 1000; 
        islandTrader.getPlayer().setBalance(startBalance);
        assertTrue(islandTrader.getPlayer().getShip().hasItem(firstInList.getItem()));
        
         
        RandomEvent pirates1 = new PiratesEncounter(100);
        islandTrader.triggerRandomSailingEvent(pirates1);
        RandomEvent pirates2 = new PiratesEncounter(100);
        islandTrader.triggerRandomSailingEvent(pirates2);
        RandomEvent pirates3 = new PiratesEncounter(100);
        islandTrader.triggerRandomSailingEvent(pirates3);
        RandomEvent pirates4 = new PiratesEncounter(100);
        islandTrader.triggerRandomSailingEvent(pirates4);
        RandomEvent pirates5 = new PiratesEncounter(100);
        islandTrader.triggerRandomSailingEvent(pirates5); 
        RandomEvent pirates6 = new PiratesEncounter(100);
        islandTrader.triggerRandomSailingEvent(pirates6); 
        RandomEvent pirates7 = new PiratesEncounter(100);
        islandTrader.triggerRandomSailingEvent(pirates7); 
        RandomEvent pirates8 = new PiratesEncounter(100);
        islandTrader.triggerRandomSailingEvent(pirates8);         
        int endBalance = islandTrader.getPlayer().getBalance();
        assertTrue(endBalance < startBalance);
        assertFalse(islandTrader.getPlayer().getShip().hasItem(firstInList.getItem()));
        
	}	
	
	@Test
	void repairShipTest() {
		//Setup
		MainCmdUI ui = new MainCmdUI();
        IslandTrader islandTrader = new IslandTrader(ui);
        ui.setManager(islandTrader);
        islandTrader.setPlayer(new Player("TestDummy"));
        islandTrader.selectShip(0);
        islandTrader.setGameLength(20);	
        
        // Create Bad Weather
        RandomEvent weather = new UnfortunateWeather(100);
        int startDamage = islandTrader.getPlayer().getShip().getDamageAmount();
        int startBalance = islandTrader.getPlayer().getBalance();
        islandTrader.triggerRandomSailingEvent(weather);
        int midDamage = islandTrader.getPlayer().getShip().getDamageAmount();
        assertTrue(midDamage > startDamage);
        
        //repair
        islandTrader.repairShip();
        int endBalance = islandTrader.getPlayer().getBalance();
        assertTrue(endBalance < startBalance);
        int endDamage = islandTrader.getPlayer().getShip().getDamageAmount();
        assertTrue(endDamage < midDamage);
        assertTrue(endDamage == startDamage);        
	}	

}
