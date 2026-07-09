// Ethan Nguyen
// 1002097430
// Project 3 -- Database Integration

package com.example.project3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Manages SQLite database connection and initial schema creation.
public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:project3.db";

    // Returns a new connection to the SQLite database.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Creates the Employee and Customer tables if they do not already exist.
    public static void initializeDatabase() {
        String createEmployee = """
                CREATE TABLE IF NOT EXISTS Employee (
                    employee_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name  TEXT NOT NULL,
                    last_name   TEXT NOT NULL,
                    email       TEXT NOT NULL,
                    phone       TEXT NOT NULL,
                    job_class   TEXT NOT NULL,
                    job_description TEXT NOT NULL,
                    salary      REAL NOT NULL
                );
                """;

        String createCustomer = """
                CREATE TABLE IF NOT EXISTS Customer (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name  TEXT NOT NULL,
                    last_name   TEXT NOT NULL,
                    phone       TEXT NOT NULL,
                    email       TEXT NOT NULL,
                    employee_id INTEGER,
                    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createEmployee);
            stmt.execute(createCustomer);
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}
