package ui.gui;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import main.IslandTrader;
import main.Route;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * This class represents the screen after the user clicked the "Sail To Another Island" button in Main Menu
 * @author kvie
 *
 */
public class SetSailingIsland extends Screen {

	/**
	 * Create the application.
  	 * @param islandTrader, the game manager 
	 */
	public SetSailingIsland(IslandTrader islandTrader) {
		super("Sailing To Another Island", islandTrader);
	}
 	
	/**
	 * Initialize the contents of the frame, which include:
	 * Buttons to get the user choice of sailing to another island
	 * Button to set sailing
     * @param frame, the frame to add content too
	 */
	@Override
	protected void initialise(final JFrame frame) {	
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.setBackground(new Color(135, 206, 250));
		frame.getContentPane().setForeground(new Color(135, 206, 250));
		frame.getContentPane().setLayout(null);
		
		// Introduce the screen
		JTextArea lblHelloTrader = new JTextArea("Hello " +getManager().getPlayer() +"!\nAre you ready to explore the next island?");
		lblHelloTrader.setLineWrap(true);
		lblHelloTrader.setForeground(Color.WHITE);
		lblHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblHelloTrader.setBackground(new Color(65, 105, 225));
		lblHelloTrader.setBounds(28, 37, 500, 60);
		frame.getContentPane().add(lblHelloTrader);
		
		// Get the route that the player wants to go
		JTextArea lblWhereToGo = new JTextArea("Where do you want to go? (* you can do)");
		lblWhereToGo.setLineWrap(true);
		lblWhereToGo.setForeground(Color.WHITE);
		lblWhereToGo.setFont(new Font("iCiel Brush Up", Font.PLAIN, 20));
		lblWhereToGo.setBackground(new Color(65, 105, 225));
		lblWhereToGo.setBounds(28, 112, 500, 40);
		frame.getContentPane().add(lblWhereToGo);
		
		// Create a Custom ListModel to store the items in the JList
		List<Route> routes = getManager().getWorld().getRoutesFromCurrent();
		ArrayList<String> routeListStrings = ((Gui)islandTrader.getUI()).routeStringList(routes, true);
		
		@SuppressWarnings("serial")
		AbstractListModel<String> routeListModel = new AbstractListModel<String>() {
	        @Override
	        public int getSize() {
	            return routeListStrings.size();
	        }

	        @Override
	        public String getElementAt(int index) {
	            return routeListStrings.get(index);
	        }
	    };
		//And a custom renderer to do the linebreaks in HTML
	    MyRouteRenderer cellRenderer = new MyRouteRenderer();
		
		// Create the scrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 190, 732, 250);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		// Create the JList
		JList<String> routeList = new JList<String>(routeListModel);
		routeList.setCellRenderer(cellRenderer);
		routeList.setLayoutOrientation(JList.VERTICAL);
		routeList.setForeground(new Color(255, 255, 255));
		routeList.setBackground(new Color(0, 0, 128));
		routeList.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		routeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Add the stuff
		scrollPane.setViewportView(routeList);
		frame.getContentPane().add(scrollPane);			
		
		// Button to start sailing
		JButton btnSailing = new JButton("Let's set sailing!");
		btnSailing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedOption = routeList.getSelectedIndex();
				getManager().getUI().setViewIsland(getManager().getWorld().getCurrentIsland()); //hack to make showing the route we sailed easier 
				getManager().sailRoute(selectedOption);
			}
		});

		btnSailing.setBounds(392, 430, 169, 61);
		frame.getContentPane().add(btnSailing);
		
		JButton btnBackToMainMenu = new JButton("Back to main menu");
		btnBackToMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new MainScreen(islandTrader);
				screen.show();
			}
		});
		btnBackToMainMenu.setBounds(191, 430, 151, 61);
		frame.getContentPane().add(btnBackToMainMenu);
	}

	/**
	 * Override the List Renderer so that it prints HTML in each cell with line breaks
	 */
	@SuppressWarnings("serial")	
	private class MyRouteRenderer extends DefaultListCellRenderer {

		  public MyRouteRenderer() {
		  }

		  @Override
		  public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		    String text = "<html>" + value.toString().replaceAll("\n", "<br/>") + "<br/></html>";
		    return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
		  }

	}
}	