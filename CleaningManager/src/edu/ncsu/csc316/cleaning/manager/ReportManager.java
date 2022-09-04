package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Iterator;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.factory.DSAFactory;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.sorter.Sorter;

/**
 * ReportManager handles behaviors associated with generating String reports for
 * the user interface, including (1) vacuum bag reports, (2) frequency reports,
 * and (3) room reports.
 * 
 * @author Dr. King
 *
 */
public class ReportManager {

	/** The format that dates will be inputted and outputted in the manager */
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    
    /** An instance of the cleaningManager to allow us to have access to maps and coverage */
    private CleaningManager cleaningManager;
    
    /** A map of containing the Room ID as the key and a list of CleaningLogEntires as the value */
    private Map<String, List<CleaningLogEntry>> mapOfRooms;
    
    /** The total amount of square footage a vaccum bag can contain */
    private static final int VACUUMBAGLIFE = 5280;
    
    /** The comparator that we will use to sort CleaningLogEntries in descending order by date */
    private CleaningLogComparator logComparator = new CleaningLogComparator();

    /**
     * Constructs a new ReportManager for building reports of cleaning history
     * information from the provided file with room information and the provided
     * file with cleaning log event information.
     * 
     * @param pathToRoomFile the path to the file that contains room information
     * @param pathToLogFile  the path to the file that contains cleaning event log
     *                       information
     * @throws FileNotFoundException if either the room file or the cleaning event
     *                               log file cannot be read
     */
    public ReportManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
    	//Use the pathToRoomFile and pathToLogFile to create a cleaningManager
    	cleaningManager = new CleaningManager(pathToRoomFile, pathToLogFile);
    	//Use the CleaningManager and to create a map of all the rooms
    	mapOfRooms = cleaningManager.getEventsByRoom();	
    }

    /**
     * Returns a report that indicates how many square feet remain to be cleaned
     * until a vacuum bag change is recommended.
     * 
     * @param timestamp the date and time of the previous vacuum bag change
     * @return a report that indicates how many more square feet can be cleaned
     *         until a vacuum bag change is recommened
     */
    public String getVacuumBagReport(String timestamp) {
        LocalDateTime dateTime = null;
        //Try to parse the inputed data
        try {
            dateTime = LocalDateTime.parse(timestamp, DATE_TIME_FORMAT);
            //If the date is invalid then return to the user that the date is not formatted correctly
        } catch (DateTimeParseException e) {
            return "Date & time must be in the format: MM/DD/YYYY HH:MM:SS";
        }
        //Create a StringBuilder that starts the report of the bag life
        StringBuilder bagReport = new StringBuilder("Vacuum Bag Report (last replaced " + timestamp + ") [\n");
        //Get the coverage from the CleaningManager
        int coverage = cleaningManager.getCoverageSince(dateTime);
        //If the coverage is greater than the bag's life then report that the bag is due for replacement
        if (coverage > VACUUMBAGLIFE) {
        	bagReport.append("   Bag is overdue for replacement!\n]");
        }
        //If the coverage is less than the bag life then subtract the coverage from the bag life and report it.
        else {
        	int remainingLife = VACUUMBAGLIFE - coverage;
        	bagReport.append("   Bag is due for replacement in " + remainingLife + " SQ FT\n]");
        }
        //Return the report
		return bagReport.toString();
    }

    /**
     * Returns a report of the top X rooms cleaned, sorted from most frequent to
     * least frequent.
     * 
     * @param number the number of rooms to include in the report
     * @return a report of the top X rooms cleaned
     */
    public String getFrequencyReport(int number) {
    	//If the number of rooms is less 1 then return that the user needs to input a number greater than 0
    	if (number <= 0) {
    		return "Number of rooms must be greater than 0.";
    	}
    	//If there are no rooms in the map then return that no rooms were cleaned.
    	if (mapOfRooms.size() == 0) {
    		return "No rooms have been cleaned.";
    	}
    	int numberOfRooms = number;
    	int emptyRoomCount = 0;
    	//If the number is greater than the number of rooms in the map then set it to the size of the map
    	if (number > mapOfRooms.size()) {
    		numberOfRooms = mapOfRooms.size();
    	}
    	//Create a copy of the current map. Use the copy of the map to then remove rooms that have been already reported.
    	Map<String, List<CleaningLogEntry>> copyOfMap = DSAFactory.getMap();
    	Iterator<Map.Entry<String, List<CleaningLogEntry>>> copyIt = mapOfRooms.entrySet().iterator();
    	while (copyIt.hasNext()) {
    		Map.Entry<String, List<CleaningLogEntry>> copiedEntry = copyIt.next();
    		copyOfMap.put(copiedEntry.getKey(), copiedEntry.getValue());
    		//Check for rooms that haven't been cleaned in the map
    		if (copiedEntry.getValue().size() == 0) {
    			emptyRoomCount++;
    		}
    	}
    	//If every room in the map has not been cleaned then report that no rooms have been cleaned.
    	if (emptyRoomCount == copyOfMap.size()) {
			return "No rooms have been cleaned.";
		}
    	//Create a StringBuilder that starts the reporting of the frequency cleaning
    	StringBuilder frequencyReport = new StringBuilder("Frequency of Cleanings [\n");
    	
    	//Repeat iterating through the map for the number of rooms that wanted to be reported.
    	for (int i = 0; i < numberOfRooms; i++) {
    		//Set the max to negative one. (Since we may eventually reach entries that are size 0).
    		int max = -1;	
    		
    		//Create an iterator and iterate through the map
    		Iterator<Map.Entry<String, List<CleaningLogEntry>>> it = copyOfMap.entrySet().iterator();
    		//Use printEntry to copy entries that we will want to print
    		Map.Entry<String, List<CleaningLogEntry>> printEntry = null;
        	while (it.hasNext())  {
        		Map.Entry<String, List<CleaningLogEntry>> entry = it.next();
        		//Check for a entry that has a list size greater than the max.
        		//Since rooms are ordered alphabetically, if every room had the same list size
        		//it should only get the first room that is greater than the max
        		if (entry.getValue().size() > max) {
        			//Copy the entry to printEntry
        			printEntry = entry;
        			//Set the max to the size of the entry
        			max = entry.getValue().size();
        		}
        	}
        	//Delete the room that will be printed so that room is no longer used when iterating again
        	copyOfMap.remove(printEntry.getKey());
        	//Add the information of the entry to the report
        	frequencyReport.append("   " + printEntry.getKey() + " has been cleaned " + max + " times\n");
    	}
    	frequencyReport.append("]");
    	//Return the report
		return frequencyReport.toString();
    }

    /**
     * Returns a report that includes a list of cleaning log events associated with
     * each room. The report lists rooms in alphabetical (ascending) order, and
     * cleaning log entries for each room as sorted in descending chronological
     * order (most recent to oldest).
     * 
     * @return a report of cleaning log events for each room
     */
    public String getRoomReport() {
    	//If there are no rooms in the map then return that no rooms were cleaned.
    	if (mapOfRooms.size() == 0) {
    		return "No rooms have been cleaned.";
    	}
    	//Create a StringBuilder that will start the report for roomReport
    	StringBuilder roomReport = new StringBuilder("Room Report [\n");
    	int emptyRoomCount = 0;
    	//Iterate through the map to get every entry and report them
    	Iterator<Map.Entry<String, List<CleaningLogEntry>>> it = mapOfRooms.entrySet().iterator();
    	while (it.hasNext())  {
    		Map.Entry<String, List<CleaningLogEntry>> entry = it.next();
    		roomReport.append("   " + entry.getKey() + " was cleaned on [\n");
    		//Check for rooms that haven't been cleaned in the map
    		if (entry.getValue().size() == 0) {
    			emptyRoomCount++;
    			//Report that this specific room hasn't been cleaned
    			roomReport.append("      (never cleaned)\n   ]\n");
    			//If every room in the map has not been cleaned then report that no rooms have been cleaned.
    			if (emptyRoomCount == mapOfRooms.size()) {
    				return "No rooms have been cleaned.";
    			}
    		}
    		//If the room has been cleaned before then report all the times it was cleaned
    		else {
    			//Put all the CleaningLogs attached to the room and add them to an array
    			CleaningLogEntry[] logs = new CleaningLogEntry[entry.getValue().size()];
    			for (int i = 0; i < entry.getValue().size(); i++) {
    				logs[i] = entry.getValue().get(i);
    			}
    			//Create a sorter that will sort the array based on descending date for the logs
    			Sorter<CleaningLogEntry> sorter = DSAFactory.getComparisonSorter(logComparator);
    			sorter.sort(logs);
    			//Report each time the room was cleaned
    			for (int j = 0; j < logs.length; j++) {
    				roomReport.append("      " + logs[j].getTimestamp().format(DATE_TIME_FORMAT) + "\n");
    			}
    			roomReport.append("   ]\n");
     		}
    	}
    	roomReport.append("]");
    	//Return the room report
		return roomReport.toString();
    }
    
    /**
     * Private inner class that creates a comparator used when sorting the CleaningLogs.
     * This should sort the CleaningLogs in descending order for dates
     * @author Daniel Avisse
     *
     */
    private class CleaningLogComparator implements Comparator<CleaningLogEntry> {

    	/**
    	 * Compares two different CleaningLogEntries and then returns that value that would sort them
    	 * based on descending order for dates
    	 */
    	@Override
    	public int compare(CleaningLogEntry one, CleaningLogEntry two) {
    		return one.compareTo(two) * -1;
    	}

    }
}
