# Overview
The Cleaning Manager project allows for the user to import a list of rooms and the date & time they were cleaned in order to receive statistical information based on the data provided. This statistical information can include the number of square feet remaining in a vacuum bag after a certain point in time, the most frequently cleaned rooms, and a list detailing every single cleaning log event of an associated room. The main goal of this project was to try to provide this information efficiently to the user when they import large files.
# Usage
This project is written in java. To run this program, you need to have Java installed.

Follow these steps to run the implementation:

1. Open command prompt or git bash
2. Type the following command to run the jar file: `java -jar CleaningManager.jar`

After this, a menu in the command prompt or git bash should show up asking you to import some room and log files. After successfully importing the files, another menu will display with different options that you can use with the room and log files.

**Note**: The Jar file is placed inside the input folder where you can use some of the input files to test out the CleaningManager.

### Cleaning Manager Options:
**View The Most Frequently Cleaned Rooms:** This option shows you a list of the rooms that have been cleaned the most. The option will prompt you for input based on how many rooms you want to see.

**View a Report of Cleanings by Room:** This option shows you a full report of every room and each date that they have been cleaned. Rooms that have never been cleaned will also be listed but will just display “never cleaned.”

**View Estimated Remaining Vacuum Bag Life:** This option tells you how much space is remaining inside a vacuum bag with a maximum capacity of 5280. This option will prompt you for a date of when the bag was lasted replaced and will then total up all the square footage cleaned past that date to alert you of when the bag should be replaced.
# Design Requirements
### Programming Language
The implementation of this project is required to be created in Java.
## Overall Design
The overall design of this project contains 5 packages with a various amount of classes. Descriptions of each package and they classes they contain are provided below.

**Manager:** This package contains two managers that manage the major functionalities of the cleaning manager.
* The ReportManager class is responsible for reporting information based on the imported room and logs files. Information that can be reported includes the different rooms available and how often they have been cleaned.
* The CleaningManager class is responsible for importing a list of rooms and cleaning events into a map which can then be used by the ReportManager to report all the information gathered.

**UI:** This package contains the CleaningMangerUI class. The CleaningMangerUI class provides a user interface which can allow for anyone to interact with managers. The user can use the CleaningMangerUI to import files containing rooms and cleaning events and then see different reports based on the files they imported.

**Data:** The data package contains two classes that represent the room and cleaning events objects.
* The RoomRecord class represents the rooms that are cleaned in the CleaningManager. Rooms come with an ID to identify them and a width and length to show how large they are. The code for this class was provided in a jar file.
* The CleaningLogEntry class represents the cleaning log entries which tell when a room was cleaned. Cleaning log entries come with a timestamp to tell when the event happened, the room that was cleaned, and percentage of that room cleaned. The code for this class was provided in a jar file.

**Factory**: This package contains the DSAFactory class. The DSAFactory class gives quick access to different types of data structures that are used in the algorithms for the ReportManager and CleaningManager classes. A partial implementation for this class was provided and could be edited to use the appropriate data structures needed for this project.

**IO:** This package contains the InputReader class. The InputReader class is used to read in the input files containing rooms and log entries. This class will read each room or cleaning log entry and put them into a list of RoomRecords and CleaningLogEntries which can then be used by the CleaningManger class. This code was provided in the form of a jar file.

### Additional Information
The implementation for this project was a small section of a larger 3-part project in my Data Structures class. Part 1 of this project involved designing algorithms for the implementation part of the project, with the implementation being the second part. Part 3 was an analysis where we would review and analyze how efficient our algorithms were. Part 1 and 3 allowed for students to work in pairs but the actual implementation for this project was entirely independent and the algorithms created in the previous part are not required to be used for the actual implementation. So, all the implementation for this project was done entirely by me. Any code that was provided belongs entirely to the NCSU CSC Department.

# Implementation
The first part of this project allowed us to work in pairs to create algorithms that could be used in the implementation part of the CleaningManager. However, since this project is entirely dependent on trying to be as efficient as possible, the algorithms that my partner and I originally created were not sufficient for the needs of the project. The algorithm in the method getEventByRoom() is the main algorithm used by the CleaningManager to put all the rooms and cleaning events into a map that could be easily accessed by other methods and classes. The original runtime for the first algorithm that we created was O(n^2).  This algorithm was too slow and needed to be adjusted. What I did to fix this was to first change our data structure from a SearchTableMap to a SkipListMap. Originally our first algorithm was developed with the SearchTableMap in mind since the runtimes for a SearchTableMap were consistent. However, while a SkipListMap’s worst case runtimes are O(n), it can theoretically reach runtimes way faster than a SearchTableMap (O(logn)). This was the first mistake. The second mistake was that the original algorithm had a nested for loop. By having a nested for loop this dramatically increased the runtime, so the new algorithm I had to create needed to avoid having nested loops to save time. By fixing these two major mistakes, I was able to bring the runtime down to a theoretical O(nlogn). This made the whole program significantly faster than if I were to use the original algorithm that my partner and I had at the beginning. My partner and I also created another algorithm for the method getCoverageSince() which is used to get the total square footage cleaned from a certain date. This algorithm also needed optimizations since the first algorithm was also very slow. The original algorithm also featured a nested loop so I needed to create a new algorithm that would avoid using nested loops. Once I did this, getCoverageSince() was much faster than the first algorithm that was designed for it. There were also other parts of this project that needed to be efficient. For example, this project also required cleaning events to be sorted when the user asks for the room report. To provide the best runtime for this, I had to use the most efficient sorting algorithm, that being merge sort, since it has a runtime of O(nlogn).  Making thoughtful decisions and analyzing my mistakes was key to making this project as efficient as possible. 
