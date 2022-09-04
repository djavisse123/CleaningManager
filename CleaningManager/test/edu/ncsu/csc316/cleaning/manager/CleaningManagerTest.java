package edu.ncsu.csc316.cleaning.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;

/**
 * The test class for the CleaningManager. Tests to see if the CleaningManager is
 * able to import room and log files and use them to create a map of rooms
 * and also get the coverage since the vacuum bag was replaced.
 * @author Daniel Avisse
 *
 */
public class CleaningManagerTest {
	
	/** The CleaningManager we will use to test the different methods */
	private CleaningManager manager;
	
	/** Another CleaningManager containing different rooms and logs */
	private CleaningManager manager2;
	
	/** Another CleaningManager containing different rooms and logs */
	private CleaningManager manager3;
	
	/** String for the file location of a list of CleaningLogEntries */
	private String logs = "input/sample-logs.csv";
	
	/** String for the file location of a list of RoomRecords */
	private String rooms = "input/sample-rooms.csv";
	
	/** Another location of a list of RoomRecords */
	private String rooms2 = "input/Input-rooms-1.txt";
	
	/** Another location of a list of CleaningLogEntries */
	private String logs2 = "input/Input-cleaned-rooms-1.txt";
	
	/** Another location of a list of RoomRecords */
	private String rooms3 = "input/Input-boundary-rooms-1.txt";
	
	/** Another location of a list of CleaningLogEntries */
	private String logs3 = "input/Input-boundary-cleaned-rooms-1.txt";
		
	/**
	 * Sets up the manager by loading in the files before each test
	 * @throws FileNotFoundException  if the files could not be loaded
	 */
	@Before
	public void setup() throws FileNotFoundException {
		manager = new CleaningManager(rooms, logs);
		manager2 = new CleaningManager(rooms2, logs2);
		manager3 = new CleaningManager(rooms3, logs3);
	}

	/**
	 * Tests the getEventsByRoom method in the CleaningManager. This method should return a map
	 * containing Room IDs as the keys and a list of CleaningLogEntries associated with each room.
	 */
	@Test
	public void testGetEventsByRoom() {
		//Test that getEventsByRoom gets the correct number of rooms
		assertEquals(7, manager.getEventsByRoom().size());
		
		//Check that every room and log is in the map
		Iterator<Map.Entry<String, List<CleaningLogEntry>>> it = manager.getEventsByRoom().entrySet().iterator();
		Map.Entry<String, List<CleaningLogEntry>> entry = it.next();
		
		assertEquals("Dining Room", entry.getKey());
		assertEquals(3, entry.getValue().size());
		entry = it.next();
		
		assertEquals("Foyer", entry.getKey());
		assertEquals(1, entry.getValue().size());
		entry = it.next();
		
		assertEquals("Guest Bathroom", entry.getKey());
		assertEquals(2, entry.getValue().size());
		entry = it.next();
		
		assertEquals("Guest Bedroom", entry.getKey());
		assertEquals(2, entry.getValue().size());
		entry = it.next();
		
		assertEquals("Kitchen", entry.getKey());
		assertEquals(0, entry.getValue().size());
		entry = it.next();
		
		assertEquals("Living Room", entry.getKey());
		assertEquals(6, entry.getValue().size());
		entry = it.next();
		
		assertEquals("Office", entry.getKey());
		assertEquals(1, entry.getValue().size());	
		assertFalse(it.hasNext());
		
		//Try with a different manager
		assertEquals(10, manager2.getEventsByRoom().size());
		
		//Check that every room and log is in the map
		Iterator<Map.Entry<String, List<CleaningLogEntry>>> it2 = manager2.getEventsByRoom().entrySet().iterator();
		Map.Entry<String, List<CleaningLogEntry>> entry2 = it2.next();
		
		assertEquals("Attic", entry2.getKey());
		assertEquals(0, entry2.getValue().size());
		entry2 = it2.next();
		
		assertEquals("Bathroom", entry2.getKey());
		assertEquals(0, entry2.getValue().size());
		entry2 = it2.next();
		
		assertEquals("Bedroom", entry2.getKey());
		assertEquals(3, entry2.getValue().size());
		entry2 = it2.next();
		
		assertEquals("Closet", entry2.getKey());
		assertEquals(3, entry2.getValue().size());
		entry2 = it2.next();
		
		assertEquals("Game Room", entry2.getKey());
		assertEquals(0, entry2.getValue().size());
		entry2 = it2.next();
		
		assertEquals("Gym", entry2.getKey());
		assertEquals(2, entry2.getValue().size());
		entry2 = it2.next();
		
		assertEquals("Hallway", entry2.getKey());
		assertEquals(2, entry2.getValue().size());	
		entry2 = it2.next();
		
		assertEquals("Living Room", entry2.getKey());
		assertEquals(1, entry2.getValue().size());	
		entry2 = it2.next();

		assertEquals("Lunch Room", entry2.getKey());
		assertEquals(4, entry2.getValue().size());	
		entry2 = it2.next();
		
		assertEquals("Office", entry2.getKey());
		assertEquals(1, entry2.getValue().size());	
		assertFalse(it2.hasNext());	
		
		//Try with a different manager
		assertEquals(1, manager3.getEventsByRoom().size());
				
				//Check that every room and log is in the map
		Iterator<Map.Entry<String, List<CleaningLogEntry>>> it3 = manager3.getEventsByRoom().entrySet().iterator();
		Map.Entry<String, List<CleaningLogEntry>> entry3 = it3.next();
		
		assertEquals("Gym", entry3.getKey());
		assertEquals(2, entry3.getValue().size());	
		assertFalse(it3.hasNext());
	}

	/**
	 * Tests the getCoverageSince method in the CleaningManager. This method should return how much in total
	 * square footage the rooms have been cleaned after a certain date.
	 */
	@Test
	public void testGetCoverageSince() {
		//Test a date that occurred before all the CleaningLogs
		//manager.getEventsByRoom();
		
		LocalDateTime time1 = LocalDateTime.of(2019, 04, 21, 12, 22);
		assertEquals(5727, manager.getCoverageSince(time1));
		
		//Test a date that occurred after all the CleaningLogs
		LocalDateTime time2 = LocalDateTime.of(2024, 04, 21, 12, 22);
		assertEquals(0, manager.getCoverageSince(time2));
		
		//Test a couple more dates
		LocalDateTime time3 = LocalDateTime.of(2021, 05, 30, 12, 00);
		assertEquals(331, manager.getCoverageSince(time3));
		
		LocalDateTime time4 = LocalDateTime.of(2021, 05, 15, 12, 00);
		assertEquals(2212, manager.getCoverageSince(time4));
		
		LocalDateTime time5 = LocalDateTime.of(2021, 05, 8, 12, 00);
		assertEquals(4791, manager.getCoverageSince(time5));
		
		//Try a couple dates with a different manager
		//manager2.getEventsByRoom();
		
		//A date that occurred before all the CleaningLogs
		LocalDateTime time6 = LocalDateTime.of(2021, 07, 12, 05, 29);
		assertEquals(5186, manager2.getCoverageSince(time6));
		
		//A data that occurred after all the CleaningLogs
		LocalDateTime time7 = LocalDateTime.of(2024, 02, 21, 11, 21);
		assertEquals(0, manager2.getCoverageSince(time7));
		
		LocalDateTime time8 = LocalDateTime.of(2022, 10, 25, 10, 00);
		assertEquals(4265, manager2.getCoverageSince(time8));
		
		LocalDateTime time9 = LocalDateTime.of(2022, 06, 18, 19, 59);
		assertEquals(4853, manager2.getCoverageSince(time9));
		
		LocalDateTime time10 = LocalDateTime.of(2022, 10, 30, 8, 29);
		assertEquals(815, manager2.getCoverageSince(time10));
		
		//Try a couple dates with a different manager
		//manager3.getEventsByRoom();
		
		//A date that occurred before all the CleaningLogs
		LocalDateTime time11 = LocalDateTime.of(2018, 07, 12, 05, 29);
		assertEquals(5280, manager3.getCoverageSince(time11));
		
		//A date that occurred after all the CleaningLogs
		LocalDateTime time12 = LocalDateTime.of(2023, 8, 13, 15, 32);
		assertEquals(0, manager3.getCoverageSince(time12));
		
		LocalDateTime time13 = LocalDateTime.of(2022, 9, 06, 07, 26);
		assertEquals(2640, manager3.getCoverageSince(time13));
	}

}
