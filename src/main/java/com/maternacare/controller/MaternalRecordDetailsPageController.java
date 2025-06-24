package com.maternacare.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Separator;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import com.maternacare.model.MaternalRecord;
import com.maternacare.model.PregnancyHistory;
import com.maternacare.model.VitalSignsEntry;
import com.maternacare.service.MaternalRecordService;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

public class MaternalRecordDetailsPageController {

    // Personal & Contact Info
    @FXML
    private Label patientIdLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label purokLabel;
    @FXML
    private Label contactNumberLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label husbandNameLabel;
    @FXML
    private Label lmpLabel;
    @FXML
    private Label eddLabel;
    @FXML
    private Label nextAppointmentLabel;

    // Vitals & Pregnancy Info
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label pulseRateLabel;
    @FXML
    private Label respiratoryRateLabel;
    @FXML
    private Label aogLabel;
    @FXML
    private Label heightLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label fhtLabel;
    @FXML
    private Label presentationLabel;
    @FXML
    private Label chiefComplaintLabel;
    @FXML
    private Label toComeBackLabel;
    @FXML
    private Label remarksLabel;

    // Stats
    @FXML
    private Label gravidaLabel;
    @FXML
    private Label paraLabel;
    @FXML
    private Label livingChildrenLabel;
    @FXML
    private Label abortionLabel;

    // Dynamic containers
    @FXML
    private VBox pregnancyHistoryContainer;
    @FXML
    private VBox followUpContainer;
    @FXML
    private VBox followUpFormContainer;
    @FXML
    private VBox detailsRoot;

    @FXML
    private Button editRecordButton;
    @FXML
    private Button deleteRecordButton;

    @FXML
    private HBox deleteConfirmBox;
    @FXML
    private Button confirmDeleteButton;
    @FXML
    private Button cancelDeleteButton;

    @FXML
    private Button backButton;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private Runnable onBackCallback;

    private MaternalRecord currentRecord;

    private VBox currentlyConfirmingDeleteCard = null;

    private MaternalRecordsController recordsController;
    private ObservableList<MaternalRecord> records;
    private MaternalRecordService maternalRecordService = new MaternalRecordService();

    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        if (onBackCallback != null) {
            onBackCallback.run();
        }
    }

    @FXML
    private void initialize() {
        detailsRoot.getStylesheets()
                .add(getClass().getResource("/styles/maternal_record_details.css").toExternalForm());
        if (editRecordButton != null) {
            editRecordButton.setOnAction(e -> handleEditRecord());
        }
        if (deleteRecordButton != null) {
            deleteRecordButton.setOnAction(e -> showDeleteConfirmation());
        }
        if (confirmDeleteButton != null) {
            confirmDeleteButton.setOnAction(e -> handleConfirmDelete());
        }
        if (cancelDeleteButton != null) {
            cancelDeleteButton.setOnAction(e -> hideDeleteConfirmation());
        }
        if (backButton != null) {
            backButton.setOnAction(e -> handleBack(null));
        }
        if (deleteConfirmBox != null) {
            deleteConfirmBox.setVisible(false);
            deleteConfirmBox.setManaged(false);
        }
    }

    public void setMaternalRecord(MaternalRecord record) {
        this.currentRecord = record;
        // Personal & Contact Info
        patientIdLabel.setText(record.getPatientId());
        fullNameLabel.setText(record.getFullName());
        dobLabel.setText(formatDate(record.getDateOfBirth()));
        ageLabel.setText(record.getDateOfBirth() != null
                ? String.valueOf(LocalDate.now().getYear() - record.getDateOfBirth().getYear())
                : "");
        addressLabel.setText(record.getAddress());
        purokLabel.setText(record.getPurok());
        contactNumberLabel.setText(record.getContactNumber());
        emailLabel.setText(record.getEmail());
        husbandNameLabel.setText(record.getHusbandName() != null ? record.getHusbandName() : "N/A");
        lmpLabel.setText(formatDate(record.getLastMenstrualPeriod()));
        eddLabel.setText(formatDate(record.getExpectedDeliveryDate()));
        nextAppointmentLabel.setText(formatDate(record.getNextAppointment()));

        // Vitals & Pregnancy Info
        temperatureLabel.setText(record.getTemperature());
        pulseRateLabel.setText(""); // If you have a pulse rate field, set it here
        respiratoryRateLabel.setText(""); // If you have a respiratory rate field, set it here
        aogLabel.setText(String.valueOf(record.getAgeOfGestation()));
        heightLabel.setText(String.valueOf(record.getHeight()));
        weightLabel.setText(String.valueOf(record.getWeight()));
        fhtLabel.setText(String.valueOf(record.getFetalHeartTone()));
        presentationLabel.setText(record.getPresentation());
        chiefComplaintLabel.setText(record.getChiefComplaint());
        toComeBackLabel.setText(formatDate(record.getNextAppointment()));
        remarksLabel.setText(record.getRemarks());

        // Stats
        gravidaLabel.setText(record.getGravida());
        paraLabel.setText(record.getPara());
        livingChildrenLabel.setText(record.getLivingChildren());
        abortionLabel.setText(record.getAbortion());

        // Pregnancy History
        pregnancyHistoryContainer.getChildren().clear();
        int count = 1;
        for (PregnancyHistory history : record.getPregnancyHistory()) {
            pregnancyHistoryContainer.getChildren().add(createPregnancyHistoryCard(history, count++));
        }

        // Follow-Up Vital Signs
        followUpContainer.getChildren().clear();
        for (VitalSignsEntry entry : record.getFollowUpVitalSigns()) {
            followUpContainer.getChildren().add(createFollowUpCard(entry));
        }
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.format(dateFormatter) : "";
    }

    private Node createPregnancyHistoryCard(PregnancyHistory history, int number) {
        VBox card = new VBox(8);
        card.getStyleClass().add("history-box");
        card.setPadding(new Insets(12));
        card.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 4; -fx-background-color: #fff;");

        // Title with pregnancy number
        Label titleLabel = new Label("Patient #" + number);
        titleLabel.getStyleClass().add("history-title");
        titleLabel.setStyle(
                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: #eb0000; -fx-background-radius: 6; -fx-padding: 4 12;");

        // Details grid (single column, label and value per row)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getColumnConstraints().addAll(
                new ColumnConstraints(), new ColumnConstraints());
        int row = 0;
        grid.add(new Label("Delivery:"), 0, row);
        grid.add(new Label(history.getDeliveryType()), 1, row++);
        grid.add(new Label("Gender:"), 0, row);
        grid.add(new Label(history.getGender()), 1, row++);
        grid.add(new Label("Place:"), 0, row);
        grid.add(new Label(history.getPlaceOfDelivery()), 1, row++);
        grid.add(new Label("Year:"), 0, row);
        grid.add(new Label(String.valueOf(history.getYearDelivered())), 1, row++);
        grid.add(new Label("Attended By:"), 0, row);
        grid.add(new Label(history.getAttendedBy()), 1, row++);
        grid.add(new Label("Status:"), 0, row);
        grid.add(new Label(history.getStatus()), 1, row++);
        grid.add(new Label("Birth Date:"), 0, row);
        grid.add(new Label(formatDate(history.getBirthDate())), 1, row++);
        grid.add(new Label("TT Injection:"), 0, row);
        grid.add(new Label(history.getTtInjection()), 1, row++);

        card.getChildren().addAll(titleLabel, grid);
        return card;
    }

    private Node createFollowUpCard(VitalSignsEntry entry) {
        VBox card = new VBox(8);
        card.getStyleClass().add("followup-box");
        card.setPadding(new Insets(12));
        card.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 4; -fx-background-color: #fafafa;");

        // Date label
        Label dateLabel = new Label(entry.getDate() != null ? entry.getDate().format(dateFormatter) : "N/A");
        dateLabel.getStyleClass().add("followup-date-label");
        dateLabel.setStyle(
                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: #eb0000; -fx-background-radius: 6; -fx-padding: 4 12;");

        // Only show the date label at the top
        card.getChildren().add(dateLabel);

        // Details grid (single column, label and value per row)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getColumnConstraints().addAll(
                new ColumnConstraints(), new ColumnConstraints());
        int row = 0;
        grid.add(new Label("Blood Pressure:"), 0, row);
        grid.add(new Label(entry.getBloodPressure()), 1, row++);
        grid.add(new Label("Temperature:"), 0, row);
        grid.add(new Label(entry.getTemperature()), 1, row++);
        grid.add(new Label("Pulse Rate:"), 0, row);
        grid.add(new Label(entry.getPulseRate()), 1, row++);
        grid.add(new Label("Respiratory Rate:"), 0, row);
        grid.add(new Label(entry.getRespiratoryRate()), 1, row++);
        grid.add(new Label("Age of Gestation:"), 0, row);
        grid.add(new Label(entry.getAog()), 1, row++);
        grid.add(new Label("Height (cm):"), 0, row);
        grid.add(new Label(entry.getHeight()), 1, row++);
        grid.add(new Label("Weight (lbs):"), 0, row);
        grid.add(new Label(entry.getWeight()), 1, row++);
        grid.add(new Label("Fetal Heart Tone:"), 0, row);
        grid.add(new Label(entry.getFht()), 1, row++);
        grid.add(new Label("Presentation:"), 0, row);
        grid.add(new Label(entry.getPresentation()), 1, row++);
        grid.add(new Label("Chief Complaint:"), 0, row);
        grid.add(new Label(entry.getChiefComplaint()), 1, row++);
        grid.add(new Label("To come Back:"), 0, row);
        grid.add(new Label(formatDate(entry.getToComeBack())), 1, row++);
        grid.add(new Label("Remarks:"), 0, row);
        grid.add(new Label(entry.getRemarks()), 1, row++);

        card.getChildren().add(grid);

        return card;
    }

    private void handleEditFollowUp(VitalSignsEntry entry) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vital_signs_form.fxml"));
            Node form = loader.load();
            followUpFormContainer.getChildren().setAll(form);
            VitalSignsFormController controller = loader.getController();
            controller.setPatientName(currentRecord.getFullName());
            controller.setOnSaveCallback(updatedEntry -> {
                // Replace the old entry with the updated one
                int idx = currentRecord.getFollowUpVitalSigns().indexOf(entry);
                if (idx != -1) {
                    currentRecord.getFollowUpVitalSigns().set(idx, updatedEntry);
                    saveAndRefreshFollowUps();
                }
                followUpFormContainer.getChildren().clear();
            });
            controller.setOnBackCallback(() -> followUpFormContainer.getChildren().clear());
            // Pre-fill the form fields
            controller.prefillFields(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAndRefreshFollowUps() {
        // Save the current record (implement saving as needed)
        if (records != null) {
            try {
                maternalRecordService.saveRecords(records);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Then refresh the followUpContainer
        followUpContainer.getChildren().clear();
        for (VitalSignsEntry entry : currentRecord.getFollowUpVitalSigns()) {
            followUpContainer.getChildren().add(createFollowUpCard(entry));
        }
    }

    @FXML
    private void handleFollowUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vital_signs_form.fxml"));
            Node form = loader.load();
            followUpFormContainer.getChildren().setAll(form);

            // Set up callback to clear form after save
            VitalSignsFormController controller = loader.getController();
            controller.setOnSaveCallback(entry -> {
                // Add entry to followUpContainer (optional: update list)
                followUpFormContainer.getChildren().clear();
                // Optionally, refresh followUpContainer here
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRecordsController(MaternalRecordsController controller, ObservableList<MaternalRecord> records) {
        this.recordsController = controller;
        this.records = records;
    }

    private void handleEditRecord() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maternal_record_edit_form.fxml"));
            VBox editFormRoot = loader.load();
            com.maternacare.controller.MaternalRecordEditFormController editController = loader.getController();
            editController.setRecordForEditing(currentRecord); // You implement this method in your new controller
            editController.setRecordsController(recordsController); // Pass the records controller for navigation

            // Replace the main content area
            if (recordsController != null && recordsController.getMainApplication() != null) {
                recordsController.getMainApplication().setContent(editFormRoot);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmation() {
        if (deleteConfirmBox != null) {
            deleteConfirmBox.setVisible(true);
            deleteConfirmBox.setManaged(true);
        }
    }

    private void hideDeleteConfirmation() {
        if (deleteConfirmBox != null) {
            deleteConfirmBox.setVisible(false);
            deleteConfirmBox.setManaged(false);
        }
    }

    private void handleConfirmDelete() {
        if (recordsController != null && currentRecord != null && records != null) {
            recordsController.deleteRecord(currentRecord);
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        }
    }

    private void handleDeleteRecord() {
        showDeleteConfirmation();
    }
}