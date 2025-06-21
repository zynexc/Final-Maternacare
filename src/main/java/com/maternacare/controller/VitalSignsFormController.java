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

public class VitalSignsFormController {
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
    private TextField presentationField;
    @FXML
    private TextField chiefComplaintField;
    @FXML
    private DatePicker toComeBackPicker;

    private Consumer<VitalSignsEntry> onSaveCallback;

    public void setOnSaveCallback(Consumer<VitalSignsEntry> callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> handleSave());
    }

    private void handleSave() {
        String bp = bloodPressureField.getText();
        String temp = temperatureField.getText();
        String pulse = pulseRateField.getText();
        String resp = respiratoryRateField.getText();
        String aog = aogField.getText();
        String height = heightField.getText();
        String weight = weightField.getText();
        String fht = fhtField.getText();
        String presentation = presentationField.getText();
        String chiefComplaint = chiefComplaintField.getText();
        String remarks = remarksField.getText();
        java.time.LocalDate toComeBack = toComeBackPicker.getValue();
        VitalSignsEntry entry = new VitalSignsEntry(LocalDate.now(), bp, temp, pulse, resp, remarks, aog, height,
                weight, fht, presentation, chiefComplaint, toComeBack);
        if (onSaveCallback != null) {
            onSaveCallback.accept(entry);
        }
        // Show success popup
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Follow-up vital signs successfully saved!");
        alert.showAndWait();
        // Close the window
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}