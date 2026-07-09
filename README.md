# INSY-4305---Database-Integration-Project

## Overview
Building upon the JavaFX GUI and JSON storage functionality from Project 2, **Project 3** extends the application by integrating a relational database to persist data. Instead of relying solely on JSON files, the application now stores and retrieves Customer and Employee data using a database managed directly within IntelliJ.

---

## Objectives & Learning Outcomes
This project reinforces and introduces key software development concepts:
* **Database Design:** Table creation and managing entity relationships.
* **JDBC (Java Database Connectivity):** Connecting Java applications to a relational database.
* **SQL Execution:** Performing CRUD operations via `INSERT`, `SELECT`, `UPDATE`, and `DELETE` statements.
* **GUI Integration:** Merging database functionality seamlessly into a JavaFX user interface.
* **Dual-Persistence:** Maintaining existing JSON functionality alongside database storage.
* **Robust Design:** Applying object-oriented principles and proper exception handling.

---

## Database Architecture
The application utilizes an **SQLite** (or MySQL/H2) database consisting of two tables establishing a **1:Many (1:M)** relationship, where an Employee acts as a sales representative assigned to multiple customers.

### 1. Customer Table
| Column Name | Data Type | Description |
| :--- | :--- | :--- |
| `id` | `INTEGER` | Primary Key (PK) |
| `first_name` | `TEXT` | Customer's first name |
| `last_name` | `TEXT` | Customer's last name |
| `phone` | `TEXT` | Contact phone number |
| `email` | `TEXT` | Contact email address |
| `employee_id` | `INTEGER` | Foreign Key (FK) -> Links to Employee Table |

### 2. Employee Table
| Column Name | Data Type | Description |
| :--- | :--- | :--- |
| `employee_id` | `INTEGER` | Primary Key (PK) |
| `first_name` | `TEXT` | Employee's first name |
| `last_name` | `TEXT` | Employee's last name |
| `email` | `TEXT` | Contact email address |
| `phone` | `TEXT` | Contact phone number |
| `job_class` | `TEXT` | Job classification |
| `job_description`| `TEXT` | Job description |
| `salary` | `DOUBLE` / `BIGDECIMAL` | Employee salary |

---

## Functional Requirements

### 1. Database Connectivity
* Managed via a dedicated `DatabaseConnection.java` class using JDBC.
* Properly establishes connection strings and handles connection lifecycles/errors.

### 2. Dual Data Persistence
* **Customer Window:** Clicking **Save** inserts data into the SQLite `Customer` table *and* appends it to `CustomerData.json`.
* **Employee Window:** Clicking **Save** inserts data into the SQLite `Employee` table *and* appends it to `EmployeeData.json`.

### 3. Data Retrieval (New Feature)
* **View Customers Button:** Fetches and displays all customer rows from the database using a `TableView`.
* **View Employees Button:** Fetches and displays all employee rows from the database using a `TableView`.

### 4. UI Enhancements & State Control
* Added a **Sales Rep ID** text field to the Customer window to correctly map the `employee_id` foreign key.
* Dynamic button state management (e.g., controlling when `Save` and `Reset` are clickable based on user actions).

### 5. Exception Handling
Graceful handling and user-facing alert dialogs/status labels for:
* Database connection failures
* SQL state exceptions
* `FileWriter` I/O exceptions

Included fully functional search functionality within both the Customer and Employee windows.

---

## General & Submission Requirements
* **Environment:** Built as a **Gradle Java project** inside IntelliJ.
* **UI Framework:** Strict **JavaFX GUI** application (no console I/O).
* **Code Standards:** Proper naming conventions, clean indentation, and file headers.
