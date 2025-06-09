package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.IOException;
import com.maternacare.MainApplication;

public class LogIn {
    private MainApplication mainApplication;

    public LogIn() {
    }

    @FXML
    private Button button;
    @FXML
    private Label wrongLogIn;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ProgressIndicator loginProgress;

    public void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    // Method to set the text of the wrongLogIn label
    public void setWrongLogInText(String text) {
        Platform.runLater(() -> wrongLogIn.setText(text));
    }

    public void userLogIn(ActionEvent event) throws IOException {
        wrongLogIn.setText("");
        checkLogin();
    }

    private void checkLogin() {
        // Simple hardcoded credentials for demonstration
        String validUsername = "admin";
        String validPassword = "admin123";

        if (username.getText().equals(validUsername) && password.getText().equals(validPassword)) {
            wrongLogIn.setText("Success!");
            if (mainApplication != null) {
                Platform.runLater(() -> {
                    try {
                        mainApplication.showMainApplicationScene();
                    } catch (IOException ex) {
                        wrongLogIn.setText("Error loading main application!");
                    }
                });
            }
        } else {
            wrongLogIn.setText("Wrong username or password!");
        }
    }
}