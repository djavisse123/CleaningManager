package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.io.InputReader;
import edu.ncsu.csc316.cleaning.data.RoomRecord;
import edu.ncsu.csc316.cleaning.factory.DSAFactory;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;

/**
 * CleaningManager provides behaviors for retrieving information about cleaning
 * history, including lists of cleaning events for each room and the number of
 * square feet cleaned since a provided date.
 * 
 * @author Dr. King
 *
 */
public class CleaningManager {
	
	/** A list of RoomRecords */
	private List<RoomRecord> listOfRooms;
	
	/** A list of CleaningLogEntries */
	private List<CleaningLogEntry> listOfCleaningLogs;
	
	/** A map of containing the Room ID as the key and a list of CleaningLogEntires as the value */
	private Map<String, List<CleaningLogEntry>> mapOfRooms;


    /**
     * Constructs a new CleaningManager for processing cleaning history information
     * from the provided file with room information and the provided file with
     * cleaning log event information.
     * 
     * @param pathToRoomFile the path to the file that contains room information
     * @param pathToLogFile  the path to the file that contains cleaning event log
     *                       information
     * @throws FileNotFoundException if either the room file or the cleaning event
     *                               log file cannot be read
     */
    public CleaningManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {  
    	
    	//Read in rooms from the pathToRoomFile parameter
    	listOfRooms = InputReader.readRoomFile(pathToRoomFile);

    	//Read in logs from the pathToLogFile parameter
    	listOfCleaningLogs = InputReader.readLogFile(pathToLogFile);
    }

    /**
     * Returns a map of cleaning event logs for each room. In the returned map, the
     * key of each entry is represented by the room ID. The value of each entry is
     * represented by a list of cleaning event log entries.
     * 
     * If there is no room information or there are no cleaning log events, returns
     * null.
     * 
     * @return a map of cleaning event logs for each room
     */
    public Map<String, List<CleaningLogEntry>> getEventsByRoom() {  	
    								//Old Algorithms 
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//    	First Algorithm
//    	for (int i = 0; i <= listOfRooms.size() - 1; i++) {
//    		ArrayBasedList<CleaningLogEntry> list = new ArrayBasedList<CleaningLogEntry>();
//    		for (int j = 0; j <= listOfCleaningLogs.size() - 1; j++) {
//    			if (listOfRooms.get(i).getRoomID().equals(listOfCleaningLogs.get(j).getRoomID())) {
//    				list.addLast(listOfCleaningLogs.get(j));
//    			}
//    		}
//    		mapOfRooms.put(listOfRooms.get(i).getRoomID(), list);
//    	}
//   	
//    	Second Algorithm
//    	for (RoomRecord room : listOfRooms) {
//    		List<CleaningLogEntry> list = DSAFactory.getIndexedList();
//    		for (CleaningLogEntry log : listOfCleaningLogs) {
//    			if (room.getRoomID().equals(log.getRoomID())) {
//    				list.addLast(log);
//    			}
//    		}
//    		mapOfRooms.put(room.getRoomID(), list);
//    	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//Create a new map
    	mapOfRooms = DSAFactory.getMap();
    	
    	//Add every room into the map first with an empty list
    	for(RoomRecord room : listOfRooms) {
    		List<CleaningLogEntry> emptyList = DSAFactory.getIndexedList();
    		mapOfRooms.put(room.getRoomID(), emptyList);
    	}
    	//Add every log into the map
    	for (CleaningLogEntry log : listOfCleaningLogs) {
    		//Use the ID to get the list of logs and then add the new log
    		List<CleaningLogEntry> listOfLogs = mapOfRooms.get(log.getRoomID());
    		listOfLogs.addLast(log);
    		//Update the map with the new list
    		mapOfRooms.put(log.getRoomID(), listOfLogs);
    	}
    	//Return the map
		return mapOfRooms;
    }

    /**
     * Returns the square footage (as a whole number) cleaned since a provided date
     * and time. Partial square feet are rounded down to the nearest whole number.
     * 
     * @param time the time since which to calculate square footage cleaned
     * @return the square footage cleaned since the provided date and time
     */
    public int getCoverageSince(LocalDateTime time) {    	
    				//Old Algorithms
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    	//First Algorithm
//    	if (mapOfRooms == null) {
//    		getEventsByRoom();
//    	}
//    	Iterator<Map.Entry<String, List<CleaningLogEntry>>> it = mapOfRooms.entrySet().iterator();
//    	while (it.hasNext()) {
//    		Map.Entry<String, List<CleaningLogEntry>> entry = it.next();
//    		RoomRecord room = null;
//    		for (int i = 0; i < listOfRooms.size(); i++) {
//    			if (entry.getKey().equals(listOfRooms.get(i).getRoomID())) {
//    				room = listOfRooms.get(i);
//    				break;
//    			}
//    		}
//    		for (RoomRecord roomInList : listOfRooms) {
//    			if (entry.getKey().equals(roomInList.getRoomID())) {
//    				room = roomInList;
//    				break;
//    			}
//    		}
//    		List<CleaningLogEntry> list = DSAFactory.getIndexedList();
//    		list = entry.getValue();
//    		int squareFoot = 0;
//    		if (room != null) {
//    			squareFoot = room.getLength() * room.getWidth();
//    		}
//    		for (int j = 0; j < list.size(); j++) {
//    			if (time.compareTo(list.get(j).getTimestamp()) < 0) {
//    				//System.out.println("Number:" + (squareFoot * list.get(j).getPercentCompleted())/100.00);
//    				coverage += squareFoot * list.get(j).getPercentCompleted() / 100;
//     			}
//    		}
//    		for (CleaningLogEntry log : list) {
//    			if (time.compareTo(log.getTimestamp()) < 0) {
//    				coverage += squareFoot * log.getPercentCompleted() / 100;
//    			}
//    		}
//    	}
//    	
//    	//Second Algorithm
//    	for (CleaningLogEntry log : listOfCleaningLogs) {
//    		if (time.compareTo(log.getTimestamp()) < 0) {
//    			for (RoomRecord room : listOfRooms) {
//    				if (room.getRoomID().equals(log.getRoomID())) {
//    					int squareFoot = room.getLength() * room.getWidth();
//    					coverage += squareFoot * log.getPercentCompleted() / 100;
//    					break;
//    				}
//    			}			
//    		}		
//    	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
    	
    	//Set coverage to zero
    	int coverage = 0;
    	
    	//Create a map with rooms as the value. Use this to get
    	//easier access to rooms when iterating for the cleaninglogs
    	Map<String, RoomRecord> roomMap = DSAFactory.getMap();
    	for(RoomRecord room : listOfRooms) {
    		roomMap.put(room.getRoomID(), room);
    	}
    	//Iterate through all the CleaningLogs
    	for (CleaningLogEntry log : listOfCleaningLogs) {
    		//Check if the event of the log happened after the inputed date
    		if (time.compareTo(log.getTimestamp()) < 0) {
    			//Get the room from the roommap and use it to calculate the square footage
    			RoomRecord room = roomMap.get(log.getRoomID());
    			int squareFoot = room.getLength() * room.getWidth();
    			//Use the square footage and log percent cleaned and add it to coverage
    			coverage += squareFoot * log.getPercentCompleted() / 100;
    		}			
    	}
    	//Return coverage
		return coverage;
    }
}
