/**
 * 
 */
package ui.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import main.IslandTrader;

/**
 * This class represents the screen after the player sail to another island
 */
public class SailingScreen extends Screen {
	
	private JTextArea headerHelloTrader; 
	private JTextArea routeDetail;

	/**
	 * Create the application.
  	 * @param islandTrader, the game manager 
	 */
	public SailingScreen(IslandTrader islandTrader) {
		super("", islandTrader);
	}
 	
	/**
	 * Initialize the contents of the frame, which include:
	 * JLabel to introduce the screen
	 * Button to back to main menu
     * @param frame, the frame to add content too
	 */
	@Override
	protected void initialise(final JFrame frame) {
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.getContentPane().setLayout(null);	
		
		// Introduce the screen
		headerHelloTrader = new JTextArea("Hello trader! Every day is a new adventure.\nLet's set sailing to another island! Lots of interesting things are waiting for us!");
		headerHelloTrader.setLineWrap(true);
		headerHelloTrader.setForeground(new Color(0, 0, 128));
		headerHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		headerHelloTrader.setBackground(new Color(135, 206, 250));
		headerHelloTrader.setBounds(32, 32, 701, 105);
		frame.getContentPane().add(headerHelloTrader);
		
		// Button to back to main menu
		JButton btnBackToMainMenu = new JButton("Back to main menu");
		btnBackToMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new MainScreen(islandTrader);
		    	screen.show();						
			}
		});
		btnBackToMainMenu.setBounds(293, 460, 151, 61);
		frame.getContentPane().add(btnBackToMainMenu);
		
		routeDetail = new JTextArea("");
		routeDetail.setLineWrap(true);	
		routeDetail.setForeground(new Color(255, 255, 255));
		routeDetail.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		routeDetail.setBackground(new Color(70, 130, 180));		
		routeDetail.setBounds(32, 166, 732, 266);
		frame.getContentPane().add(routeDetail);

	}
	
	/**
	 * @return the header JTextArea so we can add text to it from outside this frame
	 */
	public JTextArea getHeaderTextArea() {
		return this.headerHelloTrader;
	}
	
	/**
	 * @return the header JTextArea so we can add text to it from outside this frame
	 */
	public JTextArea getDetailTextArea() {
		return this.routeDetail;
	}	
}
