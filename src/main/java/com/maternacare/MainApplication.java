package com.maternacare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import com.maternacare.controller.MaternalFormController;
import com.maternacare.controller.MaternalRecordsController;
import com.maternacare.controller.DashboardController;
import sample.LogIn;
import java.io.IOException;
import java.nio.file.Paths;

public class MainApplication extends Application {

    private Stage primaryStage;
    private BorderPane mainLayout;
    private Scene mainScene;
    private MaternalRecordsController recordsController;
    private MaternalFormController formController;
    private LogIn loginController; // Store login controller instance
    private Parent maternalRecordsRoot; // Declare as instance variable
    private Parent maternalFormRoot; // Declare as instance variable

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MaternaCare");

        // Set the application icon
        try {
            Image applicationIcon = new Image(
                    Paths.get("C:/Users/angel/OneDrive/Desktop/MaternaCare/src/main/resources/images/mylogo.jpg")
                            .toUri().toString());
            primaryStage.getIcons().add(applicationIcon);
        } catch (Exception e) {
            System.err.println("Failed to load application icon: " + e.getMessage());
            e.printStackTrace();
        }

        // Load the login screen initially
        showLoginScreen();

        primaryStage.show();
    }

    public void showLoginScreen() throws IOException {
        System.out.println("Showing login screen...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LogIn.fxml"));
        Parent loginRoot = loader.load();
        loginController = loader.getController(); // Store the controller
        loginController.setMainApplication(this);

        Scene loginScene = new Scene(loginRoot, 600, 400);

        primaryStage.setScene(loginScene);
        System.out.println("Login screen shown.");
    }

    public void showMainApplicationScene() throws IOException {
        System.out.println("Attempting to show main application scene...");
        // Load the main application layout if not already loaded
        if (mainLayout == null) {
            System.out.println("mainLayout is null, creating new layout.");
            mainLayout = new BorderPane();

            // Create the left sidebar with buttons
            VBox sidebar = new VBox(10); // 10 pixels spacing between buttons
            sidebar.getStyleClass().add("sidebar");
            sidebar.setPadding(new Insets(20, 0, 20, 0));
            sidebar.setPrefWidth(200);

            // Create navigation buttons
            Button dashboardBtn = createNavButton("Dashboard");
            Button maternalFormBtn = createNavButton("Maternal Form");
            Button maternalRecordsBtn = createNavButton("Maternal Records");

            // Add buttons to sidebar
            sidebar.getChildren().addAll(dashboardBtn, maternalFormBtn, maternalRecordsBtn);

            // Create content container
            VBox contentContainer = new VBox();
            contentContainer.getStyleClass().add("content-container");

            // Pre-load the records controller
            System.out.println("Attempting to load maternal_records.fxml");
            try {
                FXMLLoader recordsLoader = new FXMLLoader(getClass().getResource("/fxml/maternal_records.fxml"));
                System.out.println("Created FXMLLoader for maternal_records.fxml");
                maternalRecordsRoot = recordsLoader.load(); // Assign to instance variable
                System.out.println("Successfully loaded maternal_records.fxml.");
                maternalRecordsRoot.getStylesheets()
                        .add(getClass().getResource("/styles/maternal_records.css").toExternalForm());
                recordsController = recordsLoader.getController();
                System.out.println("Records controller loaded: " + (recordsController != null ? "success" : "null"));
            } catch (IOException ex) {
                System.err.println("Failed to load maternal_records.fxml: " + ex.getMessage());
                ex.printStackTrace();
                recordsController = null; // Set to null on failure
            }

            // Pre-load the form controller
            System.out.println("Attempting to load maternal_form.fxml");
            try {
                FXMLLoader formLoader = new FXMLLoader(getClass().getResource("/fxml/maternal_form.fxml"));
                System.out.println("Created FXMLLoader for maternal_form.fxml");
                maternalFormRoot = formLoader.load(); // Assign to instance variable
                System.out.println("Successfully loaded maternal_form.fxml.");
                maternalFormRoot.getStylesheets()
                        .add(getClass().getResource("/styles/maternal_form.css").toExternalForm());
                formController = formLoader.getController();
                System.out.println("Form controller loaded: " + (formController != null ? "success" : "null"));
            } catch (IOException ex) {
                System.err.println("Failed to load maternal_form.fxml: " + ex.getMessage());
                ex.printStackTrace();
                formController = null; // Set to null on failure
            }

            // Connect controllers bidirectionally
            System.out.println("Attempting to connect controllers...");
            System.out.println("Form controller: " + (formController != null ? "available" : "null"));
            System.out.println("Records controller: " + (recordsController != null ? "available" : "null"));
            if (formController != null && recordsController != null) {
                formController.setRecordsController(recordsController);
                recordsController.setFormController(formController);
                System.out.println("Controllers connected successfully.");
            } else {
                System.err.println("Controllers not loaded successfully, cannot connect.");
            }

            // Set up button actions
            dashboardBtn.setOnAction(e -> {
                System.out.println("Dashboard button clicked. Clearing contentContainer...");
                contentContainer.getChildren().clear();
                try {
                    System.out.println("Attempting to load dashboard.fxml for display...");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                    Parent dashboardRoot = loader.load();
                    System.out.println("Successfully loaded dashboard.fxml for display.");
                    dashboardRoot.getStylesheets()
                            .add(getClass().getResource("/styles/dashboard.css").toExternalForm());
                    DashboardController dashboardController = loader.getController();
                    dashboardController.setMainApplication(this);
                    System.out.println("Adding dashboardRoot to contentContainer.");
                    contentContainer.getChildren().add(dashboardRoot);
                    System.out.println("dashboardRoot added to contentContainer.");
                } catch (IOException ex) {
                    System.out.println("Failed to load dashboard.fxml: " + ex.getMessage());
                    ex.printStackTrace();
                    if (loginController != null) {
                        loginController.setWrongLogInText("Error loading dashboard: " + ex.getMessage());
                    }
                }
                updateButtonStyles(dashboardBtn, maternalFormBtn, maternalRecordsBtn);
            });

            maternalFormBtn.setOnAction(e -> {
                System.out.println("Maternal Form button clicked. Clearing contentContainer...");
                contentContainer.getChildren().clear();
                if (maternalFormRoot != null) {
                    System.out.println("Adding maternalFormRoot to contentContainer.");
                    try {
                        contentContainer.getChildren().add(maternalFormRoot);
                        System.out.println("maternalFormRoot added to contentContainer.");
                    } catch (Exception ex) {
                        System.err.println("Error adding maternalFormRoot to contentContainer: " + ex.getMessage());
                        ex.printStackTrace();
                        if (loginController != null) {
                            loginController.setWrongLogInText("Error displaying Maternal Form: " + ex.getMessage());
                        }
                    }
                } else {
                    System.err.println("maternalFormRoot is null, cannot add to contentContainer.");
                    if (loginController != null) {
                        loginController.setWrongLogInText("Error: Maternal Form not loaded.");
                    }
                }
                updateButtonStyles(maternalFormBtn, dashboardBtn, maternalRecordsBtn);
            });

            maternalRecordsBtn.setOnAction(e -> {
                System.out.println("Maternal Records button clicked. Clearing contentContainer...");
                contentContainer.getChildren().clear();
                try {
                    System.out.println("Attempting to load maternal_records.fxml for display...");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maternal_records.fxml"));
                    Parent recordsRoot = loader.load();
                    System.out.println("Successfully loaded maternal_records.fxml for display.");
                    recordsRoot.getStylesheets()
                            .add(getClass().getResource("/styles/maternal_records.css").toExternalForm());
                    MaternalRecordsController currentRecordsController = loader.getController();

                    // Ensure controllers are connected even when reloading
                    if (formController != null && currentRecordsController != null) {
                        formController.setRecordsController(currentRecordsController);
                        currentRecordsController.setFormController(formController);
                        System.out.println("Controllers connected after reloading maternal records.");
                    } else {
                        System.err
                                .println("Controllers not available after reloading maternal records, cannot connect.");
                    }

                    System.out.println("Adding recordsRoot to contentContainer.");
                    contentContainer.getChildren().add(recordsRoot);
                    System.out.println("recordsRoot added to contentContainer.");
                } catch (IOException ex) {
                    System.err.println("Failed to load maternal_records.fxml for display: " + ex.getMessage());
                    ex.printStackTrace();
                    // Optionally show an error message to the user
                    if (loginController != null) {
                        loginController.setWrongLogInText("Error loading Maternal Records: " + ex.getMessage());
                    }
                }
                updateButtonStyles(maternalRecordsBtn, dashboardBtn, maternalFormBtn);
            });

            // Set initial button style (Dashboard is default)
            updateButtonStyles(dashboardBtn, maternalFormBtn, maternalRecordsBtn);

            // Set the sidebar and content in the border pane
            mainLayout.setLeft(sidebar);

            // Load and set initial content (Dashboard) into the content container
            System.out.println("Attempting to load initial dashboard.fxml...");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                Parent dashboardRoot = loader.load();
                System.out.println("Successfully loaded initial dashboard.fxml.");
                dashboardRoot.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
                DashboardController dashboardController = loader.getController();
                dashboardController.setMainApplication(this);
                System.out.println("Adding initial dashboardRoot to contentContainer.");
                contentContainer.getChildren().add(dashboardRoot);
                System.out.println("Initial dashboardRoot added to contentContainer.");
            } catch (IOException ex) {
                System.out.println("Failed to load initial dashboard.fxml: " + ex.getMessage());
                ex.printStackTrace();
                if (loginController != null) {
                    loginController.setWrongLogInText("Error loading initial dashboard: " + ex.getMessage());
                }
            }
            mainLayout.setCenter(contentContainer);
            System.out.println("contentContainer set as center of mainLayout.");

            // Create the main scene once and store it
            mainScene = new Scene(mainLayout, 1200, 800);
            mainScene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            System.out.println("mainScene created.");
        } else {
            System.out.println("mainLayout already exists.");
        }

        // Set the main scene to the primary stage directly without animation
        System.out.println("Setting mainScene directly...");
        primaryStage.setScene(mainScene);
        System.out.println("mainScene set.");
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPadding(new Insets(15, 20, 15, 20));
        return button;
    }

    private void updateButtonStyles(Button selected, Button... others) {
        selected.getStyleClass().add("selected");
        for (Button button : others) {
            button.getStyleClass().remove("selected");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}