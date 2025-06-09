package com.maternacare.controller;

import com.maternacare.model.MaternalRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;

public class MaternalRecordDetailsPopupController {

    @FXML
    private Label idLabel;
    @FXML
    private Label ageOfGestationLabel;
    @FXML
    private Label bloodPressureLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label heightLabel;
    @FXML
    private Label fetalHeartToneLabel;
    @FXML
    private Label presentationLabel;
    @FXML
    private Label fundalHeightLabel;
    @FXML
    private Label nextAppointmentLabel;

    public void setRecord(MaternalRecord record) {
        if (record != null) {
            idLabel.setText(String.valueOf(record.getId()));
            ageOfGestationLabel.setText(String.valueOf(record.getAgeOfGestation()));
            bloodPressureLabel.setText(record.getBloodPressure());
            weightLabel.setText(String.valueOf(record.getWeight()));
            heightLabel.setText(String.valueOf(record.getHeight()));
            fetalHeartToneLabel.setText(String.valueOf(record.getFetalHeartTone()));
            presentationLabel.setText(record.getPresentation());
            fundalHeightLabel.setText(String.valueOf(record.getFundalHeight()));

            // Format the date
            if (record.getNextAppointment() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                nextAppointmentLabel.setText(record.getNextAppointment().format(formatter));
            } else {
                nextAppointmentLabel.setText("N/A");
            }
        }
    }
}