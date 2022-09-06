# Overview
The Cleaning Manager project allows for the user to import a list of rooms and the times they were cleaned in order to receive statistical information based on data provided. This statistical information can include the number of square feet remaining in vacuum bag after a certain point in time, the most frequently cleaned rooms, and a list detailing every single cleaning log event of an associated room. The main goal of this project was to try and provide this information efficiently to the user when they import large files.
# Usage
This project is written in java. To run this program, you need to have Java installed.
Follow these steps to run the implementation:
1. Open command prompt or git bash
2. Type the following command to run the jar file: `java -jar CleaningManager.jar`
After this, a menu in the command prompt or git bash should show up asking you to import some room and log files. After successfully importing the files, another menu will display with different options that you can use with the room and log files.
### Cleaning Manager Options:
View The Most Frequently Cleaned Rooms: This option shows you a list of the rooms that have been cleaned the most. The option will prompt you for input based on how many rooms you want to see.

View a Report of Cleanings by Room: This option shows you a full report of every room and each date that they have been cleaned. Rooms that have never been cleaned will also be listed but will just display “never cleaned.”
View Estimated Remaining Vacuum Bag Life: This option tells you how much space is remaining inside a vacuum bag with a maximum capacity of 5280. This option will prompt you for a date of when the bag was lasted replaced and will then total up all the square footage cleaned past that date to alert you of when the bag should be replaced.
# Design Requirements
### Programming Language
The implementation of this project is required to be created in Java.
### UML Diagram
The overall design of this project contains 5 packages with a various amount of classes in each package to make the Cleaning Manager work. The UML below shows all the different classes and each package they belong to.

![CleaningManagerUML](https://user-images.githubusercontent.com/112775148/188530911-df0912c3-2817-4014-9668-e12ac7836464.png)


**Manager:** This package contains two managers that manage the major functionalities of the cleaning manager.
* The ReportManager class is responsible for reporting information based on the imported room and logs files. Information that can be reported includes the different rooms available and how often they have been cleaned.
* The CleaningManager class is responsible for importing a list of rooms and cleaning events into a map which can then be used by the ReportManager to report all the information gathered. 
**UI:** This package contains the CleaningMangerUI class. The CleaningMangerUI class provides a user interface which can allow for anyone to interact with managers. The user can use the CleaningMangerUI to import files containing rooms and cleaning events and then see different reports based on the files they imported.

**Data:** The data package contains two classes that represent the room and cleaning events objects.
* The RoomRecord class represents the rooms that are cleaned in the CleaningManager. Rooms come with an ID to identify them and a width and length to show how large they are. The code for this class was provided in a jar file
* The CleaningLogEntry class represents the cleaning log entries which tell when a room was cleaned. Cleaning log entries come with a timestamp to tell when the event happened, the room that was cleaned, and percentage of that room cleaned. The code for this class was provided in a jar file.
Factory: This package contains the DSAFactory class. The DSAFactory class gives quick access to different types of data structures that are used in the algorithms for the ReportManager and CleaningManager classes. A partial implementation for this class was provided and could be edited to use the appropriate data structures needed for this project.

**IO:** This package contains the InputReader class. The InputReader class is used to read in the input files containing rooms and log entries. This class will read each room or cleaning log entry and put them into a list of RoomRecords and CleaningLogEntries which can then be used by the CleaningManger class. This code was provided in the form of a jar file.

### Additional Information
This project was split into 3 parts. Part 1 is designing algorithms, part 2 is implementation, and part 3 is experimental analysis. Parts 1 and 3 were worked in pairs while part 2 was strictly individual. The algorithms created in part 1 may be used in part 2 but are not required and can be adjusted. The only data structures that can be accessed for this project are the ArrayList, LinkedList, UnorderedLinkedMap, SearchTableMap, and SkipListMap. All sorting algorithms in the DSA library are allowed. Java.util libraries are only allowed for throwing exceptions and scanning user input. No libraries outside of the DSA library are allowed except for the listed exceptions. All diagrams presented in this README belong to the NCSU CSC Department.

# Implementation
The first part 1 of this project allowed us to work in pairs to create algorithms that could be used in the implementation part of the CleaningManager. However, since this project is entirely dependent on trying to be as efficient as possible, the algorithms that my partner and I originally created were not efficient as they needed to be. The algorithm getEventByRoom() is the main algorithm used by the CleaningManager to put all the rooms and cleaning events into a map that could be easily accessed by other methods and classes. The original runtime for the first algorithm that was created was O(n^2).  This algorithm was too slow and need to be adjusted. What I did to fix this was to first change our data structure from a SearchTableMap to a SkipListMap. Originally our first algorithm was developed with the SearchTableMap in mind since the runtimes for a SearchTableMap were consistent. However, while a SkipListMap’s worst case runtimes are O(n), it can theoretically reach runtimes way faster than a SearchTableMap (O(logn)). This was the first mistake. The second mistake was the first algorithm had a nested for loop. By having a nested for loop this dramatically increases runtime so the new algorithm I had to create need to avoid having nested loops to save time. By fixing these two major mistakes, I was able to bring the runtime down to a theoretical O(nlogn). This made the whole program significantly faster than if I were to use the original algorithm that my partner and I had at the beginning. My partner and I also created another algorithm for getCoverageSince() which is used to get the total square footage cleaned from a certain date. This algorithm also needed optimizations as well since the first algorithm was also very slow. The original algorithm as featured a nested loops so I need to create a new algorithm that would avoid using nested loops. Once I did this the getCoverageSince() was much faster than the first algorithm that was designed for it. There were also other parts of this project I need to efficient in too. For example, this project also required for our cleaning events to be sorted when the user asks for the room report. To provide the best runtimes for this, I had to use the more efficient sorting algorithm, that being merge sort since it has a runtime of O(nlogn). Decisions like these allowed for faster execution overall in all parts of the project.
