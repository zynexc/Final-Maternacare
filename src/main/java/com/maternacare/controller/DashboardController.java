package com.maternacare.controller;

import com.maternacare.model.PatientData;
import com.maternacare.model.MaternalRecord;
import com.maternacare.service.MaternalRecordService;
import com.maternacare.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.geometry.Pos;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.IOException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;

public class DashboardController {
    @FXML
    private AreaChart<String, Number> barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private TableView<PatientData> recordsTable;
    @FXML
    private TableColumn<PatientData, String> patientIdColumn;
    @FXML
    private TableColumn<PatientData, String> lastNameColumn;
    @FXML
    private TableColumn<PatientData, String> firstNameColumn;
    @FXML
    private TableColumn<PatientData, Integer> ageColumn;
    @FXML
    private TableColumn<PatientData, String> purokColumn;
    @FXML
    private TableColumn<PatientData, String> contactColumn;
    @FXML
    private TableColumn<PatientData, String> emailColumn;
    @FXML
    private TableColumn<PatientData, Double> ageOfGestationColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Label totalPatientsLabel;
    @FXML
    private Label completedFormsLabel;
    @FXML
    private Label severeCasesLabel;
    @FXML
    private StackPane totalPatientsIconContainer;
    @FXML
    private StackPane completedFormsIconContainer;
    @FXML
    private StackPane severeCasesIconContainer;
    @FXML
    private HBox purokLegendBox;

    private ObservableList<PatientData> patientData = FXCollections.observableArrayList();
    private FilteredList<PatientData> filteredData;
    private MainApplication mainApplication;
    private MaternalRecordService recordService = new MaternalRecordService();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableSearch();
        setupIcons();
        loadMaternalRecords();
        updateCharts();
        updateStatCards();
    }

    private void setupTableColumns() {
        // Disable column reordering for the entire table
        recordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Setup columns with fixed properties
        patientIdColumn.setSortable(false);
        patientIdColumn.setReorderable(false);
        patientIdColumn.setResizable(true);
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        lastNameColumn.setSortable(false);
        lastNameColumn.setReorderable(false);
        lastNameColumn.setResizable(true);
        // Use full name for the "Full Name" column
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        firstNameColumn.setSortable(false);
        firstNameColumn.setReorderable(false);
        firstNameColumn.setResizable(true);
        // Hide the "First Name" column since we now use full name
        firstNameColumn.setVisible(false);

        ageColumn.setSortable(false);
        ageColumn.setReorderable(false);
        ageColumn.setResizable(true);
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageColumn.setStyle("-fx-alignment: CENTER;"); // Center alignment for Age column

        ageOfGestationColumn.setSortable(false);
        ageOfGestationColumn.setReorderable(false);
        ageOfGestationColumn.setResizable(true);
        ageOfGestationColumn.setCellValueFactory(new PropertyValueFactory<>("ageOfGestation"));
        ageOfGestationColumn.setStyle("-fx-alignment: CENTER;"); // Center alignment for Age of Gestation column

        purokColumn.setSortable(false);
        purokColumn.setReorderable(false);
        purokColumn.setResizable(true);
        purokColumn.setCellValueFactory(new PropertyValueFactory<>("purok"));
        purokColumn.setStyle("-fx-alignment: CENTER;"); // Center alignment for Purok column

        contactColumn.setSortable(false);
        contactColumn.setReorderable(false);
        contactColumn.setResizable(true);
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        emailColumn.setSortable(false);
        emailColumn.setReorderable(false);
        emailColumn.setResizable(true);
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void setupTableSearch() {
        filteredData = new FilteredList<>(patientData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(patient -> {
                // Only include patients aged 10-17 in the table
                int age = patient.getAge();
                if (age < 10 || age > 17) {
                    return false;
                }
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return patient.getPatientId().toLowerCase().contains(lowerCaseFilter) ||
                        patient.getName().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(patient.getAge()).contains(lowerCaseFilter) ||
                        String.valueOf(patient.getAgeOfGestation()).contains(lowerCaseFilter) ||
                        patient.getPurok().toLowerCase().contains(lowerCaseFilter) ||
                        patient.getContactNumber().toLowerCase().contains(lowerCaseFilter) ||
                        patient.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<PatientData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(recordsTable.comparatorProperty());
        recordsTable.setItems(sortedData);
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadMaternalRecords();
        updateCharts();
        updateStatCards();
    }

    private void loadMaternalRecords() {
        patientData.clear();

        // Load maternal records
        List<MaternalRecord> records = recordService.loadRecords();
        for (MaternalRecord record : records) {
            if (record.getDateOfBirth() != null) {
                int age = Period.between(record.getDateOfBirth(), LocalDate.now()).getYears();
                // Only add patients aged 10-17 to patientData for the table
                if (age >= 10 && age <= 17) {
                    patientData.add(new PatientData(
                            record.getPatientId(),
                            record.getFullName(),
                            age,
                            record.getPurok(),
                            record.getAgeOfGestation(),
                            record.getBloodPressure(),
                            record.getWeight(),
                            record.getHeight(),
                            record.getNextAppointment(),
                            record.getContactNumber(),
                            record.getEmail()));
                }
            }
        }
    }

    private void updateStatCards() {
        List<MaternalRecord> allRecords = recordService.loadRecords();
        long completedForms = allRecords.stream().filter(r -> r.getFormTimestamp() != null).count();

        totalPatientsLabel.setText(String.valueOf(allRecords.size()));
        completedFormsLabel.setText(String.valueOf(completedForms));
        severeCasesLabel.setText(String.valueOf(patientData.size()));
    }

    private void updateCharts() {
        // Clear existing data
        barChart.getData().clear();
        pieChart.getData().clear();

        // Prepare data for bar chart (age groups for severe cases)
        XYChart.Series<String, Number> ageSeries = new XYChart.Series<>();
        ageSeries.setName("Age Distribution (Severe Cases)");

        // Count patients in age groups (10-17 years)
        int[] ageGroups = new int[4]; // 10-11, 12-13, 14-15, 16-17
        for (PatientData data : patientData) {
            int age = data.getAge();
            if (age >= 10 && age <= 11)
                ageGroups[0]++;
            else if (age >= 12 && age <= 13)
                ageGroups[1]++;
            else if (age >= 14 && age <= 15)
                ageGroups[2]++;
            else if (age >= 16 && age <= 17)
                ageGroups[3]++;
        }

        // Add data to bar chart
        ageSeries.getData().add(new XYChart.Data<>("10-11", ageGroups[0]));
        ageSeries.getData().add(new XYChart.Data<>("12-13", ageGroups[1]));
        ageSeries.getData().add(new XYChart.Data<>("14-15", ageGroups[2]));
        ageSeries.getData().add(new XYChart.Data<>("16-17", ageGroups[3]));

        barChart.getData().add(ageSeries);

        // Prepare data for pie chart (patients per purok)
        int[] purokCounts = new int[6]; // Purok 1 to 6
        java.util.Set<String>[] purokPatientIds = new java.util.HashSet[6];
        for (int i = 0; i < 6; i++)
            purokPatientIds[i] = new java.util.HashSet<>();
        for (PatientData data : patientData) {
            String purok = data.getPurok();
            if (purok != null && purok.matches("Purok [1-6]")) {
                int purokNumber = Integer.parseInt(purok.split(" ")[1]);
                String patientId = data.getPatientId();
                if (!purokPatientIds[purokNumber - 1].contains(patientId)) {
                    purokPatientIds[purokNumber - 1].add(patientId);
                    purokCounts[purokNumber - 1]++;
                }
            }
        }

        // Add data to pie chart
        final int total;
        {
            int t = 0;
            for (int count : purokCounts)
                t += count;
            total = t;
        }
        for (int i = 0; i < 6; i++) {
            PieChart.Data data = new PieChart.Data("", purokCounts[i]); // Hide label
            pieChart.getData().add(data);
        }
        // Add tooltips to pie chart slices
        Platform.runLater(() -> {
            for (int i = 0; i < pieChart.getData().size(); i++) {
                PieChart.Data data = pieChart.getData().get(i);
                int count = purokCounts[i];
                double percent = total > 0 ? (count * 100.0 / total) : 0.0;
                String tooltipText = String.format("Purok %d: %.1f%%", i + 1, percent);
                Tooltip tooltip = new Tooltip(tooltipText);
                Tooltip.install(data.getNode(), tooltip);
            }
        });

        // Remove pie chart radial lines (chart lines)
        pieChart.lookupAll(".chart-pie-label-line").forEach(node -> node.setStyle("-fx-stroke: transparent;"));

        // Custom legend for Purok colors
        purokLegendBox.getChildren().clear();
        for (int i = 0; i < pieChart.getData().size(); i++) {
            PieChart.Data data = pieChart.getData().get(i);
            // Get the color from the pie slice
            String color = data.getNode().getStyle();
            // Fallback to default JavaFX pie colors if not set
            if (color == null || color.isEmpty()) {
                // JavaFX default pie colors
                String[] defaultColors = { "#f3622d", "#fba71b", "#57b757", "#41a9c9", "#4258c9", "#9a42c8" };
                color = "-fx-pie-color: " + defaultColors[i % defaultColors.length] + ";";
            }
            String pieColor = color.replace("-fx-pie-color:", "").replace(";", "").trim();
            Circle circle = new Circle(7, Paint.valueOf(pieColor));
            Label label = new Label("Purok " + (i + 1));
            label.setStyle("-fx-font-size: 13px; -fx-padding: 0 6 0 4;");
            HBox legendItem = new HBox(4, circle, label);
            legendItem.setAlignment(Pos.CENTER);
            purokLegendBox.getChildren().add(legendItem);
        }
    }

    public void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @FXML
    private void handleLogout() {
        if (mainApplication != null) {
            try {
                mainApplication.showLoginScreen();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Logout Error", "Failed to return to login screen.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setupIcons() {
        // Person Icon
        FontAwesomeIconView usersIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
        usersIcon.setSize("3em");
        usersIcon.setFill(Color.web("#eb0000"));
        totalPatientsIconContainer.getChildren().add(usersIcon);

        // Note Icon
        FontAwesomeIconView fileIcon = new FontAwesomeIconView(FontAwesomeIcon.FILE_TEXT_ALT);
        fileIcon.setSize("3em");
        fileIcon.setFill(Color.web("#eb0000"));
        completedFormsIconContainer.getChildren().add(fileIcon);

        // Warning Icon
        FontAwesomeIconView warningIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        warningIcon.setSize("3em");
        warningIcon.setFill(Color.web("#eb0000"));
        severeCasesIconContainer.getChildren().add(warningIcon);
    }

    @FXML
    private void handleViewRecords() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maternal_records.fxml"));
            VBox recordsView = loader.load();
            recordsView.getStylesheets().add(getClass().getResource("/styles/maternal_records.css").toExternalForm());

            MaternalRecordsController controller = loader.getController();
            controller.setDashboardController(this);

            // Get the parent of the dashboard VBox and replace it with the records view
            VBox dashboardVBox = (VBox) totalPatientsLabel.getScene().getRoot();
            VBox parentContainer = (VBox) dashboardVBox.getParent();
            parentContainer.getChildren().setAll(recordsView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}