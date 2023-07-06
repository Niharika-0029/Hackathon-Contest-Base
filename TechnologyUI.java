import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class TechnologyUI extends JFrame {
    private JTextField tfTechnologyID;
    private JTextField tfTechnologyName;
    private JButton btnInsert;
    private JButton btnModify;
    private JButton btnDelete;
    private JButton btnDisplay;
    private JTable table;
    private DefaultTableModel tableModel;

    // Setter and getter functions for Technology ID
    public void setTechnologyID(String technologyID) {
        tfTechnologyID.setText(technologyID);
    }

    public String getTechnologyID() {
        return tfTechnologyID.getText();
    }

    // Setter and getter functions for Technology Name
    public void setTechnologyName(String technologyName) {
        tfTechnologyName.setText(technologyName);
    }

    public String getTechnologyName() {
        return tfTechnologyName.getText();
    }

    // Constructor
    public TechnologyUI() {
        tfTechnologyID = new JTextField(10);
        tfTechnologyName = new JTextField(20);
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
        add(new JLabel("Technology ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(tfTechnologyID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Technology Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(tfTechnologyName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(btnInsert, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(btnModify, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(btnDelete, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(btnDisplay, gbc);

        // Initialize the table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Technology ID");
        tableModel.addColumn("Technology Name");

        // Create the JTable using the table model
        table = new JTable(tableModel);

        // Add a selection listener to populate the text fields when a row is selected
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow() != -1) {
                    String technologyID = table.getValueAt(table.getSelectedRow(), 0).toString();
                    String technologyName = table.getValueAt(table.getSelectedRow(), 1).toString();
                    setTechnologyID(technologyID);
                    setTechnologyName(technologyName);
                }
            }
        });

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(scrollPane, gbc);

        // Register action listeners for the buttons
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertTechnology();
            }
        });

        btnModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyTechnology();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTechnology();
            }
        });

        btnDisplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayTechnology();
            }
        });

        setTitle("Technology Management");
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

    // Method to insert a new technology
    private void insertTechnology() {
        try (Connection conn = getConnection()) {
            String technologyID = getTechnologyID();
            String technologyName = getTechnologyName();

            // Check if technology name is not null and unique
            if (technologyName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a technology name.");
                return;
            }

            // Check if technology name is unique
            String checkQuery = "SELECT * FROM Technology WHERE name = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, technologyName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Technology name already exists.");
                    return;
                }
            }

            // Insert the new technology
            String insertQuery = "INSERT INTO Technology (technology_id, name) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, technologyID);
                insertStmt.setString(2, technologyName);
                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Technology inserted successfully.");
                    displayTechnology(); // Refresh the table after insertion
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error inserting technology: " + e.getMessage());
        }
    }

    // Method to modify an existing technology
    private void modifyTechnology() {
        try (Connection conn = getConnection()) {
            String technologyID = getTechnologyID();
            String technologyName = getTechnologyName();

            // Check if technology name is not null and unique
            if (technologyName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a technology name.");
                return;
            }

            // Check if technology name is unique
            String checkQuery = "SELECT * FROM Technology WHERE name = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, technologyName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Technology name already exists.");
                    return;
                }
            }

            // Modify the existing technology
            String updateQuery = "UPDATE Technology SET name = ? WHERE technology_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, technologyName);
                updateStmt.setString(2, technologyID);
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Technology modified successfully.");
                    displayTechnology(); // Refresh the table after modification
                } else {
                    JOptionPane.showMessageDialog(this, "Technology ID not found.");
                }
            }
} catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error modifying technology: " + e.getMessage());
        }
    }

    // Method to delete an existing technology
    private void deleteTechnology() {
        try (Connection conn = getConnection()) {
            String technologyID = getTechnologyID();

            // Delete the existing technology
            String deleteQuery = "DELETE FROM Technology WHERE technology_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, technologyID);
                int rowsDeleted = deleteStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Technology deleted successfully.");
                    displayTechnology(); // Refresh the table after deletion
                } else {
                    JOptionPane.showMessageDialog(this, "Technology ID not found.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting technology: " + e.getMessage());
        }
    }

    // Method to display all technologies
    private void displayTechnology() {
        try (Connection conn = getConnection()) {
            String selectQuery = "SELECT * FROM Technology";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                ResultSet rs = selectStmt.executeQuery();
                tableModel.setRowCount(0); // Clear the existing rows

                while (rs.next()) {
                    String technologyID = rs.getString("technology_id");
                    String technologyName = rs.getString("name");
                    tableModel.addRow(new Object[]{technologyID, technologyName});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error displaying technologies: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TechnologyUI();
            }
        });
    }
}
