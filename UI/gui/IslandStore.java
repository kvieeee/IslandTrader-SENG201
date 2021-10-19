package ui.gui;

import javax.swing.JFrame;

import java.awt.Color;
import javax.swing.JTextArea;
import main.FailureState;
import main.IslandTrader;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * This class represents the screen after the user clicked the "View Island Store" button in Main Menu
 * @author kvie
 *
 */
public class IslandStore extends Screen {

	/**
	 * Create the application.
 	 * @param islandTrader, the game manager
	 */
	public IslandStore(IslandTrader islandTrader) {
		super("Island Store", islandTrader);
	}
	
	/**
	 * Initialize the contents of the frame, which includes:
 	 * Some labels to let the user know they are at the store
 	 * Buttons for the user to choose what they want to do next
     * @param frame, the frame to add content too
 	 */
	@Override
	protected void initialise(final JFrame frame) {
		frame.getContentPane().setBackground(new Color(184, 134, 11));
		frame.getContentPane().setLayout(null);	
		
		// Introduce the screen
		JTextArea txtrStoreWelcome = new JTextArea("Welcome to the Island's store");
		txtrStoreWelcome.setForeground(Color.WHITE);
		txtrStoreWelcome.setFont(new Font("Holiday Sun", Font.PLAIN, 24));
		txtrStoreWelcome.setBackground(new Color(184, 134, 11));
		txtrStoreWelcome.setBounds(19, 19, 350, 40);
		frame.getContentPane().add(txtrStoreWelcome);
		
		JTextArea lblHelloTrader = new JTextArea("Hello " +getManager().getPlayer() +"! How's your adventure going so far? \n\nThis is the Island's store, where you can buy awesome items and sell your cool items. \n\n\n\n");
		lblHelloTrader.setLineWrap(true);
		lblHelloTrader.setForeground(Color.WHITE);
		lblHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 17));
		lblHelloTrader.setBackground(new Color(128, 0, 0));
		lblHelloTrader.setBounds(19, 62, 752, 85);
		frame.getContentPane().add(lblHelloTrader);
		
		// Get store images
		JLabel store = new JLabel("");
		store.setIcon(new ImageIcon(IslandStore.class.getResource("/STORE1.png")));
		store.setBounds(449, 320, 336, 228);
		frame.getContentPane().add(store);
		
		JLabel eatingburger = new JLabel("");
		eatingburger.setIcon(new ImageIcon(IslandStore.class.getResource("/1eatingBURGER.png")));
		eatingburger.setBounds(531, 159, 79, 180);
		frame.getContentPane().add(eatingburger);
		
		JLabel groceryperson = new JLabel("");
		groceryperson.setIcon(new ImageIcon(IslandStore.class.getResource("/groceryPERSONpng.png")));
		groceryperson.setBounds(608, 206, 121, 133);
		frame.getContentPane().add(groceryperson);
		
		JButton btnSeeWhatSells = new JButton("See what we have for sale");
		btnSeeWhatSells.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new IslandSellsItem(islandTrader);
				screen.show();
			}
		});
		btnSeeWhatSells.setBounds(37, 193, 189, 73);
		frame.getContentPane().add(btnSeeWhatSells);
		
		JButton btnSeeWhatBuy = new JButton("See what we are buying");
		btnSeeWhatBuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new IslandBuysItem(islandTrader);
				screen.show();
			}
		});
		btnSeeWhatBuy.setBounds(248, 193, 189, 73);
		frame.getContentPane().add(btnSeeWhatBuy);
		
		JButton btnViewPastPurchases = new JButton("View past purchases and sales");
		btnViewPastPurchases.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new PastPurchases(islandTrader);
				screen.show();
			}
		});
		btnViewPastPurchases.setBounds(116, 278, 215, 85);
		frame.getContentPane().add(btnViewPastPurchases);
		
		// Button to repair ship if have any damage
		JButton btnRepairShip = new JButton("Repair your ship (" +getManager().getPlayer().getShip().getDamageAmount() +" damage)");
		btnRepairShip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		    	int damage = getManager().getPlayer().getShip().getDamageAmount();
		    	int repairCost = getManager().getPlayer().getShip().getDamageAmount();
		    	if (damage > 0) {
		    		if (getManager().validateRepair(getManager().getPlayer().getShip()) == FailureState.NOMONEY)
		    			JOptionPane.showMessageDialog(null, "You have " +repairCost +" worth of damage, but only have " +getManager().getPlayer().getBalance() +"dollars. Trade to get more money");
		    		else {
		    			getManager().repairShip();
		    			JOptionPane.showMessageDialog(null, "Repaired " +damage +" damage to your ship, costing " +repairCost +" dollars");
		    		}
		    	} else {
		    		JOptionPane.showMessageDialog(null, "You have no damage to your ship");
		    	}					

			}
		});
		
		btnRepairShip.setBounds(116, 375, 215, 73);
		frame.getContentPane().add(btnRepairShip);
		
		// Button to back to main menu
		JButton btnBackToMain = new JButton("Back to main menu");
		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new MainScreen(islandTrader);
				screen.show();
			}
		});
		btnBackToMain.setBounds(116, 466, 215, 73);
		frame.getContentPane().add(btnBackToMain);

	}
}
