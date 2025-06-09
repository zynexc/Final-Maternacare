package com.maternacare.model;

import javafx.beans.property.*;

public class PatientData {
    private final StringProperty name;
    private final IntegerProperty age;
    private final StringProperty sex;
    private final DoubleProperty value;

    public PatientData(String name, int age, String sex, double value) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.sex = new SimpleStringProperty(sex);
        this.value = new SimpleDoubleProperty(value);
    }

    // Getters and setters for name
    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Getters and setters for age
    public int getAge() {
        return age.get();
    }

    public void setAge(int value) {
        age.set(value);
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    // Getters and setters for sex
    public String getSex() {
        return sex.get();
    }

    public void setSex(String value) {
        sex.set(value);
    }

    public StringProperty sexProperty() {
        return sex;
    }

    // Getters and setters for value
    public double getValue() {
        return value.get();
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public DoubleProperty valueProperty() {
        return value;
    }
}