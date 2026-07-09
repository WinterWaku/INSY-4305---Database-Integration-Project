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

// Controls the Customer data entry window.
public class CustomerController implements Initializable {

    // Input fields
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtSalesRepId;   // FK to Employee

    // Buttons
    @FXML private Button btnSave;
    @FXML private Button btnReset;

    // TableView
    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String>  colFirst;
    @FXML private TableColumn<Customer, String>  colLast;
    @FXML private TableColumn<Customer, String>  colPhone;
    @FXML private TableColumn<Customer, String>  colEmail;
    @FXML private TableColumn<Customer, Integer> colSalesRep;

    // Search
    @FXML private TextField txtSearch;

    private boolean isAdding = false;

    // Holds the full unfiltered list fetched from the DB
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirst.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLast.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSalesRep.setCellValueFactory(new PropertyValueFactory<>("salesRepId"));

        setSaveResetDisabled(true);
        loadCustomers();
    }

    @FXML
    protected void onAddButtonClick(ActionEvent event) {
        isAdding = true;
        setSaveResetDisabled(false);
        clearFields();
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        if (!isAdding) return;

        if (txtFirstName.getText().isBlank() || txtLastName.getText().isBlank() ||
                txtPhone.getText().isBlank() || txtEmail.getText().isBlank()    ||
                txtSalesRepId.getText().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Please fill in all fields before saving.");
            return;
        }

        int salesRepId;
        try {
            salesRepId = Integer.parseInt(txtSalesRepId.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Sales Rep ID", "Sales Rep ID must be a valid integer.");
            return;
        }

        // Verify the Sales Rep ID exists in the Employee table
        String checkSql = "SELECT COUNT(*) FROM Employee WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, salesRepId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Sales Rep",
                        "No employee found with ID " + salesRepId + ". Please enter a valid Employee ID.");
                return;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not verify Sales Rep ID: " + e.getMessage());
            return;
        }

        // Insert into database
        String sql = "INSERT INTO Customer (first_name, last_name, phone, email, employee_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtFirstName.getText().trim());
            ps.setString(2, txtLastName.getText().trim());
            ps.setString(3, txtPhone.getText().trim());
            ps.setString(4, txtEmail.getText().trim());
            ps.setInt(5, salesRepId);
            ps.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save customer: " + e.getMessage());
            return;
        }

        // Write JSON
        Customer cust = new Customer(0,
                txtFirstName.getText().trim(), txtLastName.getText().trim(),
                txtPhone.getText().trim(),     txtEmail.getText().trim(),
                salesRepId);
        try {
            new FileCreator().CreateFile("CustomerData.json", cust.toJson());
        } catch (IOException e) {
            showAlert(Alert.AlertType.WARNING, "File Warning", "Saved to DB but could not write JSON: " + e.getMessage());
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Customer saved to database and CustomerData.json.");
        isAdding = false;
        setSaveResetDisabled(true);
        clearFields();
        loadCustomers();
    }

    @FXML
    protected void onResetButtonClick(ActionEvent event) {
        if (!isAdding) return;
        clearFields();
    }

    // Reloads all customers from the DB and clears any active search filter.
    @FXML
    protected void onViewButtonClick(ActionEvent event) {
        txtSearch.clear();
        loadCustomers();
    }

    // Filters the already-loaded list by the search term; re-fetches from DB first to ensure fresh data.
    @FXML
    protected void onSearchButtonClick(ActionEvent event) {
        String term = txtSearch.getText().trim().toLowerCase();
        if (term.isBlank()) {
            loadCustomers();
            return;
        }

        // Always search against a fresh DB pull
        loadCustomers();

        ObservableList<Customer> filtered = FXCollections.observableArrayList();
        for (Customer c : allCustomers) {
            if (c.getFirstName().toLowerCase().contains(term) ||
                c.getLastName().toLowerCase().contains(term)  ||
                String.valueOf(c.getId()).contains(term)) {
                filtered.add(c);
            }
        }
        tableCustomers.setItems(filtered);
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
    private void loadCustomers() {
        allCustomers.clear();
        String sql = "SELECT id, first_name, last_name, phone, email, employee_id FROM Customer";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allCustomers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getInt("employee_id")
                ));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load customers: " + e.getMessage());
        }
        tableCustomers.setItems(FXCollections.observableArrayList(allCustomers));
    }

    private void clearFields() {
        txtFirstName.clear();
        txtLastName.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtSalesRepId.clear();
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
