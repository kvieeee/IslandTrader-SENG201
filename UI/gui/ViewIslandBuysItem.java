package ui.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

import main.Island;
import main.IslandTrader;
import main.PricedItem;

import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

/**
 * This class represents the screen after the user clicked the "See What We Buys" button in View Island Properties Screen
 * @author kvie
 *
 */
public class ViewIslandBuysItem extends Screen {
	
	/**
	* Create the application.
  	 * @param islandTrader, the game manager 
	*/
	public ViewIslandBuysItem(IslandTrader islandTrader) {
		super(islandTrader.getUI().getViewIsland().getName()+" buys the following items", islandTrader);
			
	}
	
	/**
	 * Initialize the contents of the frame, which include:
 	 * list of items for the user to view
 	 * a "Back To Island Properties" button to go back to Island Properties screen
     * @param frame, the frame to add content too
	 */
	@Override
	protected void initialise(final JFrame container) {
		container.getContentPane().setBackground(new Color(47, 79, 79));
		container.getContentPane().setLayout(null);
		
		// Introduce the screen
		JTextArea lblHelloTrader = new JTextArea("Hello " +getManager().getPlayer() +"!\nHave you experienced some cool things in this island?\n\nHere are items that this island buys: (* you can sell)");
		lblHelloTrader.setLineWrap(true);
		lblHelloTrader.setForeground(Color.WHITE);
		lblHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 22));
		lblHelloTrader.setBackground(new Color(47, 79, 79));
		lblHelloTrader.setBounds(38, 40, 653, 104);
		container.getContentPane().add(lblHelloTrader);
		
		// Create the scroll pane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 172, 498, 333);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Create a ListModel to store the items in the JList
		DefaultListModel<String> buyListModel = new DefaultListModel<String>();
		
		// Add the existing items to the List Model
		refreshList(buyListModel);
		
		// Create the JList
		JList<String> buyItemList = new JList<String>(buyListModel);
		
		buyItemList.setBorder(UIManager.getBorder("ScrollPane.border"));
		buyItemList.setLayoutOrientation(JList.VERTICAL); //must be vertical
		buyItemList.setFont(new Font("Lucida Grande", Font.PLAIN, 19));
		buyItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		buyItemList.setBounds(42, 172, 498, 333);
		
		//Add the scroll pane
		scrollPane.setViewportView(buyItemList);
		container.getContentPane().add(scrollPane);	
		
		// Get images of the store
		JLabel lblStoreImage = new JLabel("");
		lblStoreImage.setIcon(new ImageIcon(ViewIslandBuysItem.class.getResource("/storeIMAGE.png")));
		lblStoreImage.setBounds(603, 415, 176, 133);
		container.getContentPane().add(lblStoreImage);
		
		JLabel storePeople = new JLabel("");
		storePeople.setIcon(new ImageIcon(ViewIslandBuysItem.class.getResource("/storePEOPLE.png")));
		storePeople.setBounds(603, 210, 117, 92);
		container.getContentPane().add(storePeople);
		
		JButton btnIslandProperties = new JButton("Back to Island Properties");
		btnIslandProperties.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		btnIslandProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new IslandProperties(islandTrader);
				screen.show();
			}
		});
		btnIslandProperties.setBounds(577, 296, 187, 67);
		container.getContentPane().add(btnIslandProperties);
		
	}
	
	/**
	 * Refreshes the list of items after a successful purchase
	 * @param buyListModel, the UI list of items to be refreshed
	 */	
	private void refreshList(DefaultListModel<String> buyListModel) {
		Island viewIsland = islandTrader.getUI().getViewIsland();		
		buyListModel.removeAllElements();
		List<PricedItem> itemList = viewIsland.getStore().getToBuyList();
		ArrayList<String> itemListStrings = ((Gui)islandTrader.getUI()).stringList(itemList, true);
		buyListModel.addAll(itemListStrings);
	}	
}



