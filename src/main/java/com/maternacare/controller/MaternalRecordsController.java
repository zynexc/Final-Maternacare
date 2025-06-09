package com.maternacare.controller;

import com.maternacare.model.MaternalRecord;
import com.maternacare.service.MaternalRecordService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MaternalRecordsController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<MaternalRecord> recordsTable;

    // Only keep the columns we're actually using
    @FXML
    private TableColumn<MaternalRecord, String> patientIdColumn;
    @FXML
    private TableColumn<MaternalRecord, String> lastNameColumn;
    @FXML
    private TableColumn<MaternalRecord, String> firstNameColumn;
    @FXML
    private TableColumn<MaternalRecord, String> ageColumn;

    private ObservableList<MaternalRecord> records = FXCollections.observableArrayList();
    private FilteredList<MaternalRecord> filteredRecords;
    private MaternalRecordService recordService = new MaternalRecordService();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @FXML
    public void initialize() {
        try {
            // Set up table columns
            setupTableColumns();

            // Load saved records
            loadSavedRecords();

            // Set up filtered list for table search
            setupTableSearch();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to initialize maternal records: " + e.getMessage());
        }
    }

    private void loadSavedRecords() {
        List<MaternalRecord> savedRecords = recordService.loadRecords();
        records.setAll(savedRecords);
    }

    private void setupTableColumns() {
        try {
            patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            ageColumn.setCellValueFactory(cellData -> {
                MaternalRecord record = cellData.getValue();
                if (record.getDateOfBirth() != null) {
                    int age = Period.between(record.getDateOfBirth(), LocalDate.now()).getYears();
                    return new SimpleStringProperty(String.valueOf(age));
                }
                return new SimpleStringProperty("");
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to setup table columns: " + e.getMessage());
        }
    }

    @FXML
    private void handleRecordClick(javafx.scene.input.MouseEvent event) {
        if (event.getClickCount() == 2) { // Double click to show details
            MaternalRecord selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
            if (selectedRecord != null) {
                showRecordDetails(selectedRecord);
            }
        }
    }

    private void showRecordDetails(MaternalRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maternal_record_details.fxml"));
            Parent root = loader.load();

            MaternalRecordDetailsController controller = loader.getController();
            controller.setRecord(record);

            Stage stage = new Stage();
            stage.setTitle("Patient Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load patient details.");
        }
    }

    private void setupTableSearch() {
        filteredRecords = new FilteredList<>(records, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRecords.setPredicate(record -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return record.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                        record.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                        record.getPatientId().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<MaternalRecord> sortedData = new SortedList<>(filteredRecords);
        sortedData.comparatorProperty().bind(recordsTable.comparatorProperty());
        recordsTable.setItems(sortedData);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        refreshTable();
    }

    // Method to add a new record
    public void addRecord(MaternalRecord record) {
        // Generate a new ID based on the current size of records
        record.setId(records.stream().mapToInt(MaternalRecord::getId).max().orElse(0) + 1);
        records.add(record);
        recordService.saveRecords(records); // Save after adding
    }

    // Method to update an existing record
    public void updateRecord(MaternalRecord oldRecord, MaternalRecord newRecord) {
        int index = records.indexOf(oldRecord);
        if (index != -1) {
            newRecord.setId(oldRecord.getId()); // Preserve the ID
            records.set(index, newRecord);
            recordService.saveRecords(records); // Save after updating
        }
    }

    // Method to find a record by ID
    public MaternalRecord findRecordById(int id) {
        return records.stream()
                .filter(record -> record.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Method to get the form controller
    private MaternalFormController formController;

    public void setFormController(MaternalFormController controller) {
        this.formController = controller;
    }

    // Method to load a record into the form
    public void loadRecordIntoForm(MaternalRecord record) {
        if (formController != null) {
            formController.loadRecord(record);
        }
    }

    // Method to save a record (add or update)
    public void saveRecord(MaternalRecord record) {
        boolean found = false;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId() == record.getId() && record.getId() != 0) {
                // Update existing record
                records.set(i, record);
                found = true;
                break;
            }
        }
        if (!found) {
            // Add new record with a new ID
            int newId = records.stream().mapToInt(MaternalRecord::getId).max().orElse(0) + 1;
            record.setId(newId);
            records.add(record);
        }
        recordService.saveRecords(records); // Save after adding or updating

        // Refresh the table
        refreshTable();
    }

    private void refreshTable() {
        // Reload records from file
        loadSavedRecords();

        // Refresh the table view
        recordsTable.refresh();

        // Force the table to update its layout
        recordsTable.requestLayout();
    }
}