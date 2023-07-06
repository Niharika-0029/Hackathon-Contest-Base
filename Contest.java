import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Contest extends JFrame {
    private JTextField tfContestID;
    private JTextField tfContestName;
    private JTextField tfStartDate;
    private JTextField tfEndDate;
    private JTextArea taDescription;
    private JButton btnInsert;
    private JButton btnModify;
    private JButton btnDelete;
    private JButton btnDisplay;

    // Setter and getter functions for Contest ID
    public void setContestID(String contestID) {
        tfContestID.setText(contestID);
    }

    public String getContestID() {
        return tfContestID.getText();
    }

    // Setter and getter functions for Contest Name
    public void setContestName(String contestName) {
        tfContestName.setText(contestName);
    }

    public String getContestName() {
        return tfContestName.getText();
    }

    // Setter and getter functions for Start Date
    public void setStartDate(String startDate) {
        tfStartDate.setText(startDate);
    }

    public String getStartDate() {
        return tfStartDate.getText();
    }

    // Setter and getter functions for End Date
    public void setEndDate(String endDate) {
        tfEndDate.setText(endDate);
    }

    public String getEndDate() {
        return tfEndDate.getText();
    }

    // Setter and getter functions for Description
    public void setDescription(String description) {
        taDescription.setText(description);
    }

    public String getDescription() {
        return taDescription.getText();
    }

    // Constructor
    public Contest() {
        tfContestID = new JTextField(10);
        tfContestName = new JTextField(20);
        tfStartDate = new JTextField(10);
        tfEndDate = new JTextField(10);
        taDescription = new JTextArea(5, 20);
        btnInsert = new JButton("Insert");
        btnModify = new JButton("Modify");
        btnDelete = new JButton("Delete");
        btnDisplay = new JButton("Display");

        // Create the GridBagLayout and GridBagConstraints
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Set padding

        // Add components to the frame using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Contest ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(tfContestID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Contest Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(tfContestName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(tfStartDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("End Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(tfEndDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(new JScrollPane(taDescription), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(btnInsert, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(btnModify, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(btnDelete, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        add(btnDisplay, gbc);

        // Register action listeners for the buttons
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertContest();
            }
        });

        btnModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyContest();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteContest();
            }
        });

        btnDisplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayContests();
            }
        });

        setTitle("Contest Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // Method to establish a JDBC connection
    private Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "gattu_niharika";
        String password = "niharika29";
        return DriverManager.getConnection(url, username, password);
    }

    // Method to insert a new contest
    private void insertContest() {
        try (Connection conn = getConnection()) {
            String contestID = getContestID();
            String contestName = getContestName();
            String startDate = getStartDate();
            String endDate = getEndDate();
            String description = getDescription();

            // Check if contest name, start date, end date, and description are not null
            if (contestName.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all contest details.");
                return;
            }

            // Convert start date and end date to java.sql.Date objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date sqlStartDate = new java.sql.Date(dateFormat.parse(startDate).getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(dateFormat.parse(endDate).getTime());

            // Insert the new contest
            String insertQuery = "INSERT INTO Contest (contest_id, name, start_date, end_date, description) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, contestID);
                insertStmt.setString(2, contestName);
                insertStmt.setDate(3, sqlStartDate);
                insertStmt.setDate(4, sqlEndDate);
                insertStmt.setString(5, description);
                insertStmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Contest inserted successfully.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inserting contest: " + e.getMessage());
        }
    }

    // Method to modify an existing contest
    private void modifyContest() {
        try (Connection conn = getConnection()) {
            String contestID = getContestID();
            String contestName = getContestName();
            String startDate = getStartDate();
            String endDate = getEndDate();
            String description = getDescription();

            // Check if contest name, start date, end date, and description are not null
            if (contestName.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all contest details.");
                return;
            }

            // Convert start date and end date to java.sql.Date objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date sqlStartDate = new java.sql.Date(dateFormat.parse(startDate).getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(dateFormat.parse(endDate).getTime());

            // Modify the existing contest
            String updateQuery = "UPDATE Contest SET name = ?, start_date = ?, end_date = ?, description = ? " +
                    "WHERE contest_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, contestName);
                updateStmt.setDate(2, sqlStartDate);
                updateStmt.setDate(3, sqlEndDate);
                updateStmt.setString(4, description);
                updateStmt.setString(5, contestID);
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Contest modified successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Contest ID not found.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error modifying contest: " + e.getMessage());
        }
    }

    // Method to delete an existing contest
    private void deleteContest() {
        try (Connection conn = getConnection()) {
            String contestID = getContestID();

            // Delete the existing contest
            String deleteQuery = "DELETE FROM Contest WHERE contest_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, contestID);
                int rowsDeleted = deleteStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Contest deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Contest ID not found.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting contest: " + e.getMessage());
        }
    }

    // Method to display all contests
    private void displayContests() {
        try (Connection conn = getConnection()) {
            String selectQuery = "SELECT * FROM Contest";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                ResultSet rs = selectStmt.executeQuery();
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    String contestID = rs.getString("contest_id");
                    String contestName = rs.getString("name");
                    Date startDate = rs.getDate("start_date");
                    Date endDate = rs.getDate("end_date");
                    String description = rs.getString("description");
                    sb.append("Contest ID: ").append(contestID).append(", Contest Name: ").append(contestName)
                            .append(", Start Date: ").append(startDate).append(", End Date: ").append(endDate)
                            .append(", Description: ").append(description).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error displaying contests: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Contest();
            }
        });
    }
}

