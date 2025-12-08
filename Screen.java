import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Provides the GUI.
 * NOTE: Screen is currently non-functional and is not
 *       meant to be run or tested. It, along with this
 *       class, are placeholders at the time of writing.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author jastip, chan531, lab sec L23
 * @version December 6, 2025
 */

public class Screen extends JPanel implements ActionListener, ScreenInterface {
    //instance variables
    private Color background;
    private Color text;

    // fields for login and register
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField emailField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton createAccountButton;
    private JButton backToLoginButton;

    // fields for after logging in.
    private JButton makeReservationScreenButton;
    private JButton viewReservationScreenButton;
    private JButton deleteAccountButton;
    private JButton logoutButton;
    private JTextField nameField2;
    private JTextField timestampField;
    private JTextField partySizeField;
    private JTextField seatsField; // user enters "1,2,3"
    private JButton submitReservationButton;
    private JButton checkSeatsButton; // to call updateSeatStatuses
    private JButton backToDashButton;
    private JTextArea displayArea; // to show getReservations or printDatabase
    private JScrollPane scrollPane;
    private JButton deleteReservationButton;
    private JButton refreshReservationButton;
    private JButton printDbButton;
    private JPanel seatingDisplay;
    private JLabel[][] grid;
    private int row = 5;
    private int col = 6;

    private JLabel errorLabel;

    // Screen #-What screen they lead to: 0-Login, 1-Register, 2-Dashboard, 3-Make Reservation, 4-View/Manage
    private int currentScreen;
    private Client client;

    //constructors
    public Screen(Client client) {
        this.client = client;
        background = new Color(35, 37, 40);
        text = new Color(255, 255, 255);
        currentScreen = 0;

        //initializing fields
        usernameField = new JTextField();
        usernameField.setBounds(160, 180, 200, 30);
        add(usernameField);
        usernameField.addActionListener(this);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 230, 200, 30);
        add(passwordField);
        passwordField.addActionListener(this);

        nameField = new JTextField();
        nameField.setBounds(160, 280, 200, 30);
        add(nameField);
        nameField.setVisible(false);

        emailField = new JTextField();
        emailField.setBounds(160, 330, 200, 30);
        add(emailField);
        emailField.setVisible(false);

        loginButton = new JButton("Login");
        loginButton.setBounds(50, 280, 100, 30);
        add(loginButton);
        loginButton.addActionListener(this);

        registerButton = new JButton("Register");
        registerButton.setBounds(50, 400, 100, 30);
        add(registerButton);
        registerButton.addActionListener(this);

        createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(50, 380, 150, 30); // widened slightly
        createAccountButton.addActionListener(this);
        createAccountButton.setVisible(false);
        add(createAccountButton);

        backToLoginButton = new JButton("Back");
        backToLoginButton.setBounds(210, 380, 100, 30);
        backToLoginButton.addActionListener(this);
        backToLoginButton.setVisible(false);
        add(backToLoginButton);

        makeReservationScreenButton = new JButton("Make Reservation");
        makeReservationScreenButton.setBounds(265, 150, 200, 40);
        makeReservationScreenButton.addActionListener(this);
        makeReservationScreenButton.setVisible(false);
        add(makeReservationScreenButton);

        viewReservationScreenButton = new JButton("View/Edit Reservations");
        viewReservationScreenButton.setBounds(265, 210, 200, 40);
        viewReservationScreenButton.addActionListener(this);
        viewReservationScreenButton.setVisible(false);
        add(viewReservationScreenButton);

        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBounds(265, 270, 200, 40);
        deleteAccountButton.addActionListener(this);
        deleteAccountButton.setBackground(Color.RED);
        deleteAccountButton.setOpaque(true);
        deleteAccountButton.setVisible(false);
        add(deleteAccountButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(265, 330, 200, 40);
        logoutButton.addActionListener(this);
        logoutButton.setVisible(false);
        add(logoutButton);

        nameField2 = new JTextField();
        nameField2.setBounds(200, 100, 100, 30);
        nameField2.setVisible(false);
        add(nameField2);

        timestampField = new JTextField();
        timestampField.setBounds(200, 150, 100, 30);
        timestampField.setVisible(false);
        add(timestampField);

        partySizeField = new JTextField();
        partySizeField.setBounds(200, 200, 100, 30);
        partySizeField.setVisible(false);
        add(partySizeField);

        seatsField = new JTextField();
        seatsField.setBounds(200, 250, 100, 30);
        seatsField.setVisible(false);
        add(seatsField);

        submitReservationButton = new JButton("Book Table");
        submitReservationButton.setBounds(50, 300, 150, 30);
        submitReservationButton.addActionListener(this);
        submitReservationButton.setVisible(false);
        add(submitReservationButton);

        checkSeatsButton = new JButton("Check Availability");
        checkSeatsButton.setBounds(50, 350, 150, 30);
        checkSeatsButton.addActionListener(this);
        checkSeatsButton.setVisible(false);
        add(checkSeatsButton);

        backToDashButton = new JButton("Main Menu");
        backToDashButton.setBounds(50, 400, 150, 30);
        backToDashButton.addActionListener(this);
        backToDashButton.setVisible(false);
        add(backToDashButton);

        displayArea = new JTextArea();
        displayArea.setLineWrap(true);
        displayArea.setEditable(false);
        scrollPane = new JScrollPane(displayArea);
        scrollPane.setBounds(350, 100, 320, 150);
        scrollPane.setVisible(false);
        add(scrollPane);

        refreshReservationButton = new JButton("Refresh List");
        refreshReservationButton.setBounds(50, 100, 150, 30);
        refreshReservationButton.addActionListener(this);
        refreshReservationButton.setVisible(false);
        add(refreshReservationButton);

        deleteReservationButton = new JButton("Delete Above");
        deleteReservationButton.setBounds(50, 340, 150, 30);
        deleteReservationButton.addActionListener(this);
        deleteReservationButton.setVisible(false);
        add(deleteReservationButton);

        printDbButton = new JButton("Print Full DB");
        printDbButton.setBounds(220, 100, 120, 30);
        printDbButton.addActionListener(this);
        printDbButton.setVisible(false);
        add(printDbButton);

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(50, 440, 600, 30);
        add(errorLabel);

        seatingDisplay = new JPanel();
        seatingDisplay.setBounds(350, 270, 320, 170);
        seatingDisplay.setLayout(new GridLayout(row, col));

        grid = new JLabel[row][col];
        int seatNum = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = new JLabel();
                grid[i][j].setBorder(new LineBorder(background, 1));
                grid[i][j].setBackground(new Color(55, 55, 55));
                grid[i][j].setOpaque(true);
                seatingDisplay.add(grid[i][j]);

                grid[i][j].setText(String.valueOf(seatNum));
                grid[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                grid[i][j].setForeground(Color.WHITE);
                seatNum++;
            }
        }
        seatingDisplay.setVisible(false);
        add(seatingDisplay);

        setLayout(null);
        setFocusable(true);
    }
    //sets the GUI for various screens
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBounds(0, 0, 725, 500);
        setSize(725, 500);
        g.setColor(background);
        g.fillRect(0, 0, 1366, 768);

        g.setFont(new Font("Uni Sans", Font.BOLD, 36));
        g.setColor(text);

        if (currentScreen == 0) { // login
            g.drawString("Restaurant Reservation Manager", 50, 75);
            g.setFont(new Font("Uni Sans", Font.BOLD, 25));
            g.drawString("Welcome!", 50, 120);
            g.setFont(new Font("Uni Sans", Font.BOLD, 20));
            g.drawString("Please login or register.", 50, 155);
            g.setFont(new Font("Uni Sans", Font.BOLD, 15));
            g.drawString("Username:", 50, 200);
            g.drawString("Password:", 50, 250);
            g.setFont(new Font("Uni Sans", Font.BOLD, 20));
            g.drawString("If you don't have an account, please register:", 50, 380);
        } else if (currentScreen == 1) { // register
            g.drawString("Create Account", 50, 75);
            g.setFont(new Font("Uni Sans", Font.BOLD, 15));
            g.drawString("Username:", 50, 200);
            g.drawString("Password:", 50, 250);
            g.drawString("Full name:", 50, 300);
            g.drawString("Email:", 50, 350);
        } else if (currentScreen == 2) { // dashboard
            g.drawString("Dashboard", 270, 75);
            g.setFont(new Font("Uni Sans", Font.BOLD, 20));
            g.drawString("What would you like to do?", 235, 120);
        } else if (currentScreen == 3) { // make reservation
            g.drawString("New Reservation", 50, 75);
            g.setFont(new Font("Uni Sans", Font.BOLD, 15));
            g.drawString("Name:", 50, 120);
            g.drawString("Timestamp:", 50, 170);
            g.drawString("Party Size:", 50, 220);
            g.drawString("Seat #s (e.g. 1,2,3):", 50, 270);
        } else if (currentScreen == 4) { // view/edit reservation
            g.drawString("My Reservations", 50, 75);
            g.setFont(new Font("Uni Sans", Font.BOLD, 14));
            g.drawString("To delete, enter exact details", 50, 150);
            g.drawString("into the fields below:", 50, 170);
            g.drawString("Name:", 50, 200);
            g.drawString("Time:", 50, 240);
            g.drawString("Size:", 50, 280);
            g.drawString("Seats:", 50, 320);
        }
    }

    //provides logic for various actions
    @Override
    public void actionPerformed(ActionEvent e) {
        //login logic
        if (e.getSource() == loginButton && currentScreen == 0) {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (user.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Enter username and password.");
                repaint();
                return;
            }
            UserAccount account = client.login(user, pass);
            if (account == null) {
                errorLabel.setText("Invalid login.");
            } else {
                errorLabel.setText("");
                currentScreen = 2; // go to dashboard

                //hide login screen
                usernameField.setVisible(false);
                passwordField.setVisible(false);
                loginButton.setVisible(false);
                registerButton.setVisible(false);

                // show dashboard
                makeReservationScreenButton.setVisible(true);
                viewReservationScreenButton.setVisible(true);
                deleteAccountButton.setVisible(true);
                logoutButton.setVisible(true);

                // clear fields for safety
                usernameField.setText("");
                passwordField.setText("");
            }
            repaint();
            return;
        }

        //register logic
        if (e.getSource() == registerButton && currentScreen == 0) {
            usernameField.setText("");
            passwordField.setText("");
            currentScreen = 1;
            nameField.setVisible(true);
            emailField.setVisible(true);
            createAccountButton.setVisible(true);
            backToLoginButton.setVisible(true);
            loginButton.setVisible(false);
            registerButton.setVisible(false);
            errorLabel.setText("");
            repaint();
            return;
        }

        if (e.getSource() == createAccountButton && currentScreen == 1) {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            String full = nameField.getText();
            String email = emailField.getText();

            if (user.isEmpty() || pass.isEmpty() || full.isEmpty() || email.isEmpty()) {
                errorLabel.setText("Fill all fields.");
                repaint();
                return;
            }

            boolean added = client.addAccount(user, pass, full, email);
            if (!added) {
                errorLabel.setText("Account already exists or username is taken.");
            } else {
                errorLabel.setText("Account created. Please login.");
                currentScreen = 0;
                nameField.setVisible(false);
                emailField.setVisible(false);
                createAccountButton.setVisible(false);
                backToLoginButton.setVisible(false);
                loginButton.setVisible(true);
                registerButton.setVisible(true);
                usernameField.setText("");
                passwordField.setText("");
                nameField.setText("");
                emailField.setText("");
            }
            repaint();
            return;
        }

        //back to login logic
        if (e.getSource() == backToLoginButton) {
            currentScreen = 0;
            nameField.setVisible(false);
            emailField.setVisible(false);
            createAccountButton.setVisible(false);
            backToLoginButton.setVisible(false);
            loginButton.setVisible(true);
            registerButton.setVisible(true);
            usernameField.setText("");
            passwordField.setText("");
            nameField.setText("");
            emailField.setText("");
            errorLabel.setText("");
            repaint();
            return;
        }

        //dashboard logic
        if (e.getSource() == makeReservationScreenButton) {
            currentScreen = 3;
            // hide Dash
            makeReservationScreenButton.setVisible(false);
            viewReservationScreenButton.setVisible(false);
            deleteAccountButton.setVisible(false);
            logoutButton.setVisible(false);

            // show reservation fields
            nameField2.setVisible(true);
            timestampField.setVisible(true);
            partySizeField.setVisible(true);
            seatsField.setVisible(true);
            submitReservationButton.setVisible(true);
            checkSeatsButton.setVisible(true);
            backToDashButton.setVisible(true);

            // re-use display area for seat check
            scrollPane.setVisible(true);
            displayArea.setText("Enter a timestamp (YYYY-MM-DD HH:MM) and click Check Availability to see seats.");
            seatingDisplay.setVisible(true);
            for (int i = 0; i < row; i++){
                for (int j = 0; j < col; j++){
                    grid[i][j].setBackground(new Color(55, 55, 55));
                }
            }

            errorLabel.setText("");
            repaint();
        }

        if (e.getSource() == viewReservationScreenButton) {
            currentScreen = 4;
            // Hide Dash
            makeReservationScreenButton.setVisible(false);
            viewReservationScreenButton.setVisible(false);
            deleteAccountButton.setVisible(false);
            logoutButton.setVisible(false);

            // show view fields
            nameField2.setVisible(true);
            timestampField.setVisible(true);
            partySizeField.setVisible(true);
            seatsField.setVisible(true);

            // position them for deletion
            nameField2.setBounds(100, 180, 150, 30);
            timestampField.setBounds(100, 220, 150, 30);
            partySizeField.setBounds(100, 260, 150, 30);
            seatsField.setBounds(100, 300, 150, 30);

            refreshReservationButton.setVisible(true);
            printDbButton.setVisible(true);
            deleteReservationButton.setVisible(true);
            backToDashButton.setVisible(true);
            scrollPane.setVisible(true);

            displayArea.setText(client.getReservations());
            errorLabel.setText("");
            repaint();
        }

        if (e.getSource() == logoutButton) {
            currentScreen = 0;
            makeReservationScreenButton.setVisible(false);
            viewReservationScreenButton.setVisible(false);
            deleteAccountButton.setVisible(false);
            logoutButton.setVisible(false);

            usernameField.setVisible(true);
            passwordField.setVisible(true);
            loginButton.setVisible(true);
            registerButton.setVisible(true);

            errorLabel.setText("Logged out.");
            repaint();
        }

        if (e.getSource() == deleteAccountButton) {
            boolean deleted = client.deleteAccount();
            if (deleted) {
                // return to login screen
                currentScreen = 0;
                makeReservationScreenButton.setVisible(false);
                viewReservationScreenButton.setVisible(false);
                deleteAccountButton.setVisible(false);
                logoutButton.setVisible(false);

                usernameField.setVisible(true);
                passwordField.setVisible(true);
                loginButton.setVisible(true);
                registerButton.setVisible(true);
                errorLabel.setText("Account deleted.");
            } else {
                errorLabel.setText("Failed to delete account.");
            }
            repaint();
        }

        // --- MAKE RESERVATION LOGIC ---
        if (e.getSource() == submitReservationButton) {
            try {
                // 1. Parse the data first
                String name = nameField2.getText();
                String time = timestampField.getText();
                int size = Integer.parseInt(partySizeField.getText());
                String seatsStr = seatsField.getText();

                ArrayList<Integer> seats = new ArrayList<>();
                String[] split = seatsStr.split(",");
                for (String s : split) {
                    seats.add(Integer.parseInt(s.trim()));
                }

                // 2. TRIGGER CONFIRMATION DIALOG HERE
                int response = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to book this reservation?",
                        "Confirm Booking",
                        JOptionPane.YES_NO_OPTION);

                // 3. Only proceed if user clicks "Yes"
                if (response == JOptionPane.YES_OPTION) {
                    boolean success = client.addReservation(name, time, size, seats);

                    if (success) {
                        errorLabel.setText("Reservation Successful!");
                        nameField2.setText("");
                        timestampField.setText("");
                        partySizeField.setText("");
                        seatsField.setText("");
                        displayArea.setText("Reservation booked.");

                        // Clear grid on success
                        for (int i = 0; i < row; i++) {
                            for (int j = 0; j < col; j++) {
                                grid[i][j].setBackground(new Color(55, 55, 55));
                            }
                        }
                    } else {
                        errorLabel.setText("Failed: overlap or invalid data.");
                    }
                }
            } catch (Exception ex) {
                errorLabel.setText("Invalid format. Use numbers for size/seats.");
            }
            repaint();
        }

        if (e.getSource() == checkSeatsButton) {
            String time = timestampField.getText();
            if (time.isEmpty() || !time.matches("^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}$")) {
                errorLabel.setText("Enter timestamp (YYYY-MM-DD HH:MM) to check.");
                displayArea.setText("");
                return;
            }
            boolean[] statuses = client.updateSeatStatuses(time);
            if (statuses != null) {
                StringBuilder sb = new StringBuilder("Availability at " + time + ":\n");
                for (int i = 0; i < statuses.length; i++) {
                    sb.append("Seat ").append(i).append(": ");
                    if (statuses[i]) {
                        sb.append("TAKEN\n");
                    } else {
                        sb.append("FREE\n");
                    }
                }

                // Update Visual Grid
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        int seatIndex = (i * col) + j;

                        if (seatIndex < statuses.length) {
                            if (statuses[seatIndex]) {
                                grid[i][j].setBackground(new Color(128, 67, 67)); // Red for Taken
                            } else {
                                grid[i][j].setBackground(new Color(93, 138, 96)); // Green for Free
                            }
                        }
                    }
                }
                displayArea.setText(sb.toString());
                errorLabel.setText("");
            } else {
                displayArea.setText("Could not get statuses.");
            }
        }

        // --- VIEW/MANAGE LOGIC ---
        if (e.getSource() == refreshReservationButton) {
            displayArea.setText(client.getReservations());
        }

        if (e.getSource() == printDbButton) {
            displayArea.setText(client.printDatabase());
        }

        if (e.getSource() == deleteReservationButton) {
            try {
                // 1. Parse data
                String name = nameField2.getText();
                String time = timestampField.getText();
                int size = Integer.parseInt(partySizeField.getText());
                String seatsStr = seatsField.getText();
                ArrayList<Integer> seats = new ArrayList<>();
                String[] split = seatsStr.split(",");
                for (String s : split) {
                    seats.add(Integer.parseInt(s.trim()));
                }

                // 2. TRIGGER CONFIRMATION DIALOG HERE
                int response = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to DELETE this reservation?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                // 3. Only proceed if user clicks "Yes"
                if (response == JOptionPane.YES_OPTION) {
                    boolean success = client.deleteReservation(name, time, size, seats);
                    if (success) {
                        errorLabel.setText("Reservation deleted.");
                        displayArea.setText(client.getReservations());
                        nameField2.setText("");
                        timestampField.setText("");
                        partySizeField.setText("");
                        seatsField.setText("");
                    } else {
                        errorLabel.setText("Delete failed. Details must match exactly.");
                    }
                }
            } catch (Exception ex) {
                errorLabel.setText("Invalid format.");
            }
        }

        // --- BACK TO DASHBOARD ---
        if (e.getSource() == backToDashButton) {
            currentScreen = 2;

            // Hide Reservation/View Components
            nameField2.setVisible(false);
            timestampField.setVisible(false);
            partySizeField.setVisible(false);
            seatsField.setVisible(false);
            submitReservationButton.setVisible(false);
            checkSeatsButton.setVisible(false);
            backToDashButton.setVisible(false);
            refreshReservationButton.setVisible(false);
            deleteReservationButton.setVisible(false);
            printDbButton.setVisible(false);
            scrollPane.setVisible(false);

            // reset Field positions for next time (in case we came from screen 4)
            nameField2.setBounds(200, 100, 100, 30);
            timestampField.setBounds(200, 150, 100, 30);
            partySizeField.setBounds(200, 200, 100, 30);
            seatsField.setBounds(200, 250, 100, 30);

            // show dash
            makeReservationScreenButton.setVisible(true);
            viewReservationScreenButton.setVisible(true);
            deleteAccountButton.setVisible(true);
            logoutButton.setVisible(true);
            seatingDisplay.setVisible(false);

            nameField2.setText("");
            timestampField.setText("");
            partySizeField.setText("");
            seatsField.setText("");
            errorLabel.setText("");
            repaint();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(725, 500);
    }
}