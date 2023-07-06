import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Org extends JFrame {
    private JTextField tfOrganizationID;
    private JTextField tfOrganizationName;
    private JTextField tfOrganizationWebsite;
    private JButton btnInsert;
    private JButton btnModify;
    private JButton btnDelete;
    private JButton btnDisplay;
    private JTable table;
    private DefaultTableModel tableModel;

    // Setter and getter functions for Organization ID
    public void setOrganizationID(String organizationID) {
        tfOrganizationID.setText(organizationID);
    }

    public String getOrganizationID() {
        return tfOrganizationID.getText();
    }

    // Setter and getter functions for Organization Name
    public void setOrganizationName(String organizationName) {
        tfOrganizationName.setText(organizationName);
    }

    public String getOrganizationName() {
        return tfOrganizationName.getText();
    }

    // Setter and getter functions for Organization Website
    public void setOrganizationWebsite(String organizationWebsite) {
        tfOrganizationWebsite.setText(organizationWebsite);
    }

    public String getOrganizationWebsite() {
        return tfOrganizationWebsite.getText();
    }

    // Constructor
    public Org() {
        tfOrganizationID = new JTextField(10);
        tfOrganizationName = new JTextField(20);
        tfOrganizationWebsite = new JTextField(20);
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
        add(new JLabel("Organization ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(tfOrganizationID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Organization Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(tfOrganizationName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Organization Website:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(tfOrganizationWebsite, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(btnInsert, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(btnModify, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(btnDelete, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(btnDisplay, gbc);

        // Initialize the table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Organization ID");
        tableModel.addColumn("Organization Name");
        tableModel.addColumn("Organization Website");

        // Create the JTable using the table model
        table = new JTable(tableModel);

        // Add a selection listener to populate the text fields when a row is selected
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow() != -1) {
                    String organizationID = table.getValueAt(table.getSelectedRow(), 0).toString();
                    String organizationName = table.getValueAt(table.getSelectedRow(), 1).toString();
                    String organizationWebsite = table.getValueAt(table.getSelectedRow(), 2).toString();
                    setOrganizationID(organizationID);
                    setOrganizationName(organizationName);
                    setOrganizationWebsite(organizationWebsite);
                }
            }
        });

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(scrollPane, gbc);

        // Register action listeners for the buttons
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertOrganization();
            }
        });

        btnModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyOrganization();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteOrganization();
            }
        });

        btnDisplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayOrganization();
            }
        });

        setTitle("Organization Management");
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

    // Method to insert a new organization
    private void insertOrganization() {
        try (Connection conn = getConnection()) {
            String organizationID = getOrganizationID();
            String organizationName = getOrganizationName();
            String organizationWebsite = getOrganizationWebsite();

            // Check if organization name is not null and unique
            if (organizationName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an organization name.");
                return;
            }

            // Check if organization name is unique
            String checkQuery = "SELECT * FROM Organization WHERE name = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, organizationName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Organization name already exists.");
                    return;
                }
            }

            // Insert the new organization
            String insertQuery = "INSERT INTO Organization (organization_id, name, website) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, organizationID);
                insertStmt.setString(2, organizationName);
                insertStmt.setString(3, organizationWebsite);
                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Organization inserted successfully.");
                    displayOrganization(); // Refresh the table after insertion
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error inserting organization: " + e.getMessage());
        }
    }

    // Method to modify an existing organization
    private void modifyOrganization() {
        try (Connection conn = getConnection()) {
            String organizationID = getOrganizationID();
            String organizationName = getOrganizationName();
            String organizationWebsite = getOrganizationWebsite();

            // Check if organization name is not null and unique
            if (organizationName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an organization name.");
                return;
            }

            // Check if organization nameis unique
            String checkQuery = "SELECT * FROM Organization WHERE name = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, organizationName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Organization name already exists.");
                    return;
                }
            }

            // Modify the existing organization
            String updateQuery = "UPDATE Organization SET name = ?, website = ? WHERE organization_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, organizationName);
                updateStmt.setString(2, organizationWebsite);
                updateStmt.setString(3, organizationID);
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Organization modified successfully.");
                    displayOrganization(); // Refresh the table after modification
                } else {
                    JOptionPane.showMessageDialog(this, "Organization ID not found.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error modifying organization: " + e.getMessage());
        }
    }

    // Method to delete an existing organization
    private void deleteOrganization() {
        try (Connection conn = getConnection()) {
            String organizationID = getOrganizationID();

            // Delete the existing organization
            String deleteQuery = "DELETE FROM Organization WHERE organization_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, organizationID);
                int rowsDeleted = deleteStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Organization deleted successfully.");
                    displayOrganization(); // Refresh the table after deletion
                } else {
                    JOptionPane.showMessageDialog(this, "Organization ID not found.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting organization: " + e.getMessage());
        }
    }

    // Method to display all organizations
    private void displayOrganization() {
        try (Connection conn = getConnection()) {
            String selectQuery = "SELECT * FROM Organization";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                ResultSet rs = selectStmt.executeQuery();
                tableModel.setRowCount(0); // Clear the existing rows

                while (rs.next()) {
                    String organizationID = rs.getString("organization_id");
                    String organizationName = rs.getString("name");
                    String organizationWebsite = rs.getString("website");
                    tableModel.addRow(new Object[]{organizationID, organizationName, organizationWebsite});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error displaying organizations: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Org();
            }
        });
    }
}
