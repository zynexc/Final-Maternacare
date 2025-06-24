package com.maternacare.controller;

import com.maternacare.model.MaternalRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class MaternalRecordEditFormController {
    @FXML
    private VBox rootPane;
    @FXML
    private Label formTimestampLabel;
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField fullNameField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextField ageField;
    @FXML
    private TextField husbandNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> purokCombo;
    @FXML
    private DatePicker lastMenstrualPeriodPicker;
    @FXML
    private DatePicker expectedDeliveryDatePicker;
    @FXML
    private TextField gravidaField;
    @FXML
    private TextField pretermField;
    @FXML
    private TextField paraField;
    @FXML
    private TextField abortionField;
    @FXML
    private TextField termField;
    @FXML
    private TextField livingChildrenField;
    @FXML
    private TextField chiefComplaintField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField ageOfGestationField;
    @FXML
    private TextField weightField;
    @FXML
    private TextField fetalHeartToneField;
    @FXML
    private TextField temperatureField;
    @FXML
    private TextField fundalHeightField;
    @FXML
    private TextField bloodPressureField;
    @FXML
    private ComboBox<String> presentationCombo;
    @FXML
    private DatePicker toComeBackPicker;
    @FXML
    private TextArea remarksField;
    @FXML
    private Label messageLabel;
    private MaternalRecord currentRecord;
    private MaternalRecordsController recordsController;

    public void setRecordForEditing(MaternalRecord record) {
        this.currentRecord = record;
        if (record == null)
            return;
        patientIdField.setText(record.getPatientId());
        fullNameField.setText(record.getFullName());
        dateOfBirthPicker.setValue(record.getDateOfBirth());
        husbandNameField.setText(record.getHusbandName());
        addressField.setText(record.getAddress());
        contactNumberField.setText(record.getContactNumber());
        emailField.setText(record.getEmail());
        purokCombo.setValue(record.getPurok());
        lastMenstrualPeriodPicker.setValue(record.getLastMenstrualPeriod());
        expectedDeliveryDatePicker.setValue(record.getExpectedDeliveryDate());
        gravidaField.setText(record.getGravida());
        pretermField.setText(record.getPreterm());
        termField.setText(record.getTerm());
        paraField.setText(record.getPara());
        abortionField.setText(record.getAbortion());
        livingChildrenField.setText(record.getLivingChildren());
        chiefComplaintField.setText(record.getChiefComplaint());
        heightField.setText(String.valueOf(record.getHeight()));
        ageOfGestationField.setText(String.valueOf(record.getAgeOfGestation()));
        weightField.setText(String.valueOf(record.getWeight()));
        fetalHeartToneField.setText(String.valueOf(record.getFetalHeartTone()));
        temperatureField.setText(record.getTemperature());
        fundalHeightField.setText(String.valueOf(record.getFundalHeight()));
        bloodPressureField.setText(record.getBloodPressure());
        presentationCombo.setValue(record.getPresentation());
        toComeBackPicker.setValue(record.getNextAppointment());
        remarksField.setText(record.getRemarks());
    }

    public void setRecordsController(MaternalRecordsController controller) {
        this.recordsController = controller;
    }

    @FXML
    private void handleBack() {
        if (recordsController != null && currentRecord != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maternal_record_details_page.fxml"));
                Parent detailsPage = loader.load();
                MaternalRecordDetailsPageController detailsController = loader.getController();
                detailsController.setMaternalRecord(currentRecord);
                detailsController.setRecordsController(recordsController, null);
                if (recordsController.getMainApplication() != null) {
                    recordsController.getMainApplication().setContent(detailsPage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSave() {
        if (currentRecord == null || recordsController == null) {
            showMessage("Unable to save: missing record or controller.", true);
            return;
        }
        // Update currentRecord with form values
        currentRecord.setPatientId(patientIdField.getText());
        currentRecord.setFullName(fullNameField.getText());
        currentRecord.setDateOfBirth(dateOfBirthPicker.getValue());
        currentRecord.setHusbandName(husbandNameField.getText());
        currentRecord.setAddress(addressField.getText());
        currentRecord.setContactNumber(contactNumberField.getText());
        currentRecord.setEmail(emailField.getText());
        currentRecord.setPurok(purokCombo.getValue());
        currentRecord.setLastMenstrualPeriod(lastMenstrualPeriodPicker.getValue());
        currentRecord.setExpectedDeliveryDate(expectedDeliveryDatePicker.getValue());
        currentRecord.setPara(paraField.getText());
        currentRecord.setAbortion(abortionField.getText());
        currentRecord.setLivingChildren(livingChildrenField.getText());
        currentRecord.setTerm(termField.getText());
        currentRecord.setPreterm(pretermField.getText());
        currentRecord.setChiefComplaint(chiefComplaintField.getText());
        currentRecord.setHeight(parseDouble(heightField.getText()));
        currentRecord.setAgeOfGestation(parseDouble(ageOfGestationField.getText()));
        currentRecord.setWeight(parseDouble(weightField.getText()));
        currentRecord.setFetalHeartTone(parseInt(fetalHeartToneField.getText()));
        currentRecord.setTemperature(temperatureField.getText());
        currentRecord.setFundalHeight(parseDouble(fundalHeightField.getText()));
        currentRecord.setBloodPressure(bloodPressureField.getText());
        currentRecord.setPresentation(presentationCombo.getValue());
        currentRecord.setNextAppointment(toComeBackPicker.getValue());
        currentRecord.setRemarks(remarksField.getText());
        // TODO: update pregnancy history and other fields as needed

        // Save the record
        recordsController.saveRecord(currentRecord);

        showMessage("Maternal record saved successfully!", false);

        // Go back to details page after a short delay
        new Thread(() -> {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException ignored) {
            }
            javafx.application.Platform.runLater(this::handleBack);
        }).start();
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setVisible(true);
            messageLabel.setManaged(true);
            if (isError) {
                messageLabel.setStyle(
                        "-fx-text-fill: #d32f2f; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
            } else {
                messageLabel.setStyle(
                        "-fx-text-fill: #28a745; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
            }
        }
    }

    // Helper methods for parsing
    private double parseDouble(String value) {
        try {
            if (value == null || value.trim().isEmpty())
                return 0.0;
            return Double.parseDouble(value.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseInt(String value) {
        try {
            if (value == null || value.trim().isEmpty())
                return 0;
            return Integer.parseInt(value.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @FXML
    private void handleAddPregnancyHistory() {
        // TODO: Implement add pregnancy history logic
    }

    @FXML
    private void handleDeletePregnancyHistory() {
        // TODO: Implement delete pregnancy history logic
    }

    @FXML
    private void initialize() {
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets()
                        .add(getClass().getResource("/styles/maternal_record_edit_form.css").toExternalForm());
            }
        });
    }
}