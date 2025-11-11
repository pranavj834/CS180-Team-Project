# Team Project - Phase 1

Team members: Shawn Zhu, Ryan Chan, Pranav Jasti

### How to Compile and Run Project

Running javac *.java and java fileName should compile and run the project. Alternatively, the run button on 
IntelliJ/VS Code/other IDEs should work. The Main class provides a command-line menu interface for the database,
although there is also DatabaseTest, ReservationTest, and UserAccountTest.

#### Ryan Chan - Submitted Vocareum workspace

## Core Classes
### Database.java

Purpose: Manages user data and reservations using UserAccount and Reservation lists to communicate with a future server.

Dependencies: Implements DatabaseInterface, uses UserAccount and Reservation.

Methods (excluding generic getters/setters):

- addAccount: If it doesn't exist already, adds account and returns true. Otherwise returns false.
- addReservation: If the time slot is not currently occupied and the account is registered, adds a reservation and 
returns true. Otherwise returns false.
- deleteAccount: If the account is registered, remove all reservations registered under the account before 
removing the account and returning true. If the account is not regist returns false.
- deleteReservation: If both reservation and account exist and the reservation was registered under the account,
returns true. Otherwise returns false.
- getAccounts: Returns the internal list of Accounts.
- getReservations: Returns the internal list of Reservations.
- toString: Prints all user accounts and reservations.


### UserAccount.java

Purpose: Represents a single user profile.

Functionality: Stores a username, password, name, email, and a list of their specific Reservation objects. 
Contains an equals() method that compares username and password to verify user login credentials. 

Dependencies: Implements UserAccountInterface. Uses Reservation.

Methods (excluding generic getters/setters):
- addReservation: Adds a reservation to the internal Reservation list.
- removeReservation: Removes a reservation to the internal Reservation list.
- equals: If username and password match, returns true. If not (or if object is null/another type), returns false.
- toString: Returns a string with the name, username, and email.

### Reservation.java

Purpose: Represents a single booking instance.

Functionality: Stores booking details, namely name, username, date, time, number of people, and an list of seats
(expressed as integers).

Dependencies: Implements ReservationInterface.

Methods (excluding generic getters/setters):
- equals: If all six fields match, returns true. If not (or if object is null/another type), returns false.
- toString: Returns a string with the name, date, time, and number of people.

## Execution & Interface Classes
### Main.java

Purpose: Provides an intuitive way to interact with the database. In the future, will launch a GUI instead of
a command line interface. 

Functionality: Runs a loop with a menu to take user input and interact with the database and its methods.
Instructions are provided in the menu

Dependencies: Implements MainInterface. Uses the above classes.

### Screen.java (Placeholder)

Purpose: Placeholder for future GUI development. Non-functional.

Dependencies: Implements ScreenInterface.

### Interfaces (*Interface.java)

Purpose: Provides the blueprint methods for Database, UserAccount, Reservation, and Screen.

## Testing Classes (*Test.java)

### DatabaseTest.java

Purpose: Tests getters, adding/deleting accounts and reservations, and toString.

### UserAccountTest.java

Purpose: Tests initialization, equals, adding/removing reservations, getters/setters, and toString.

### ReservationTest.java

Purpose: Tests initialization, updates, equals, and toString.