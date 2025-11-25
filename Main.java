import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the entire program (as of phase 1, is only terminal based).
 * NOTE: There is no MainInterface because the main method is static
 *       and the main method is the only method in this class. As of
 *       phase 2, this class is non-functional.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version November 10, 2025
 */

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Database db = new Database();
        System.out.println("Database Testing - Phase 1");

        while (true) {
            System.out.println("Menu:");
            System.out.println("1 - Add UserAccount");
            System.out.println("2 - Add Reservation");
            System.out.println("3 - Delete UserAccount");
            System.out.println("4 - Delete Reservation");
            System.out.println("5 - Print Database Details");
            System.out.println("6 - Exit");

            try {
                int input = Integer.parseInt(sc.nextLine());
                if (input == 1) {
                    System.out.println("Enter account username, password, full name, and email (space-separated):");
                    String[] info = sc.nextLine().split("\\s+");

                    // if successfully added
                    if (db.addAccount(new UserAccount(info[0], info[1], info[2], info[3]))) {
                        System.out.println("Account successfully added");
                    } else {
                        System.out.println("Failed to add.");
                    }
                } else if (input == 2) {
                    System.out.println("Enter account username, password, full name, and email (space-separated):");
                    String[] acctInfo = sc.nextLine().split("\\s+");
                    UserAccount acct = new UserAccount(acctInfo[0], acctInfo[1], acctInfo[2], acctInfo[3]);

                    System.out.println("Enter reservation name, username, date, time, " +
                            "and number of people (space-separated):");
                    String[] reservation = sc.nextLine().split("\\s+");

                    System.out.println("Enter integer seats (space-separated):");
                    ArrayList<Integer> seats = new ArrayList<>();
                    String[] seatInfo = sc.nextLine().split("\\s+");
                    for (String seat: seatInfo) {
                        System.out.println(seat);
                        seats.add(Integer.parseInt(seat));
                    }

                    // if successfully added
                    if (db.addReservation(acct, new Reservation(reservation[0], reservation[1], reservation[2],
                            reservation[3], Integer.parseInt(reservation[4]), seats))) {
                        System.out.println("Reservation successfully added.");
                    } else {
                        System.out.println("Failed to add.");
                    }
                } else if (input == 3) {
                    System.out.println("Enter account username, password, full name, and email (space-separated):");
                    String[] info = sc.nextLine().split("\\s+");

                    // if successfully deleted
                    if (db.deleteAccount(new UserAccount(info[0], info[1], info[2], info[3]))) {
                        System.out.println("Account successfully deleted.");
                    } else {
                        System.out.println("Failed to delete.");
                    }
                } else if (input == 4) {
                    System.out.println("Enter account username, password, full name, and email (space-separated):");
                    String[] acctInfo = sc.nextLine().split("\\s+");
                    UserAccount acct = new UserAccount(acctInfo[0], acctInfo[1], acctInfo[2], acctInfo[3]);

                    System.out.println("Enter reservation name, username, date, time, " +
                            "and number of people (space-separated):");
                    String[] reservation = sc.nextLine().split("\\s+");

                    System.out.println("Enter integer seats (space-separated):");
                    ArrayList<Integer> seats = new ArrayList<>();
                    String[] seatInfo = sc.nextLine().split("\\s+");
                    for (String seat: seatInfo) {
                        seats.add(Integer.parseInt(seat));
                    }

                    // if successfully deleted
                    if (db.deleteReservation(acct, new Reservation(reservation[0], reservation[1], reservation[2],
                            reservation[3], Integer.parseInt(reservation[4]), seats))) {
                        System.out.println("Reservation successfully deleted.");
                    } else {
                        System.out.println("Failed to delete.");
                    }
                } else if (input == 5) {
                    System.out.println(db);
                } else if (input == 6) {
                    return;
                } else {
                    System.out.println("Unrecognized input. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
                e.printStackTrace();
                // 1
                // janedoe 234 JaneDoe jane@email.com
                // 2
                // janedoe 234 JaneDoe jane@email.com
                // JaneDoe janedoe 2025-01-01 13:30 2
                // 1 2
                // 1
                // johndoe 123 JohnDoe john@email.com
                // 2
                // janedoe 234 JaneDoe jane@email.com
                // JohnDoe johndoe 2025-01-31 18:00 3
                // 3 4 5
            }
        }
    }
}

/*
NOTE: graphics functionality has been disabled for phase 1. the above code is for testing overall functionality.

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Restaurant Reservation");

        Screen sc = new Screen();
        frame.add(sc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}*/