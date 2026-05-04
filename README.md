# 🩸 Blood Bank Management System (BBMS)

## 📌 Project Overview

The Blood Bank Management System (BBMS) is a database-driven application designed to manage blood donor records, blood inventory, and patient requests efficiently. This project demonstrates core concepts of Database Management Systems (DBMS) using SQL and Java.

---

## 🎯 Features

* 🧑‍🤝‍🧑 Donor Registration & Management
* 🩸 Blood Inventory Tracking
* 🏥 Patient Request Handling
* 🔍 Search & Filter Records
* 🔗 Database Connectivity using JDBC

---

## 🛠️ Technologies Used

* **Language:** Java
* **Database:** Microsoft SQL Server
* **Connectivity:** JDBC Driver
* **Tools:** SQL Server Management Studio (SSMS), IntelliJ / VS Code

---

## 📂 Project Structure

```
DBMSProject/
│── BBMS.sql                  # Database schema & queries
│── connection.txt           # Database connection details
│── src/
│   └── databaseconnection/
│       ├── DatabaseConnection.java
│       └── DatabaseQueries.java
│── lib/
│   └── mssql-jdbc driver    # JDBC driver for SQL Server
│── Documentation/
│   ├── DB-BBMS.pdf
│   └── Presentation.pptx
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/rafiullah39/BLOOD-BANK-MANAGEMENT-SYSTEM-.git
cd BLOOD-BANK-MANAGEMENT-SYSTEM-
```

### 2️⃣ Setup Database

* Open SQL Server (SSMS)
* Run `BBMS.sql` file to create database and tables

### 3️⃣ Configure Connection

* Open `connection.txt` or Java file
* Update:

  * Server name
  * Database name
  * Username & password

### 4️⃣ Run the Project

* Open project in IDE
* Add JDBC driver (from `/lib` folder)
* Run `DatabaseConnection.java`

---

## 📊 Learning Outcomes

* Understanding relational database design
* Writing SQL queries (CRUD operations)
* Implementing database connectivity using JDBC
* Managing real-world data scenarios

---

## 📸 Screenshots / Demo

(Add screenshots here if needed)

---

## 👨‍💻 Author

**Rafiullah**
GitHub: https://github.com/rafiullah39

---

## 📜 License

This project is for educational purposes only.
