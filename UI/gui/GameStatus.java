package ui.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JTextArea;

import main.IslandTrader;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class represents the screen after the user clicked the "View Game Status" button in Main Menu
 * @author kvie
 *
 */
public class GameStatus extends Screen {
	/**
	 * Create the application.
	 * @param islandTrader, the game manager 
	 */
	public GameStatus(IslandTrader islandTrader) {
		super("Game Status", islandTrader);
	}
	
	/**
	 * Initialize the contents of the frame, which include:
	 * Some labels to let the user know their current balance, days remaining and game score
	 * Button to back to main menu
     * @param frame, the frame to add content too
	 */
	protected void initialise(final JFrame frame) {
		frame.getContentPane().setBackground(new Color(173, 216, 230));
		frame.getContentPane().setLayout(null);		
		
		// Set up introduction for the screen
		JTextArea txtrHelloTrader = new JTextArea("Hello " +getManager().getPlayer() +"! How are you doing? \n");
		txtrHelloTrader.setForeground(Color.WHITE);
		txtrHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		txtrHelloTrader.setBackground(new Color(70, 130, 180));
		txtrHelloTrader.setBounds(19, 36, 450, 37);
		frame.getContentPane().add(txtrHelloTrader);
		
		JTextArea txtGameStatus = new JTextArea("Here is the game status:");
		txtGameStatus.setForeground(Color.WHITE);
		txtGameStatus.setFont(new Font("iCiel Brush Up", Font.PLAIN, 22));
		txtGameStatus.setBackground(new Color(70, 130, 180));
		txtGameStatus.setBounds(19, 85, 450, 37);
		frame.getContentPane().add(txtGameStatus);		
		
		JLabel lblBalanceRemaining = new JLabel("You currently have " + getManager().getPlayer().getBalance() + " dollars.");
		lblBalanceRemaining.setBackground(new Color(70, 130, 180));
		lblBalanceRemaining.setForeground(new Color(0, 0, 128));
		lblBalanceRemaining.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblBalanceRemaining.setBounds(149, 149, 317, 37);
		frame.getContentPane().add(lblBalanceRemaining);
		
		// Get the captain image
		JLabel seacaptain = new JLabel("");
		seacaptain.setIcon(new ImageIcon(GameStatus.class.getResource("/seacaptain.png")));
		seacaptain.setBounds(19, 317, 235, 231);
		frame.getContentPane().add(seacaptain);
		
		JButton btnBackToMainMenu = new JButton("Back to main menu");
		btnBackToMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new MainScreen(islandTrader);
		    	screen.show();
			}
		});
		btnBackToMainMenu.setBounds(311, 395, 174, 68);
		frame.getContentPane().add(btnBackToMainMenu);		
		
		// Get the profit value, days remaining and game score
		int[] profitvalue = getManager().getPlayer().getProfitValue();
		JLabel lblProfit = new JLabel("You made " + profitvalue[0] + " dollars profit and have " + profitvalue[1] + " dollars of cargo");
		lblProfit.setForeground(new Color(0, 0, 128));
		lblProfit.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblProfit.setBackground(new Color(70, 130, 180));
		lblProfit.setBounds(149, 198, 544, 37);
		frame.getContentPane().add(lblProfit);
		
		int dayRemaining = getManager().getGameLength() - getManager().getTime();
		JLabel lblDaysRemaining = new JLabel("You are on day " + getManager().getTime() + " of " + getManager().getGameLength() + ". " + dayRemaining + " days left.");
		lblDaysRemaining.setForeground(new Color(0, 0, 128));
		lblDaysRemaining.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblDaysRemaining.setBackground(new Color(70, 130, 180));
		lblDaysRemaining.setBounds(149, 246, 518, 37);
		frame.getContentPane().add(lblDaysRemaining);
		
		JLabel lblGameScore = new JLabel("Your score is: " + getManager().gameScore());
		lblGameScore.setForeground(new Color(0, 0, 128));
		lblGameScore.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblGameScore.setBackground(new Color(70, 130, 180));
		lblGameScore.setBounds(199, 295, 309, 37);
		frame.getContentPane().add(lblGameScore);
	}
}
