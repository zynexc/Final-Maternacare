package com.maternacare.controller;

import com.maternacare.model.PatientData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientRecordsController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<PatientData> recordsTable;
    @FXML
    private TableColumn<PatientData, String> nameColumn;
    @FXML
    private TableColumn<PatientData, Integer> ageColumn;
    @FXML
    private TableColumn<PatientData, String> sexColumn;
    @FXML
    private TableColumn<PatientData, Integer> valueColumn;

    private ObservableList<PatientData> records = FXCollections.observableArrayList();
    private FilteredList<PatientData> filteredRecords;

    @FXML
    public void initialize() {
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Set up filtered list for table search
        setupTableSearch();

        // Add sample data
        addSampleData();
    }

    private void setupTableSearch() {
        filteredRecords = new FilteredList<>(records, p -> true);

        // Add listener to search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRecords.setPredicate(record -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return record.getName().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(record.getAge()).contains(lowerCaseFilter) ||
                        record.getSex().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(record.getValue()).contains(lowerCaseFilter);
            });
        });

        // Wrap the FilteredList in a SortedList
        SortedList<PatientData> sortedRecords = new SortedList<>(filteredRecords);
        sortedRecords.comparatorProperty().bind(recordsTable.comparatorProperty());

        // Set the table's items to the sorted list
        recordsTable.setItems(sortedRecords);
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        recordsTable.refresh();
    }

    private void addSampleData() {
        records.addAll(
                new PatientData("John Doe", 25, "Male", 85),
                new PatientData("Jane Smith", 35, "Female", 92),
                new PatientData("Mike Johnson", 45, "Male", 78),
                new PatientData("Sarah Williams", 28, "Female", 88));
    }
}