# Team Project - Phase 2 (updated 11/24)
## Team members: Ryan Chan, Pranav Jasti

### How to Compile and Run Project

Compile the project by running javac *.java.

To run the Server, execute java Server. The server will start listening for client connections on port 500. To
run the Client, execute java Client. The client will attempt to connect to the server on localhost:500 and 
present a command-line menu interface.

Ensure the server is running before attempting to run the client.

To test network IO, try printing the database by entering 7. The server console should indicate that it has
received a packet with type TO_STRING. Then try adding an account by entering 1 and the corresponding information, 
then entering 0 and logging into that account. 

From there, you can test out options 2-6 to add, delete, and get information from, as well as entering 
nonsensical values or items in the wrong format. The menu is self-explanatory.

## Core Classes
### Database.java

Purpose: Persists user account and reservation data by loading and saving information to text files 
(accounts.txt and reservations.txt). It also manages the seat availability status for all reservations 
across different time slots using a HashMap<String, boolean[]>.

Functionality: Now includes methods for loading (loadFromFiles) and saving (saveToFiles) the entire 
database state to disk, making the data persistent between server restarts. It uses synchronization for thread-safety.

Dependencies: Implements DatabaseInterface, uses UserAccount and Reservation.

Methods (excluding generic getters/setters and Persistence API methods):
- addAccount: Adds account if it doesn't exist; returns success/failure. Saves data.
- addReservation: Checks for time conflicts/account existence, adds reservation, updates seatStatuses. 
Returns success/failure. Saves data.
- deleteAccount: Removes account and all its associated reservations. Returns success/failure. Saves data.
- deleteReservation: Removes a specific reservation from the account and the main list, and frees up the 
reserved seats in seatStatuses. Returns success/failure. Saves data.
- getAccountReservations: Returns a list of reservations made by a specific account.
- getSeatStatusesAtTime: Returns a boolean[] of the 30 seat statuses for a given timestamp, creating a 
new array if the timestamp is not yet tracked.
- toString: Prints all user accounts and reservations.

## Network Communication Classes (New)
### Server.java

Purpose: Listens for incoming client connections and handles client requests in separate threads. It acts
as the intermediary between the client and the Database. 

Functionality: Implements the Runnable interface to run the server in a separate thread. It continuously 
accepts client connections and delegates each one to the handleClient method, which runs on a new thread. 
The handleClient method reads incoming Packet objects, processes the request by interacting with the 
Database, and sends a response Packet back.

Dependencies: Implements ServerInterface and Runnable. Uses Database, Packet, and PacketType.

Methods (excluding main and startServer):
- run: The main server loop; binds a ServerSocket to the PORT (500) and continuously calls accept().
- handleClient: Manages the communication lifecycle for a single client connection, reading/writing Packet 
objects indefinitely until the client disconnects or an error occurs.

### Client.java

Purpose: Provides the command-line interface for the user and communicates with the Server using the defined protocol.

Functionality: Connects to the server via a Socket and uses ObjectOutputStream and ObjectInputStream to send
and receive Packet objects. It presents a menu for logged-out and logged-in users, translating user choices 
into network requests (Packet objects) and processing the server's responses.

Dependencies: Implements ClientInterface. Uses UserAccount, Reservation, Packet, and PacketType.

### Packet.java

Purpose: Encapsulates the data being sent over the network.

Functionality: Contains a PacketType (the header) to specify the request/response type and an Object[] array
(the payload) to hold the data (e.g., UserAccount, Reservation, status boolean).

Dependencies: Implements PacketInterface and Serializable. Uses PacketType.

### PacketType.java

Purpose: An enum that defines all possible operations and messages exchanged between the client and the server.

Functionality: Used to identify the intent of a Packet. Examples include LOGIN, ADD_RESERVATION, 
GET_SEAT_STATUSES_AT_TIME, etc.

## Utility Classes (Phase 1)

### UserAccount.java

Purpose: Represents a single user profile.

Functionality: Stores credentials and personal information, validates input, and holds a list of the user's 
Reservation objects. The equals method only checks username and password for login verification. Now uses 
synchronized methods for thread safety.

Dependencies: Implements UserAccountInterface, uses Reservation.

### Reservation.java

Purpose: Represents a single booking instance.

Functionality: Stores booking details (name, date, time, party size, seats) and performs basic input 
validation using regex for date/time formats.

Dependencies: Implements ReservationInterface.

## Testing Classes (*Test.java)

The project includes test classes for the core components:

DatabaseTest.java (Tests account/reservation management and persistence, but not network I/O).

UserAccountTest.java (Tests getters, setters, validation, and equality).

ReservationTest.java (Tests getters, setters, validation, and equality).

ServerTest.java.

ClientTest.java.

-----------------------------------------------------------------------------

# Team Project - Phase 1 (updated 11/10)

Team members: Shawn Zhu, Ryan Chan, Pranav Jasti

### How to Compile and Run Project

Running javac *.java and java fileName should compile and run the project. Alternatively, the run button on 
IntelliJ/VS Code/other IDEs should work. The Main class provides a command-line menu interface for the database,
although there is also DatabaseTest, ReservationTest, and UserAccountTest.

#### Ryan Chan - Submitted Vocareum workspace

## Core Classes
### Database.java

Purpose: Manages user data and reservations using UserAccount and Reservation lists to communicate 
with a future server.

Dependencies: Implements DatabaseInterface, uses UserAccount and Reservation.

Methods (excluding generic getters/setters):

- addAccount: If it doesn't exist already, adds account and returns true. Otherwise returns false.
- addReservation: If the time slot is not currently occupied and the account is registered, adds a reservation and 
returns true. Otherwise returns false.
- deleteAccount: If the account is registered, remove all reservations registered under the account before 
removing the account and returning true. If the account is not registered, returns false.
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
