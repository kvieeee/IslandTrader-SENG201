package ui.gui;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;

import main.Island;
import main.IslandTrader;
import main.Route;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * This class represents the screen after the user clicked the "View Routes to This Island" button in View Island Properties Screen.
 * @author kvie
 *
 */
public class ViewIslandRoutes extends Screen {
	
	/**
  	 * @param islandTrader, the game manager 
	 */
	public ViewIslandRoutes(IslandTrader islandTrader) {		
		super("View Routes from " + islandTrader.getWorld().getCurrentIsland() +" to " +islandTrader.getUI().getViewIsland(), islandTrader);
	}
	
	/**
	 * Initialize the contents of the frame, which include:
 	 * a list of routes for the user to view
 	 * a "Back To Island Properties" button
     * @param frame, the frame to add content too
	 */
	@Override
	protected void initialise(final JFrame frame) {
		frame.setBackground(new Color(47, 79, 79));
		frame.getContentPane().setBackground(new Color(47, 79, 79));
		frame.getContentPane().setLayout(null);	
		
		// Labels to introduce the screen
		JTextArea lblHelloTrader = new JTextArea("Hello " +getManager().getPlayer()+"! Is it time to go?\n\nHere are all routes available to " +islandTrader.getUI().getViewIsland());
		lblHelloTrader.setLineWrap(true);
		lblHelloTrader.setForeground(Color.WHITE);
		lblHelloTrader.setFont(new Font("iCiel Brush Up", Font.PLAIN, 22));
		lblHelloTrader.setBackground(new Color(0, 128, 128));
		lblHelloTrader.setBounds(27, 48, 653, 104);
		frame.getContentPane().add(lblHelloTrader);

		// Create a Custom ListModel to store the items in the JList
		Island viewIsland = islandTrader.getUI().getViewIsland();
		List<Route> routes = getManager().getWorld().getRoutes(getManager().getWorld().getCurrentIsland(), viewIsland);
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
		scrollPane.setBounds(27, 219, 732, 118);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);	    
		
		// Create the JList
		JList<String> routeList = new JList<String>(routeListModel);
		routeList.setCellRenderer(cellRenderer);
		routeList.setLayoutOrientation(JList.VERTICAL);
		routeList.setForeground(new Color(255, 255, 255));
		routeList.setBackground(new Color(85, 107, 47));
		routeList.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		routeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		routeList.setBounds(0, 0, 732, 118);
		scrollPane.setViewportView(routeList);
		frame.getContentPane().add(scrollPane);	
		
		// Button to back to Island Properties
		JButton btnBackToIslandProperties = new JButton("Back to Island Properties");
		btnBackToIslandProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
				Screen screen = new IslandProperties(islandTrader);
				screen.show();
			}
		});
		btnBackToIslandProperties.setBounds(298, 428, 189, 75);
		frame.getContentPane().add(btnBackToIslandProperties);
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
