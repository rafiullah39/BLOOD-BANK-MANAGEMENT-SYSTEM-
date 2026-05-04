package databaseconnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DatabaseConnection extends JFrame {
    private JTextArea textArea;

    public DatabaseConnection() {
        setTitle("Blood Bank Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

       
        JTabbedPane tabbedPane = new JTabbedPane();

        
        JPanel donorPanel = createButtonPanel(new String[]{
            "Donor Data", "Donation Summary", "Emergency Contacts","Blood requests", "View Patient Records",
              "View Staff Records"
        }, new ActionListener[]{
            e -> fetchAndDisplayDonorData(),
            e -> fetchAndDisplayDonorDonationSummary(),
            e -> fetchAndDisplayDonorEmergencyContacts(),
            e -> fetchAndDisplayBloodRequestDetails(), e -> fetchAndDisplayPatientRecords(),
             e -> fetchAndDisplayStaffRecords()
        });

        
        JPanel bloodPanel = createButtonPanel(new String[]{
            "Inventory", "Pending Requests", "Donation Details", "Expired Inventory"
        }, new ActionListener[]{
            e -> fetchAndDisplayBloodInventory(),
            e -> fetchAndDisplayPendingRequests(),
            e -> fetchAndDisplayDonationDetails(),
            e -> fetchAndDisplayExpiredBloodInventory()
        });

       
        JPanel campaignPanel = createButtonPanel(new String[]{
            "Campaign Details", "Transaction Details", "Appointments", "View Supplier-Supply Details",
            "View supplier-without supply"
        }, new ActionListener[]{
            e -> fetchAndDisplayCampaignDetails(),
            e -> fetchAndDisplayTransactionDetails(),
            e -> fetchAndDisplayAppointmentDetails(),
            e -> fetchAndDisplaySupplierSupplyDetails(),
            e -> fetchAndDisplaySuppliersWithNoSupply()
        });

        
        JPanel feedbackPanel = createButtonPanel(new String[]{
            "Feedback Details"
        }, new ActionListener[]{
            e -> fetchAndDisplayFeedbackDetails()
        });
        
        JPanel insertionPanel = createButtonPanel(new String[]{
        "Insert Donar", "Insert Blood Inventory","Insert Patient","Insert Blood Request",
        "Insert Emergency Contact","Insert Feedback","Insert Campaign","Insert Staff","Insert Transaction",
         "Insert Payment","Insert Donation","Add Supplier","Add Supply"
        }, new ActionListener[]{
             e -> insertDonorDetails(),
             e -> showInsertBloodInventoryForm(),
             e -> showInsertPatientForm(),
             e -> showInsertBloodRequestForm(),
             e -> showInsertEmergencyContactForm(),
              e -> showInsertFeedbackForm(),
              e -> showInsertCampaignForm(),e -> showInsertStaffForm(),e -> showInsertTransactionForm(),
              e -> showInsertPaymentForm(), e -> showInsertDonationForm(), e -> showInsertSupplierForm(),
               e -> showInsertSupplyForm()
         });


JPanel updatePanel = createButtonPanel(new String[] {
    "Merge Emergency Contact", "Update Blood Request","Update Feedback", "Update Campaign", 
    "Update Blood Inventory","Update Staff Role"
}, new ActionListener[] {
    e -> showAddEmergencyContactForm(),
    e -> showUpdateBloodRequestForm(),
    e -> showUpdateFeedbackForm(), e -> showUpdateCampaignForm(),  e -> showUpdateBloodInventoryForm(),
     e -> showUpdateStaffRoleForm()
});



JPanel deletePanel = createButtonPanel(new String[]{
   
     "Delete Blood Request","Delete Donors (Age > 60)","Delete Unlinked Patients", "Delete Unlinked Donors",
    "Delete Supplier Without Supply"

}, new ActionListener[]{
  e -> showDeleteBloodRequestForm(), e -> showDeleteDonorForm(),
    e -> deleteUnlinkedPatients(),e -> deleteUnlinkedDonors(),
    e -> deleteSupplierWithoutSupply()
});




        
       
        tabbedPane.addTab("Donors", donorPanel);
        tabbedPane.addTab("Blood", bloodPanel);
        tabbedPane.addTab("Campaigns & Transactions", campaignPanel);
        tabbedPane.addTab("Feedback", feedbackPanel);
  tabbedPane.addTab("Insert", insertionPanel);
    tabbedPane.addTab("Update", updatePanel);

    tabbedPane.addTab("Delete", deletePanel);
        
        add(scrollPane, BorderLayout.CENTER);
        add(tabbedPane, BorderLayout.SOUTH);
    }

    private void insertDonorDetails() {
    showDonorInputForm();
}
    
private void deleteSupplierWithoutSupply() {
    int confirmation = JOptionPane.showConfirmDialog(
        null, 
        "Are you sure you want to delete suppliers without any supply?\nThis action cannot be undone.", 
        "Confirm Deletion", 
        JOptionPane.YES_NO_OPTION
    );

    if (confirmation == JOptionPane.YES_OPTION) {
        try {
            int rowsAffected = DatabaseQueries.deleteSuppliersWithoutSupply();
          
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, rowsAffected + " suppliers deleted successfully.", "Deletion Status", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No suppliers without supply found.", "Deletion Status", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while deleting suppliers. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


    private void deleteUnlinkedDonors() {
    int confirmation = JOptionPane.showConfirmDialog(
        null, 
        "Are you sure you want to delete unlinked donors?\nThis action cannot be undone.",
        "Confirm Deletion", 
        JOptionPane.YES_NO_OPTION
    );

    if (confirmation == JOptionPane.YES_OPTION) {
        String result = DatabaseQueries.deleteUnlinkedDonors();
        JOptionPane.showMessageDialog(null, result, "Deletion Status", JOptionPane.INFORMATION_MESSAGE);
    }
}

    private void deleteUnlinkedPatients() {
    int confirmation = JOptionPane.showConfirmDialog(
        null, 
        "Are you sure you want to delete unlinked patients?\nThis action cannot be undone.",
        "Confirm Deletion", 
        JOptionPane.YES_NO_OPTION
    );

    if (confirmation == JOptionPane.YES_OPTION) {
        String result = DatabaseQueries.deleteUnlinkedPatients();
        JOptionPane.showMessageDialog(null, result, "Deletion Status", JOptionPane.INFORMATION_MESSAGE);
    }
}

    private void showDeleteDonorForm() {
    int confirm = JOptionPane.showConfirmDialog(
        null, 
        "Are you sure you want to delete all donors aged above 60?", 
        "Confirm Deletion", 
        JOptionPane.YES_NO_OPTION
    );
    
    if (confirm == JOptionPane.YES_OPTION) {
        if (DatabaseQueries.deleteDonorsAboveAge60()) {
            JOptionPane.showMessageDialog(
                null, 
                "Donors aged above 60 have been successfully deleted.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to delete donor records.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

    private void showDeleteBloodRequestForm() {
    JFrame deleteFrame = new JFrame("Delete Blood Request");
    deleteFrame.setSize(400, 250);
    deleteFrame.setLayout(new BorderLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(2, 2));

    JLabel requestDateLabel = new JLabel("Enter request status:");
    JTextField requestDateField = new JTextField();
    
    panel.add(requestDateLabel);
    panel.add(requestDateField);

    JButton deleteButton = new JButton("Delete Blood Request");
    deleteButton.addActionListener(e -> deleteBloodRequest(requestDateField));
    panel.add(deleteButton);

    deleteFrame.add(panel, BorderLayout.CENTER);
    deleteFrame.setVisible(true);
}
private void deleteBloodRequest(JTextField requestDateField) {
    try {
        String requestDate = requestDateField.getText();
        String result = DatabaseQueries.deleteBloodRequestFromDB(requestDate);
        JOptionPane.showMessageDialog(null, result, "Delete Status", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error while deleting: " + e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void showUpdateStaffRoleForm() {
    JFrame updateFrame = new JFrame("Update Staff Role");
    updateFrame.setSize(400, 300);
    updateFrame.setLayout(new BorderLayout());
    updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10); 
    gbc.anchor = GridBagConstraints.WEST;
    

    JLabel staffIDLabel = new JLabel("Enter Staff ID:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(staffIDLabel, gbc);

    JTextField staffIDField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 0;
    panel.add(staffIDField, gbc);

    JLabel roleLabel = new JLabel("New Role:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(roleLabel, gbc);

    JTextField roleField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(roleField, gbc);


    JButton updateButton = new JButton("Update");
    gbc.gridx = 1;
    gbc.gridy = 2;
    panel.add(updateButton, gbc);
    

    updateButton.addActionListener(e -> updateStaffRole(staffIDField, roleField));

    JScrollPane scrollPane = new JScrollPane(panel);
    updateFrame.add(scrollPane, BorderLayout.CENTER);

    updateFrame.setLocationRelativeTo(null);
    updateFrame.setVisible(true);
}
private void updateStaffRole(JTextField staffIDField, JTextField roleField) {
    try {
        int staffID = Integer.parseInt(staffIDField.getText());
        String role = roleField.getText();
        
       
        if (role.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please provide a valid role.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = DatabaseQueries.updateStaffRole(staffID, role);
        JOptionPane.showMessageDialog(null, result, "Update Status", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid Staff ID. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
   private void showUpdateBloodInventoryForm() {
    JFrame updateFrame = new JFrame("Update Blood Inventory");
    updateFrame.setSize(400, 300);
    updateFrame.setLayout(new BorderLayout());
    updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    
    JLabel inventoryIDLabel = new JLabel("Enter Inventory ID:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(inventoryIDLabel, gbc);

    JTextField inventoryIDField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 0;
    panel.add(inventoryIDField, gbc);

    JLabel expiryDateLabel = new JLabel("New Expiry Date:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(expiryDateLabel, gbc);

    JTextField expiryDateField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(expiryDateField, gbc);

    JLabel quantityLabel = new JLabel("New Quantity:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(quantityLabel, gbc);

    JTextField quantityField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 2;
    panel.add(quantityField, gbc);

    JButton updateButton = new JButton("Update");
    gbc.gridx = 1;
    gbc.gridy = 3;
    panel.add(updateButton, gbc);
    
    updateButton.addActionListener(e -> updateBloodInventory(inventoryIDField, expiryDateField, quantityField));

    JScrollPane scrollPane = new JScrollPane(panel);
    updateFrame.add(scrollPane, BorderLayout.CENTER);

    updateFrame.setLocationRelativeTo(null);
    updateFrame.setVisible(true);
}

private void updateBloodInventory(JTextField inventoryIDField, JTextField expiryDateField, JTextField quantityField) {
    try {
        int inventoryID = Integer.parseInt(inventoryIDField.getText());
        String expiryDate = expiryDateField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        
        if (expiryDate.isEmpty() || quantity <= 0) {
            JOptionPane.showMessageDialog(null, "Please provide valid expiry date and quantity.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = DatabaseQueries.updateBloodInventory(inventoryID, expiryDate, quantity);
        JOptionPane.showMessageDialog(null, result, "Update Status", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid Inventory ID or Quantity. Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void showUpdateCampaignForm() {
    JFrame updateFrame = new JFrame("Update Campaign");
    updateFrame.setSize(400, 300);
    updateFrame.setLayout(new BorderLayout());
    updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    
    JLabel campaignIDLabel = new JLabel("Enter Campaign ID:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(campaignIDLabel, gbc);

    JTextField campaignIDField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 0;
    panel.add(campaignIDField, gbc);

    JLabel startDateLabel = new JLabel("New Start Date:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(startDateLabel, gbc);

    JTextField startDateField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(startDateField, gbc);

    JLabel endDateLabel = new JLabel("New End Date:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(endDateLabel, gbc);

    JTextField endDateField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 2;
    panel.add(endDateField, gbc);

    JLabel locationLabel = new JLabel("New Location:");
    gbc.gridx = 0;
    gbc.gridy = 3;
    panel.add(locationLabel, gbc);

    JTextField locationField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 3;
    panel.add(locationField, gbc);

    JButton updateButton = new JButton("Update");
    gbc.gridx = 1;
    gbc.gridy = 4;
    panel.add(updateButton, gbc);
    
    updateButton.addActionListener(e -> updateCampaign(campaignIDField, startDateField, endDateField, locationField));

    JScrollPane scrollPane = new JScrollPane(panel);
    updateFrame.add(scrollPane, BorderLayout.CENTER);

    updateFrame.setLocationRelativeTo(null);
    updateFrame.setVisible(true);
}

private void updateCampaign(JTextField campaignIDField, JTextField startDateField, JTextField endDateField, JTextField locationField) {
    try {
        int campaignID = Integer.parseInt(campaignIDField.getText());
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        String location = locationField.getText();
        
        if (startDate.isEmpty() || endDate.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled correctly!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = DatabaseQueries.updateCampaign(campaignID, startDate, endDate, location);
        JOptionPane.showMessageDialog(null, result, "Update Status", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid Campaign ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void showUpdateFeedbackForm() {
    JFrame updateFrame = new JFrame("Update Feedback");
    updateFrame.setSize(400, 300);
    updateFrame.setLayout(new BorderLayout());
    updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    
    JLabel feedbackIDLabel = new JLabel("Enter Feedback ID:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(feedbackIDLabel, gbc);

    JTextField feedbackIDField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 0;
    panel.add(feedbackIDField, gbc);

    JLabel feedbackDateLabel = new JLabel("New Feedback Date:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(feedbackDateLabel, gbc);

    JTextField feedbackDateField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(feedbackDateField, gbc);

    JLabel commentsLabel = new JLabel("New Comments:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(commentsLabel, gbc);

    JTextField commentsField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 2;
    panel.add(commentsField, gbc);

    JButton updateButton = new JButton("Update");
    gbc.gridx = 1;
    gbc.gridy = 3;
    panel.add(updateButton, gbc);
    
    updateButton.addActionListener(e -> updateFeedback(feedbackIDField, feedbackDateField, commentsField));

    JScrollPane scrollPane = new JScrollPane(panel);
    updateFrame.add(scrollPane, BorderLayout.CENTER);

    updateFrame.setLocationRelativeTo(null);
    updateFrame.setVisible(true);
}

private void updateFeedback(JTextField feedbackIDField, JTextField feedbackDateField, JTextField commentsField) {
    try {
        int feedbackID = Integer.parseInt(feedbackIDField.getText());
        String feedbackDate = feedbackDateField.getText();
        String comments = commentsField.getText();
        
        if (feedbackDate.isEmpty() || comments.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled correctly!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = DatabaseQueries.updateFeedback(feedbackID, feedbackDate, comments);
        JOptionPane.showMessageDialog(null, result, "Update Status", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid Feedback ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void showUpdateBloodRequestForm() {
    JFrame updateFrame = new JFrame("Update Blood Request");
    updateFrame.setSize(400, 300);
    updateFrame.setLayout(new BorderLayout());
    updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    
    JLabel requestIDLabel = new JLabel("Enter Request ID:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(requestIDLabel, gbc);

    JTextField requestIDField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 0;
    panel.add(requestIDField, gbc);

    JLabel bloodTypeLabel = new JLabel("New Blood Type:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(bloodTypeLabel, gbc);

    JTextField bloodTypeField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(bloodTypeField, gbc);

    JLabel quantityLabel = new JLabel("New Quantity:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(quantityLabel, gbc);

    JTextField quantityField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 2;
    panel.add(quantityField, gbc);

    JLabel requestDateLabel = new JLabel("New Request Date:");
    gbc.gridx = 0;
    gbc.gridy = 3;
    panel.add(requestDateLabel, gbc);

    JTextField requestDateField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 3;
    panel.add(requestDateField, gbc);

    JLabel statusLabel = new JLabel("New Status:");
    gbc.gridx = 0;
    gbc.gridy = 4;
    panel.add(statusLabel, gbc);

    JTextField statusField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 4;
    panel.add(statusField, gbc);

    JButton updateButton = new JButton("Update");
    gbc.gridx = 1;
    gbc.gridy = 5;
    panel.add(updateButton, gbc);
    
    updateButton.addActionListener(e -> updateBloodRequest(requestIDField, bloodTypeField, quantityField, requestDateField, statusField));

    JScrollPane scrollPane = new JScrollPane(panel);
    updateFrame.add(scrollPane, BorderLayout.CENTER);

    updateFrame.setLocationRelativeTo(null);
    updateFrame.setVisible(true);
}

private void updateBloodRequest(JTextField requestIDField, JTextField bloodTypeField, JTextField quantityField, JTextField requestDateField, JTextField statusField) {
    try {
        int requestID = Integer.parseInt(requestIDField.getText());
        String bloodType = bloodTypeField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        String requestDate = requestDateField.getText();
        String status = statusField.getText();
        
        if (bloodType.isEmpty() || quantity <= 0 || requestDate.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled correctly!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = DatabaseQueries.updateBloodRequest(requestID, bloodType, quantity, requestDate, status);
        JOptionPane.showMessageDialog(null, result, "Update Status", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid number format for Request ID or Quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void showAddEmergencyContactForm() {
    JFrame mergeFrame = new JFrame("Merge Emergency Contact");
    mergeFrame.setSize(400, 300);
    mergeFrame.setLayout(new BorderLayout());
    mergeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    
    JLabel contactIDLabel = new JLabel("Enter Contact ID:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(contactIDLabel, gbc);

    JTextField contactIDField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 0;
    panel.add(contactIDField, gbc);

    JLabel donorIDLabel = new JLabel("Enter Donor ID:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(donorIDLabel, gbc);

    JTextField donorIDField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(donorIDField, gbc);

    JLabel nameLabel = new JLabel("New Name:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(nameLabel, gbc);

    JTextField nameField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 2;
    panel.add(nameField, gbc);

    JLabel relationLabel = new JLabel("New Relation:");
    gbc.gridx = 0;
    gbc.gridy = 3;
    panel.add(relationLabel, gbc);

    JTextField relationField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 3;
    panel.add(relationField, gbc);

    JLabel phoneLabel = new JLabel("New Phone:");
    gbc.gridx = 0;
    gbc.gridy = 4;
    panel.add(phoneLabel, gbc);

    JTextField phoneField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 4;
    panel.add(phoneField, gbc);

    JButton mergeButton = new JButton("Merge");
    gbc.gridx = 1;
    gbc.gridy = 5;
    panel.add(mergeButton, gbc);
    
    mergeButton.addActionListener(e -> mergeEmergencyContact(contactIDField, donorIDField, nameField, relationField, phoneField));

    JScrollPane scrollPane = new JScrollPane(panel);
    mergeFrame.add(scrollPane, BorderLayout.CENTER);

    mergeFrame.setLocationRelativeTo(null);
    mergeFrame.setVisible(true);
}

private void mergeEmergencyContact(JTextField contactIDField, JTextField donorIDField, JTextField nameField, JTextField relationField, JTextField phoneField) {
    try {
        int contactID = Integer.parseInt(contactIDField.getText());
        int donorID = Integer.parseInt(donorIDField.getText());
        String name = nameField.getText();
        String relation = relationField.getText();
        String phone = phoneField.getText();
        
        if (name.isEmpty() || relation.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = DatabaseQueries.mergeEmergencyContact(contactID, donorID, name, relation, phone);
        JOptionPane.showMessageDialog(null, result, "Merge Status", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid Contact ID or Donor ID. Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void showInsertSupplyForm() {
    JFrame insertFrame = new JFrame("Add Supply Record");
    insertFrame.setSize(400, 350);
    insertFrame.setLayout(new GridLayout(6, 2));

    JLabel lblSupplyID = new JLabel("Supply ID:");
    JTextField txtSupplyID = new JTextField();
    JLabel lblSupplierID = new JLabel("Supplier ID:");
    JTextField txtSupplierID = new JTextField();
    JLabel lblInventoryID = new JLabel("Inventory ID:");
    JTextField txtInventoryID = new JTextField();
    JLabel lblSupplyDate = new JLabel("Supply Date (YYYY-MM-DD):");
    JTextField txtSupplyDate = new JTextField();
    JLabel lblQuantity = new JLabel("Quantity:");
    JTextField txtQuantity = new JTextField();

    JButton btnInsert = new JButton("Insert");

    insertFrame.add(lblSupplyID);
    insertFrame.add(txtSupplyID);
    insertFrame.add(lblSupplierID);
    insertFrame.add(txtSupplierID);
    insertFrame.add(lblInventoryID);
    insertFrame.add(txtInventoryID);
    insertFrame.add(lblSupplyDate);
    insertFrame.add(txtSupplyDate);
    insertFrame.add(lblQuantity);
    insertFrame.add(txtQuantity);
    insertFrame.add(new JLabel());
    insertFrame.add(btnInsert);

    btnInsert.addActionListener(e -> {
        try {
            int supplyID = Integer.parseInt(txtSupplyID.getText());
            int supplierID = Integer.parseInt(txtSupplierID.getText());
            int inventoryID = Integer.parseInt(txtInventoryID.getText());
            String supplyDate = txtSupplyDate.getText();
            int quantity = Integer.parseInt(txtQuantity.getText());

            String result = DatabaseQueries.insertSupply(supplyID, supplierID, inventoryID, supplyDate, quantity);
            JOptionPane.showMessageDialog(insertFrame, result, "Insertion Status", JOptionPane.INFORMATION_MESSAGE);
            insertFrame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(insertFrame, "Invalid numeric input. Please ensure IDs and Quantity are numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    insertFrame.setVisible(true);
}

private void showInsertSupplierForm() {
    JFrame insertFrame = new JFrame("Add Supplier");
    insertFrame.setSize(400, 300);
    insertFrame.setLayout(new GridLayout(5, 2));

    JLabel lblID = new JLabel("Supplier ID:");
    JTextField txtID = new JTextField();
    JLabel lblName = new JLabel("Name:");
    JTextField txtName = new JTextField();
    JLabel lblContact = new JLabel("Contact:");
    JTextField txtContact = new JTextField();
    JLabel lblAddress = new JLabel("Address:");
    JTextField txtAddress = new JTextField();

    JButton btnInsert = new JButton("Insert");

    insertFrame.add(lblID);
    insertFrame.add(txtID);
    insertFrame.add(lblName);
    insertFrame.add(txtName);
    insertFrame.add(lblContact);
    insertFrame.add(txtContact);
    insertFrame.add(lblAddress);
    insertFrame.add(txtAddress);
    insertFrame.add(new JLabel());
    insertFrame.add(btnInsert);

    btnInsert.addActionListener(e -> {
        try {
            int supplierID = Integer.parseInt(txtID.getText());
            String name = txtName.getText();
            String contact = txtContact.getText();
            String address = txtAddress.getText();

            String result = DatabaseQueries.insertSupplier(supplierID, name, contact, address);
            JOptionPane.showMessageDialog(insertFrame, result, "Insertion Status", JOptionPane.INFORMATION_MESSAGE);
            insertFrame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(insertFrame, "Invalid Supplier ID. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    insertFrame.setVisible(true);
}

private void showInsertDonationForm() {
    JTextField donationIdField = new JTextField(10);
    JTextField donorIdField = new JTextField(10);
    JTextField inventoryIdField = new JTextField(10);
    JTextField donationDateField = new JTextField(15);
    JTextField quantityField = new JTextField(10);

    JPanel donationPanel = new JPanel(new GridLayout(6, 2));
    donationPanel.add(new JLabel("Donation ID:"));
    donationPanel.add(donationIdField);
    donationPanel.add(new JLabel("Donor ID:"));
    donationPanel.add(donorIdField);
    donationPanel.add(new JLabel("Inventory ID:"));
    donationPanel.add(inventoryIdField);
    donationPanel.add(new JLabel("Donation Date (YYYY-MM-DD):"));
    donationPanel.add(donationDateField);
    donationPanel.add(new JLabel("Quantity:"));
    donationPanel.add(quantityField);

    int result = JOptionPane.showConfirmDialog(null, donationPanel, "Enter Donation Details", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String donationId = donationIdField.getText();
        String donorId = donorIdField.getText();
        String inventoryId = inventoryIdField.getText();
        String donationDate = donationDateField.getText();
        String quantity = quantityField.getText();

        DatabaseQueries.insertDonationToDatabase(donationId, donorId, inventoryId, donationDate, quantity);
    }
}

private void showInsertPaymentForm() {
    JTextField paymentIdField = new JTextField(10);
    JTextField transactionIdField = new JTextField(10);
    JTextField amountField = new JTextField(10);
    JTextField paymentDateField = new JTextField(15);
    JTextField paymentMethodField = new JTextField(10);

    JPanel paymentPanel = new JPanel(new GridLayout(6, 2));
    paymentPanel.add(new JLabel("Payment ID:"));
    paymentPanel.add(paymentIdField);
    paymentPanel.add(new JLabel("Transaction ID:"));
    paymentPanel.add(transactionIdField);
    paymentPanel.add(new JLabel("Amount:"));
    paymentPanel.add(amountField);
    paymentPanel.add(new JLabel("Payment Date (YYYY-MM-DD):"));
    paymentPanel.add(paymentDateField);
    paymentPanel.add(new JLabel("Payment Method:"));
    paymentPanel.add(paymentMethodField);

    int result = JOptionPane.showConfirmDialog(null, paymentPanel, "Enter Payment Details", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String paymentId = paymentIdField.getText();
        String transactionId = transactionIdField.getText();
        String amount = amountField.getText();
        String paymentDate = paymentDateField.getText();
        String paymentMethod = paymentMethodField.getText();

        DatabaseQueries.insertPaymentToDatabase(paymentId, transactionId, amount, paymentDate, paymentMethod);
    }
}

private void showInsertTransactionForm() {
    JTextField transactionIdField = new JTextField(10);
    JTextField patientIdField = new JTextField(10);
    JTextField inventoryIdField = new JTextField(10);
    JTextField transactionDateField = new JTextField(15);
    JTextField quantityField = new JTextField(10);

    JPanel transactionPanel = new JPanel(new GridLayout(6, 2));
    transactionPanel.add(new JLabel("Transaction ID:"));
    transactionPanel.add(transactionIdField);
    transactionPanel.add(new JLabel("Patient ID:"));
    transactionPanel.add(patientIdField);
    transactionPanel.add(new JLabel("Inventory ID:"));
    transactionPanel.add(inventoryIdField);
    transactionPanel.add(new JLabel("Transaction Date (YYYY-MM-DD):"));
    transactionPanel.add(transactionDateField);
    transactionPanel.add(new JLabel("Quantity:"));
    transactionPanel.add(quantityField);

    int result = JOptionPane.showConfirmDialog(null, transactionPanel, "Enter Transaction Details", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String transactionId = transactionIdField.getText();
        String patientId = patientIdField.getText();
        String inventoryId = inventoryIdField.getText();
        String transactionDate = transactionDateField.getText();
        String quantity = quantityField.getText();

        DatabaseQueries.insertTransactionToDatabase(transactionId, patientId, inventoryId, transactionDate, quantity);
    }
}

private void showInsertStaffForm() {
    JTextField staffIdField = new JTextField(10);
    JTextField nameField = new JTextField(20);
    JTextField roleField = new JTextField(20);
    JTextField contactField = new JTextField(15);
    JTextField emailField = new JTextField(25);
    JTextField addressField = new JTextField(30);

    JPanel staffPanel = new JPanel(new GridLayout(7, 2));
    staffPanel.add(new JLabel("Staff ID:"));
    staffPanel.add(staffIdField);
    staffPanel.add(new JLabel("Name:"));
    staffPanel.add(nameField);
    staffPanel.add(new JLabel("Role:"));
    staffPanel.add(roleField);
    staffPanel.add(new JLabel("Contact:"));
    staffPanel.add(contactField);
    staffPanel.add(new JLabel("Email:"));
    staffPanel.add(emailField);
    staffPanel.add(new JLabel("Address:"));
    staffPanel.add(addressField);

    int result = JOptionPane.showConfirmDialog(null, staffPanel, "Enter Staff Details", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String staffId = staffIdField.getText();
        String name = nameField.getText();
        String role = roleField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();
        String address = addressField.getText();

        DatabaseQueries.insertStaffToDatabase(staffId, name, role, contact, email, address);
    }
}

private void showInsertCampaignForm() {
    JTextField campaignIdField = new JTextField(10);
    JTextField nameField = new JTextField(20);
    JTextField startDateField = new JTextField(10);
    JTextField endDateField = new JTextField(10);
    JTextField locationField = new JTextField(30);
    JTextField staffIdField = new JTextField(10);

    JPanel campaignPanel = new JPanel(new GridLayout(7, 2));
    campaignPanel.add(new JLabel("Campaign ID:"));
    campaignPanel.add(campaignIdField);
    campaignPanel.add(new JLabel("Name:"));
    campaignPanel.add(nameField);
    campaignPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
    campaignPanel.add(startDateField);
    campaignPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
    campaignPanel.add(endDateField);
    campaignPanel.add(new JLabel("Location:"));
    campaignPanel.add(locationField);
    campaignPanel.add(new JLabel("Staff ID:"));
    campaignPanel.add(staffIdField);

    int result = JOptionPane.showConfirmDialog(null, campaignPanel, "Enter Campaign Details", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String campaignId = campaignIdField.getText();
        String name = nameField.getText();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        String location = locationField.getText();
        String staffId = staffIdField.getText();

        DatabaseQueries.insertCampaignToDatabase(campaignId, name, startDate, endDate, location, staffId);
    }
}

private void showInsertFeedbackForm() {
    JTextField feedbackIdField = new JTextField(10);
    JTextField donorIdField = new JTextField(10);
    JTextField patientIdField = new JTextField(10);
    JTextField feedbackDateField = new JTextField(10);
    JTextField commentsField = new JTextField(20);

    JPanel feedbackPanel = new JPanel(new GridLayout(6, 2));
    feedbackPanel.add(new JLabel("Feedback ID:"));
    feedbackPanel.add(feedbackIdField);
    feedbackPanel.add(new JLabel("Donor ID:"));
    feedbackPanel.add(donorIdField);
    feedbackPanel.add(new JLabel("Patient ID:"));
    feedbackPanel.add(patientIdField);
    feedbackPanel.add(new JLabel("Feedback Date (YYYY-MM-DD):"));
    feedbackPanel.add(feedbackDateField);
    feedbackPanel.add(new JLabel("Comments:"));
    feedbackPanel.add(commentsField);

    int result = JOptionPane.showConfirmDialog(null, feedbackPanel, "Enter Feedback Details", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String feedbackId = feedbackIdField.getText();
        String donorId = donorIdField.getText();
        String patientId = patientIdField.getText();
        String feedbackDate = feedbackDateField.getText();
        String comments = commentsField.getText();

        DatabaseQueries.insertFeedbackToDatabase(feedbackId, donorId, patientId, feedbackDate, comments);
    }
}

private void showInsertEmergencyContactForm() {
    JTextField contactIdField = new JTextField(10);
    JTextField donorIdField = new JTextField(10);
    JTextField nameField = new JTextField(10);
    JTextField relationField = new JTextField(10);
    JTextField phoneField = new JTextField(10);

    JPanel emergencyContactPanel = new JPanel();
    emergencyContactPanel.setLayout(new GridLayout(6, 2));
    emergencyContactPanel.add(new JLabel("Contact ID:"));
    emergencyContactPanel.add(contactIdField);
    emergencyContactPanel.add(new JLabel("Donor ID:"));
    emergencyContactPanel.add(donorIdField);
    emergencyContactPanel.add(new JLabel("Name:"));
    emergencyContactPanel.add(nameField);
    emergencyContactPanel.add(new JLabel("Relation:"));
    emergencyContactPanel.add(relationField);
    emergencyContactPanel.add(new JLabel("Phone:"));
    emergencyContactPanel.add(phoneField);

    int result = JOptionPane.showConfirmDialog(null, emergencyContactPanel, "Enter Emergency Contact Details", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String contactId = contactIdField.getText();
        String donorId = donorIdField.getText();
        String name = nameField.getText();
        String relation = relationField.getText();
        String phone = phoneField.getText();

        DatabaseQueries.insertEmergencyContactToDatabase(contactId, donorId, name, relation, phone);
    }
}

private void showInsertBloodRequestForm() {
    JTextField requestIdField = new JTextField(10);
    JTextField patientIdField = new JTextField(10);
    JTextField bloodTypeField = new JTextField(10);
    JTextField quantityField = new JTextField(10);
    JTextField requestDateField = new JTextField(10);
    JTextField statusField = new JTextField(10);

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridLayout(6, 2));
    formPanel.add(new JLabel("Request ID:"));
    formPanel.add(requestIdField);
    formPanel.add(new JLabel("Patient ID:"));
    formPanel.add(patientIdField);
    formPanel.add(new JLabel("Blood Type:"));
    formPanel.add(bloodTypeField);
    formPanel.add(new JLabel("Quantity:"));
    formPanel.add(quantityField);
    formPanel.add(new JLabel("Request Date:"));
    formPanel.add(requestDateField);
    formPanel.add(new JLabel("Status:"));
    formPanel.add(statusField);

    int result = JOptionPane.showConfirmDialog(null, formPanel, "Enter Blood Request Details", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String requestId = requestIdField.getText();
        String patientId = patientIdField.getText();
        String bloodType = bloodTypeField.getText();
        String quantity = quantityField.getText();
        String requestDate = requestDateField.getText();
        String status = statusField.getText();

        DatabaseQueries.insertBloodRequest(requestId, patientId, bloodType, quantity, requestDate, status);
    }
}
private void showInsertPatientForm() {
    JTextField patientIdField = new JTextField(10);  
    JTextField nameField = new JTextField(10);       
    JTextField ageField = new JTextField(10);        
    JTextField genderField = new JTextField(10);     
    JTextField contactField = new JTextField(10);    
    JTextField addressField = new JTextField(10);

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridLayout(6, 2));  

    formPanel.add(new JLabel("Patient ID:"));
    formPanel.add(patientIdField);

    formPanel.add(new JLabel("Name:"));
    formPanel.add(nameField);

    formPanel.add(new JLabel("Age:"));
    formPanel.add(ageField);

    formPanel.add(new JLabel("Gender:"));
    formPanel.add(genderField);

    formPanel.add(new JLabel("Contact:"));
    formPanel.add(contactField);

    formPanel.add(new JLabel("Address:"));
    formPanel.add(addressField);

    int result = JOptionPane.showConfirmDialog(null, formPanel, "Enter Patient Details", JOptionPane.OK_CANCEL_OPTION);
    
    
    if (result == JOptionPane.OK_OPTION) {
       
        String patientId = patientIdField.getText();
        String name = nameField.getText();
        String age = ageField.getText();
        String gender = genderField.getText();
        String contact = contactField.getText();
        String address = addressField.getText();

        
        DatabaseQueries.insertPatient(patientId, name, age, gender, contact, address);
    }
}
private void showInsertBloodInventoryForm() {
    
    JTextField inventoryIdField = new JTextField(10);  
    JTextField bloodTypeField = new JTextField(10);    
    JTextField quantityField = new JTextField(10);     
    JTextField expiryDateField = new JTextField(10);

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridLayout(4, 2)); 

    formPanel.add(new JLabel("Inventory ID:"));
    formPanel.add(inventoryIdField);

    formPanel.add(new JLabel("Blood Type:"));
    formPanel.add(bloodTypeField);

    formPanel.add(new JLabel("Quantity:"));
    formPanel.add(quantityField);

    formPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
    formPanel.add(expiryDateField);

    int result = JOptionPane.showConfirmDialog(null, formPanel, "Enter Blood Inventory Details", JOptionPane.OK_CANCEL_OPTION);
    
    
    if (result == JOptionPane.OK_OPTION) {
        String inventoryId = inventoryIdField.getText();
        String bloodType = bloodTypeField.getText();
        String quantity = quantityField.getText();
        String expiryDate = expiryDateField.getText();

       DatabaseQueries.insertBloodInventoryy(inventoryId, bloodType, quantity, expiryDate);
    }
}

private void showDonorInputForm() {
    JPanel donorFormPanel = new JPanel(new GridLayout(9, 2, 10, 10));
    donorFormPanel.setBorder(BorderFactory.createTitledBorder("Donor Form"));

    JLabel idLabel = new JLabel("Donor ID:");
    JTextField idField = new JTextField(15);

    JLabel nameLabel = new JLabel("Donor Name:");
    JTextField nameField = new JTextField(15);

    JLabel ageLabel = new JLabel("Age:");
    JTextField ageField = new JTextField(15);

    JLabel genderLabel = new JLabel("Gender:");
    JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

    JLabel bloodTypeLabel = new JLabel("Blood Type:");
    JTextField bloodTypeField = new JTextField(15);

    JLabel contactLabel = new JLabel("Contact:");
    JTextField contactField = new JTextField(15);

    JLabel emailLabel = new JLabel("Email:");
    JTextField emailField = new JTextField(15);

    JLabel addressLabel = new JLabel("Address:");
    JTextField addressField = new JTextField(15);

    donorFormPanel.add(idLabel);
    donorFormPanel.add(idField);
    donorFormPanel.add(nameLabel);
    donorFormPanel.add(nameField);
    donorFormPanel.add(ageLabel);
    donorFormPanel.add(ageField);
    donorFormPanel.add(genderLabel);
    donorFormPanel.add(genderComboBox);
    donorFormPanel.add(bloodTypeLabel);
    donorFormPanel.add(bloodTypeField);
    donorFormPanel.add(contactLabel);
    donorFormPanel.add(contactField);
    donorFormPanel.add(emailLabel);
    donorFormPanel.add(emailField);
    donorFormPanel.add(addressLabel);
    donorFormPanel.add(addressField);

    JButton submitButton = new JButton("Submit");
    JButton cancelButton = new JButton("Cancel");

    donorFormPanel.add(submitButton);
    donorFormPanel.add(cancelButton);

    int result = JOptionPane.showConfirmDialog(
        null, donorFormPanel,
        "Insert Donor Details",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
        String donorId = idField.getText().trim();
        String donorName = nameField.getText().trim();
        String age = ageField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String bloodType = bloodTypeField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (donorId.isEmpty() || donorName.isEmpty() || age.isEmpty() || gender.isEmpty() || bloodType.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } else {
            insertDonorDetails(donorId, donorName, age, gender, bloodType, contact, email, address);
        }
    }
}
private void insertDonorDetails(String id, String name, String age, String gender, String bloodType, String contact, String email, String address) {
    DatabaseQueries.insertDonor(id, name, age, gender, bloodType, contact, email, address);
}


    private JPanel createButtonPanel(String[] buttonNames, ActionListener[] listeners) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10)); 

        if (buttonNames.length != listeners.length) {
            throw new IllegalArgumentException("Button names and listeners must have the same length!");
        }

        for (int i = 0; i < buttonNames.length; i++) {
            JButton button = new JButton(buttonNames[i]);
            button.addActionListener(listeners[i]);
            panel.add(button);
        }

        return panel;
    }

    private void fetchAndDisplayDonorData() {
        textArea.setText(DatabaseQueries.fetchDonorData());
    }

    private void fetchAndDisplayDonorDonationSummary() {
        textArea.setText(DatabaseQueries.fetchDonorDonationSummary());
    }

    private void fetchAndDisplayDonorEmergencyContacts() {
        textArea.setText(DatabaseQueries.fetchDonorEmergencyContacts());
    }
private void fetchAndDisplayBloodRequestDetails() {
    textArea.setText(DatabaseQueries.fetchBloodRequestDetails());
}

    private void fetchAndDisplayBloodInventory() {
        textArea.setText(DatabaseQueries.fetchBloodInventory());
    }

    private void fetchAndDisplayPendingRequests() {
        textArea.setText(DatabaseQueries.fetchPendingBloodRequests());
    }

    private void fetchAndDisplayDonationDetails() {
        textArea.setText(DatabaseQueries.fetchDonationDetails());
    }

    private void fetchAndDisplayExpiredBloodInventory() {
        textArea.setText(DatabaseQueries.fetchExpiredBloodInventory());
    }


    private void fetchAndDisplayCampaignDetails() {
        textArea.setText(DatabaseQueries.fetchCampaignDetails());
    }

    private void fetchAndDisplayTransactionDetails() {
        textArea.setText(DatabaseQueries.fetchTransactionDetails());
    }

    private void fetchAndDisplayAppointmentDetails() {
        textArea.setText(DatabaseQueries.fetchAppointmentDetails());
    }

    private void fetchAndDisplayFeedbackDetails() {
        textArea.setText(DatabaseQueries.fetchFeedbackDetails());
    }
    private void fetchAndDisplayPatientRecords() {
    textArea.setText(DatabaseQueries.fetchPatientRecords());
    }
    private void fetchAndDisplayStaffRecords() {
        textArea.setText(DatabaseQueries.fetchStaffRecords());
    }
    private void fetchAndDisplaySupplierSupplyDetails() {
        textArea.setText(DatabaseQueries.fetchSupplierSupplyDetails());
    }
    private void fetchAndDisplaySuppliersWithNoSupply() {
        textArea.setText(DatabaseQueries.fetchSuppliersWithNoSupply());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseConnection frame = new DatabaseConnection();
            frame.setVisible(true);
        });
    }
}
