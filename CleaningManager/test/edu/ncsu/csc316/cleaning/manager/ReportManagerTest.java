package edu.ncsu.csc316.cleaning.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

/**
 * The test class for the ReportManager. Tests to see if the ReportManager is
 * able to import room and log files into the CleaningManager, allowing for it
 * to have access to a map. This will allow it to report which rooms have been 
 * cleaned the most, report the how much square feet is remaining in the vacuum bag,
 * and h
 * @author Daniel Avisse
 *
 */
public class ReportManagerTest {
	
	/** The ReportManager we will use to test the different methods */
	private ReportManager manager;
	
	/** Another ReportManager containing different rooms and logs */
	private ReportManager manager2;
	
	/** Another ReportManager containing different rooms and logs */
	private ReportManager manager3;
	
	/** Another ReportManager containing different rooms and logs */
	private ReportManager manager4;
	
	/** Another ReportManager containing different rooms and logs */
	private ReportManager manager5;
	
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
	
	/** Another location of a list of RoomRecords */
	private String rooms4 = "input/empty-rooms.txt";
	
	/** Another location of a list of CleaningLogEntries */
	private String logs4 = "input/empty-logs.txt";
	
	
	
	/**
	 * Sets up the manager by loading in the files before each test
	 * @throws FileNotFoundException if the files could not be loaded
	 */
	@Before
	public void setup() throws FileNotFoundException {
		manager = new ReportManager(rooms, logs);
		manager2 = new ReportManager(rooms2, logs2);
		manager3 = new ReportManager(rooms3, logs3);
		manager4 = new ReportManager(rooms2, logs4);
		manager5 = new ReportManager(rooms4, logs4);
	}
 
	/**
	 * Tests the getVacuumBagReport method. This method will use the getCoverage method in CleaningManager
	 * to help find the remaining capacity of the vacuum bag.
	 */
	@Test
	public void testGetVacuumBagReport() {
		//Test a date that occurs when the bag would by way over replacement
		assertEquals("Vacuum Bag Report (last replaced 06/21/2019 14:50:02) [\n"
				+ "   Bag is overdue for replacement!\n]", manager.getVacuumBagReport("06/21/2019 14:50:02"));
		
		//Test a date where the bag would still be empty
		assertEquals("Vacuum Bag Report (last replaced 09/15/2024 12:59:42) [\n"
				+ "   Bag is due for replacement in 5280 SQ FT\n]", manager.getVacuumBagReport("09/15/2024 12:59:42"));
		
		//Test a couple more dates
		assertEquals("Vacuum Bag Report (last replaced 05/30/2021 12:00:00) [\n"
				+ "   Bag is due for replacement in 4949 SQ FT\n]", manager.getVacuumBagReport("05/30/2021 12:00:00"));
		
		assertEquals("Vacuum Bag Report (last replaced 05/15/2021 12:00:00) [\n"
				+ "   Bag is due for replacement in 3068 SQ FT\n]", manager.getVacuumBagReport("05/15/2021 12:00:00"));
		
		assertEquals("Vacuum Bag Report (last replaced 05/08/2021 12:00:00) [\n"
				+ "   Bag is due for replacement in 489 SQ FT\n]", manager.getVacuumBagReport("05/08/2021 12:00:00"));
		
		//Try using an invalid format
	    assertEquals("Date & time must be in the format: MM/DD/YYYY HH:MM:SS", manager.getVacuumBagReport("2021/06/09 11:59:59"));
		 
		 //Try using a different manager.
		 
		 assertEquals("Vacuum Bag Report (last replaced 07/12/2021 05:29:02) [\n"
					+ "   Bag is due for replacement in 94 SQ FT\n]", manager2.getVacuumBagReport("07/12/2021 05:29:02"));
		 
		 assertEquals("Vacuum Bag Report (last replaced 02/21/2024 11:21:04) [\n"
					+ "   Bag is due for replacement in 5280 SQ FT\n]", manager2.getVacuumBagReport("02/21/2024 11:21:04"));
		
		 assertEquals("Vacuum Bag Report (last replaced 10/25/2022 10:00:15) [\n"
					+ "   Bag is due for replacement in 1015 SQ FT\n]", manager2.getVacuumBagReport("10/25/2022 10:00:15"));
		 
		 assertEquals("Vacuum Bag Report (last replaced 06/18/2022 19:59:04) [\n"
					+ "   Bag is due for replacement in 427 SQ FT\n]", manager2.getVacuumBagReport("06/18/2022 19:59:04"));
		 
		 assertEquals("Vacuum Bag Report (last replaced 10/30/2022 08:29:30) [\n"
					+ "   Bag is due for replacement in 4465 SQ FT\n]", manager2.getVacuumBagReport("10/30/2022 08:29:30"));
		 
		 //Try with another manager.
		 
		 //Boundary Check. Check to see if the Report will output 0 SQ FT
		 assertEquals("Vacuum Bag Report (last replaced 07/12/2018 05:29:02) [\n"
					+ "   Bag is due for replacement in 0 SQ FT\n]", manager3.getVacuumBagReport("07/12/2018 05:29:02"));
		 
		 assertEquals("Vacuum Bag Report (last replaced 08/13/2023 13:15:12) [\n"
					+ "   Bag is due for replacement in 5280 SQ FT\n]", manager3.getVacuumBagReport("08/13/2023 13:15:12"));
		 
		 assertEquals("Vacuum Bag Report (last replaced 09/06/2022 07:26:59) [\n"
					+ "   Bag is due for replacement in 2640 SQ FT\n]", manager3.getVacuumBagReport("09/06/2022 07:26:59"));
	}

	/**
	 * Tests the getFrequenceyReport method. This method should output the number of most clean rooms the 
	 * user inputs. If two rooms have the same frequency then they should be outputted based on
	 * alphabetical order.
	 */
	@Test
	public void testGetFrequencyReport() {
		//Try to see if it will return the single most cleaned room
		assertEquals("Frequency of Cleanings [\n"
				+ "   Living Room has been cleaned 6 times\n]", 
				manager.getFrequencyReport(1));
		
		//Try to get the frequency of all rooms
		assertEquals("Frequency of Cleanings [\n"
				+ "   Living Room has been cleaned 6 times\n"
				+ "   Dining Room has been cleaned 3 times\n"
				+ "   Guest Bathroom has been cleaned 2 times\n"
				+ "   Guest Bedroom has been cleaned 2 times\n"
				+ "   Foyer has been cleaned 1 times\n"
				+ "   Office has been cleaned 1 times\n"
				+ "   Kitchen has been cleaned 0 times\n"
				+ "]", manager.getFrequencyReport(7));
		
		//Try a couple different frequencies 
		assertEquals("Frequency of Cleanings [\n"
				+ "   Living Room has been cleaned 6 times\n"
				+ "   Dining Room has been cleaned 3 times\n"
				+ "   Guest Bathroom has been cleaned 2 times\n"
				+ "]", manager.getFrequencyReport(3));
		
		assertEquals("Frequency of Cleanings [\n"
				+ "   Living Room has been cleaned 6 times\n"
				+ "   Dining Room has been cleaned 3 times\n"
				+ "   Guest Bathroom has been cleaned 2 times\n"
				+ "   Guest Bedroom has been cleaned 2 times\n"
				+ "]", manager.getFrequencyReport(4));
		
		assertEquals("Frequency of Cleanings [\n"
				+ "   Living Room has been cleaned 6 times\n"
				+ "   Dining Room has been cleaned 3 times\n"
				+ "   Guest Bathroom has been cleaned 2 times\n"
				+ "   Guest Bedroom has been cleaned 2 times\n"
				+ "   Foyer has been cleaned 1 times\n"
				+ "   Office has been cleaned 1 times\n"
				+ "]", manager.getFrequencyReport(6));
		
		//Try using a number less than 1
		assertEquals("Number of rooms must be greater than 0.", manager.getFrequencyReport(-2));
		
		//Try a frequency larger than the size of the map
		assertEquals("Frequency of Cleanings [\n"
				+ "   Living Room has been cleaned 6 times\n"
				+ "   Dining Room has been cleaned 3 times\n"
				+ "   Guest Bathroom has been cleaned 2 times\n"
				+ "   Guest Bedroom has been cleaned 2 times\n"
				+ "   Foyer has been cleaned 1 times\n"
				+ "   Office has been cleaned 1 times\n"
				+ "   Kitchen has been cleaned 0 times\n"
				+ "]", manager.getFrequencyReport(12));
		
		//Try with a different manager
		
		//Try to see if it will return the single most cleaned room
		assertEquals("Frequency of Cleanings [\n"
				+ "   Lunch Room has been cleaned 4 times\n]", 
				manager2.getFrequencyReport(1));
		
		//Try to get the frequency of all rooms
		assertEquals("Frequency of Cleanings [\n"
				+ "   Lunch Room has been cleaned 4 times\n"
				+ "   Bedroom has been cleaned 3 times\n"
				+ "   Closet has been cleaned 3 times\n"
				+ "   Gym has been cleaned 2 times\n"
				+ "   Hallway has been cleaned 2 times\n"
				+ "   Living Room has been cleaned 1 times\n"
				+ "   Office has been cleaned 1 times\n"
				+ "   Attic has been cleaned 0 times\n"
				+ "   Bathroom has been cleaned 0 times\n"
				+ "   Game Room has been cleaned 0 times\n"
				+ "]", manager2.getFrequencyReport(10));
		
		//Try a couple different frequencies 
		assertEquals("Frequency of Cleanings [\n"
				+ "   Lunch Room has been cleaned 4 times\n"
				+ "   Bedroom has been cleaned 3 times\n"
				+ "   Closet has been cleaned 3 times\n"
				+ "   Gym has been cleaned 2 times\n"
				+ "   Hallway has been cleaned 2 times\n"
				+ "   Living Room has been cleaned 1 times\n"
				+ "   Office has been cleaned 1 times\n"
				+ "]", manager2.getFrequencyReport(7));
		
		assertEquals("Frequency of Cleanings [\n"
				+ "   Lunch Room has been cleaned 4 times\n"
				+ "   Bedroom has been cleaned 3 times\n"
				+ "]", manager2.getFrequencyReport(2));
		
		assertEquals("Frequency of Cleanings [\n"
				+ "   Lunch Room has been cleaned 4 times\n"
				+ "   Bedroom has been cleaned 3 times\n"
				+ "   Closet has been cleaned 3 times\n"
				+ "   Gym has been cleaned 2 times\n"
				+ "   Hallway has been cleaned 2 times\n"
				+ "]", manager2.getFrequencyReport(5));
		
		assertEquals("Frequency of Cleanings [\n"
				+ "   Lunch Room has been cleaned 4 times\n"
				+ "   Bedroom has been cleaned 3 times\n"
				+ "   Closet has been cleaned 3 times\n"
				+ "]", manager2.getFrequencyReport(3));
		
		//Try a frequency larger than the size of the map
		assertEquals("Frequency of Cleanings [\n"
				+ "   Lunch Room has been cleaned 4 times\n"
				+ "   Bedroom has been cleaned 3 times\n"
				+ "   Closet has been cleaned 3 times\n"
				+ "   Gym has been cleaned 2 times\n"
				+ "   Hallway has been cleaned 2 times\n"
				+ "   Living Room has been cleaned 1 times\n"
				+ "   Office has been cleaned 1 times\n"
				+ "   Attic has been cleaned 0 times\n"
				+ "   Bathroom has been cleaned 0 times\n"
				+ "   Game Room has been cleaned 0 times\n"
				+ "]", manager2.getFrequencyReport(15));
		
		//Try a different manager
		assertEquals("Frequency of Cleanings [\n"
				+ "   Gym has been cleaned 2 times\n"
				+ "]", manager3.getFrequencyReport(1));
		
		//Try a frequency larger than the size of the map
		assertEquals("Frequency of Cleanings [\n"
				+ "   Gym has been cleaned 2 times\n"
				+ "]", manager3.getFrequencyReport(19));
		
		//Test where all in rooms in the map have never been cleaned
		assertEquals("No rooms have been cleaned.", manager4.getFrequencyReport(1));
		
		//Test where there are no rooms in the map.
		assertEquals("No rooms have been cleaned.", manager5.getFrequencyReport(3));
	}

	/**
	 * Tests the getRoomReport method. This method should report all the rooms inside the manager and when
	 * they have been cleaned. Rooms should be ordered by alphabetical order and the dates of the cleaning events
	 * should be in descending order.
	 */
	@Test
	public void testGetRoomReport() {
		assertEquals("Room Report [\n"
				+ "   Dining Room was cleaned on [\n"
				+ "      05/31/2021 09:27:45\n"
				+ "      05/23/2021 18:22:11\n"
				+ "      05/21/2021 09:16:33\n"
				+ "   ]\n"
				+ "   Foyer was cleaned on [\n"
				+ "      05/01/2021 10:03:11\n"
				+ "   ]\n"
				+ "   Guest Bathroom was cleaned on [\n"
				+ "      05/17/2021 04:37:31\n"
				+ "      05/08/2021 07:01:51\n"
				+ "   ]\n"
				+ "   Guest Bedroom was cleaned on [\n"
				+ "      05/23/2021 11:51:19\n"
				+ "      05/13/2021 22:20:34\n"
				+ "   ]\n"
				+ "   Kitchen was cleaned on [\n"
				+ "      (never cleaned)\n"
				+ "   ]\n"
				+ "   Living Room was cleaned on [\n"
				+ "      05/30/2021 10:14:41\n"
				+ "      05/28/2021 17:22:52\n"
				+ "      05/12/2021 18:59:12\n"
				+ "      05/11/2021 19:00:12\n"
				+ "      05/09/2021 18:44:23\n"
				+ "      05/03/2021 17:22:52\n"
				+ "   ]\n"
				+ "   Office was cleaned on [\n"
				+ "      06/01/2021 13:39:01\n"
				+ "   ]\n"
				+ "]", manager.getRoomReport());
		
		//Try with a different manager
		assertEquals("Room Report [\n"
				+ "   Attic was cleaned on [\n"
				+ "      (never cleaned)\n"
				+ "   ]\n"
				+ "   Bathroom was cleaned on [\n"
				+ "      (never cleaned)\n"
				+ "   ]\n"
				+ "   Bedroom was cleaned on [\n"
				+ "      11/08/2022 12:00:00\n"
				+ "      10/10/2022 08:15:34\n"
				+ "      07/04/2022 07:04:22\n"
				+ "   ]\n"
				+ "   Closet was cleaned on [\n"
				+ "      05/08/2022 07:32:10\n"
				+ "      03/14/2022 03:14:15\n"
				+ "      02/21/2022 00:00:00\n"
				+ "   ]\n"
				+ "   Game Room was cleaned on [\n"
				+ "      (never cleaned)\n"
				+ "   ]\n"
				+ "   Gym was cleaned on [\n"
				+ "      10/26/2022 23:59:59\n"
				+ "      10/26/2022 04:17:22\n"
				+ "   ]\n"
				+ "   Hallway was cleaned on [\n"
				+ "      12/31/2022 00:00:00\n"
				+ "      01/01/2022 03:17:22\n"
				+ "   ]\n"
				+ "   Living Room was cleaned on [\n"
				+ "      10/31/2022 05:01:21\n"
				+ "   ]\n"
				+ "   Lunch Room was cleaned on [\n"
				+ "      12/25/2022 02:22:22\n"
				+ "      07/14/2022 04:51:57\n"
				+ "      06/19/2022 06:01:01\n"
				+ "      02/14/2022 04:01:22\n"
				+ "   ]\n"
				+ "   Office was cleaned on [\n"
				+ "      04/20/2022 05:30:59\n"
				+ "   ]\n"
				+ "]", manager2.getRoomReport());
		
		//Try with a different manager
		assertEquals("Room Report [\n"
				+ "   Gym was cleaned on [\n"
				+ "      12/31/2022 23:59:59\n"
				+ "      01/01/2022 00:00:00\n"
				+ "   ]\n"
				+ "]", manager3.getRoomReport());
		
		//Test where all in rooms in the map have never been cleaned
		assertEquals("No rooms have been cleaned.", manager4.getRoomReport());
		
		//Test where there are no rooms in the map.
		assertEquals("No rooms have been cleaned.", manager5.getRoomReport());
	}

}
