import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Stores UserAccount and Reservation info to be
 * accessed by the server. Saves information to
 * text files and retains information in between runs.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, zhu1220, lab sec L23
 * @version November 24, 2025
 */

public class Database implements DatabaseInterface {
    private final ArrayList<UserAccount> accounts;
    private final ArrayList<Reservation> reservations;
    private final HashMap<String, boolean[]> seatStatuses;
    private String accountFile;
    private String reservationFile;

    // initializes arraylists
    public Database(String accountFile, String reservationFile) {
        accounts = new ArrayList<>();
        reservations = new ArrayList<>();
        seatStatuses = new HashMap<>();
        this.accountFile = accountFile;
        this.reservationFile = reservationFile;
        loadFromFiles(accountFile, reservationFile);
    }

    // =============== PERSISTENCE API =================

    /**
     * Saves all accounts and reservations to the given text files.
     *
     * @param accountFile      path for accounts file
     * @param reservationFile  path for reservations file
     * @throws IOException if writing fails
     */
    @Override
    public synchronized void saveToFiles(String accountFile, String reservationFile) {
        try {
            saveAccounts(accountFile);
            saveReservations(reservationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all accounts and reservations from the given text files.
     * Existing in-memory data is cleared first.
     *
     * @param accountFile      path for accounts file
     * @param reservationFile  path for reservations file
     * @throws IOException if reading fails
     */
    @Override
    public synchronized void loadFromFiles(String accountFile, String reservationFile) {
        accounts.clear();
        reservations.clear();
        try {
            loadAccounts(accountFile);
            loadReservations(reservationFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Reservation reservation: reservations) {
            String username = reservation.getUsername();
            for (UserAccount account: accounts) {
                if (account.getUsername().equals(username)) {
                    System.out.println("added " + reservation + " to " + account);
                    account.addReservation(reservation);
                    break;
                }
            }
        }
    }

    // ---------- internal: accounts ----------

    private void saveAccounts(String accountFile) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(accountFile))) {
            out.println(accounts.size());
            for (UserAccount account : accounts) {
                // username password fullName email
                out.printf("%s %s %s %s%n",
                        account.getUsername(),
                        account.getPassword(),
                        account.getFullName(),
                        account.getEmail());
            }
        }
    }

    private void loadAccounts(String accountFile) throws IOException {
        File f = new File(accountFile);
        if (!f.exists()) {
            return; // nothing to load yet
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) return;

            int count = Integer.parseInt(line.trim());
            for (int i = 0; i < count; i++) {
                String data = br.readLine();
                if (data == null) break;
                String[] parts = data.split(" ");
                if (parts.length < 4) continue;

                String username = parts[0];
                String password = parts[1];
                String fullName = parts[2];
                String email = parts[3];

                UserAccount acct = new UserAccount(username, password, fullName, email);
                accounts.add(acct);
            }
        }
    }

    // ---------- internal: reservations ----------

    private void saveReservations(String reservationFile) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(reservationFile))) {
            // First line: number of reservations
            out.println(reservations.size());

            for (Reservation r : reservations) {
                // fullName username YYYY-MM-DD HH:MM partySize
                out.printf("%s %s %s %d%n",
                        r.getName(),
                        r.getUsername(),
                        r.getTimestamp(),
                        r.getPartySize());

                // Second line: list of seat numbers, space-separated
                List<Integer> seats = r.getSeats();
                if (seats != null && !seats.isEmpty()) {
                    for (int i = 0; i < seats.size(); i++) {
                        if (i > 0) {
                            out.print(" ");
                        }
                        out.print(seats.get(i));
                    }
                }
                out.println();
            }
        }
    }

    private void loadReservations(String reservationFile) throws IOException {
        File f = new File(reservationFile);
        if (!f.exists()) {
            return; // nothing to load yet
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) return;

            int count = Integer.parseInt(line.trim());
            for (int i = 0; i < count; i++) {
                String header = br.readLine();
                if (header == null) break;

                String[] parts = header.split(" ");
                if (parts.length < 5) continue;

                String fullName = parts[0];
                String username = parts[1];
                String date = parts[2]; // "YYYY-MM-DD"
                String time = parts[3]; // "HH:MM"
                int partySize = Integer.parseInt(parts[4]);

                String seatLine = br.readLine();
                ArrayList<Integer> seatNumbers = new ArrayList<>();
                if (seatLine != null && !seatLine.trim().isEmpty()) {
                    String[] seatParts = seatLine.trim().split(" ");
                    for (String s : seatParts) {
                        seatNumbers.add(Integer.parseInt(s));
                    }
                }

                String timeslot = date + " " + time;
                boolean[] restaurantSeats = seatStatuses.get(timeslot); // get the availabilities at the time
                if (restaurantSeats == null) {
                    restaurantSeats = new boolean[30]; // if doesn't exist yet, everything at the timeslot is free
                }
                for (Integer seatNumber: seatNumbers) {
                    restaurantSeats[seatNumber] = true; // mark seats as reserved
                }
                seatStatuses.put(timeslot, restaurantSeats);

                Reservation r = new Reservation(fullName, username, timeslot, partySize, seatNumbers);
                reservations.add(r);

                // also attach reservation to matching account if present
                for (UserAccount acct : accounts) {
                    if (acct.getUsername().equals(username)) {
                        acct.addReservation(r);
                        break;
                    }
                }
            }
        }
    }

    // =============== EXISTING METHODS =================

    // adds account
    @Override
    public synchronized boolean addAccount(UserAccount account) {
        if (accounts.contains(account)) {
            return false;
        }
        for (UserAccount acct: accounts) {
            if (acct.getUsername().equals(account.getUsername())) {
                return false;
            }
        }
        accounts.add(account);
        saveToFiles(accountFile, reservationFile);
        return true;
    }

    // adds reservation
    @Override
    public synchronized boolean addReservation(UserAccount account, Reservation reservation) {
        for (Reservation r : reservations) {
            if (r.getTimestamp().equals(reservation.getTimestamp())) {
                return false;
            }
        }

        // the account needs to exist to add a reservation under it
        if (!accounts.contains(account)) {
            return false;
        }

        accounts.get(accounts.indexOf(account)).addReservation(reservation);
        account.addReservation(reservation);
        reservations.add(reservation);

        boolean[] restaurantSeats = seatStatuses.get(reservation.getTimestamp()); // get the availabilities at the time
        if (restaurantSeats == null) {
            restaurantSeats = new boolean[30]; // if doesn't exist yet, everything at the timeslot is free
        }
        ArrayList<Integer> seats = reservation.getSeats();
        for (Integer seatNumber: seats) {
            restaurantSeats[seatNumber] = true; // mark seats as reserved
        }
        seatStatuses.put(reservation.getTimestamp(), restaurantSeats);
        saveToFiles(accountFile, reservationFile);
        return true;
    }

    // deletes account
    @Override
    public synchronized boolean deleteAccount(UserAccount account) {
        if (accounts.contains(account)) {
            for (Reservation r : account.getReservations()) {
                // must remove all associated reservations with the account
                reservations.remove(r);
            }
            accounts.remove(account);
            saveToFiles(accountFile, reservationFile);
            return true;
        }
        return false;
    }

    // deletes reservation
    @Override
    public synchronized boolean deleteReservation(UserAccount account, Reservation reservation) {
        // fail if account doesn't exist
        System.out.println("database delete reservation");
        int index = accounts.indexOf(account);
        if (index == -1) {
            System.out.println("account not found");
            return false;
        }

        // returns true if reservation was found under that account and removed, false otherwise
        boolean removed = accounts.get(index).removeReservation(reservation);
        if (removed) {
            reservations.remove(reservation); // only remove if the given account created the reservation

            boolean[] restaurantSeats = seatStatuses.get(reservation.getTimestamp()); // get the availabilities at the time
            ArrayList<Integer> seats = reservation.getSeats();
            for (Integer seatNumber: seats) {
                restaurantSeats[seatNumber] = false; // free up seats
            }
            seatStatuses.put(reservation.getTimestamp(), restaurantSeats);
            saveToFiles(accountFile, reservationFile);
            return true;
        }
        return false;
    }

    // getters — return defensive copies
    @Override
    public synchronized ArrayList<UserAccount> getAccounts() {
        return new ArrayList<>(accounts);
    }

    @Override
    public synchronized ArrayList<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    @Override
    public synchronized ArrayList<Reservation> getAccountReservations(UserAccount account) {
        ArrayList<Reservation> accountReservations = new ArrayList<>();
        for (Reservation res: reservations) {
            if (res.getUsername().equals(account.getUsername())) {
                accountReservations.add(res);
            }
        }
        return accountReservations;
    }

    @Override
    public synchronized HashMap<String, boolean[]> getSeatStatuses() {
        return seatStatuses;
    }

    @Override
    public synchronized boolean[] getSeatStatusesAtTime(String timestamp) {
        if (seatStatuses.get(timestamp) == null) {
            seatStatuses.put(timestamp, new boolean[30]);
        }
        return seatStatuses.get(timestamp);
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Accounts:\n");
        for (UserAccount account : accounts) {
            sb.append(account).append("\n");
        }
        sb.append("\nReservations:\n");
        for (Reservation reservation : reservations) {
            sb.append(reservation).append("\n");
        }
        return sb.toString();
    }
}
