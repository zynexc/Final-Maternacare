package com.maternacare.controller;

import com.maternacare.model.MaternalRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class MaternalRecordDetailsController {
    @FXML
    private VBox rootPane;

    // Personal Information
    @FXML
    private Label patientIdLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label middleNameLabel;
    @FXML
    private Label dateOfBirthLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label civilStatusLabel;
    @FXML
    private Label religionLabel;

    // Contact Information
    @FXML
    private Label addressLabel;
    @FXML
    private Label contactNumberLabel;
    @FXML
    private Label emailLabel;

    // Vital Signs
    @FXML
    private Label bloodPressureLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label pulseRateLabel;
    @FXML
    private Label respiratoryRateLabel;

    // Pregnancy Information
    @FXML
    private Label lastMenstrualPeriodLabel;
    @FXML
    private Label expectedDeliveryDateLabel;
    @FXML
    private Label gravidaLabel;
    @FXML
    private Label paraLabel;
    @FXML
    private Label abortionLabel;
    @FXML
    private Label livingChildrenLabel;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    public void setRecord(MaternalRecord record) {
        if (record == null)
            return;

        // Personal Information
        patientIdLabel.setText(record.getPatientId());
        lastNameLabel.setText(record.getLastName());
        firstNameLabel.setText(record.getFirstName());
        middleNameLabel.setText(record.getMiddleName());
        dateOfBirthLabel.setText(record.getDateOfBirth() != null ? record.getDateOfBirth().format(dateFormatter) : "");
        ageLabel.setText(String.valueOf(record.getDateOfBirth() != null
                ? java.time.Period.between(record.getDateOfBirth(), java.time.LocalDate.now()).getYears()
                : ""));
        civilStatusLabel.setText(record.getCivilStatus());
        religionLabel.setText(record.getReligion());

        // Contact Information
        addressLabel.setText(record.getAddress());
        contactNumberLabel.setText(record.getContactNumber());
        emailLabel.setText(record.getEmail());

        // Vital Signs
        bloodPressureLabel.setText(record.getBloodPressure());
        temperatureLabel.setText(record.getTemperature());
        pulseRateLabel.setText(record.getPulseRate());
        respiratoryRateLabel.setText(record.getRespiratoryRate());

        // Pregnancy Information
        lastMenstrualPeriodLabel.setText(
                record.getLastMenstrualPeriod() != null ? record.getLastMenstrualPeriod().format(dateFormatter) : "");
        expectedDeliveryDateLabel.setText(
                record.getExpectedDeliveryDate() != null ? record.getExpectedDeliveryDate().format(dateFormatter) : "");
        gravidaLabel.setText(record.getGravida());
        paraLabel.setText(record.getPara());
        abortionLabel.setText(record.getAbortion());
        livingChildrenLabel.setText(record.getLivingChildren());
    }

    @FXML
    private void handleClose() {
        ((Stage) rootPane.getScene().getWindow()).close();
    }
}