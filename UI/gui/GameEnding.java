package ui.gui;


import javax.swing.JFrame;

import main.IslandTrader;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.Font;

/**
 * This class represents the screen after the user clicked the "Quit Game" button and (confirm that they really want to quit the game) in Main Menu
 * @author kvie
 *
 */
public class GameEnding extends Screen {
	
	/**
	 * Create the application.
	 * @param islandTrader, the game manager
	 */
	public GameEnding(IslandTrader islandTrader) {
		super("Finish The Journey", islandTrader);
	}
 	
	/**
	 * Initialize the contents of the frame, which include: player's days played, profit made, score gained
     * @param frame, the frame to add content too
	 */
	@Override
	protected void initialise(final JFrame frame) {
		frame.getContentPane().setBackground(new Color(70, 130, 180));
		frame.getContentPane().setLayout(null);
		
		// Get the ship information
		JTextArea lblDaysPlayed = new JTextArea("You played for " + getManager().getTime() + " days, our of " + getManager().getGameLength());
		lblDaysPlayed.setLineWrap(true);
		lblDaysPlayed.setForeground(Color.WHITE);
		lblDaysPlayed.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblDaysPlayed.setBackground(new Color(30, 144, 255));
		lblDaysPlayed.setBounds(147, 171, 503, 47);
		frame.getContentPane().add(lblDaysPlayed);
		
		JTextArea lblProfitMade = new JTextArea("You made " + getManager().getPlayer().getProfitValue()[0] + " dollars");
		lblProfitMade.setLineWrap(true);
		lblProfitMade.setForeground(Color.WHITE);
		lblProfitMade.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblProfitMade.setBackground(new Color(30, 144, 255));
		lblProfitMade.setBounds(147, 239, 503, 47);
		frame.getContentPane().add(lblProfitMade);
		
		JTextArea lblScoreGained = new JTextArea("Your score is: " + getManager().gameScore());
		lblScoreGained.setLineWrap(true);
		lblScoreGained.setForeground(Color.WHITE);
		lblScoreGained.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblScoreGained.setBackground(new Color(30, 144, 255));
		lblScoreGained.setBounds(147, 298, 503, 47);
		frame.getContentPane().add(lblScoreGained);
		
		JTextArea txtrThankYou = new JTextArea("Thank you for playing!");
		txtrThankYou.setLineWrap(true);
		txtrThankYou.setForeground(Color.WHITE);
		txtrThankYou.setFont(new Font("iCiel Brush Up", Font.PLAIN, 23));
		txtrThankYou.setBackground(new Color(165, 42, 42));
		txtrThankYou.setBounds(240, 368, 280, 47);
		frame.getContentPane().add(txtrThankYou);
		
		JTextArea txtrGameStatus = new JTextArea("FINISH THE JOURNEY");
		txtrGameStatus.setLineWrap(true);
		txtrGameStatus.setForeground(Color.WHITE);
		txtrGameStatus.setFont(new Font("iCiel Brush Up", Font.PLAIN, 27));
		txtrGameStatus.setBackground(new Color(165, 42, 42));
		txtrGameStatus.setBounds(240, 112, 280, 47);
		frame.getContentPane().add(txtrGameStatus);
		frame.setBackground(new Color(47, 79, 79));

	}

}
