package ui.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import main.Island;
import main.IslandTrader;
import main.PricedItem;

import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This class represents the screen after the user clicked the "See What We Sells" button in View Island Properties Screen 
 *
 */
public class ViewIslandSellsItem extends Screen {
	
	/**
  	 * @param islandTrader, the game manager 
	*/
	public ViewIslandSellsItem(IslandTrader islandTrader) {
		super(islandTrader.getUI().getViewIsland().getName()+" buys the following items", islandTrader);		
	}
	
	/**
	 * Initialize the contents of the frame, which include:
 	 * list of items for the user to view
 	 * a "Back To Island Properties" button to go back to Island Properties screen
     * @param frame, the frame to add content too
 	 */	
	@Override
	protected void initialise(final JFrame frame) {
		frame.getContentPane().setBackground(new Color(47, 79, 79));
		frame.getContentPane().setLayout(null);
		
		// Introduce the screen
		JTextArea lblHelloTrader = new JTextArea("Hello " +getManager().getPlayer() +"!\nHave you experienced some cool things in this island?\n\nHere are items that this island sells: (* you can buy)");
		lblHelloTrader.setLineWrap(true);
		lblHelloTrader.setForeground(new Color(255, 255, 255));
		lblHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 22));
		lblHelloTrader.setBackground(new Color(47, 79, 79));
		lblHelloTrader.setBounds(38, 40, 653, 104);
		frame.getContentPane().add(lblHelloTrader);
		
		// Create the scroll pane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 172, 498, 333);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
		// Create a ListModel to store the items in the JList
		DefaultListModel<String> sellListModel = new DefaultListModel<String>();
		
		// Add the existing items to the List Model
		refreshList(sellListModel);		
		
		// Create the JList
		JList<String> sellItemList = new JList<String>(sellListModel);
		sellItemList.setLayoutOrientation(JList.VERTICAL); //must be vertical
		sellItemList.setFont(new Font("Lucida Grande", Font.PLAIN, 19));
		sellItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sellItemList.setBounds(0, 0, 498, 333);
		
		sellItemList.setVisibleRowCount(7);
		sellItemList.getSelectedValue();
		
		//Add the scroll pane
		scrollPane.setViewportView(sellItemList);
		frame.getContentPane().add(scrollPane);	
		
		
		// Button to back to main menu
		JButton btnBackToIslandProperties = new JButton("Back to Island Properties");
		btnBackToIslandProperties.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		btnBackToIslandProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new IslandProperties(islandTrader);
				screen.show();
			}

		});
		btnBackToIslandProperties.setBounds(577, 296, 187, 67);
		frame.getContentPane().add(btnBackToIslandProperties);
		
		// Get images for the store
		JLabel lblStoreImage = new JLabel("");
		lblStoreImage.setIcon(new ImageIcon(ViewIslandSellsItem.class.getResource("/storeIMAGE.png")));
		lblStoreImage.setBounds(603, 415, 176, 133);
		frame.getContentPane().add(lblStoreImage);
		
		JLabel storePeople = new JLabel("");
		storePeople.setIcon(new ImageIcon(ViewIslandSellsItem.class.getResource("/storePEOPLE.png")));
		storePeople.setBounds(603, 210, 117, 92);
		frame.getContentPane().add(storePeople);
	}
	
	/**
	 * Refreshes the list of items after a successful sale

	 */	
	private void refreshList(DefaultListModel<String> sellListModel) {
		Island viewIsland = islandTrader.getUI().getViewIsland();	
		sellListModel.removeAllElements();
		List<PricedItem> itemList = viewIsland.getStore().getToSellList();
		ArrayList<String> itemListStrings = ((Gui)islandTrader.getUI()).stringList(itemList, true);
		sellListModel.addAll(itemListStrings);
	}	
}



