package ui.gui;

//import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.IslandTrader;
import main.Player;
import main.Ship;
import java.awt.Font;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class represents the Setup Screen of the game
 * @author kvie
 *
 */
public class SetupScreen extends Screen {

	private JTextField txtbetweenCharacters;
	private JSlider slider;
	
	public static final String NAME_REGEX = "^[a-z A-Z]{3,15}$";
	
 
	/**
	 * Create the Setup Screen
	 * 
	 * @param islandTrader the island trader to configure
	 */
	protected SetupScreen(IslandTrader islandTrader) {
		super("Island Trader Setup", islandTrader);
	
	}

	/**
	 * Initialize the contents of the frame, which includes:
 	 * Set label
 	 * Get the lists of the ship + ships's detail
 	 * Show images of the ship
 	 * Get the user choice of ship - set ship based on the choice
 	 * Add button to move to Main Screen
     * @param frame, the frame to add content too
	 */	
	@Override
	protected void initialise(final JFrame frame) {
		ButtonGroup buttonGroup  = new ButtonGroup();
		JTextArea shipDetailText = new JTextArea(getManager().getWorld().getShips().get(0).details());
		frame.getContentPane().setBackground(new Color(70, 130, 180));
		frame.getContentPane().setLayout(null);
        frame.setBounds(100, 100, 785, 630);		
		
		// Get the list of ships for each radio button
		List<Ship> ships = getManager().getWorld().getShips();
		
		// Get ships for each radio button and add them to a button Group
		JRadioButton rdbtnShip1 = new JRadioButton(ships.get(0).getName());
		rdbtnShip1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnShip1.isSelected())
					shipDetailText.setText(getManager().getWorld().getShips().get(0).details());
			}
		});
		rdbtnShip1.setForeground(new Color(0, 0, 0));	
		buttonGroup.add(rdbtnShip1);
		rdbtnShip1.setSelected(true);
		rdbtnShip1.setBounds(40, 483, 141, 23);
		frame.getContentPane().add(rdbtnShip1);
		
		// Get ships for each radio button and add them to a button Group
		JRadioButton rdbtnShip2 = new JRadioButton(ships.get(1).getName());
		rdbtnShip2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnShip2.isSelected())
					shipDetailText.setText(getManager().getWorld().getShips().get(1).details());				
			}
		});		
		buttonGroup.add(rdbtnShip2);
		rdbtnShip2.setBounds(221, 483, 141, 23);
		frame.getContentPane().add(rdbtnShip2);

		// Get ships for each radio button and add them to a button Group
		JRadioButton rdbtnShip3 = new JRadioButton(ships.get(2).getName());
		rdbtnShip3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnShip3.isSelected())
					shipDetailText.setText(getManager().getWorld().getShips().get(2).details());				
			}
		});		
		buttonGroup.add(rdbtnShip3);
		rdbtnShip3.setBounds(385, 483, 141, 23);
		frame.getContentPane().add(rdbtnShip3);
		
		// Get ships for each radio button and add them to a button Group
		JRadioButton rdbtnShip4 = new JRadioButton(ships.get(3).getName());
		rdbtnShip4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnShip4.isSelected())
					shipDetailText.setText(getManager().getWorld().getShips().get(3).details());				
			}
		});			
		buttonGroup.add(rdbtnShip4);
		rdbtnShip4.setBounds(563, 483, 141, 23);
		frame.getContentPane().add(rdbtnShip4);
		

		//This button should only be able to click this if valid things are selected
		JButton btnLetsPlay = new JButton("Let's Play");
		btnLetsPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Set the player
				getManager().setPlayer(new Player(txtbetweenCharacters.getText()));
				
				//Set the game length
				getManager().setGameLength(slider.getValue());
				
				// Set the ship 
				if (rdbtnShip1.isSelected()) {
					getManager().getPlayer().setShip(getManager().getWorld().getShips().get(0));		
				} else if (rdbtnShip2.isSelected()) {
					getManager().getPlayer().setShip(getManager().getWorld().getShips().get(1));		
				} else if (rdbtnShip3.isSelected()) {
					getManager().getPlayer().setShip(getManager().getWorld().getShips().get(2));		
				} else if (rdbtnShip4.isSelected()) {
					getManager().getPlayer().setShip(getManager().getWorld().getShips().get(3));		
				} 

				//Start the game
				quit();
				getManager().onSetupFinished();
			}
		});
		btnLetsPlay.setBackground(new Color(25, 25, 112));
		btnLetsPlay.setEnabled(false);
		btnLetsPlay.setForeground(new Color(255, 255, 255));
		btnLetsPlay.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		btnLetsPlay.setBounds(647, 539, 126, 42);
		btnLetsPlay.setOpaque(true);
		frame.getContentPane().add(btnLetsPlay);		
		
		// Set up some labels for the game
		JLabel lblConfirmName = new JLabel("");
		lblConfirmName.setForeground(new Color(255, 0, 0));
		lblConfirmName.setBounds(249, 192, 392, 16);
		frame.getContentPane().add(lblConfirmName);
		
		JTextArea lblChooseName = new JTextArea("1) Choose a Trader Name");
		lblChooseName.setForeground(new Color(255, 255, 255));
		lblChooseName.setFont(new Font("SignPainter", Font.PLAIN, 22));
		lblChooseName.setBackground(new Color(25, 25, 112));
		lblChooseName.setBounds(40, 157, 202, 30);
		frame.getContentPane().add(lblChooseName);
		
		JLabel lblDaysChosen = new JLabel("50 Days");
		lblDaysChosen.setBackground(new Color(0, 0, 139));
		lblDaysChosen.setForeground(new Color(0, 0, 139));
		lblDaysChosen.setBounds(109, 255, 61, 16);
		frame.getContentPane().add(lblDaysChosen);
		
		JTextArea lblGameLength = new JTextArea("2) Decide on game length");
		lblGameLength.setForeground(new Color(255, 255, 255));
		lblGameLength.setFont(new Font("SignPainter", Font.PLAIN, 22));
		lblGameLength.setBackground(new Color(25, 25, 112));
		lblGameLength.setBounds(35, 218, 200, 31);
		frame.getContentPane().add(lblGameLength);
		
		JTextArea lblHelloTrader = new JTextArea("Welcome to Island Trader");
		lblHelloTrader.setForeground(new Color(255, 255, 255));
		lblHelloTrader.setFont(new Font("Holiday Sun", Font.PLAIN, 24));
		lblHelloTrader.setBackground(new Color(70, 130, 180));
		lblHelloTrader.setBounds(35, 21, 639, 31);
		frame.getContentPane().add(lblHelloTrader);
		
		JTextArea lblAimOfTheGame = new JTextArea("The aim of the game is to travel between islands, trading goods for profit. You will encounter tricky traders, and trecherous stormy routes with pirates on your quest.");
		lblAimOfTheGame.setForeground(new Color(255, 255, 255));
		lblAimOfTheGame.setFont(new Font("Devanagari MT", Font.PLAIN, 17));
		lblAimOfTheGame.setLineWrap(true);
		lblAimOfTheGame.setBackground(new Color(0, 0, 102));
		lblAimOfTheGame.setBounds(35, 58, 608, 52);
		frame.getContentPane().add(lblAimOfTheGame);
		
		txtbetweenCharacters = new JTextField();
		txtbetweenCharacters.setHorizontalAlignment(SwingConstants.CENTER);
		txtbetweenCharacters.getDocument().addDocumentListener(new DocumentListener() {
		    @Override public void changedUpdate(DocumentEvent e) { updateNameField(); }
		    @Override public void insertUpdate(DocumentEvent e) { updateNameField(); }
		    @Override public void removeUpdate(DocumentEvent e) { updateNameField(); }
		    private void updateNameField() {
		    	if (txtbetweenCharacters.hasFocus() == true) {
		    		if (txtbetweenCharacters.getText().matches(NAME_REGEX)) {
		    			lblConfirmName.setText("Great Name, " + txtbetweenCharacters.getText());
		    			lblConfirmName.setForeground(new Color(0, 102, 0));		    			
		    			btnLetsPlay.setEnabled(true);
		    			btnLetsPlay.setBackground(new Color(0, 102, 0));
		    			btnLetsPlay.setForeground(new Color(0, 102, 0));
		    		} else {
		    			lblConfirmName.setText("Name should be between 3-15 characters");
		    			lblConfirmName.setForeground(Color.RED);		    			
		    			btnLetsPlay.setEnabled(false);
		    			btnLetsPlay.setBackground(Color.RED);
		    			btnLetsPlay.setForeground(Color.RED);
		    		}
		    	}
		    }//
		});
		
		txtbetweenCharacters.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if ("(between 3-15 characters)".equals(txtbetweenCharacters.getText())) {
					txtbetweenCharacters.setText("");
					lblConfirmName.setText("(between 3-15 characters)");
				}
			}
		});
		
		txtbetweenCharacters.setForeground(SystemColor.inactiveCaption);
		txtbetweenCharacters.setText("(between 3-15 characters)");
		txtbetweenCharacters.setBounds(249, 157, 392, 34);
		frame.getContentPane().add(txtbetweenCharacters);
		txtbetweenCharacters.setColumns(10);
		
		JTextArea lblLetsStart = new JTextArea("Let's get started!!!");
		lblLetsStart.setForeground(new Color(255, 255, 255));
		lblLetsStart.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblLetsStart.setBackground(new Color(70, 130, 180));
		lblLetsStart.setBounds(35, 116, 172, 38);
		frame.getContentPane().add(lblLetsStart);
		
		// Create a slider for the game length
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JSlider slider = (JSlider) evt.getSource();
				lblDaysChosen.setText(String.valueOf(slider.getValue()));				
			}
		});
		slider.setBorder(new CompoundBorder());
		slider.setMinimum(20);
		slider.setMaximum(50);
		slider.setBounds(246, 221, 392, 29);
		frame.getContentPane().add(slider);
		
		JLabel lbl20Days = new JLabel("20 Days");
		lbl20Days.setForeground(new Color(255, 255, 255));
		lbl20Days.setBounds(245, 239, 61, 16);
		frame.getContentPane().add(lbl20Days);
		
		JLabel lbl50Days = new JLabel("50 Days");
		lbl50Days.setForeground(new Color(255, 255, 255));
		lbl50Days.setBounds(601, 239, 61, 16);
		frame.getContentPane().add(lbl50Days);
		
		JTextArea lblShipChoice = new JTextArea("3) Choose a Ship for your Quest");
		lblShipChoice.setForeground(new Color(255, 255, 255));
		lblShipChoice.setFont(new Font("SignPainter", Font.PLAIN, 22));
		lblShipChoice.setBackground(new Color(25, 25, 112));
		lblShipChoice.setBounds(35, 287, 235, 31);
		frame.getContentPane().add(lblShipChoice);
		
		// Get the ship images
		JLabel ship1 = new JLabel("");
		ship1.setIcon(new ImageIcon(SetupScreen.class.getResource("/another3.png")));
		ship1.setBounds(40, 314, 147, 169);
		frame.getContentPane().add(ship1);
		
		JLabel ship2 = new JLabel("");
		ship2.setIcon(new ImageIcon(SetupScreen.class.getResource("/02.png")));
		ship2.setBounds(211, 324, 163, 159);
		frame.getContentPane().add(ship2);
		
		JLabel ship3 = new JLabel("");
		ship3.setHorizontalAlignment(SwingConstants.LEFT);
		ship3.setIcon(new ImageIcon(SetupScreen.class.getResource("/0001.png")));
		ship3.setBounds(351, 324, 163, 159);
		frame.getContentPane().add(ship3);
		
		JLabel ship4 = new JLabel("");
		ship4.setIcon(new ImageIcon(SetupScreen.class.getResource("/14.png")));
		ship4.setBounds(538, 324, 192, 159);
		frame.getContentPane().add(ship4);
		
		JLabel pirate = new JLabel("");
		pirate.setHorizontalAlignment(SwingConstants.CENTER);
		pirate.setBackground(new Color(70, 130, 180));
		pirate.setIcon(new ImageIcon(SetupScreen.class.getResource("/pirate1.png")));
		pirate.setBounds(647, 6, 147, 221);
		frame.getContentPane().add(pirate);
		
		// Get the ship detail
		shipDetailText.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		shipDetailText.setLineWrap(true);
		shipDetailText.setBackground(new Color(70, 130, 180));
		shipDetailText.setBounds(33, 512, 602, 110);
		frame.getContentPane().add(shipDetailText);
		
	}
   }
