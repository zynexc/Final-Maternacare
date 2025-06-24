package com.maternacare.controller;

import com.maternacare.MainApplication;
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
import javafx.scene.layout.VBox;
import javafx.scene.Node;

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
    @FXML
    private TableColumn<MaternalRecord, LocalDate> lmpColumn;
    @FXML
    private TableColumn<MaternalRecord, LocalDate> eddColumn;
    @FXML
    private TableColumn<MaternalRecord, String> contactColumn;
    @FXML
    private TableColumn<MaternalRecord, String> emailColumn;
    @FXML
    private TableColumn<MaternalRecord, Void> viewMoreColumn;
    @FXML
    private TableColumn<MaternalRecord, Void> followUpColumn;

    private ObservableList<MaternalRecord> records = FXCollections.observableArrayList();
    private FilteredList<MaternalRecord> filteredRecords;
    private MaternalRecordService maternalRecordService = new MaternalRecordService();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private MaternalFormController formController;
    private MainApplication mainApplication;

    private MaternalRecordDetailsPopupController lastDetailsPopupController = null;
    private MaternalRecord lastDetailsPopupRecord = null;

    @FXML
    private VBox rootVBox;

    @FXML
    private VBox formContainer;

    private DashboardController dashboardController;

    public void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

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
        List<MaternalRecord> savedRecords = maternalRecordService.loadRecords();
        System.out.println("Debug - Loaded " + savedRecords.size() + " records from JSON file");

        // Debug: Check pregnancy history for each record
        for (MaternalRecord record : savedRecords) {
            System.out.println("Debug - Record " + record.getPatientId() + " (" + record.getFullName() +
                    ") has " + (record.getPregnancyHistory() != null ? record.getPregnancyHistory().size() : 0)
                    + " pregnancy history entries");
            if (record.getPregnancyHistory() != null && !record.getPregnancyHistory().isEmpty()) {
                for (var history : record.getPregnancyHistory()) {
                    System.out.println("  - Pregnancy #" + history.getPregnancyNumber() +
                            ", Year: " + history.getYearDelivered() +
                            ", Delivery: " + history.getDeliveryType());
                }
            }
        }

        records.setAll(savedRecords);
    }

    private void setupTableColumns() {
        try {
            // Set the table to use fixed column widths
            recordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            // Setup columns with fixed widths and disable sorting/reordering
            patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));

            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

            // Hide the firstNameColumn since we now use full name
            firstNameColumn.setVisible(false);

            ageColumn.setCellValueFactory(cellData -> {
                MaternalRecord record = cellData.getValue();
                if (record.getDateOfBirth() != null) {
                    int age = Period.between(record.getDateOfBirth(), LocalDate.now()).getYears();
                    return new SimpleStringProperty(String.valueOf(age));
                }
                return new SimpleStringProperty("");
            });

            // Last Menstrual Period column
            lmpColumn.setCellValueFactory(new PropertyValueFactory<>("lastMenstrualPeriod"));
            lmpColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(dateFormatter.format(date));
                    }
                }
            });

            // Expected Delivery Date column
            eddColumn.setCellValueFactory(new PropertyValueFactory<>("expectedDeliveryDate"));
            eddColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(dateFormatter.format(date));
                    }
                }
            });

            // Contact Number column
            contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

            // Email Address column
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

            // View More button column
            viewMoreColumn.setCellValueFactory(param -> null);
            viewMoreColumn.setCellFactory(param -> new TableCell<>() {
                private final Button viewMoreButton = new Button("View Details");
                {
                    viewMoreButton.getStyleClass().addAll("table-button", "view-more-button");
                    viewMoreButton.setOnAction(event -> {
                        MaternalRecord record = getTableView().getItems().get(getIndex());
                        if (record != null) {
                            showRecordDetails(record);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(viewMoreButton);
                    }
                }
            });

            // Follow Up Checkup button column
            followUpColumn.setCellFactory(param -> new TableCell<>() {
                private final Button followUpButton = new Button("Follow Up");
                {
                    followUpButton.getStyleClass().addAll("table-button", "follow-up-button");
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(followUpButton);
                        followUpButton.setOnAction(event -> {
                            MaternalRecord record = getTableView().getItems().get(getIndex());
                            showFollowUpForm(record);
                        });
                    }
                }
            });

            // Remove the column visibility menu since we want a fixed table
            recordsTable.setTableMenuButtonVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to setup table columns: " + e.getMessage());
        }
    }

    private void showRecordDetails(MaternalRecord record) {
        System.out.println("showRecordDetails called for record: " + record.getPatientId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maternal_record_details_page.fxml"));
            Parent detailsPage = loader.load();
            MaternalRecordDetailsPageController controller = loader.getController();
            controller.setMaternalRecord(record);
            controller.setRecordsController(this, records);

            // Save the current content to restore later
            var originalContent = new java.util.ArrayList<>(rootVBox.getChildren());

            // Set up a back callback to restore the records table view
            controller.setOnBackCallback(() -> {
                rootVBox.getChildren().setAll(originalContent);
                refreshTable();
            });

            // Replace the VBox content with the details page
            rootVBox.getChildren().setAll(detailsPage);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to show record details: " + e.getMessage());
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
                return record.getFullName().toLowerCase().contains(lowerCaseFilter) ||
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
        records.add(record);
        try {
            maternalRecordService.saveRecords(records); // Save after adding
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save record: " + e.getMessage());
        }
    }

    // Method to update an existing record
    public void updateRecord(MaternalRecord oldRecord, MaternalRecord newRecord) {
        int index = records.indexOf(oldRecord);
        if (index != -1) {
            newRecord.setId(oldRecord.getId()); // Preserve the ID
            records.set(index, newRecord);
            try {
                maternalRecordService.saveRecords(records); // Save after updating
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save record: " + e.getMessage());
            }
        }
    }

    // Method to find a record by ID
    public MaternalRecord getRecordById(int id) {
        return records.stream().filter(record -> record.getId() == id).findFirst().orElse(null);
    }

    // Method to delete a record
    public void deleteRecord(MaternalRecord record) {
        records.remove(record);
        try {
            maternalRecordService.saveRecords(records); // Save after deleting
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save record: " + e.getMessage());
        }
    }

    public void setFormController(MaternalFormController controller) {
        this.formController = controller;
    }

    public void loadRecordIntoForm(MaternalRecord record) {
        System.out.println(
                "[DEBUG] loadRecordIntoForm called for record: " + (record != null ? record.getPatientId() : "null"));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maternal_form.fxml"));
            VBox formRoot = loader.load();
            System.out.println("[DEBUG] Loaded maternal_form.fxml, formRoot: " + (formRoot != null));
            MaternalFormController formController = loader.getController();
            System.out.println("[DEBUG] Got MaternalFormController: " + (formController != null));
            formController.editRecord(record);
            formController.setRecordsController(this);
            if (mainApplication != null) {
                System.out.println("[DEBUG] mainApplication is not null, setting content to formRoot");
                mainApplication.setContent(formRoot);
            } else {
                System.out.println("[DEBUG] mainApplication is null!");
            }
        } catch (IOException e) {
            System.out.println("[DEBUG] Exception in loadRecordIntoForm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveRecord(MaternalRecord record) {
        // Check if the record already exists based on ID
        if (records.stream().anyMatch(r -> r.getId() == record.getId())) {
            // If it exists, update it
            updateRecord(getRecordById(record.getId()), record);
        } else {
            // Otherwise, add it as a new record
            addRecord(record);
        }
        refreshTable();
    }

    private void refreshTable() {
        // Instead of clearing the list, update it with the latest data
        List<MaternalRecord> savedRecords = maternalRecordService.loadRecords();
        records.setAll(savedRecords);
        filteredRecords.setPredicate(null); // Reset the filter
    }

    private void showFollowUpForm(MaternalRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vital_signs_form.fxml"));
            Node form = loader.load();

            VitalSignsFormController controller = loader.getController();
            String patientName = record.getFullName() != null && !record.getFullName().isEmpty()
                    ? record.getFullName()
                    : record.getPatientId();
            controller.setPatientName(patientName);
            controller.setOnSaveCallback(entry -> {
                record.getFollowUpVitalSigns().add(entry);
                try {
                    maternalRecordService.saveRecords(records);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                formContainer.getChildren().clear();
                recordsTable.refresh();
            });
            controller.setOnBackCallback(() -> formContainer.getChildren().clear());

            formContainer.getChildren().setAll(form);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., show an alert)
        }
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    public void showRecordsPage() {
        if (mainApplication != null) {
            mainApplication.setContent(mainApplication.getMaternalRecordsRoot());
        }
    }
}