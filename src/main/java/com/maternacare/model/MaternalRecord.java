package com.maternacare.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class MaternalRecord {
    // Personal Information
    private final transient StringProperty patientId;
    private final transient StringProperty lastName;
    private final transient StringProperty firstName;
    private final transient StringProperty middleName;
    private final transient ObjectProperty<LocalDate> dateOfBirth;
    private final transient StringProperty civilStatus;
    private final transient StringProperty religion;

    // Contact Information
    private final transient StringProperty address;
    private final transient StringProperty purok;
    private final transient StringProperty contactNumber;
    private final transient StringProperty email;

    // Vital Signs
    private final transient StringProperty bloodPressure;
    private final transient StringProperty temperature;
    private final transient StringProperty pulseRate;
    private final transient StringProperty respiratoryRate;

    // Pregnancy Information
    private final transient ObjectProperty<LocalDate> lastMenstrualPeriod;
    private final transient ObjectProperty<LocalDate> expectedDeliveryDate;
    private final transient StringProperty gravida;
    private final transient StringProperty para;
    private final transient StringProperty abortion;
    private final transient StringProperty livingChildren;

    // Medical Information
    private final transient IntegerProperty id;
    private final transient DoubleProperty ageOfGestation;
    private final transient DoubleProperty weight;
    private final transient DoubleProperty height;
    private final transient IntegerProperty fetalHeartTone;
    private final transient StringProperty presentation;
    private final transient DoubleProperty fundalHeight;
    private final transient ObjectProperty<LocalDate> nextAppointment;

    // Default constructor
    public MaternalRecord() {
        // Personal Information
        this.patientId = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.middleName = new SimpleStringProperty("");
        this.dateOfBirth = new SimpleObjectProperty<>(null);
        this.civilStatus = new SimpleStringProperty("");
        this.religion = new SimpleStringProperty("");

        // Contact Information
        this.address = new SimpleStringProperty("");
        this.purok = new SimpleStringProperty("");
        this.contactNumber = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");

        // Vital Signs
        this.bloodPressure = new SimpleStringProperty("");
        this.temperature = new SimpleStringProperty("");
        this.pulseRate = new SimpleStringProperty("");
        this.respiratoryRate = new SimpleStringProperty("");

        // Pregnancy Information
        this.lastMenstrualPeriod = new SimpleObjectProperty<>(null);
        this.expectedDeliveryDate = new SimpleObjectProperty<>(null);
        this.gravida = new SimpleStringProperty("");
        this.para = new SimpleStringProperty("");
        this.abortion = new SimpleStringProperty("");
        this.livingChildren = new SimpleStringProperty("");

        // Medical Information
        this.id = new SimpleIntegerProperty(0);
        this.ageOfGestation = new SimpleDoubleProperty(0.0);
        this.weight = new SimpleDoubleProperty(0.0);
        this.height = new SimpleDoubleProperty(0.0);
        this.fetalHeartTone = new SimpleIntegerProperty(0);
        this.presentation = new SimpleStringProperty("");
        this.fundalHeight = new SimpleDoubleProperty(0.0);
        this.nextAppointment = new SimpleObjectProperty<>(LocalDate.now());
    }

    // Getters and Setters for Personal Information
    public String getPatientId() {
        return patientId.get();
    }

    public void setPatientId(String value) {
        patientId.set(value);
    }

    public StringProperty patientIdProperty() {
        return patientId;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String value) {
        lastName.set(value);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String value) {
        firstName.set(value);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public void setMiddleName(String value) {
        middleName.set(value);
    }

    public StringProperty middleNameProperty() {
        return middleName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(LocalDate value) {
        dateOfBirth.set(value);
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public String getCivilStatus() {
        return civilStatus.get();
    }

    public void setCivilStatus(String value) {
        civilStatus.set(value);
    }

    public StringProperty civilStatusProperty() {
        return civilStatus;
    }

    public String getReligion() {
        return religion.get();
    }

    public void setReligion(String value) {
        religion.set(value);
    }

    public StringProperty religionProperty() {
        return religion;
    }

    // Getters and Setters for Contact Information
    public String getAddress() {
        return address.get();
    }

    public void setAddress(String value) {
        address.set(value);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getPurok() {
        return purok.get();
    }

    public void setPurok(String value) {
        purok.set(value);
    }

    public StringProperty purokProperty() {
        return purok;
    }

    public String getContactNumber() {
        return contactNumber.get();
    }

    public void setContactNumber(String value) {
        contactNumber.set(value);
    }

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String value) {
        email.set(value);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Getters and Setters for Vital Signs
    public String getBloodPressure() {
        return bloodPressure.get();
    }

    public void setBloodPressure(String value) {
        bloodPressure.set(value);
    }

    public StringProperty bloodPressureProperty() {
        return bloodPressure;
    }

    public String getTemperature() {
        return temperature.get();
    }

    public void setTemperature(String value) {
        temperature.set(value);
    }

    public StringProperty temperatureProperty() {
        return temperature;
    }

    public String getPulseRate() {
        return pulseRate.get();
    }

    public void setPulseRate(String value) {
        pulseRate.set(value);
    }

    public StringProperty pulseRateProperty() {
        return pulseRate;
    }

    public String getRespiratoryRate() {
        return respiratoryRate.get();
    }

    public void setRespiratoryRate(String value) {
        respiratoryRate.set(value);
    }

    public StringProperty respiratoryRateProperty() {
        return respiratoryRate;
    }

    // Getters and Setters for Pregnancy Information
    public LocalDate getLastMenstrualPeriod() {
        return lastMenstrualPeriod.get();
    }

    public void setLastMenstrualPeriod(LocalDate value) {
        lastMenstrualPeriod.set(value);
    }

    public ObjectProperty<LocalDate> lastMenstrualPeriodProperty() {
        return lastMenstrualPeriod;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate.get();
    }

    public void setExpectedDeliveryDate(LocalDate value) {
        expectedDeliveryDate.set(value);
    }

    public ObjectProperty<LocalDate> expectedDeliveryDateProperty() {
        return expectedDeliveryDate;
    }

    public String getGravida() {
        return gravida.get();
    }

    public void setGravida(String value) {
        gravida.set(value);
    }

    public StringProperty gravidaProperty() {
        return gravida;
    }

    public String getPara() {
        return para.get();
    }

    public void setPara(String value) {
        para.set(value);
    }

    public StringProperty paraProperty() {
        return para;
    }

    public String getAbortion() {
        return abortion.get();
    }

    public void setAbortion(String value) {
        abortion.set(value);
    }

    public StringProperty abortionProperty() {
        return abortion;
    }

    public String getLivingChildren() {
        return livingChildren.get();
    }

    public void setLivingChildren(String value) {
        livingChildren.set(value);
    }

    public StringProperty livingChildrenProperty() {
        return livingChildren;
    }

    // Getters and Setters for Medical Information
    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public double getAgeOfGestation() {
        return ageOfGestation.get();
    }

    public void setAgeOfGestation(double value) {
        ageOfGestation.set(value);
    }

    public DoubleProperty ageOfGestationProperty() {
        return ageOfGestation;
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(double value) {
        weight.set(value);
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public double getHeight() {
        return height.get();
    }

    public void setHeight(double value) {
        height.set(value);
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public int getFetalHeartTone() {
        return fetalHeartTone.get();
    }

    public void setFetalHeartTone(int value) {
        fetalHeartTone.set(value);
    }

    public IntegerProperty fetalHeartToneProperty() {
        return fetalHeartTone;
    }

    public String getPresentation() {
        return presentation.get();
    }

    public void setPresentation(String value) {
        presentation.set(value);
    }

    public StringProperty presentationProperty() {
        return presentation;
    }

    public double getFundalHeight() {
        return fundalHeight.get();
    }

    public void setFundalHeight(double value) {
        fundalHeight.set(value);
    }

    public DoubleProperty fundalHeightProperty() {
        return fundalHeight;
    }

    public LocalDate getNextAppointment() {
        return nextAppointment.get();
    }

    public void setNextAppointment(LocalDate value) {
        nextAppointment.set(value);
    }

    public ObjectProperty<LocalDate> nextAppointmentProperty() {
        return nextAppointment;
    }

    // Add DTO class for serialization
    public static class MaternalRecordDTO {
        public int id;
        public String patientId;
        public String lastName;
        public String firstName;
        public String middleName;
        public String civilStatus;
        public String religion;
        public String address;
        public String purok;
        public String contactNumber;
        public String email;
        public String bloodPressure;
        public String temperature;
        public String pulseRate;
        public String respiratoryRate;
        public String gravida;
        public String para;
        public String abortion;
        public String livingChildren;
        public String presentation;
        public double ageOfGestation;
        public double weight;
        public double height;
        public int fetalHeartTone;
        public double fundalHeight;
        public String dateOfBirth;
        public String lastMenstrualPeriod;
        public String expectedDeliveryDate;
        public String nextAppointment;
    }

    public MaternalRecordDTO toDTO() {
        MaternalRecordDTO dto = new MaternalRecordDTO();
        dto.id = getId();
        dto.patientId = getPatientId();
        dto.lastName = getLastName();
        dto.firstName = getFirstName();
        dto.middleName = getMiddleName();
        dto.civilStatus = getCivilStatus();
        dto.religion = getReligion();
        dto.address = getAddress();
        dto.purok = getPurok();
        dto.contactNumber = getContactNumber();
        dto.email = getEmail();
        dto.bloodPressure = getBloodPressure();
        dto.temperature = getTemperature();
        dto.pulseRate = getPulseRate();
        dto.respiratoryRate = getRespiratoryRate();
        dto.gravida = getGravida();
        dto.para = getPara();
        dto.abortion = getAbortion();
        dto.livingChildren = getLivingChildren();
        dto.presentation = getPresentation();
        dto.ageOfGestation = getAgeOfGestation();
        dto.weight = getWeight();
        dto.height = getHeight();
        dto.fetalHeartTone = getFetalHeartTone();
        dto.fundalHeight = getFundalHeight();
        dto.dateOfBirth = getDateOfBirth() != null ? getDateOfBirth().toString() : null;
        dto.lastMenstrualPeriod = getLastMenstrualPeriod() != null ? getLastMenstrualPeriod().toString() : null;
        dto.expectedDeliveryDate = getExpectedDeliveryDate() != null ? getExpectedDeliveryDate().toString() : null;
        dto.nextAppointment = getNextAppointment() != null ? getNextAppointment().toString() : null;
        return dto;
    }

    public static MaternalRecord fromDTO(MaternalRecordDTO dto) {
        MaternalRecord record = new MaternalRecord();
        record.setId(dto.id);
        record.setPatientId(dto.patientId);
        record.setLastName(dto.lastName);
        record.setFirstName(dto.firstName);
        record.setMiddleName(dto.middleName);
        record.setCivilStatus(dto.civilStatus);
        record.setReligion(dto.religion);
        record.setAddress(dto.address);
        record.setPurok(dto.purok);
        record.setContactNumber(dto.contactNumber);
        record.setEmail(dto.email);
        record.setBloodPressure(dto.bloodPressure);
        record.setTemperature(dto.temperature);
        record.setPulseRate(dto.pulseRate);
        record.setRespiratoryRate(dto.respiratoryRate);
        record.setGravida(dto.gravida);
        record.setPara(dto.para);
        record.setAbortion(dto.abortion);
        record.setLivingChildren(dto.livingChildren);
        record.setPresentation(dto.presentation);
        record.setAgeOfGestation(dto.ageOfGestation);
        record.setWeight(dto.weight);
        record.setHeight(dto.height);
        record.setFetalHeartTone(dto.fetalHeartTone);
        record.setFundalHeight(dto.fundalHeight);
        record.setDateOfBirth(dto.dateOfBirth != null ? java.time.LocalDate.parse(dto.dateOfBirth) : null);
        record.setLastMenstrualPeriod(
                dto.lastMenstrualPeriod != null ? java.time.LocalDate.parse(dto.lastMenstrualPeriod) : null);
        record.setExpectedDeliveryDate(
                dto.expectedDeliveryDate != null ? java.time.LocalDate.parse(dto.expectedDeliveryDate) : null);
        record.setNextAppointment(dto.nextAppointment != null ? java.time.LocalDate.parse(dto.nextAppointment) : null);
        return record;
    }
}