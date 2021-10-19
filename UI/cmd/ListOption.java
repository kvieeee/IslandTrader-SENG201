package ui.cmd;

import java.util.ArrayList;

/**
 * Manages a cmd user input request when a choice from a list of objects is being requested.
 * Shows user list of options, reindexed so that the first option is option 1 instead of 0 in option list 
 * 
 * Extends the Option class for user requests
 *
 */
public abstract class ListOption extends Option {	

    /**
	 * ArrayList of options to present to the user to choose from
	 * Assumes that the exit option is always index 0 in the list, and is chosen by typically -1
	 */	
    protected ArrayList<String> options;      
    
    /**
	 * Initialize the ListOption, with ui default regex.
	 * Default regex is range from 1-99. Does not support input from user outside of this. User input is also
	 * 
	 * @param ui, the ui object this ListOption os part of
	 */
	public ListOption(MainCmdUI ui) {
		super(ui, "-?[1-9][0-9]?$"); //Menu can only have 99 items		
	}  
    
    /**
	 * Remaps the option index to more user friendly index
	 * index 0 in options = -1 in display (is exit criteria)
	 * index 1+ in options is display as 1+
	 */		
    private int getDisplayIndex(int arrayIndex) {
    	if(arrayIndex == 0)
    		return -1;
    	else
    		return arrayIndex;
    }       
    
    /**
	 * Print the list of indexed options to choose from to the user
	 */	  
    @Override
    protected void printOptions() { 
    	if (options.size() > 1) {
    		for(int i = 1; i < options.size(); i++) {
    			System.out.println("(" + getDisplayIndex(i) + ") " + options.get(i));
    		}
    	}
		System.out.println("(" + getDisplayIndex(0) + ") " + options.get(0));
    }  
	
    @Override
    /**
	 * Validates the user input, both against the regex and to make sure its not out of bounds
	 * versus the input option list
	 */	    
	protected void validateInput(String input) {
    	super.validateInput(input);
		int intInput = Integer.parseInt(input);
		if (intInput < -1 || intInput == 0 || intInput >= options.size()) {
			throw new InvalidInputException("Input `" + input + "` is invalid. Value must be -1 or <= " +options.size());
		}
	}

}
