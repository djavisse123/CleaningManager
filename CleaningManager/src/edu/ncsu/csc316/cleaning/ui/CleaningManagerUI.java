package edu.ncsu.csc316.cleaning.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.cleaning.manager.CleaningManager;
import edu.ncsu.csc316.cleaning.manager.ReportManager;

/**
 * This is the UI class for CleaningManager. This is the class that the user will use to interact with 
 * the program. The user will be prompted to first enter the file names of the files they want to use
 * to report Rooms and Cleaning Logs. This class interacts with the ReportManager class, allowing for 
 * this class to display the information returned for the ReportManager like frequency report or
 * vaccum bag life.
 * @author Daniel Avisse
 */
public class CleaningManagerUI {
	
	/** An instance of the ReportManager to be able to report different information relating to cleaned rooms */
	private static ReportManager manager; 
	
	
	/**
	 * This is the main method. This method is what creates the actual UI the user will use to interact
	 * with the ReportManager class to be able to load valid files of rooms and logs and use those to
	 * see which rooms have been clean.
	 * @param args command line arguments
	 */
	public static void main (String args[]) {
		
		/* int startTime = (int) System.currentTimeMillis();
		try {
			CleaningManager manager = new CleaningManager("input/experimentFiles/rooms_22.csv", "input/experimentFiles/cleaningEvents_22.csv");
			manager.getEventsByRoom();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		int endTime = (int) System.currentTimeMillis(); 
		System.out.println("Time it took (MS):" + (endTime - startTime)); */
		
		//Start the program by creating a new scanner that will take user input
		Scanner scnr = new Scanner(System.in);
        String roomFileLocation = "";
        String logsFileLocation = "";
        //Greet the user and prompt them for valid files
        System.out.println("Hello and Welcome to the Cleaning Manager. To use start using the program. \nPlease"
        		+ " enter the location of the files containing Room Records and Cleaning Log Entries.\n"
        		+ "If you are having trouble and would like to quit the program just type in the letter Q.");     

        //If the files are invalid then keep prompting them until they are valid
        boolean filesCorrectlyLoad = false;
        while (!filesCorrectlyLoad) {
        	System.out.print("\nFile Location of Rooms: ");
            roomFileLocation = scnr.nextLine();
            
            //Check if the user wants to quit
            if ("Q".equalsIgnoreCase(roomFileLocation)) {
            	scnr.close();
                System.out.println("\nThank you for using the Cleaning Manager.");
                System.exit(0);
            }
            
            System.out.print("File Location of Cleaning Log Entries: ");
            logsFileLocation = scnr.nextLine();
            
            //Check if the user wants to quit
            if ("Q".equalsIgnoreCase(logsFileLocation)) {
            	scnr.close();
                System.out.println("\nThank you for using the Cleaning Manager.");
                System.exit(0);
            }
            
        	 try {
             	manager = new ReportManager(roomFileLocation, logsFileLocation);
             	filesCorrectlyLoad = true;
             }
             catch (FileNotFoundException e) {
             	System.out.println("\nERROR: The files you have entered either do not exist or are formatted incorrectly.\n"
             			+ "\nPlease try again.");
             }
        	 
        }
        
        //Once valid files have be imported begin asking the user for different options
        System.out.println("\nYour files have been successfully loaded!");
        String userOption = "";
        while (!"Q".equalsIgnoreCase(userOption)) {
        	displayMenu();
        	System.out.print("\nOption: ");
        	userOption = scnr.nextLine();
        	
        	//If the option is F then report most frequently clean rooms
        	if ("F".equalsIgnoreCase(userOption)) {
        		int numberOfRooms = 0;
        		String strNumberOfRooms = "";
        		System.out.println("\nPlease enter the number of rooms you would like to see.");
        		System.out.print("Number of Rooms: ");
        		//If the number that inputed isn't valid then keep prompting the user for a valid number
        		boolean validNumber = false;
        		while (!validNumber) {
        			strNumberOfRooms = scnr.nextLine();
        			try {
        				numberOfRooms = Integer.parseInt(strNumberOfRooms);
        				validNumber = true;
        			}
        			catch (Exception e) {
        				System.out.print("\nPlease Enter a Valid Number: ");
        			}
        		}
        		System.out.println("\n" + manager.getFrequencyReport(numberOfRooms));
        	}
        	
        	//If the option is R then report all the rooms and cleanings
        	else if ("R".equalsIgnoreCase(userOption)) {
        		System.out.println("\n" + manager.getRoomReport());
        	}
        	
        	//If the option is V then report the estimated bag life
        	else if ("V".equalsIgnoreCase(userOption)) {
        		System.out.print("Please Enter a Date: ");
        		String date = "";
        		date = scnr.nextLine();
        		System.out.println("\n" + manager.getVacuumBagReport(date));
        	}
        	
        	//If the option is not any of the other options or is not Q then prompt the user for a valid option
        	else if (!"Q".equalsIgnoreCase(userOption)) {
        		System.out.println("Please Enter a Valid Option!");
        	}
        }
        //Close the scanner and exit the program
        scnr.close();
        System.out.println("\nThank you for using the Cleaning Manager.");
        System.exit(0);
	}
	
	/**
	 * This method is used to display the menu options for the user whenever they use the program
	 */
	public static void displayMenu() {
		System.out.println("\nCleaning Manager - Please Choose One of the Options Below");
		System.out.println("F - View the Most Frequently Cleaned Rooms");
		System.out.println("R - View a Report of Cleanings by Room");
		System.out.println("V - View Estimated Remaining Vacuum Bag Life");
		System.out.println("Q - Quit the Program");
	}

}
