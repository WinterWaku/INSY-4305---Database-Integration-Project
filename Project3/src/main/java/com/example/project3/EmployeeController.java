// Ethan Nguyen
// 1002097430
// Project 3 -- Database Integration

package com.example.project3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

// Controls the Employee data entry window.
public class EmployeeController implements Initializable {

    // Input fields
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtJobClass;
    @FXML private TextField txtJobDescription;
    @FXML private TextField txtSalary;

    // Buttons whose enabled state changes based on mode
    @FXML private Button btnSave;
    @FXML private Button btnReset;

    // TableView for displaying employees
    @FXML private TableView<Employee> tableEmployees;
    @FXML private TableColumn<Employee, Integer> colId;
    @FXML private TableColumn<Employee, String>  colFirst;
    @FXML private TableColumn<Employee, String>  colLast;
    @FXML private TableColumn<Employee, String>  colEmail;
    @FXML private TableColumn<Employee, String>  colPhone;
    @FXML private TableColumn<Employee, String>  colJobClass;
    @FXML private TableColumn<Employee, String>  colJobDesc;
    @FXML private TableColumn<Employee, Double>  colSalary;

    // Search field
    @FXML private TextField txtSearch;

    // Tracks whether the user is currently in "adding a row" mode
    private boolean isAdding = false;

    // Holds the full unfiltered list fetched from the DB
    private ObservableList<Employee> allEmployees = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colFirst.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLast.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colJobClass.setCellValueFactory(new PropertyValueFactory<>("jobClass"));
        colJobDesc.setCellValueFactory(new PropertyValueFactory<>("jobDescription"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        setSaveResetDisabled(true);
        loadEmployees();
    }

    // Puts the form into "adding" mode so the user can fill fields.
    @FXML
    protected void onAddButtonClick(ActionEvent event) {
        isAdding = true;
        setSaveResetDisabled(false);
        clearFields();
    }

    // Saves the new employee to DB and JSON, then exits adding mode.
    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        if (!isAdding) return;

        if (txtFirstName.getText().isBlank()         || txtLastName.getText().isBlank()      ||
                txtEmail.getText().isBlank()          || txtPhone.getText().isBlank()         ||
                txtJobClass.getText().isBlank()       || txtJobDescription.getText().isBlank()||
                txtSalary.getText().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Please fill in all fields before saving.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(txtSalary.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Salary", "Salary must be a valid number.");
            return;
        }

        // Insert into database
        String sql = "INSERT INTO Employee (first_name, last_name, email, phone, job_class, job_description, salary) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtFirstName.getText().trim());
            ps.setString(2, txtLastName.getText().trim());
            ps.setString(3, txtEmail.getText().trim());
            ps.setString(4, txtPhone.getText().trim());
            ps.setString(5, txtJobClass.getText().trim());
            ps.setString(6, txtJobDescription.getText().trim());
            ps.setDouble(7, salary);
            ps.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save employee: " + e.getMessage());
            return;
        }

        // Write JSON
        Employee emp = new Employee(0,
                txtFirstName.getText().trim(), txtLastName.getText().trim(),
                txtEmail.getText().trim(),     txtPhone.getText().trim(),
                txtJobClass.getText().trim(),  txtJobDescription.getText().trim(),
                salary);
        try {
            new FileCreator().CreateFile("EmployeeData.json", emp.toJson());
        } catch (IOException e) {
            showAlert(Alert.AlertType.WARNING, "File Warning", "Saved to DB but could not write JSON: " + e.getMessage());
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Employee saved to database and EmployeeData.json.");
        isAdding = false;
        setSaveResetDisabled(true);
        clearFields();
        loadEmployees();
    }

    @FXML
    protected void onResetButtonClick(ActionEvent event) {
        if (!isAdding) return;
        clearFields();
    }

    // Reloads all employees from the DB and clears any active search filter.
    @FXML
    protected void onViewButtonClick(ActionEvent event) {
        txtSearch.clear();
        loadEmployees();
    }

    // Filters the already-loaded list by the search term; re-fetches from DB first to ensure fresh data.
    @FXML
    protected void onSearchButtonClick(ActionEvent event) {
        String term = txtSearch.getText().trim().toLowerCase();
        if (term.isBlank()) {
            loadEmployees();
            return;
        }

        // Always search against a fresh DB pull
        loadEmployees();

        ObservableList<Employee> filtered = FXCollections.observableArrayList();
        for (Employee e : allEmployees) {
            if (e.getFirstName().toLowerCase().contains(term)  ||
                e.getLastName().toLowerCase().contains(term)   ||
                String.valueOf(e.getEmployeeId()).contains(term)) {
                filtered.add(e);
            }
        }
        tableEmployees.setItems(filtered);
    }

    @FXML
    protected void onMainMenuButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(loader.load(), 400, 300);
        Stage stage = new Stage();
        stage.setTitle("INSY 4305 - Project 3");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    protected void onExitButtonClick(ActionEvent event) {
        System.exit(0);
    }

    // Queries the database, stores the full list, and displays it in the TableView.
    private void loadEmployees() {
        allEmployees.clear();
        String sql = "SELECT employee_id, first_name, last_name, email, phone, job_class, job_description, salary FROM Employee";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allEmployees.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("job_class"),
                        rs.getString("job_description"),
                        rs.getDouble("salary")
                ));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load employees: " + e.getMessage());
        }
        tableEmployees.setItems(FXCollections.observableArrayList(allEmployees));
    }

    private void clearFields() {
        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtJobClass.clear();
        txtJobDescription.clear();
        txtSalary.clear();
    }

    private void setSaveResetDisabled(boolean disabled) {
        btnSave.setDisable(disabled);
        btnReset.setDisable(disabled);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
