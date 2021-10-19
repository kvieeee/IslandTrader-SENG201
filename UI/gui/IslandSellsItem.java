package ui.gui;

import javax.swing.JFrame;

import main.IslandTrader;
import main.PricedItem;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class IslandSellsItem extends Screen {

	/**
	* Create the application.
	* @param islandTrader, the game manager
	*/
	public IslandSellsItem(IslandTrader islandTrader) {
		super(islandTrader.getWorld().getCurrentIsland()+" Sells the following items", islandTrader);		
	}
	
	/**
	 * Initialize the contents of the frame.
     * @param frame, the frame to add content too
	 */
	@Override
	protected void initialise(final JFrame frame) {
		frame.getContentPane().setBackground(new Color(47, 79, 79));
		frame.getContentPane().setLayout(null);
		
		JTextArea lblHelloTrader = new JTextArea("Hello " +getManager().getPlayer() +"!\nHave you experienced some cool things in this island?\n\nHere are items that this island sells: (* you can buy)");
		lblHelloTrader.setLineWrap(true);
		lblHelloTrader.setForeground(new Color(255, 255, 255));
		lblHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 22));
		lblHelloTrader.setBackground(new Color(47, 79, 79));
		lblHelloTrader.setBounds(38, 40, 653, 104);
		frame.getContentPane().add(lblHelloTrader);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(314, 237, 1, 16);
		frame.getContentPane().add(textPane);
		
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
		sellItemList.setBounds(0, 0, 498, 284);
		
		//Add scroll pane
		scrollPane.setViewportView(sellItemList);
		frame.getContentPane().add(scrollPane);	
		
		
		JButton btnSellItem = new JButton("Buy item");
		btnSellItem.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		btnSellItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getManager().buyStoreItem(sellItemList.getSelectedIndex());
				refreshList(sellListModel);
			}
		});
		btnSellItem.setBounds(585, 237, 143, 67);
		frame.getContentPane().add(btnSellItem);
		
		JButton btnBackToMain = new JButton("Back to store front");
		btnBackToMain.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new IslandStore(islandTrader);
				screen.show();
			}

		});
		btnBackToMain.setBounds(585, 335, 155, 67);
		frame.getContentPane().add(btnBackToMain);
	}
	
	/**
	 * Refreshes the list of items after a successful sale
	 * @param sellListModel, the UI list of items to be refreshed
	 */	
	private void refreshList(DefaultListModel<String> sellListModel) {
		sellListModel.removeAllElements();
		List<PricedItem> itemList = islandTrader.getWorld().getCurrentIsland().getStore().getToSellList();
		ArrayList<String> itemListStrings = ((Gui)islandTrader.getUI()).stringList(itemList, true);
		sellListModel.addAll(itemListStrings);
	}
}
