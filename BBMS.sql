
USE BBMS;

CREATE TABLE Donor (
    DonorID INT PRIMARY KEY,
    Name VARCHAR(100),
    Age INT,
    Gender VARCHAR(10),
    BloodType VARCHAR(5),
    Contact VARCHAR(50),
    Email VARCHAR(100),
    Address VARCHAR(255)
);

CREATE TABLE BloodInventory (
    InventoryID INT PRIMARY KEY,
    BloodType VARCHAR(5),
    Quantity FLOAT,
    ExpiryDate DATE
);

CREATE TABLE Donation (
    DonationID INT PRIMARY KEY,
    DonorID INT FOREIGN KEY REFERENCES Donor(DonorID),
    InventoryID INT FOREIGN KEY REFERENCES BloodInventory(InventoryID),
    DonationDate DATE,
    Quantity FLOAT
);

CREATE TABLE Patient (
    PatientID INT PRIMARY KEY,
    Name VARCHAR(100),
    Age INT,
    Gender VARCHAR(10),
    Contact VARCHAR(50),
    Address VARCHAR(255)
);

CREATE TABLE BloodRequest (
    RequestID INT PRIMARY KEY,
    PatientID INT FOREIGN KEY REFERENCES Patient(PatientID),
    BloodType VARCHAR(5),
    Quantity FLOAT,
    RequestDate DATE,
    Status VARCHAR(20)
);

CREATE TABLE Staff (
    StaffID INT PRIMARY KEY,
    Name VARCHAR(100),
    Role VARCHAR(50),
    Contact VARCHAR(50),
    Email VARCHAR(100),
    Address VARCHAR(255)
);

CREATE TABLE Test (
    TestID INT PRIMARY KEY,
    DonorID INT FOREIGN KEY REFERENCES Donor(DonorID),
    TestDate DATE,
    TestResult VARCHAR(100),
    StaffID INT FOREIGN KEY REFERENCES Staff(StaffID)
);

CREATE TABLE Transactions (
    TransactionID INT PRIMARY KEY,
    PatientID INT FOREIGN KEY REFERENCES Patient(PatientID),
    InventoryID INT FOREIGN KEY REFERENCES BloodInventory(InventoryID),
    TransactionDate DATE,
    Quantity FLOAT
);

CREATE TABLE Supplier (
    SupplierID INT PRIMARY KEY,
    Name VARCHAR(100),
    Contact VARCHAR(50),
    Address VARCHAR(255)
);

CREATE TABLE Supply (
    SupplyID INT PRIMARY KEY,
    SupplierID INT FOREIGN KEY REFERENCES Supplier(SupplierID),
    InventoryID INT FOREIGN KEY REFERENCES BloodInventory(InventoryID),
    SupplyDate DATE,
    Quantity FLOAT
);

CREATE TABLE Appointment (
    AppointmentID INT PRIMARY KEY,
    DonorID INT FOREIGN KEY REFERENCES Donor(DonorID),
    StaffID INT FOREIGN KEY REFERENCES Staff(StaffID),
    AppointmentDate DATETIME
);

CREATE TABLE Campaign (
    CampaignID INT PRIMARY KEY,
    Name VARCHAR(100),
    StartDate DATE,
    EndDate DATE,
    Location VARCHAR(100),
    StaffID INT FOREIGN KEY REFERENCES Staff(StaffID)
);

CREATE TABLE Feedback (
    FeedbackID INT PRIMARY KEY,
    DonorID INT FOREIGN KEY REFERENCES Donor(DonorID),
    PatientID INT FOREIGN KEY REFERENCES Patient(PatientID),
    FeedbackDate DATE,
    Comments TEXT
);

CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY,
    TransactionID INT FOREIGN KEY REFERENCES Transactions(TransactionID),
    Amount DECIMAL(10,2),
    PaymentDate DATE,
    PaymentMethod VARCHAR(50)
);

CREATE TABLE EmergencyContact (
    ContactID INT PRIMARY KEY,
    DonorID INT FOREIGN KEY REFERENCES Donor(DonorID),
    Name VARCHAR(100),
    Relation VARCHAR(50),
    Phone VARCHAR(50)
);

CREATE TABLE UserAccount (
    UserID INT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE,
    Password VARCHAR(255),
    Role VARCHAR(50)
);

INSERT INTO Donor (DonorID, Name, Age, Gender, BloodType, Contact, Email, Address)
VALUES 
(1, 'Ali Raza', 28, 'Male', 'B+', '03001234567', 'ali.raza@example.com', 'Lahore, Punjab'),
(2, 'Sara Khan', 35, 'Female', 'O+', '03019876543', 'sara.khan@example.com', 'Karachi, Sindh');

INSERT INTO BloodInventory (InventoryID, BloodType, Quantity, ExpiryDate)
VALUES 
(1, 'B+', 2.0, '2025-07-01'),
(2, 'O+', 1.5, '2025-06-20');


INSERT INTO Donation (DonationID, DonorID, InventoryID, DonationDate, Quantity)
VALUES 
(1, 1, 1, '2025-06-10', 0.5),
(2, 2, 2, '2025-06-09', 0.5);

INSERT INTO Patient (PatientID, Name, Age, Gender, Contact, Address)
VALUES 
(1, 'Hamza Ahmed', 45, 'Male', '03122334455', 'Islamabad, Capital Territory'),
(2, 'Ayesha Siddiqui', 60, 'Female', '03211234567', 'Faisalabad, Punjab');

INSERT INTO BloodRequest (RequestID, PatientID, BloodType, Quantity, RequestDate, Status)
VALUES 
(1, 1, 'B+', 1.0, '2025-06-10', 'Pending'),
(2, 2, 'O+', 1.0, '2025-06-08', 'Fulfilled');

INSERT INTO Staff (StaffID, Name, Role, Contact, Email, Address)
VALUES 
(1, 'Dr. Farooq', 'Technician', '03451234567', 'dr.farooq@bloodbank.pk', 'Rawalpindi, Punjab'),
(2, 'Nurse Yasmeen', 'Nurse', '03330112233', 'yasmeen@bloodbank.pk', 'Peshawar, KP');

INSERT INTO Test (TestID, DonorID, TestDate, TestResult, StaffID)
VALUES 
(1, 1, '2025-06-09', 'Clear', 1),
(2, 2, '2025-06-08', 'Clear', 2);


INSERT INTO Transactions (TransactionID, PatientID, InventoryID, TransactionDate, Quantity)
VALUES 
(1, 1, 1, '2025-06-10', 0.5),
(2, 2, 2, '2025-06-09', 0.5);

INSERT INTO Supplier (SupplierID, Name, Contact, Address)
VALUES 
(1, 'Red Crescent Society', '0429876543', 'Lahore, Punjab'),
(2, 'BloodCare Pvt Ltd', '0217654321', 'Karachi, Sindh');

INSERT INTO Supply (SupplyID, SupplierID, InventoryID, SupplyDate, Quantity)
VALUES 
(1, 1, 1, '2025-06-07', 1.0),
(2, 2, 2, '2025-06-06', 1.5);


INSERT INTO Appointment (AppointmentID, DonorID, StaffID, AppointmentDate)
VALUES 
(1, 1, 1, '2025-06-05 10:00:00'),
(2, 2, 2, '2025-06-06 11:30:00');

INSERT INTO Campaign (CampaignID, Name, StartDate, EndDate, Location, StaffID)
VALUES 
(1, 'Blood Drive Lahore', '2025-06-01', '2025-06-03', 'Lahore General Hospital', 1),
(2, 'Save Lives Karachi', '2025-06-05', '2025-06-07', 'Agha Khan Hospital', 2);

INSERT INTO Feedback (FeedbackID, DonorID, PatientID, FeedbackDate, Comments)
VALUES 
(1, 1, NULL, '2025-06-10', 'Excellent experience, well organized.'),
(2, NULL, 2, '2025-06-09', 'Grateful for the timely help.');

INSERT INTO Payment (PaymentID, TransactionID, Amount, PaymentDate, PaymentMethod)
VALUES 
(1, 1, 2000.00, '2025-06-10', 'Cash'),
(2, 2, 1500.00, '2025-06-09', 'Card');

INSERT INTO EmergencyContact (ContactID, DonorID, Name, Relation, Phone)
VALUES 
(1, 1, 'Zahra Raza', 'Wife', '03011223344'),
(2, 2, 'Ahmed Khan', 'Father', '03110001122');

INSERT INTO UserAccount (UserID, Username, Password, Role)
VALUES 
(1, 'admin', 'encryptedpass123', 'Admin'),
(2, 'donor_ali', 'pass456', 'Donor');
