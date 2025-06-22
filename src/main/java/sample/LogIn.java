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
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

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
    @FXML
    private ImageView rightImageView;
    @FXML
    private VBox loginContainer;
    @FXML
    private StackPane imageContainer;
    @FXML
    private HBox rootHBox;

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
        String validUsername = "";
        String validPassword = "";

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

    @FXML
    public void initialize() {
        // Ensure the right image fills the right half
        if (rightImageView != null && rightImageView.getParent() instanceof StackPane) {
            StackPane parent = (StackPane) rightImageView.getParent();
            rightImageView.fitWidthProperty().bind(parent.widthProperty());
            rightImageView.fitHeightProperty().bind(parent.heightProperty());
            rightImageView.setImage(new Image(getClass().getResource("/images/right-image.png").toExternalForm()));
        }
        // Responsive 50/50 split
        if (rootHBox != null && loginContainer != null && imageContainer != null) {
            rootHBox.widthProperty().addListener((obs, oldVal, newVal) -> {
                double totalWidth = newVal.doubleValue();
                loginContainer.setPrefWidth(totalWidth * 0.5);
                imageContainer.setPrefWidth(totalWidth * 0.5);
            });
        }
    }
}