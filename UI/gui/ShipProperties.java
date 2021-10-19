package ui.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import main.IslandTrader;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class represents the screen after the user clicked the "View Ship Properties" button in Main Menu
 * @author kvie
 *
 */

public class ShipProperties extends Screen {

	/**
	 * Create the application.
  	 * @param islandTrader, the game manager 
	 */
	public ShipProperties(IslandTrader islandTrader) {
		super("Ship Properties", islandTrader);
	}
 	
	/**
	 * Initialize the contents of the frame, which includes:
	 * Some labels for the introduction of the screen
	 * The properties of the ship
     * @param frame, the frame to add content too
	 */
	@Override
	protected void initialise(final JFrame frame) {
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.getContentPane().setLayout(null);
		
		JLabel shipImage = new JLabel("");
		shipImage.setIcon(new ImageIcon(ShipProperties.class.getResource("/01piratestatus.png")));
		shipImage.setBounds(603, 401, 176, 169);
		frame.getContentPane().add(shipImage);
		
		JTextArea lblHelloTrader = new JTextArea("Hello " +getManager().getPlayer() +"! How's your adventure going so far? \nDid you find any cool items from the island's store?\n\nHere is your ship properties: \n");
		lblHelloTrader.setLineWrap(true);
		lblHelloTrader.setForeground(new Color(255, 255, 255));
		lblHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 22));
		lblHelloTrader.setBackground(new Color(70, 130, 180));
		lblHelloTrader.setBounds(32, 27, 700, 127);
		frame.getContentPane().add(lblHelloTrader);
		
		// Button to back to main screen
		JButton btnNewButton = new JButton("Back to main menu");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new MainScreen(islandTrader);
		    	screen.show();						
			}
		});
		btnNewButton.setBounds(250, 460, 151, 61);
		frame.getContentPane().add(btnNewButton);
		
		// Get the information of the ship
		JTextArea lblProfit = new JTextArea(getManager().getPlayer().getShip().description());
		lblProfit.setLineWrap(true);
		lblProfit.setForeground(new Color(0, 0, 128));
		lblProfit.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblProfit.setBackground(new Color(135, 206, 250));
		lblProfit.setBounds(32, 166, 732, 266);
		frame.getContentPane().add(lblProfit);

	}
}
