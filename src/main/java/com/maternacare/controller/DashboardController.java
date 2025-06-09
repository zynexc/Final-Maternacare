package com.maternacare.controller;

import com.maternacare.model.PatientData;
import com.maternacare.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardController {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private TableView<PatientData> recordsTable;
    @FXML
    private TableColumn<PatientData, String> nameColumn;
    @FXML
    private TableColumn<PatientData, Integer> ageColumn;
    @FXML
    private TableColumn<PatientData, String> sexColumn;
    @FXML
    private TableColumn<PatientData, Double> valueColumn;
    @FXML
    private TextField searchField;

    private ObservableList<PatientData> patientData = FXCollections.observableArrayList();
    private FilteredList<PatientData> filteredData;
    private MainApplication mainApplication;

    @FXML
    public void initialize() {
        // Set up table columns
        setupTableColumns();

        // Set up filtered list for table search
        setupTableSearch();

        // Add sample data
        addSampleData();

        // Update charts
        updateCharts();
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    private void setupTableSearch() {
        filteredData = new FilteredList<>(patientData, p -> true);

        // Add listener to search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return patient.getName().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(patient.getAge()).contains(lowerCaseFilter) ||
                        patient.getSex().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(patient.getValue()).contains(lowerCaseFilter);
            });
        });

        // Wrap the FilteredList in a SortedList
        SortedList<PatientData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(recordsTable.comparatorProperty());

        // Set the table's items to the sorted list
        recordsTable.setItems(sortedData);
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        recordsTable.refresh();
        updateCharts();
    }

    private void addSampleData() {
        patientData.addAll(
                new PatientData("John Doe", 25, "Male", 75.5),
                new PatientData("Jane Smith", 30, "Female", 65.2),
                new PatientData("Mike Johnson", 28, "Male", 80.0),
                new PatientData("Sarah Williams", 35, "Female", 70.8),
                new PatientData("David Brown", 32, "Male", 78.3));
    }

    private void updateCharts() {
        // Clear existing data
        barChart.getData().clear();
        pieChart.getData().clear();

        // Prepare data for bar chart (age groups)
        XYChart.Series<String, Number> ageSeries = new XYChart.Series<>();
        ageSeries.setName("Age Distribution");

        // Count patients in age groups
        int[] ageGroups = new int[4]; // 0-20, 21-30, 31-40, 41+
        for (PatientData data : patientData) {
            int age = data.getAge();
            if (age <= 20)
                ageGroups[0]++;
            else if (age <= 30)
                ageGroups[1]++;
            else if (age <= 40)
                ageGroups[2]++;
            else
                ageGroups[3]++;
        }

        // Add data to bar chart
        ageSeries.getData().add(new XYChart.Data<>("0-20", ageGroups[0]));
        ageSeries.getData().add(new XYChart.Data<>("21-30", ageGroups[1]));
        ageSeries.getData().add(new XYChart.Data<>("31-40", ageGroups[2]));
        ageSeries.getData().add(new XYChart.Data<>("41+", ageGroups[3]));

        barChart.getData().add(ageSeries);

        // Prepare data for pie chart (gender distribution)
        int maleCount = 0;
        int femaleCount = 0;

        for (PatientData data : patientData) {
            if (data.getSex().equalsIgnoreCase("Male")) {
                maleCount++;
            } else {
                femaleCount++;
            }
        }

        pieChart.getData().add(new PieChart.Data("Male", maleCount));
        pieChart.getData().add(new PieChart.Data("Female", femaleCount));
    }

    public void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @FXML
    private void handleLogout() {
        try {
            if (mainApplication != null) {
                mainApplication.showLoginScreen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}