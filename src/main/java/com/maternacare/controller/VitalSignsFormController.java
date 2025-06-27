package com.maternacare.controller;

import com.maternacare.model.VitalSignsEntry;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.function.Consumer;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;

public class VitalSignsFormController {
    @FXML
    private Button backButton;
    @FXML
    private TextField bloodPressureField;
    @FXML
    private TextField temperatureField;
    @FXML
    private TextField pulseRateField;
    @FXML
    private TextField respiratoryRateField;
    @FXML
    private TextArea remarksField;
    @FXML
    private Button saveButton;
    @FXML
    private TextField aogField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField weightField;
    @FXML
    private TextField fhtField;
    @FXML
    private ComboBox<String> presentationComboBox;
    @FXML
    private TextField chiefComplaintField;
    @FXML
    private DatePicker toComeBackPicker;
    @FXML
    private Label patientNameLabel;
    @FXML
    private Label messageLabel;

    private Consumer<VitalSignsEntry> onSaveCallback;
    private Runnable onBackCallback;

    public void setOnSaveCallback(Consumer<VitalSignsEntry> callback) {
        this.onSaveCallback = callback;
    }

    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }

    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> handleSave());
        backButton.setOnAction(e -> handleBack());
    }

    private void handleBack() {
        if (onBackCallback != null) {
            onBackCallback.run();
        }
    }

    private void handleSave() {
        if (isAnyFieldEmpty()) {
            showMessage("All fields except remarks must be filled out.", true);
            return;
        }

        String bp = bloodPressureField.getText();
        String temp = temperatureField.getText();
        String pulse = pulseRateField.getText();
        String resp = respiratoryRateField.getText();
        String height = heightField.getText();
        String weight = weightField.getText();
        String fht = fhtField.getText();
        String presentation = presentationComboBox.getValue();
        String chiefComplaint = chiefComplaintField.getText();
        String remarks = remarksField.getText();
        java.time.LocalDate toComeBack = toComeBackPicker.getValue();
        VitalSignsEntry entry = new VitalSignsEntry(java.time.LocalDate.now(), bp, temp, pulse, resp, remarks, null,
                height,
                weight, fht, presentation, chiefComplaint, toComeBack);
        if (onSaveCallback != null) {
            onSaveCallback.accept(entry);
            showMessage("Follow-up vital signs successfully saved!", false);
        }
        // Close the window
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private boolean isAnyFieldEmpty() {
        return bloodPressureField.getText().trim().isEmpty() ||
                temperatureField.getText().trim().isEmpty() ||
                pulseRateField.getText().trim().isEmpty() ||
                respiratoryRateField.getText().trim().isEmpty() ||
                heightField.getText().trim().isEmpty() ||
                weightField.getText().trim().isEmpty() ||
                fhtField.getText().trim().isEmpty() ||
                presentationComboBox.getValue() == null ||
                chiefComplaintField.getText().trim().isEmpty() ||
                toComeBackPicker.getValue() == null;
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: #d32f2f;");
        } else {
            messageLabel.setStyle("-fx-text-fill: #28a745;");
        }
    }

    public void setPatientName(String name) {
        if (patientNameLabel != null) {
            patientNameLabel.setText(name);
        }
    }

    public void prefillFields(VitalSignsEntry entry) {
        bloodPressureField.setText(entry.getBloodPressure());
        temperatureField.setText(entry.getTemperature());
        pulseRateField.setText(entry.getPulseRate());
        respiratoryRateField.setText(entry.getRespiratoryRate());
        heightField.setText(entry.getHeight());
        weightField.setText(entry.getWeight());
        fhtField.setText(entry.getFht());
        presentationComboBox.setValue(entry.getPresentation());
        chiefComplaintField.setText(entry.getChiefComplaint());
        remarksField.setText(entry.getRemarks());
        toComeBackPicker.setValue(entry.getToComeBack());
    }
}