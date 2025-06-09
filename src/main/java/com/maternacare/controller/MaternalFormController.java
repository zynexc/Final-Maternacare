package com.maternacare.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import com.maternacare.model.MaternalRecord;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.stage.Modality;
import java.time.LocalDate;
import java.time.Period;

public class MaternalFormController {

    @FXML
    private VBox rootPane; // Assuming a VBox or similar as the root in maternal_form.fxml

    // Personal Information Fields
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField middleNameField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextField ageField;
    @FXML
    private ComboBox<String> civilStatusCombo;
    @FXML
    private TextField religionField;

    // Contact Information Fields
    @FXML
    private TextField addressField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> purokCombo;

    // Vital Signs Fields
    @FXML
    private TextField bloodPressureField;
    @FXML
    private TextField temperatureField;
    @FXML
    private TextField pulseRateField;
    @FXML
    private TextField respiratoryRateField;

    // Pregnancy Information Fields
    @FXML
    private DatePicker lastMenstrualPeriodPicker;
    @FXML
    private DatePicker expectedDeliveryDatePicker;
    @FXML
    private TextField gravidaField;
    @FXML
    private TextField paraField;
    @FXML
    private TextField abortionField;
    @FXML
    private TextField livingChildrenField;

    private MaternalRecordsController recordsController;
    private MaternalRecord currentRecord;

    @FXML
    public void initialize() {
        // Initialize civil status combo box
        ObservableList<String> civilStatusOptions = FXCollections.observableArrayList(
                "Single", "Married", "Widowed", "Separated", "Divorced");
        civilStatusCombo.setItems(civilStatusOptions);

        // Initialize purok combo box
        ObservableList<String> purokOptions = FXCollections.observableArrayList(
                "Purok 1", "Purok 2", "Purok 3", "Purok 4", "Purok 5", "Purok 6");
        purokCombo.setItems(purokOptions);

        // Add tooltips to fields
        addTooltips();

        // Add required field indicators
        markRequiredFields();

        // Add listener to date of birth to calculate age
        dateOfBirthPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int age = Period.between(newVal, LocalDate.now()).getYears();
                ageField.setText(String.valueOf(age));
            }
        });

        // Add listener to last menstrual period to calculate expected delivery date
        lastMenstrualPeriodPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Add 280 days (40 weeks) to LMP to get EDD
                LocalDate edd = newVal.plusDays(280);
                expectedDeliveryDatePicker.setValue(edd);
            }
        });

        // Set up input validation
        setupValidation();
    }

    private void addTooltips() {
        // Personal Information
        patientIdField.setTooltip(new Tooltip("Enter the patient's unique identifier"));
        lastNameField.setTooltip(new Tooltip("Enter the patient's last name"));
        firstNameField.setTooltip(new Tooltip("Enter the patient's first name"));
        middleNameField.setTooltip(new Tooltip("Enter the patient's middle name (optional)"));
        dateOfBirthPicker.setTooltip(new Tooltip("Select the patient's date of birth"));
        civilStatusCombo.setTooltip(new Tooltip("Select the patient's civil status"));
        religionField.setTooltip(new Tooltip("Enter the patient's religion (optional)"));

        // Contact Information
        addressField.setTooltip(new Tooltip("Enter the patient's complete address"));
        contactNumberField.setTooltip(new Tooltip("Enter the patient's contact number"));
        emailField.setTooltip(new Tooltip("Enter the patient's email address (optional)"));
        purokCombo.setTooltip(new Tooltip("Select the patient's purok"));

        // Vital Signs
        bloodPressureField.setTooltip(new Tooltip("Enter blood pressure in format: 120/80 or 120-80"));
        temperatureField.setTooltip(new Tooltip("Enter temperature in Celsius (e.g., 37.5 or 37)"));
        pulseRateField.setTooltip(new Tooltip("Enter pulse rate in beats per minute"));
        respiratoryRateField.setTooltip(new Tooltip("Enter respiratory rate in breaths per minute"));

        // Pregnancy Information
        lastMenstrualPeriodPicker.setTooltip(new Tooltip("Select the first day of last menstrual period"));
        expectedDeliveryDatePicker.setTooltip(new Tooltip("Expected delivery date (auto-calculated from LMP)"));
        gravidaField.setTooltip(new Tooltip("Number of pregnancies (including current)"));
        paraField.setTooltip(new Tooltip("Number of live births"));
        abortionField.setTooltip(new Tooltip("Number of abortions/miscarriages"));
        livingChildrenField.setTooltip(new Tooltip("Number of living children"));
    }

    private void markRequiredFields() {
        // Only set prompt text, do not add 'required-field' style class
        addRequiredIndicator(patientIdField, "Patient ID *");
        addRequiredIndicator(lastNameField, "Last Name *");
        addRequiredIndicator(firstNameField, "First Name *");
        addRequiredIndicator(dateOfBirthPicker, "Date of Birth *");
        addRequiredIndicator(addressField, "Address *");
        addRequiredIndicator(purokCombo, "Purok *");
        addRequiredIndicator(contactNumberField, "Contact Number *");
        addRequiredIndicator(bloodPressureField, "Blood Pressure *");
        addRequiredIndicator(temperatureField, "Temperature *");
        addRequiredIndicator(lastMenstrualPeriodPicker, "Last Menstrual Period *");
    }

    private void addRequiredIndicator(Control control, String labelText) {
        if (control instanceof TextField) {
            ((TextField) control).setPromptText(labelText);
        } else if (control instanceof DatePicker) {
            ((DatePicker) control).setPromptText(labelText);
        } else if (control instanceof ComboBox) {
            ((ComboBox<?>) control).setPromptText(labelText);
        }
        // Do not add 'required-field' style class by default
    }

    private void setupValidation() {
        // Add validation for numeric fields
        addNumericValidation(pulseRateField);
        addNumericValidation(respiratoryRateField);
        addNumericValidation(gravidaField);
        addNumericValidation(paraField);
        addNumericValidation(abortionField);
        addNumericValidation(livingChildrenField);

        // Add validation for blood pressure format (e.g., "120/80" or "120-80")
        bloodPressureField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !newVal.matches("\\d{2,3}[/-]\\d{2,3}")) {
                bloodPressureField.setStyle("-fx-border-color: #dc3545;");
                bloodPressureField.setTooltip(new Tooltip("Enter blood pressure in format: 120/80 or 120-80"));
            } else {
                bloodPressureField.setStyle("");
                bloodPressureField.setTooltip(new Tooltip("Enter blood pressure in format: 120/80 or 120-80"));
            }
        });

        // Add validation for temperature format (e.g., "37.5" or "37")
        temperatureField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !newVal.matches("\\d{2}(\\.\\d{1})?")) {
                temperatureField.setStyle("-fx-border-color: #dc3545;");
                temperatureField.setTooltip(new Tooltip("Enter temperature in Celsius (e.g., 37.5 or 37)"));
            } else {
                temperatureField.setStyle("");
                temperatureField.setTooltip(new Tooltip("Enter temperature in Celsius (e.g., 37.5 or 37)"));
            }
        });

        // Add validation for contact number (allow + and spaces)
        contactNumberField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !newVal.matches("[+]?[\\d\\s-]+")) {
                contactNumberField.setStyle("-fx-border-color: #dc3545;");
                contactNumberField.setTooltip(new Tooltip("Enter a valid phone number (e.g., +63 912 345 6789)"));
            } else {
                contactNumberField.setStyle("");
                contactNumberField.setTooltip(new Tooltip("Enter a valid phone number (e.g., +63 912 345 6789)"));
            }
        });

        // Add validation for email
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !newVal.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                emailField.setStyle("-fx-border-color: #dc3545;");
                emailField.setTooltip(new Tooltip("Enter a valid email address (e.g., name@example.com)"));
            } else {
                emailField.setStyle("");
                emailField.setTooltip(new Tooltip("Enter a valid email address (e.g., name@example.com)"));
            }
        });
    }

    private void addNumericValidation(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                field.setText(oldVal);
            }
        });
    }

    public void setRecordsController(MaternalRecordsController controller) {
        this.recordsController = controller;
    }

    public void loadRecord(MaternalRecord record) {
        if (record == null) {
            clearForm();
            return;
        }

        // Load personal information
        patientIdField.setText(record.getPatientId());
        lastNameField.setText(record.getLastName());
        firstNameField.setText(record.getFirstName());
        middleNameField.setText(record.getMiddleName());
        dateOfBirthPicker.setValue(record.getDateOfBirth());
        civilStatusCombo.setValue(record.getCivilStatus());
        religionField.setText(record.getReligion());

        // Load contact information
        addressField.setText(record.getAddress());
        purokCombo.setValue(record.getPurok());
        contactNumberField.setText(record.getContactNumber());
        emailField.setText(record.getEmail());

        // Load vital signs
        bloodPressureField.setText(record.getBloodPressure());
        temperatureField.setText(record.getTemperature());
        pulseRateField.setText(record.getPulseRate());
        respiratoryRateField.setText(record.getRespiratoryRate());

        // Load pregnancy information
        lastMenstrualPeriodPicker.setValue(record.getLastMenstrualPeriod());
        expectedDeliveryDatePicker.setValue(record.getExpectedDeliveryDate());
        gravidaField.setText(record.getGravida());
        paraField.setText(record.getPara());
        abortionField.setText(record.getAbortion());
        livingChildrenField.setText(record.getLivingChildren());
    }

    @FXML
    private void handleSave() {
        // Validate required fields
        if (!validateRequiredFields()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all required fields.");
            return;
        }

        // Create new record
        MaternalRecord record = new MaternalRecord();

        // Set personal information
        record.setPatientId(patientIdField.getText());
        record.setLastName(lastNameField.getText());
        record.setFirstName(firstNameField.getText());
        record.setMiddleName(middleNameField.getText());
        record.setDateOfBirth(dateOfBirthPicker.getValue());
        record.setCivilStatus(civilStatusCombo.getValue());
        record.setReligion(religionField.getText());

        // Set contact information
        record.setAddress(addressField.getText());
        record.setPurok(purokCombo.getValue());
        record.setContactNumber(contactNumberField.getText());
        record.setEmail(emailField.getText());

        // Set vital signs
        record.setBloodPressure(bloodPressureField.getText());
        record.setTemperature(temperatureField.getText());
        record.setPulseRate(pulseRateField.getText());
        record.setRespiratoryRate(respiratoryRateField.getText());

        // Set pregnancy information
        record.setLastMenstrualPeriod(lastMenstrualPeriodPicker.getValue());
        record.setExpectedDeliveryDate(expectedDeliveryDatePicker.getValue());
        record.setGravida(gravidaField.getText());
        record.setPara(paraField.getText());
        record.setAbortion(abortionField.getText());
        record.setLivingChildren(livingChildrenField.getText());

        // Save record
        if (recordsController != null) {
            try {
                recordsController.saveRecord(record);

                // Show success alert
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Record saved successfully.");
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.showAndWait();

                    // Clear form after alert is closed
                    clearForm();
                });
            } catch (Exception e) {
                // Show error alert if save fails
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to save record: " + e.getMessage());
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.showAndWait();
                });
            }
        } else {
            // Show error if records controller is not available
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Records controller is not available.");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            });
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    private void clearForm() {
        // Clear personal information
        patientIdField.clear();
        lastNameField.clear();
        firstNameField.clear();
        middleNameField.clear();
        dateOfBirthPicker.setValue(null);
        civilStatusCombo.setValue(null);
        religionField.clear();

        // Clear contact information
        addressField.clear();
        purokCombo.setValue(null);
        contactNumberField.clear();
        emailField.clear();

        // Clear vital signs
        bloodPressureField.clear();
        temperatureField.clear();
        pulseRateField.clear();
        respiratoryRateField.clear();

        // Clear pregnancy information
        lastMenstrualPeriodPicker.setValue(null);
        expectedDeliveryDatePicker.setValue(null);
        gravidaField.clear();
        paraField.clear();
        abortionField.clear();
        livingChildrenField.clear();
    }

    private boolean validateRequiredFields() {
        boolean isValid = true;

        // Personal Information
        if (patientIdField.getText().trim().isEmpty()) {
            showFieldError(patientIdField, "Patient ID is required");
            isValid = false;
        }

        if (lastNameField.getText().trim().isEmpty()) {
            showFieldError(lastNameField, "Last name is required");
            isValid = false;
        }

        if (firstNameField.getText().trim().isEmpty()) {
            showFieldError(firstNameField, "First name is required");
            isValid = false;
        }

        if (dateOfBirthPicker.getValue() == null) {
            showFieldError(dateOfBirthPicker, "Date of birth is required");
            isValid = false;
        }

        // Contact Information
        if (addressField.getText().trim().isEmpty()) {
            showFieldError(addressField, "Address is required");
            isValid = false;
        }

        if (purokCombo.getValue() == null || purokCombo.getValue().trim().isEmpty()) {
            showFieldError(purokCombo, "Purok is required");
            isValid = false;
        }

        if (contactNumberField.getText().trim().isEmpty()) {
            showFieldError(contactNumberField, "Contact number is required");
            isValid = false;
        }

        // Vital Signs
        if (bloodPressureField.getText().trim().isEmpty()) {
            showFieldError(bloodPressureField, "Blood pressure is required");
            isValid = false;
        } else if (!bloodPressureField.getText().matches("\\d{2,3}[/-]\\d{2,3}")) {
            showFieldError(bloodPressureField, "Invalid blood pressure format (e.g., 120/80 or 120-80)");
            isValid = false;
        }

        if (temperatureField.getText().trim().isEmpty()) {
            showFieldError(temperatureField, "Temperature is required");
            isValid = false;
        } else if (!temperatureField.getText().matches("\\d{2}(\\.\\d{1})?")) {
            showFieldError(temperatureField, "Invalid temperature format (e.g., 37.5 or 37)");
            isValid = false;
        }

        // Pregnancy Information
        if (lastMenstrualPeriodPicker.getValue() == null) {
            showFieldError(lastMenstrualPeriodPicker, "Last menstrual period is required");
            isValid = false;
        }

        return isValid;
    }

    private void showFieldError(Control field, String message) {
        field.setStyle("-fx-border-color: #dc3545;");
        Tooltip tooltip = new Tooltip(message);
        tooltip.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 12px;");
        field.setTooltip(tooltip);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}