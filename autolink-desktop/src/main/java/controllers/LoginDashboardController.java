package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Entreprise;
import models.User;
import services.UserService;
import services.impl.EntrepriseServiceImpl;
import services.EntrepriseService;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class LoginDashboardController {

    @FXML private TextField emailField;
    @FXML private Label errorLabel;
    @FXML private Label emailErrorLabel;
    @FXML private Label passwordErrorLabel;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Button loginButton;
    @FXML private AnchorPane loginPane;
    @FXML private PasswordField passwordField;
    @FXML private Button showPasswordButton;
    @FXML private TextField visiblePasswordField;

    private final UserService userService = new UserService();
    private final EntrepriseService entrepriseService = new EntrepriseServiceImpl(); // Fixed instantiation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        forgotPasswordLink.setOnAction(event -> handleForgotPassword());

        visiblePasswordField = new TextField();
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setPromptText("Password");
        visiblePasswordField.getStyleClass().add("login-field");

        ((HBox) passwordField.getParent()).getChildren().add(visiblePasswordField);
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        emailField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
    }

    private void validateInputs() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        boolean isEmailValid = !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
        boolean isPasswordValid = !password.isEmpty();

        if (email.isEmpty()) {
            emailErrorLabel.setText("Email is required");
            emailErrorLabel.setVisible(true);
        } else if (!isEmailValid) {
            emailErrorLabel.setText("Please enter a valid email address");
            emailErrorLabel.setVisible(true);
        } else {
            emailErrorLabel.setVisible(false);
        }

        if (password.isEmpty()) {
            passwordErrorLabel.setText("Password is required");
            passwordErrorLabel.setVisible(true);
        } else {
            passwordErrorLabel.setVisible(false);
        }

        loginButton.setDisable(!isEmailValid || !isPasswordValid);
    }

    @FXML
    private void togglePasswordVisibility() {
        boolean isPasswordVisible = visiblePasswordField.isVisible();

        if (isPasswordVisible) {
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            ImageView eyeIcon = (ImageView) showPasswordButton.getGraphic();
            eyeIcon.setImage(new Image(getClass().getResourceAsStream("/icons/eye.png")));
        } else {
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            ImageView eyeIcon = (ImageView) showPasswordButton.getGraphic();
            eyeIcon.setImage(new Image(getClass().getResourceAsStream("/icons/eye-crossed.png")));
        }
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            try {
                User authenticatedUser = userService.authenticate(email, password);
                SessionManager.setCurrentUser(authenticatedUser);

                int roleId = authenticatedUser.getRoleId();

                if (roleId == 1) {
                    loadAdminDashboard();
                } else if (roleId == 6) {
                    loadClientDashboard(authenticatedUser);
                } else if (roleId == 5) {
                    loadEntrepriseDashboard(); // 🎯 ONLY based on role_id
                } else {
                    showError("Unknown role_id: " + roleId);
                }

            } catch (IllegalArgumentException e1) {
                try {
                    Entreprise authenticatedEntreprise = entrepriseService.authenticate(email, password);
                    SessionManager.setCurrentEntreprise(authenticatedEntreprise);

                    if (authenticatedEntreprise.getRoleId() == 5) {
                        loadEntrepriseDashboard(); // fallback entreprise login
                    } else {
                        showError("Entreprise role_id is not 5");
                    }

                } catch (IllegalArgumentException e2) {
                    emailErrorLabel.setVisible(false);
                    passwordErrorLabel.setText("Incorrect email or password");
                    passwordErrorLabel.setVisible(true);
                }
            }

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }




    private void loadAdminDashboard() {
        try {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("/AdminDashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load admin dashboard");
            e.printStackTrace();
        }
    }

    private void loadEntrepriseDashboard() {
        try {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("/DashboardEntreprise.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Enterprise Dashboard");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load enterprise dashboard");
            e.printStackTrace();
        }
    }

    private void loadClientDashboard(User user) {
        try {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientDashboard.fxml"));
            Parent root = loader.load();
            ClientDashboardController controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Client Dashboard");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load client dashboard");
            e.printStackTrace();
        }
    }

    private void handleForgotPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password Recovery");
        dialog.setHeaderText("Enter your registered email");
        dialog.setContentText("Email:");

        dialog.showAndWait().ifPresent(email -> {
            try {
                userService.sendPasswordReset(email);
                showAlert(Alert.AlertType.INFORMATION,
                        "Password Reset",
                        "If this email exists in our system, you'll receive a reset link.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR,
                        "Error",
                        "Password reset failed: " + e.getMessage());
            }
        });
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #d9534f;");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
