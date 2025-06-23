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

import com.maternacare.model.MaternalRecord;
import com.maternacare.model.PregnancyHistory;
import com.maternacare.model.VitalSignsEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    private VBox detailsRoot;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private Runnable onBackCallback;

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
    }

    public void setMaternalRecord(MaternalRecord record) {
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
        VBox card = new VBox(6);
        card.getStyleClass().add("pregnancy-history-card");
        card.setPadding(new Insets(0, 0, 12, 0));

        // Header
        HBox header = new HBox();
        header.getStyleClass().add("pregnancy-header");
        header.getChildren().add(new Label("Pregnancy # " + number));
        card.getChildren().add(header);

        // Details grid
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(6);

        int row = 0;
        grid.add(new Label("Delivery:"), 0, row);
        grid.add(new Label(history.getDeliveryType()), 1, row);
        grid.add(new Label("Gender:"), 2, row);
        grid.add(new Label(history.getGender()), 3, row);
        row++;
        grid.add(new Label("Place:"), 0, row);
        grid.add(new Label(history.getPlaceOfDelivery()), 1, row);
        grid.add(new Label("Year:"), 2, row);
        grid.add(new Label(String.valueOf(history.getYearDelivered())), 3, row);
        row++;
        grid.add(new Label("Attended By:"), 0, row);
        grid.add(new Label(history.getAttendedBy()), 1, row);
        grid.add(new Label("Status:"), 2, row);
        grid.add(new Label(history.getStatus()), 3, row);
        row++;
        grid.add(new Label("Birth Date:"), 0, row);
        grid.add(new Label(formatDate(history.getBirthDate())), 1, row);
        grid.add(new Label("TT Injection:"), 2, row);
        grid.add(new Label(history.getTtInjection()), 3, row);

        card.getChildren().add(grid);
        return card;
    }

    private Node createFollowUpCard(VitalSignsEntry entry) {
        VBox card = new VBox(6);
        card.getStyleClass().add("vital-signs-card");
        card.setPadding(new Insets(0, 0, 12, 0));

        // Date header
        HBox dateHeader = new HBox();
        dateHeader.getStyleClass().add("vital-signs-date");
        dateHeader.getChildren().add(new Label("Date: " + formatDate(entry.getDate())));
        card.getChildren().add(dateHeader);

        // Details grid
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(6);

        int row = 0;
        grid.add(new Label("Blood Pressure:"), 0, row);
        grid.add(new Label(entry.getBloodPressure()), 1, row);
        grid.add(new Label("Temperature:"), 2, row);
        grid.add(new Label(entry.getTemperature()), 3, row);
        row++;
        grid.add(new Label("Pulse Rate:"), 0, row);
        grid.add(new Label(entry.getPulseRate()), 1, row);
        grid.add(new Label("Respiratory Rate:"), 2, row);
        grid.add(new Label(entry.getRespiratoryRate()), 3, row);
        row++;
        grid.add(new Label("Age of Gestation:"), 0, row);
        grid.add(new Label(entry.getAog()), 1, row);
        grid.add(new Label("Height (cm):"), 2, row);
        grid.add(new Label(entry.getHeight()), 3, row);
        row++;
        grid.add(new Label("Weight (lbs):"), 0, row);
        grid.add(new Label(entry.getWeight()), 1, row);
        grid.add(new Label("Fetal Heart Tone:"), 2, row);
        grid.add(new Label(entry.getFht()), 3, row);
        row++;
        grid.add(new Label("Presentation:"), 0, row);
        grid.add(new Label(entry.getPresentation()), 1, row);
        grid.add(new Label("Chief Complaint:"), 2, row);
        grid.add(new Label(entry.getChiefComplaint()), 3, row);
        row++;
        grid.add(new Label("To come Back:"), 0, row);
        grid.add(new Label(formatDate(entry.getToComeBack())), 1, row);
        grid.add(new Label("Remarks:"), 2, row);
        grid.add(new Label(entry.getRemarks()), 3, row);

        card.getChildren().add(grid);
        return card;
    }
}