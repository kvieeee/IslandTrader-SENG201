package ui.gui;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import main.IslandTrader;
/**
 * This class represents the main Screen that manages all the calls to other screens of the game
 * @author kvie
 *
 */
public abstract class Screen {

	private JFrame frame;
	
	// The game instance that this screen interacts with
	protected final IslandTrader islandTrader;

	/**
	 * Create the application.
	 * @param title, the title of the window
	 * @param islandTrader, the game manager
	 */
	protected Screen(final String title, final IslandTrader islandTrader) {
		this.islandTrader = islandTrader;
		initialise(title);
	}

    /**
     * Initialises this screen's UI.
	 * @param title, the title of the window
     */
    protected void initialise(final String title) {
    	((Gui)this.islandTrader.getUI()).setScreen(this);
        frame = new JFrame();
        frame.setTitle(title);      
        frame.setBounds(100, 100, 785, 582);
        frame.setDefaultCloseOperation(JFrame. DO_NOTHING_ON_CLOSE);        

        // Prevent the frame from closing immediately when the user clicks the close button.
        // Instead we add a WindowListener so we can tell our rocket manager that the user
        // has requested to quit. This allows the rocket manager to perform actions that may
        // be required before quitting E.g. Confirming the user really wants to quit,
        // saving state etc.
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	islandTrader.onFinish();
            }
        });

        initialise(frame);

        // Size our frame
        //frame.pack();

        // We set the location of our frame relative to null. This causes the frame to be placed
        // in the centre of the screen.
        frame.setLocationRelativeTo(null);
    }
    
    /**
     * Creates and adds the required graphical components to the given container.
     *
     * @param frame, the frame to add content too
     */
    protected abstract void initialise(JFrame frame);    
	
	/**
	 * Gets the {@link IslandTrader} that this screen supports
	 * @return the IslandTrader for this screen
	 */
	protected IslandTrader getManager() {
		return islandTrader;
	}
	/**
	 * Gets the JFrame of this screen
	 * @return frame for this screen
	 */
	protected JFrame getFrame() {
 		return frame;
 	}
	
	/**
	 * Shows this screen
	 */
	protected void show() {
		frame.setVisible(true);
	}
	
	/**
	 * Confirms if the user wants to quit this screen
	 * @return
	 */
	protected boolean confirmQuit() {
		int selection = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		return selection == JOptionPane.YES_OPTION;
	}
	
	/**
	 * Quits this screen
	 */
	void quit() {
		frame.dispose();
	}
	
	/**
	 * Reports the given error to the user
	 * @param error the error to report
	 */
	void showError(String error) {
		JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
