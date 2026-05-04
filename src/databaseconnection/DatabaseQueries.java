package databaseconnection;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseQueries {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BBMS;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "Sa123!@#";

    public static String fetchDonorData() {
        StringBuilder result = new StringBuilder();
        result.append("DonorID\tName\tAge\tGender\tBloodType\tContact\tEmail\tAddress\n");
        result.append("-------------------------------------------------------------\n");
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (connection != null) {
                String query = "SELECT DonorID, Name, Age, Gender, BloodType, Contact, Email, Address FROM Donor";
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(query)) {
                     
                    while (resultSet.next()) {
                        result.append(resultSet.getInt("DonorID")).append("\t");
                        result.append(resultSet.getString("Name")).append("\t");
                        result.append(resultSet.getInt("Age")).append("\t");
                        result.append(resultSet.getString("Gender")).append("\t");
                        result.append(resultSet.getString("BloodType")).append("\t");
                        result.append(resultSet.getString("Contact")).append("\t");
                        result.append(resultSet.getString("Email")).append("\t");
                        result.append(resultSet.getString("Address")).append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            result.append("Failed to fetch data: ").append(e.getMessage());
        }
        
        return result.toString();
    }

    public static String fetchRecentDonations() {
        StringBuilder result = new StringBuilder();
        result.append("DonationID\tDonorID\tInventoryID\tDonationDate\n");
        result.append("-------------------------------------------------------------\n");
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (connection != null) {
                String query = "SELECT DonationID, DonorID, InventoryID, DonationDate " +
                               "FROM Donation " +
                               "ORDER BY DonationDate DESC ";
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(query)) {
                     
                    while (resultSet.next()) {
                        result.append(resultSet.getInt("DonationID")).append("\t");
                        result.append(resultSet.getInt("DonorID")).append("\t");
                        result.append(resultSet.getInt("InventoryID")).append("\t");
                        result.append(resultSet.getDate("DonationDate")).append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            result.append("Failed to fetch data: ").append(e.getMessage());
        }
        
        return result.toString();
    }
    
    
    
    public static String fetchPendingBloodRequests() {
    StringBuilder result = new StringBuilder();
    result.append("RequestID\tBloodType\tQuantity\tRequestDate\tPatientName\tContact\n");
    result.append("--------------------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT br.RequestID, br.BloodType, br.Quantity, br.RequestDate, " +
                           "p.Name AS PatientName, p.Contact " +
                           "FROM BloodRequest br " +
                           "INNER JOIN Patient p ON br.PatientID = p.PatientID " +
                           "WHERE br.Status = 'Pending'";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("RequestID")).append("\t");
                    result.append(resultSet.getString("BloodType")).append("\t");
                    result.append(resultSet.getInt("Quantity")).append("\t");
                    result.append(resultSet.getDate("RequestDate")).append("\t");
                    result.append(resultSet.getString("PatientName")).append("\t");
                    result.append(resultSet.getString("Contact")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String fetchBloodInventory() {
    StringBuilder result = new StringBuilder();
    result.append("InventoryID\tBloodType\tQuantity\tExpiryDate\n");
    result.append("-------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT bi.InventoryID, bi.BloodType, bi.Quantity,bi.ExpiryDate  "+
                           "FROM BloodInventory bi ";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("InventoryID")).append("\t");
                    result.append(resultSet.getString("BloodType")).append("\t");
                    result.append(resultSet.getInt("Quantity")).append("\t");
                    result.append(resultSet.getString("ExpiryDate")).append("\t\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String fetchDonationDetails() {
    StringBuilder result = new StringBuilder();
    result.append("DonationID\tDonorID\tDonorName\tTotalDonated\n");
    result.append("-------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT dn.DonationID, dn.DonorID, d.Name AS DonorName, SUM(dn.Quantity) AS TotalDonated " +
                           "FROM Donation dn " +
                           "INNER JOIN Donor d ON dn.DonorID = d.DonorID " +
                           "GROUP BY dn.DonationID, dn.DonorID, d.Name";
            
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                 
                while (resultSet.next()) {
                    result.append(resultSet.getInt("DonationID")).append("\t");
                    result.append(resultSet.getInt("DonorID")).append("\t");
                    result.append(resultSet.getString("DonorName")).append("\t");
                    result.append(resultSet.getInt("TotalDonated")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}

public static String fetchTransactionDetails() {
    StringBuilder result = new StringBuilder();
    result.append("TransactionID\tPatientName\tContact\tBloodType\tQuantity\tTransactionDate\n");
    result.append("----------------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT t.TransactionID, p.Name AS PatientName, p.Contact, " +
                           "bi.BloodType, t.Quantity, t.TransactionDate " +
                           "FROM Transactions t " +
                           "INNER JOIN Patient p ON t.PatientID = p.PatientID " +
                           "INNER JOIN BloodInventory bi ON t.InventoryID = bi.InventoryID";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("TransactionID")).append("\t");
                    result.append(resultSet.getString("PatientName")).append("\t");
                    result.append(resultSet.getString("Contact")).append("\t");
                    result.append(resultSet.getString("BloodType")).append("\t");
                    result.append(resultSet.getInt("Quantity")).append("\t");
                    result.append(resultSet.getDate("TransactionDate")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}

public static String fetchDonorEmergencyContacts() {
    StringBuilder result = new StringBuilder();
    result.append("DonorID\tContactID\tDonorName\tEmergencyContactName\tRelation\tPhone\n");
    result.append("-------------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT d.DonorID, ec.ContactID, d.Name AS DonorName, ec.Name AS EmergencyContactName, " +
                           "ec.Relation, ec.Phone " +
                           "FROM Donor d " +
                           "JOIN EmergencyContact ec ON d.DonorID = ec.DonorID";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("DonorID")).append("\t");
                    result.append(resultSet.getInt("ContactID")).append("\t");
                    result.append(resultSet.getString("DonorName")).append("\t");
                    result.append(resultSet.getString("EmergencyContactName") != null 
                        ? resultSet.getString("EmergencyContactName") : "N/A").append("\t");
                    result.append(resultSet.getString("Relation") != null 
                        ? resultSet.getString("Relation") : "N/A").append("\t");
                    result.append(resultSet.getString("Phone") != null 
                        ? resultSet.getString("Phone") : "N/A").append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}

public static String fetchCampaignDetails() {
    StringBuilder result = new StringBuilder();
    result.append("CampaignID\tCampaignName\tStartDate\tEndDate\tLocation\tStaffName\tRole\n");
    result.append("------------------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT c.CampaignID, c.Name AS CampaignName, c.StartDate, c.EndDate, " +
                           "c.Location, s.Name AS StaffName, s.Role " +
                           "FROM Campaign c " +
                           "INNER JOIN Staff s ON c.StaffID = s.StaffID";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("CampaignID")).append("\t");
                    result.append(resultSet.getString("CampaignName")).append("\t");
                    result.append(resultSet.getDate("StartDate")).append("\t");
                    result.append(resultSet.getDate("EndDate")).append("\t");
                    result.append(resultSet.getString("Location")).append("\t");
                    result.append(resultSet.getString("StaffName")).append("\t");
                    result.append(resultSet.getString("Role")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String fetchFeedbackDetails() {
    StringBuilder result = new StringBuilder();
    result.append("FeedbackID\tDonorName\tPatientName\tFeedbackDate\tComments\n");
    result.append("-------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT f.FeedbackID, d.Name AS DonorName, p.Name AS PatientName, " +
                           "f.FeedbackDate, f.Comments " +
                           "FROM Feedback f " +
                           "LEFT JOIN Donor d ON f.DonorID = d.DonorID " +
                           "LEFT JOIN Patient p ON f.PatientID = p.PatientID";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("FeedbackID")).append("\t");
                    result.append(resultSet.getString("DonorName") != null 
                        ? resultSet.getString("DonorName") : "N/A").append("\t");
                    result.append(resultSet.getString("PatientName") != null 
                        ? resultSet.getString("PatientName") : "N/A").append("\t");
                    result.append(resultSet.getDate("FeedbackDate")).append("\t");
                    result.append(resultSet.getString("Comments") != null 
                        ? resultSet.getString("Comments") : "N/A").append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String fetchDonorDonationSummary() {
    StringBuilder result = new StringBuilder();
    result.append("DonorName\tTotalDonated\n");
    result.append("---------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT d.Name AS DonorName, SUM(dn.Quantity) AS TotalDonated " +
                           "FROM Donor d " +
                           "INNER JOIN Donation dn ON d.DonorID = dn.DonorID " +
                           "GROUP BY d.Name";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getString("DonorName")).append("\t");
                    result.append(resultSet.getInt("TotalDonated")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String fetchAppointmentDetails() {
    StringBuilder result = new StringBuilder();
    result.append("AppointmentID\tDonorName\tStaffName\tAppointmentDate\n");
    result.append("---------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT a.AppointmentID, d.Name AS DonorName, s.Name AS StaffName, a.AppointmentDate " +
                           "FROM Appointment a " +
                           "INNER JOIN Donor d ON a.DonorID = d.DonorID " +
                           "INNER JOIN Staff s ON a.StaffID = s.StaffID " +
                           "ORDER BY a.AppointmentDate DESC";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("AppointmentID")).append("\t");
                    result.append(resultSet.getString("DonorName")).append("\t");
                    result.append(resultSet.getString("StaffName")).append("\t");
                    result.append(resultSet.getDate("AppointmentDate")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String fetchExpiredBloodInventory() {
    StringBuilder result = new StringBuilder();
    result.append("InventoryID\tBloodType\tExpiryDate\tSupplierName\n");
    result.append("---------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT bi.InventoryID, bi.BloodType, bi.ExpiryDate, s.Name AS SupplierName " +
                           "FROM BloodInventory bi " +
                           "INNER JOIN Supply sp ON bi.InventoryID = sp.InventoryID " +
                           "INNER JOIN Supplier s ON sp.SupplierID = s.SupplierID " +
                           "WHERE bi.ExpiryDate > '2025-01-01'";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("InventoryID")).append("\t");
                    result.append(resultSet.getString("BloodType")).append("\t");
                    result.append(resultSet.getDate("ExpiryDate")).append("\t");
                    result.append(resultSet.getString("SupplierName")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String insertBloodInventory(String inventoryId, String bloodType, String expiryDate, String quantityAvailable) {
    String result = "Insertion Failed!";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "INSERT INTO BloodInventory (InventoryID, BloodType, ExpiryDate, QuantityAvailable) " +
                           "VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Integer.parseInt(inventoryId));
                preparedStatement.setString(2, bloodType);  
                preparedStatement.setString(3, expiryDate);
                preparedStatement.setInt(4, Integer.parseInt(quantityAvailable));  
                
                int rowsAffected = preparedStatement.executeUpdate();
                
                if (rowsAffected > 0) {
                    result = "Blood Inventory inserted successfully!";
                } else {
                    result = "No rows inserted.";
                }
            }
        }
    } catch (SQLException e) {
        result = "Error occurred: " + e.getMessage();
    }
    
    return result;
}

public static void insertDonor(String id, String name, String age, String gender, String bloodType, String contact, String email, String address) {
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String query = "INSERT INTO Donor (DonorID, Name, Age, Gender, BloodType, Contact, Email, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(id));
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, Integer.parseInt(age));
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, bloodType);
            preparedStatement.setString(6, contact);
            preparedStatement.setString(7, email);
            preparedStatement.setString(8, address);
            
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Donor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "ID and Age must be valid numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error inserting donor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

    public static void insertBloodInventoryy(String inventoryId, String bloodType, String quantity, String expiryDate) {
   
    String query = "INSERT INTO BloodInventory (InventoryID, BloodType, Quantity, ExpiryDate) VALUES (?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        
        stmt.setInt(1, Integer.parseInt(inventoryId));  
        stmt.setString(2, bloodType);                    
        stmt.setInt(3, Integer.parseInt(quantity));      
        stmt.setString(4, expiryDate);                   

        
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Blood Inventory inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to insert Blood Inventory.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter valid numbers for Inventory ID and Quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertPatient(String patientId, String name, String age, String gender, String contact, String address) {
    
    String query = "INSERT INTO Patient (PatientID, Name, Age, Gender, Contact, Address) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

       
        stmt.setInt(1, Integer.parseInt(patientId));  
        stmt.setString(2, name);                      
        stmt.setInt(3, Integer.parseInt(age));        
        stmt.setString(4, gender);                    
        stmt.setString(5, contact);                   
        stmt.setString(6, address);                   

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Patient inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to insert Patient.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter valid numbers for Patient ID and Age.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertBloodRequest(String requestId, String patientId, String bloodType, String quantity, String requestDate, String status) {
    String query = "INSERT INTO BloodRequest (RequestID, PatientID, BloodType, Quantity, RequestDate, Status) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        
        stmt.setInt(1, Integer.parseInt(requestId));   
        stmt.setInt(2, Integer.parseInt(patientId));    
        stmt.setString(3, bloodType);                   
        stmt.setInt(4, Integer.parseInt(quantity));     
        stmt.setDate(5, Date.valueOf(requestDate));     
        stmt.setString(6, status);                      

       
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Blood Request inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to insert Blood Request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter valid numbers for Request ID, Patient ID, and Quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(null, "Please enter a valid date for Request Date (yyyy-mm-dd).", "Date Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertEmergencyContactToDatabase(String contactId, String donorId, String name, String relation, String phone) {
    String query = "INSERT INTO EmergencyContact (ContactID, DonorID, Name, Relation, Phone) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, Integer.parseInt(contactId)); 
        stmt.setInt(2, Integer.parseInt(donorId));   
        stmt.setString(3, name);                     
        stmt.setString(4, relation);                 
        stmt.setString(5, phone);                    

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Emergency Contact added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add Emergency Contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter valid numbers for Contact ID and Donor ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertFeedbackToDatabase(String feedbackId, String donorId, String patientId, String feedbackDate, String comments) {
    String query = "INSERT INTO Feedback (FeedbackID, DonorID, PatientID, FeedbackDate, Comments) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        
        stmt.setInt(1, Integer.parseInt(feedbackId));   
        stmt.setInt(2, Integer.parseInt(donorId));      
        stmt.setInt(3, Integer.parseInt(patientId));    
        stmt.setDate(4, Date.valueOf(feedbackDate));   
        stmt.setString(5, comments);                   

        
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Feedback added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add Feedback.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid input! Ensure IDs are numbers and the date is valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertCampaignToDatabase(String campaignId, String name, String startDate, String endDate, String location, String staffId) {
    String query = "INSERT INTO Campaign (CampaignID, Name, StartDate, EndDate, Location, StaffID) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        
        stmt.setInt(1, Integer.parseInt(campaignId)); 
        stmt.setString(2, name);                      
        stmt.setDate(3, Date.valueOf(startDate));     
        stmt.setDate(4, Date.valueOf(endDate));       
        stmt.setString(5, location);                 
        stmt.setInt(6, Integer.parseInt(staffId));    

        
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Campaign added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add Campaign.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid input! Ensure IDs are numbers and the date is valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertStaffToDatabase(String staffId, String name, String role, String contact, String email, String address) {
    String query = "INSERT INTO Staff (StaffID, Name, Role, Contact, Email, Address) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, Integer.parseInt(staffId));  
        stmt.setString(2, name);                  
        stmt.setString(3, role);                   
        stmt.setString(4, contact);               
        stmt.setString(5, email);                  
        stmt.setString(6, address);                

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Staff added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add Staff.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid input! Ensure IDs are numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertTransactionToDatabase(String transactionId, String patientId, String inventoryId, String transactionDate, String quantity) {
    String query = "INSERT INTO Transactions (TransactionID, PatientID, InventoryID, TransactionDate, Quantity) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, Integer.parseInt(transactionId));   
        stmt.setInt(2, Integer.parseInt(patientId));      
        stmt.setInt(3, Integer.parseInt(inventoryId));     
        stmt.setDate(4, java.sql.Date.valueOf(transactionDate));
        stmt.setInt(5, Integer.parseInt(quantity));        

        
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add Transaction.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid input! Ensure IDs and Quantity are numbers, and Date is in YYYY-MM-DD format.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertPaymentToDatabase(String paymentId, String transactionId, String amount, String paymentDate, String paymentMethod) {
    String query = "INSERT INTO Payment (PaymentID, TransactionID, Amount, PaymentDate, PaymentMethod) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        
        stmt.setInt(1, Integer.parseInt(paymentId));        
        stmt.setInt(2, Integer.parseInt(transactionId));    
        stmt.setDouble(3, Double.parseDouble(amount));      
        stmt.setDate(4, java.sql.Date.valueOf(paymentDate));
        stmt.setString(5, paymentMethod);                  

       
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Payment added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add Payment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid input! Ensure IDs and Amount are numbers, and Date is in YYYY-MM-DD format.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static void insertDonationToDatabase(String donationId, String donorId, String inventoryId, String donationDate, String quantity) {
    String query = "INSERT INTO Donation (DonationID, DonorID, InventoryID, DonationDate, Quantity) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        
        stmt.setInt(1, Integer.parseInt(donationId));        
        stmt.setInt(2, Integer.parseInt(donorId));           
        stmt.setInt(3, Integer.parseInt(inventoryId));       
        stmt.setDate(4, java.sql.Date.valueOf(donationDate));
        stmt.setInt(5, Integer.parseInt(quantity));          

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Donation added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add Donation.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid input! Ensure IDs, Quantity are numbers, and Date is in YYYY-MM-DD format.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static String mergeEmergencyContact(int contactID, int donorID, String name, String relation, String phone) {
    String result = "Merge Successful!";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "MERGE INTO EmergencyContact AS target " +
                           "USING (SELECT ? AS ContactID, ? AS DonorID, ? AS Name, ? AS Relation, ? AS Phone) AS source " +
                           "ON target.DonorID = source.DonorID AND target.ContactID = source.ContactID " +
                           "WHEN MATCHED THEN " +
                           "UPDATE SET target.Name = source.Name, target.Relation = source.Relation, target.Phone = source.Phone " +
                           "WHEN NOT MATCHED BY TARGET THEN " +
                           "INSERT (ContactID, DonorID, Name, Relation, Phone) " +
                           "VALUES (source.ContactID, source.DonorID, source.Name, source.Relation, source.Phone);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, contactID);
                preparedStatement.setInt(2, donorID);
                preparedStatement.setString(3, name);
                preparedStatement.setString(4, relation);
                preparedStatement.setString(5, phone);

                int rowsAffected = preparedStatement.executeUpdate();
                result = rowsAffected + " rows merged successfully.";
            }
        }
    } catch (SQLException e) {
        result = "Failed to merge emergency contact: " + e.getMessage();
    }
    
    return result;
}
public static String updateBloodRequest(int requestID, String bloodType, int quantity, String requestDate, String status) {
    String result = "Update Successful!";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "UPDATE BloodRequest " +
                           "SET BloodType = ?, Quantity = ?, RequestDate = ?, Status = ? " +
                           "WHERE RequestID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, bloodType);
                preparedStatement.setInt(2, quantity);
                preparedStatement.setString(3, requestDate);
                preparedStatement.setString(4, status);
                preparedStatement.setInt(5, requestID);

                int rowsAffected = preparedStatement.executeUpdate();
                result = rowsAffected + " rows updated successfully.";
            }
        }
    } catch (SQLException e) {
        result = "Failed to update blood request: " + e.getMessage();
    }
    
    return result;
}
public static String updateFeedback(int feedbackID, String feedbackDate, String comments) {
    String result = "Update Successful!";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "UPDATE Feedback " +
                           "SET FeedbackDate = ?, Comments = ? " +
                           "WHERE FeedbackID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, feedbackDate);
                preparedStatement.setString(2, comments);
                preparedStatement.setInt(3, feedbackID);

                int rowsAffected = preparedStatement.executeUpdate();
                result = rowsAffected + " rows updated successfully.";
            }
        }
    } catch (SQLException e) {
        result = "Failed to update feedback: " + e.getMessage();
    }
    
    return result;
}
public static String updateCampaign(int campaignID, String startDate, String endDate, String location) {
    String result = "Update Successful!";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "UPDATE Campaign " +
                           "SET StartDate = ?, EndDate = ?, Location = ? " +
                           "WHERE CampaignID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, startDate);
                preparedStatement.setString(2, endDate);
                preparedStatement.setString(3, location);
                preparedStatement.setInt(4, campaignID);

                int rowsAffected = preparedStatement.executeUpdate();
                result = rowsAffected + " rows updated successfully.";
            }
        }
    } catch (SQLException e) {
        result = "Failed to update campaign: " + e.getMessage();
    }
    
    return result;
}
public static String fetchBloodRequestDetails() {
    StringBuilder result = new StringBuilder();
    result.append("RequestID\tPatientID\tBloodType\tQuantity\tRequestDate\tStatus\n");
    result.append("-------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT RequestID, PatientID, BloodType, Quantity, RequestDate, Status FROM BloodRequest";
            
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("RequestID")).append("\t");
                    result.append(resultSet.getInt("PatientID")).append("\t");
                    result.append(resultSet.getString("BloodType") != null 
                        ? resultSet.getString("BloodType") : "N/A").append("\t");
                    result.append(resultSet.getInt("Quantity")).append("\t");
                    result.append(resultSet.getDate("RequestDate")).append("\t");
                    result.append(resultSet.getString("Status") != null 
                        ? resultSet.getString("Status") : "N/A").append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}

public static String updateBloodInventory(int inventoryID, String expiryDate, int quantity) {
    String result = "Update Successful!";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "UPDATE BloodInventory " +
                           "SET ExpiryDate = ?, Quantity = ? " +
                           "WHERE InventoryID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, expiryDate);
                preparedStatement.setInt(2, quantity);
                preparedStatement.setInt(3, inventoryID);

                int rowsAffected = preparedStatement.executeUpdate();
                result = rowsAffected + " rows updated successfully.";
            }
        }
    } catch (SQLException e) {
        result = "Failed to update blood inventory: " + e.getMessage();
    }
    
    return result;
}
public static String updateStaffRole(int staffID, String role) {
    String result = "Update Successful!";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "UPDATE Staff " +
                           "SET Role = ? " +
                           "WHERE StaffID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, role);
                preparedStatement.setInt(2, staffID);

                int rowsAffected = preparedStatement.executeUpdate();
                result = rowsAffected + " rows updated successfully.";
            }
        }
    } catch (SQLException e) {
        result = "Failed to update staff role: " + e.getMessage();
    }
    
    return result;
}

public static String deleteBloodRequestFromDB(String requestDate) {
    String result = "Deletion Failed";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String query = "DELETE FROM BloodRequest WHERE status = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, requestDate);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                result = "Blood Request(s) deleted successfully!";
            } else {
                result = "No records found with the given Request Date.";
            }
        }
    } catch (SQLException e) {
        result = "Failed to delete Blood Request: " + e.getMessage();
    }
    return result;
}
public static boolean deleteDonorsAboveAge60() {
    String query = "DELETE FROM Donor WHERE Age > 60";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
            null, 
            "Error during donor deletion: " + e.getMessage(), 
            "Database Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
    return false;
}
public static String fetchPatientRecords() {
    StringBuilder result = new StringBuilder();
    result.append("PatientID\tName\tAge\tGender\tContact\tAddress\n");
    result.append("---------------------------------------------------------\n");
    
    String query = "SELECT PatientID, Name, Age, Gender, Contact, Address FROM Patient";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                
                while (resultSet.next()) {
                    result.append(resultSet.getInt("PatientID")).append("\t");
                    result.append(resultSet.getString("Name")).append("\t");
                    result.append(resultSet.getInt("Age")).append("\t");
                    result.append(resultSet.getString("Gender")).append("\t");
                    result.append(resultSet.getString("Contact")).append("\t");
                    result.append(resultSet.getString("Address")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String fetchStaffRecords() {
    StringBuilder result = new StringBuilder();
    result.append("StaffID\tName\tRole\tContact\tEmail\tAddress\n");
    result.append("------------------------------------------------------------\n");
    
    String query = "SELECT StaffID, Name, Role, Contact, Email, Address FROM Staff";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                
                while (resultSet.next()) {
                    result.append(resultSet.getInt("StaffID")).append("\t");
                    result.append(resultSet.getString("Name")).append("\t");
                    result.append(resultSet.getString("Role")).append("\t");
                    result.append(resultSet.getString("Contact")).append("\t");
                    result.append(resultSet.getString("Email")).append("\t");
                    result.append(resultSet.getString("Address")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String deleteUnlinkedPatients() {
    String query = "DELETE FROM Patient " +
                   "WHERE PatientID NOT IN (SELECT DISTINCT PatientID FROM BloodRequest) " +
                   "AND PatientID NOT IN (SELECT DISTINCT PatientID FROM Feedback) " +
                   "AND PatientID NOT IN (SELECT DISTINCT PatientID FROM Transactions)";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                int rowsAffected = statement.executeUpdate(query);
                return rowsAffected + " unlinked patient(s) deleted successfully.";
            }
        }
    } catch (SQLException e) {
        return "Failed to delete unlinked patients: " + e.getMessage();
    }
    return "No unlinked patients were found for deletion.";
}
public static String deleteUnlinkedDonors() {
    String query = "DELETE FROM Donor " +
                   "WHERE DonorID NOT IN (SELECT DISTINCT DonorID FROM Donation) " +
                   "AND DonorID NOT IN (SELECT DISTINCT DonorID FROM Test) " +
                   "AND DonorID NOT IN (SELECT DISTINCT DonorID FROM Appointment) " +
                   "AND DonorID NOT IN (SELECT DISTINCT DonorID FROM Feedback) " +
                   "AND DonorID NOT IN (SELECT DISTINCT DonorID FROM EmergencyContact)";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                int rowsAffected = statement.executeUpdate(query);
                return rowsAffected + " unlinked donor(s) deleted successfully.";
            }
        }
    } catch (SQLException e) {
        return "Failed to delete unlinked donors: " + e.getMessage();
    }
    return "No unlinked donors were found for deletion.";
}
public static String insertSupplier(int supplierID, String name, String contact, String address) {
    String query = "INSERT INTO Supplier (SupplierID, Name, Contact, Address) VALUES (?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, supplierID);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, contact);
                preparedStatement.setString(4, address);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected + " supplier(s) added successfully.";
            }
        }
    } catch (SQLException e) {
        return "Failed to add supplier: " + e.getMessage();
    }
    return "No supplier record was added.";
}
public static String insertSupply(int supplyID, int supplierID, int inventoryID, String supplyDate, int quantity) {
    String query = "INSERT INTO Supply (SupplyID, SupplierID, InventoryID, SupplyDate, Quantity) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, supplyID);
                preparedStatement.setInt(2, supplierID);
                preparedStatement.setInt(3, inventoryID);
                preparedStatement.setString(4, supplyDate);
                preparedStatement.setInt(5, quantity);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected + " supply record(s) added successfully.";
            }
        }
    } catch (SQLException e) {
        return "Failed to add supply record: " + e.getMessage();
    }
    return "No supply record was added.";
}
public static String fetchSupplierSupplyDetails() {
    StringBuilder result = new StringBuilder();
    result.append("SupplierID\tSupplierName\tContact\tAddress\tSupplyID\tInventoryID\tSupplyDate\tQuantity\n");
    result.append("---------------------------------------------------------------------------------------------\n");
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT " +
                           "    s.SupplierID, " +
                           "    s.Name AS SupplierName, " +
                           "    s.Contact, " +
                           "    s.Address, " +
                           "    sp.SupplyID, " +
                           "    sp.InventoryID, " +
                           "    sp.SupplyDate, " +
                           "    sp.Quantity " +
                           "FROM Supplier s " +
                           "INNER JOIN Supply sp ON s.SupplierID = sp.SupplierID";
                           
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("SupplierID")).append("\t");
                    result.append(resultSet.getString("SupplierName")).append("\t");
                    result.append(resultSet.getString("Contact")).append("\t");
                    result.append(resultSet.getString("Address")).append("\t");
                    result.append(resultSet.getInt("SupplyID")).append("\t");
                    result.append(resultSet.getInt("InventoryID")).append("\t");
                    result.append(resultSet.getString("SupplyDate")).append("\t");
                    result.append(resultSet.getInt("Quantity")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static String fetchSuppliersWithNoSupply() {
    StringBuilder result = new StringBuilder();
    result.append("SupplierID\tSupplierName\tContact\tAddress\n");
    result.append("----------------------------------------------------\n");

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        if (connection != null) {
            String query = "SELECT " +
                           "    s.SupplierID, " +
                           "    s.Name AS SupplierName, " +
                           "    s.Contact, " +
                           "    s.Address " +
                           "FROM Supplier s " +
                           "LEFT JOIN Supply sp ON s.SupplierID = sp.SupplierID " +
                           "WHERE sp.SupplyID IS NULL";
                           
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                     
                while (resultSet.next()) {
                    result.append(resultSet.getInt("SupplierID")).append("\t");
                    result.append(resultSet.getString("SupplierName")).append("\t");
                    result.append(resultSet.getString("Contact")).append("\t");
                    result.append(resultSet.getString("Address")).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        result.append("Failed to fetch data: ").append(e.getMessage());
    }
    
    return result.toString();
}
public static int deleteSuppliersWithoutSupply() throws SQLException {
    String query = "DELETE FROM Supplier " +
                   "WHERE SupplierID NOT IN ( " +
                   "    SELECT DISTINCT SupplierID " +
                   "    FROM Supply " +
                   ")";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        
        return preparedStatement.executeUpdate();  // Returns the number of rows affected
    } catch (SQLException e) {
        // Propagate the error to the calling method
        throw new SQLException("Error occurred while deleting suppliers: " + e.getMessage(), e);
    }
}
}