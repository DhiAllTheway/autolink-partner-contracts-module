package controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.UserService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddClientViewController implements Initializable {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label phoneErrorLabel;

    private Stage stage;
    private final UserService userService = new UserService();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|org|net|fr|tn|eu)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{8}$");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add input validation
        saveButton.setDisable(true);
        
        // Create error labels if they don't exist in FXML
        if (emailErrorLabel == null) {
            emailErrorLabel = new Label();
            emailErrorLabel.setStyle("-fx-text-fill: red;");
        }
        if (phoneErrorLabel == null) {
            phoneErrorLabel = new Label();
            phoneErrorLabel.setStyle("-fx-text-fill: red;");
        }
        
        // Add listeners for real-time validation
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateEmail(newValue);
            validateFields();
        });
        
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Only allow numbers and limit to 8 digits
            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 8) {
                phoneField.setText(oldValue);
            }
            validatePhone(newValue);
            validateFields();
        });
        
        // Other field listeners
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }

    private void validateEmail(String email) {
        if (email.isEmpty()) {
            emailErrorLabel.setText("Email is required");
            emailErrorLabel.setVisible(true);
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            emailErrorLabel.setText("Email must end with: .com, .org, .net, .fr, .tn, or .eu");
            emailErrorLabel.setVisible(true);
        } else {
            emailErrorLabel.setVisible(false);
        }
    }

    private void validatePhone(String phone) {
        if (phone.isEmpty()) {
            phoneErrorLabel.setText("Phone number is required");
            phoneErrorLabel.setVisible(true);
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            phoneErrorLabel.setText("Must contain 8 numbers");
            phoneErrorLabel.setVisible(true);
        } else {
            phoneErrorLabel.setVisible(false);
        }
    }

    private void validateFields() {
        boolean isValid = !firstNameField.getText().isEmpty() &&
                         !lastNameField.getText().isEmpty() &&
                         !emailField.getText().isEmpty() &&
                         EMAIL_PATTERN.matcher(emailField.getText()).matches() &&
                         !phoneField.getText().isEmpty() &&
                         PHONE_PATTERN.matcher(phoneField.getText()).matches() &&
                         !passwordField.getText().isEmpty();
        
        saveButton.setDisable(!isValid);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleSave() {
        try {
            // Validate inputs one last time
            if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) {
                showError("Email must end with: .com, .org, .net, .fr, .tn, or .eu");
                return;
            }
            if (!PHONE_PATTERN.matcher(phoneField.getText()).matches()) {
                showError("Must contain 8 numbers");
                return;
            }

            // Create new client user
            User newClient = new User();
            newClient.setName(firstNameField.getText());
            newClient.setLastName(lastNameField.getText());
            newClient.setEmail(emailField.getText());
            newClient.setPhone(Integer.parseInt(phoneField.getText()));
            newClient.setPassword(passwordField.getText());
            newClient.setRoleId(2); // 2 for client role
            newClient.setCreatedAt(java.time.LocalDateTime.now());

            // Add to database
            userService.ajouter(newClient);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Client added successfully!");
            alert.showAndWait();

            // Close the window
            stage.close();
        } catch (SQLException e) {
            showError("An error occurred while adding the client: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to add client");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }
} 