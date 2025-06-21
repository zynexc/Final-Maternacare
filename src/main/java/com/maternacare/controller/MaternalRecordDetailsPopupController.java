package com.maternacare.controller;

import com.maternacare.model.MaternalRecord;
import com.maternacare.model.PregnancyHistory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;

public class MaternalRecordDetailsPopupController {

        @FXML
        private Label formTimestampLabel;
        @FXML
        private Label patientIdLabel;
        @FXML
        private Label fullNameLabel;
        @FXML
        private Label dateOfBirthLabel;
        @FXML
        private Label husbandNameLabel;
        @FXML
        private Label addressLabel;
        @FXML
        private Label purokLabel;
        @FXML
        private Label contactNumberLabel;
        @FXML
        private Label emailLabel;
        @FXML
        private Label ageOfGestationLabel;
        @FXML
        private Label weightLabel;
        @FXML
        private Label heightLabel;
        @FXML
        private Label bloodPressureLabel;
        @FXML
        private Label temperatureLabel; // Body Temperature label
        @FXML
        private Label chiefComplaintLabel;
        @FXML
        private Label fetalHeartToneLabel;
        @FXML
        private Label presentationLabel;
        @FXML
        private Label fundalHeightLabel;
        @FXML
        private Label lastMenstrualPeriodLabel;
        @FXML
        private Label expectedDeliveryDateLabel;
        @FXML
        private Label paraLabel;
        @FXML
        private Label abortionLabel;
        @FXML
        private Label livingChildrenLabel;
        @FXML
        private Label remarksLabel;
        @FXML
        private Label nextAppointmentLabel;

        @FXML
        private VBox pregnancyHistoryContainer;
        @FXML
        private VBox followUpVitalSignsContainer;

        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        @FXML
        public void initialize() {
                // Initialize the pregnancy history container if it's null
                if (pregnancyHistoryContainer == null) {
                        pregnancyHistoryContainer = new VBox(10);
                        pregnancyHistoryContainer.getStyleClass().add("form-section");
                }
        }

        public void setRecord(MaternalRecord record) {
                if (record != null) {
                        System.out.println("Debug - Setting record for patient: " + record.getPatientId());
                        System.out.println("Debug - Record has pregnancy history: "
                                        + (record.getPregnancyHistory() != null ? "yes" : "no"));
                        if (record.getPregnancyHistory() != null) {
                                System.out.println("Debug - Pregnancy history size: "
                                                + record.getPregnancyHistory().size());
                        }

                        DateTimeFormatter timestampFormatter = DateTimeFormatter
                                        .ofPattern("MMMM dd, yyyy 'at' hh:mm a");

                        formTimestampLabel.setText(
                                        record.getFormTimestamp() != null
                                                        ? record.getFormTimestamp().format(timestampFormatter)
                                                        : "N/A");
                        patientIdLabel.setText(record.getPatientId() != null ? record.getPatientId() : "N/A");
                        fullNameLabel.setText(record.getFullName() != null ? record.getFullName() : "N/A");
                        dateOfBirthLabel
                                        .setText(record.getDateOfBirth() != null
                                                        ? record.getDateOfBirth().format(dateFormatter)
                                                        : "N/A");
                        husbandNameLabel.setText(record.getHusbandName() != null ? record.getHusbandName() : "N/A");
                        addressLabel.setText(record.getAddress() != null ? record.getAddress() : "N/A");
                        purokLabel.setText(record.getPurok() != null ? record.getPurok() : "N/A");
                        contactNumberLabel
                                        .setText(record.getContactNumber() != null ? record.getContactNumber() : "N/A");
                        emailLabel.setText(record.getEmail() != null ? record.getEmail() : "N/A");

                        ageOfGestationLabel.setText(
                                        record.getAgeOfGestation() > 0.0
                                                        ? String.format("%.1f weeks", record.getAgeOfGestation())
                                                        : "N/A");
                        weightLabel.setText(record.getWeight() > 0.0 ? String.format("%.1f kg", record.getWeight())
                                        : "N/A");
                        heightLabel.setText(record.getHeight() > 0.0 ? String.format("%.1f cm", record.getHeight())
                                        : "N/A");
                        bloodPressureLabel
                                        .setText(record.getBloodPressure() != null ? record.getBloodPressure() : "N/A");
                        temperatureLabel.setText(
                                        record.getTemperature() != null ? record.getTemperature() + "°C" : "N/A"); // Body
                                                                                                                   // Temperature
                        chiefComplaintLabel.setText(
                                        record.getChiefComplaint() != null ? record.getChiefComplaint() : "N/A");
                        fetalHeartToneLabel.setText(
                                        record.getFetalHeartTone() > 0 ? record.getFetalHeartTone() + " bpm" : "N/A");
                        presentationLabel.setText(record.getPresentation() != null ? record.getPresentation() : "N/A");
                        fundalHeightLabel.setText(
                                        record.getFundalHeight() > 0.0
                                                        ? String.format("%.1f cm", record.getFundalHeight())
                                                        : "N/A");

                        lastMenstrualPeriodLabel.setText(
                                        record.getLastMenstrualPeriod() != null
                                                        ? record.getLastMenstrualPeriod().format(dateFormatter)
                                                        : "N/A");
                        expectedDeliveryDateLabel.setText(
                                        record.getExpectedDeliveryDate() != null
                                                        ? record.getExpectedDeliveryDate().format(dateFormatter)
                                                        : "N/A");
                        paraLabel.setText(record.getPara() != null ? record.getPara() : "N/A");
                        abortionLabel.setText(record.getAbortion() != null ? record.getAbortion() : "N/A");
                        livingChildrenLabel.setText(
                                        record.getLivingChildren() != null ? record.getLivingChildren() : "N/A");
                        remarksLabel.setText(record.getRemarks() != null ? record.getRemarks() : "N/A");
                        nextAppointmentLabel.setText(
                                        record.getNextAppointment() != null
                                                        ? record.getNextAppointment().format(dateFormatter)
                                                        : "N/A");

                        // Load pregnancy history
                        loadPregnancyHistory(record);
                        // Load follow-up vital signs
                        loadFollowUpVitalSigns(record);
                }
        }

        private VBox createPregnancyHistoryBox(PregnancyHistory history) {
                VBox box = new VBox(8);
                box.getStyleClass().add("history-box");
                box.setPadding(new Insets(12));
                box.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 4; -fx-background-color: #fafafa;");

                // Title with pregnancy number
                Label titleLabel = new Label("Pregnancy #" + history.getPregnancyNumber());
                titleLabel.getStyleClass().add("history-title");
                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

                // Create a simple grid for the details
                GridPane grid = new GridPane();
                grid.setHgap(15);
                grid.setVgap(8);
                grid.setPadding(new Insets(8, 0, 0, 0));

                // Add pregnancy history details in a compact format
                addSimpleHistoryDetail(grid, "Delivery:", history.getDeliveryType(), 0);
                addSimpleHistoryDetail(grid, "Gender:", history.getGender(), 1);
                addSimpleHistoryDetail(grid, "Place:", history.getPlaceOfDelivery(), 2);
                addSimpleHistoryDetail(grid, "Year:", String.valueOf(history.getYearDelivered()), 3);
                addSimpleHistoryDetail(grid, "Attended By:", history.getAttendedBy(), 4);
                addSimpleHistoryDetail(grid, "Status:", history.getStatus(), 5);
                addSimpleHistoryDetail(grid, "Birth Date:",
                                history.getBirthDate() != null ? history.getBirthDate().format(dateFormatter) : "N/A",
                                6);
                addSimpleHistoryDetail(grid, "TT Injection:", history.getTtInjection(), 7);

                box.getChildren().addAll(titleLabel, grid);
                return box;
        }

        private void addSimpleHistoryDetail(GridPane grid, String label, String value, int row) {
                Label labelNode = new Label(label);
                labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: #555; -fx-font-size: 11px;");

                Label valueNode = new Label(value != null && !value.trim().isEmpty() ? value : "N/A");
                valueNode.setStyle("-fx-text-fill: #333; -fx-font-size: 11px;");

                grid.add(labelNode, 0, row);
                grid.add(valueNode, 1, row);
        }

        private void loadPregnancyHistory(MaternalRecord record) {
                // Debug logging for pregnancy history
                System.out.println("Debug - Pregnancy History in record: "
                                + (record.getPregnancyHistory() != null ? record.getPregnancyHistory().size()
                                                : "null")
                                + " entries");
                if (record.getPregnancyHistory() != null) {
                        for (PregnancyHistory history : record.getPregnancyHistory()) {
                                System.out.println("Debug - Pregnancy History Entry: " +
                                                "Number=" + history.getPregnancyNumber() +
                                                ", Delivery=" + history.getDeliveryType() +
                                                ", Gender=" + history.getGender() +
                                                ", Place=" + history.getPlaceOfDelivery() +
                                                ", Year=" + history.getYearDelivered() +
                                                ", AttendedBy=" + history.getAttendedBy() +
                                                ", Status=" + history.getStatus() +
                                                ", BirthDate="
                                                + (history.getBirthDate() != null
                                                                ? history.getBirthDate().toString()
                                                                : "null")
                                                +
                                                ", TT=" + history.getTtInjection());
                        }
                }

                // Clear and update pregnancy history container
                System.out.println("Debug - Pregnancy history container: "
                                + (pregnancyHistoryContainer != null ? "not null" : "null"));
                if (pregnancyHistoryContainer == null) {
                        pregnancyHistoryContainer = new VBox(10);
                        pregnancyHistoryContainer.getStyleClass().add("form-section");
                        System.out.println("Debug - Created new pregnancy history container");
                }
                pregnancyHistoryContainer.getChildren().clear();
                System.out.println("Debug - Cleared pregnancy history container children");

                // Display all pregnancy history entries (simplified validation)
                List<PregnancyHistory> validHistory = new ArrayList<>();
                if (record.getPregnancyHistory() != null) {
                        System.out.println("Debug - Processing " + record.getPregnancyHistory().size()
                                        + " pregnancy history entries");
                        for (PregnancyHistory history : record.getPregnancyHistory()) {
                                System.out.println("Debug - Processing pregnancy history #"
                                                + history.getPregnancyNumber());

                                // Accept any pregnancy history entry that has a pregnancy number > 0
                                if (history.getPregnancyNumber() > 0) {
                                        validHistory.add(history);
                                        System.out.println("Debug - Added pregnancy history #"
                                                        + history.getPregnancyNumber() + " to valid list");
                                } else {
                                        System.out.println(
                                                        "Debug - Skipping pregnancy history with number 0 or negative");
                                }
                        }
                }

                System.out.println("Debug - Final valid history count: " + validHistory.size());

                if (!validHistory.isEmpty()) {
                        System.out.println("Debug - Adding " + validHistory.size()
                                        + " valid pregnancy history entries");
                        for (PregnancyHistory history : validHistory) {
                                VBox historyBox = createPregnancyHistoryBox(history);
                                pregnancyHistoryContainer.getChildren().add(historyBox);
                                System.out.println("Debug - Added pregnancy history box for pregnancy #"
                                                + history.getPregnancyNumber());
                        }
                } else {
                        System.out.println(
                                        "Debug - No valid pregnancy history to display, showing 'No pregnancy history recorded' message");
                        Label noHistoryLabel = new Label("No pregnancy history recorded");
                        noHistoryLabel.getStyleClass().add("form-label");
                        pregnancyHistoryContainer.getChildren().add(noHistoryLabel);
                }
                System.out.println("Debug - Pregnancy history container now has "
                                + pregnancyHistoryContainer.getChildren().size() + " children");
        }

        private void loadFollowUpVitalSigns(MaternalRecord record) {
                if (followUpVitalSignsContainer != null) {
                        followUpVitalSignsContainer.getChildren().clear();
                        if (record.getFollowUpVitalSigns() != null && !record.getFollowUpVitalSigns().isEmpty()) {
                                System.out.println("[DEBUG] Loading " + record.getFollowUpVitalSigns().size()
                                                + " follow-up entries for record: " + record.getPatientId());
                                for (com.maternacare.model.VitalSignsEntry entry : record.getFollowUpVitalSigns()) {
                                        System.out.println("[DEBUG] Follow-up entry: " + entry);
                                        VBox box = new VBox(4);
                                        box.setPadding(new Insets(8));
                                        box.setStyle("-fx-border-color: #b0b0b0; -fx-border-radius: 4; -fx-background-color: #f8f8f8;");
                                        Label dateLabel = new Label("Date: " + (entry.getDate() != null
                                                        ? entry.getDate().format(dateFormatter)
                                                        : "N/A"));
                                        Label bpLabel = new Label("Blood Pressure: "
                                                        + (entry.getBloodPressure() != null ? entry.getBloodPressure()
                                                                        : "N/A"));
                                        Label tempLabel = new Label("Temperature: "
                                                        + (entry.getTemperature() != null ? entry.getTemperature()
                                                                        : "N/A"));
                                        Label pulseLabel = new Label("Pulse Rate: "
                                                        + (entry.getPulseRate() != null ? entry.getPulseRate()
                                                                        : "N/A"));
                                        Label respLabel = new Label(
                                                        "Respiratory Rate: " + (entry.getRespiratoryRate() != null
                                                                        ? entry.getRespiratoryRate()
                                                                        : "N/A"));
                                        Label aogLabel = new Label("Age of Gestation (AOG): "
                                                        + (entry.getAog() != null ? entry.getAog() : "N/A"));
                                        Label heightLabel = new Label("Height (cm): "
                                                        + (entry.getHeight() != null ? entry.getHeight() : "N/A"));
                                        Label weightLabel = new Label("Weight (lbs): "
                                                        + (entry.getWeight() != null ? entry.getWeight() : "N/A"));
                                        Label fhtLabel = new Label("Fetal Heart Tone (FHT): "
                                                        + (entry.getFht() != null ? entry.getFht() : "N/A"));
                                        Label presentationLabel = new Label("Presentation: "
                                                        + (entry.getPresentation() != null ? entry.getPresentation()
                                                                        : "N/A"));
                                        Label chiefComplaintLabel = new Label("Chief Complaint: "
                                                        + (entry.getChiefComplaint() != null ? entry.getChiefComplaint()
                                                                        : "N/A"));
                                        Label toComeBackLabel = new Label(
                                                        "To come Back: " + (entry.getToComeBack() != null
                                                                        ? entry.getToComeBack().format(dateFormatter)
                                                                        : "N/A"));
                                        Label remarksLabel = new Label("Remarks: "
                                                        + (entry.getRemarks() != null ? entry.getRemarks() : "N/A"));
                                        box.getChildren().addAll(dateLabel, bpLabel, tempLabel, pulseLabel, respLabel,
                                                        aogLabel, heightLabel, weightLabel, fhtLabel, presentationLabel,
                                                        chiefComplaintLabel, toComeBackLabel, remarksLabel);
                                        followUpVitalSignsContainer.getChildren().add(box);
                                }
                        } else {
                                System.out.println("[DEBUG] No follow-up vital signs recorded for record: "
                                                + record.getPatientId());
                                followUpVitalSignsContainer.getChildren()
                                                .add(new Label("No follow-up vital signs recorded."));
                        }
                }
        }

        public void refresh(MaternalRecord record) {
                setRecord(record);
        }
}