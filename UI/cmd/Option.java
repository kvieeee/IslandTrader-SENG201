package ui.cmd;

import java.util.Scanner;

/**
 * Manages a cmd user input request to an Option presented
 * 
 * Is initialized with the option / question to print to the user and regex
 * to validate the input. Loops until correct input is received and then passes
 * the input to a (typically overidden) handleOption method 
 * 
 * Typically this will be subclassed as a specific input menu with default values passed
 */
public class Option {    
	
	// The UI
	protected MainCmdUI ui;        
    
	// Regex that validates the user input
	protected final String INPUT_REGEX;
    
	// Boolean to indicate that we have finished with this Input getter
    protected boolean finish = false;    
    
    /**
	 * Exception throw when we do not receive valid input
	 */    
    @SuppressWarnings("serial") // Unsure why eclipse wants this
	protected class InvalidInputException extends IllegalArgumentException {    	

		InvalidInputException(String message) {
    		super(message);
    	}
    }   

    /**
	 * Initialize the Option, with ui and valid regex
	 * 
	 * @param ui, the ui object this Option is linked to
	 * @param INPUT_REGEX, string indicating regex succesful input to this Option must match
	 */        
    public Option(MainCmdUI ui, String INPUT_REGEX) {
    	this.INPUT_REGEX = INPUT_REGEX;
    	this.ui = ui;
    }      
    
    /**
	 * Method to collect user input, looping until valid input is received
	 * 
	 * @param scanner, cmd line input scanner object
	 */      
	protected void getUserOption(Scanner scanner) {
		//Print the overall header
		oneHeader();
		
        while (!finish) {        	
        	
        	//Print the pre loop header
        	eachHeader();
        	//Print the options to the user - could be blank
            printOptions();            
    		
            // try to get valid input
            try {
            	String input = scanner.nextLine();
            	validateInput(input); //throws exception if input is not valid
            	handleOption(input);
            } catch (InvalidInputException e) {
            	System.out.println("Please try again: "+e.getMessage());     	
            } catch (Exception e) {            	
            	System.out.println("OPPS:\n" +e.getMessage());
            	e.printStackTrace();
            }    		    		

        }	
        //Print the final footer, normally just a newline
        oneFooter();
        
        // Reset the Option to initial state, because it is often reused in game
        finish = false;        
	}
	
    /**
	 * Print the list of indexed options to choose from to the user
	 * Dummy method in Option, will be overridden later
	 */	   	
	protected void printOptions() {
		//Default is blank
	}
	
	/**
	 * Method to print a header once  when starting this menu item
	 */
	protected void oneHeader() {
    	System.out.println("****************************************");		
	}
	
	/**
	 * Method to print a header every loop of menu
	 */	
	protected void eachHeader() {
    	System.out.println("");		
	}
	
	/**
	 * Method to print a footer when finishing with menu
	 */	
	protected void oneFooter() {
    	System.out.println("\n");		
	}	
	
    /**
	 * Validate that the user input matches the INPUT_REGEX, throw exception if not
	 * @param input, the string to validate
	 */  	
	protected void validateInput(String input) {
    	if(input.matches(INPUT_REGEX) == false) {
    		throw new InvalidInputException("Input `" + input + "` is invalid. Regex: " +INPUT_REGEX);
    	}		
	}
	
    /**
	 * Method to handle next steps once valid input has been received
	 * @param option, the regex validated string that the user entered
	 */  	
	protected void handleOption(String option) {	
		this.setMenuFinish();
	}	
	
    /**
	 * Sets the finish property. Option will be excited after this is set
	 */  	
	public void setMenuFinish() {
		finish = true;
	}	

}
