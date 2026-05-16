# 🩸 Blood Bank Management System (BBMS)

A desktop application built with **Java Swing** and **Microsoft SQL Server** that digitizes and streamlines blood bank operations — from donor registration to blood inventory tracking and request management.

---

## 📌 Overview

Managing a blood bank manually is error-prone and slow. BBMS provides a clean, tabbed GUI that gives staff instant access to donor records, blood inventory, patient requests, donation histories, campaigns, and supplier information — all backed by a relational SQL database.

---

## ✨ Features

### 👤 Donor Management
- View complete donor profiles (name, age, gender, blood type, contact, address)
- Track donation summaries and history per donor
- Manage emergency contacts for each donor

### 🩸 Blood Inventory
- Real-time blood inventory tracking by blood type and quantity
- Expiry date monitoring with alerts for expired stock
- View pending and fulfilled blood requests

### 🏥 Patient & Request Management
- Register patients and link them to blood requests
- Track request status (pending / fulfilled)
- View full patient records with contact information

### 📋 Staff Management
- Maintain staff records including roles, contacts, and addresses

### 🎗️ Campaign Management
- Track blood donation campaigns with full details
- Manage appointments scheduled during campaigns

### 💰 Transactions & Suppliers
- Log financial transactions linked to donations and requests
- Manage supplier records and track supply deliveries
- Identify suppliers with no active supplies

### ➕ Data Insertion Forms
Full GUI forms for inserting:
- Donors, Patients, Staff
- Blood inventory, Donations, Blood requests
- Emergency contacts, Feedback, Campaigns
- Transactions, Payments, Suppliers, Supplies

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java (JDK 11+) |
| GUI Framework | Java Swing |
| Database | Microsoft SQL Server |
| JDBC Driver | mssql-jdbc 12.10.0 |
| Architecture | Tabbed GUI + SQL Query Layer |

---

## 🗄️ Database Schema

The system is built on a normalized relational schema with the following core tables:

- `Donor` — donor personal and contact info
- `BloodInventory` — blood type, quantity, expiry tracking
- `Donation` — links donors to inventory entries
- `Patient` — patient records
- `BloodRequest` — links patients to specific blood type requests with status tracking
- `Staff` — staff roles and contact details
- `Campaign` — blood drive campaign details
- `Appointment` — donor appointments per campaign
- `Transaction` — financial records
- `Supplier / Supply` — supplier management
- `Feedback` — donor/patient feedback records
- `EmergencyContact` — emergency contacts linked to donors

---

## ⚙️ Setup & Installation

### Prerequisites
- Java JDK 11 or higher
- Microsoft SQL Server (local instance)
- SQL Server Authentication enabled

### Steps

1. **Clone the repository**
```bash
git clone https://github.com/rafiullah39/BLOOD-BANK-MANAGEMENT-SYSTEM-.git
cd BLOOD-BANK-MANAGEMENT-SYSTEM-
```

2. **Set up the database**
   - Open SQL Server Management Studio (SSMS)
   - Run the provided `BBMS.sql` script to create and populate the database

3. **Configure the connection**
   - Open `DatabaseQueries.java`
   - Update the connection credentials if needed:
```java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BBMS;...";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

4. **Add the JDBC driver**
   - The `mssql-jdbc-12.10.0.jre11.jar` is included in the `/lib` folder
   - Add it to your project's classpath in your IDE

5. **Run the application**
   - Compile and run `DatabaseConnection.java` as the main entry point

---

## 📁 Project Structure

```
DBMSProject/
├── src/
│   └── databaseconnection/
│       ├── DatabaseConnection.java   # Main GUI — tabbed interface & event handling
│       └── DatabaseQueries.java      # All SQL queries and database operations
├── lib/
│   └── mssql-jdbc-12.10.0.jre11.jar # JDBC driver
├── BBMS.sql                          # Full database schema and setup script
└── DB-BBMS.pdf                       # Project documentation and ER diagram
```

---

## 🎓 Academic Context

Developed as a semester project for the **Database Management Systems (DBMS)** course at university level. Demonstrates practical application of relational database design, SQL query writing, JDBC connectivity, and Java GUI development.

---

## 👨‍💻 Author

**Rafi Khan** — CS Student  
GitHub: [@rafiullah39](https://github.com/rafiullah39)  
Fiverr: [@rafii_codes](https://fiverr.com/rafii_codes)
