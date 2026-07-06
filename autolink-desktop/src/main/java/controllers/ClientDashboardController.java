package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;

import java.io.IOException;

public class ClientDashboardController {
    @FXML
    private VBox contentArea;
    
    @FXML
    private Button homeBtn;
    
    @FXML
    private Button productsBtn;
    
    @FXML
    private MenuButton pagesBtn;
    
    @FXML
    private Button blogBtn;
    
    @FXML
    private Button contactBtn;
    
    @FXML
    private Button businessBtn;
    
    @FXML
    private Button favoritesBtn;
    
    @FXML
    private Button cartBtn;
    
    @FXML
    private MenuButton userMenuBtn;
    
    @FXML
    private ImageView userImage;
    
    @FXML
    private MenuItem profileMenuItem;
    
    @FXML
    private MenuItem logoutMenuItem;
    
    private User currentUser;

    @FXML
    public void initialize() {
        setupUserMenu();
    }

    private void setupUserMenu() {
        logoutMenuItem.setOnAction(e -> handleLogout());
    }

    private void handleLogout() {
        try {
            // Clear current user session
            currentUser = null;
            
            // Load login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardLogin.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) contentArea.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Error during logout: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserProfile();
    }

    private void updateUserProfile() {
        if (currentUser != null) {
            try {
                Image image;
                String imagePath = currentUser.getImage_path();
                System.out.println("User image path: " + imagePath); // Debug log

                if (imagePath != null && !imagePath.isEmpty()) {
                    // Try to load user's image
                    if (imagePath.startsWith("uploads/")) {
                        // If path starts with uploads/, it's a relative path
                        image = new Image(getClass().getResourceAsStream("/" + imagePath));
                    } else if (imagePath.startsWith("/")) {
                        // If path starts with /, it's an absolute path
                        image = new Image("file:" + imagePath);
                    } else {
                        // Try both approaches
                        try {
                            image = new Image(getClass().getResourceAsStream("/" + imagePath));
                        } catch (Exception e) {
                            image = new Image("file:" + imagePath);
                        }
                    }
                } else {
                    // Load default logo image if no user image is available
                    System.out.println("Loading default logo image"); // Debug log
                    image = new Image(getClass().getResourceAsStream("/images/logo.jpg"));
                }
                
                if (image != null && !image.isError()) {
                    userImage.setImage(image);
                    System.out.println("Image loaded successfully"); // Debug log
                } else {
                    throw new Exception("Failed to load image");
                }
            } catch (Exception e) {
                System.err.println("Error loading user image: " + e.getMessage());
                // If image loading fails, load the default logo
                try {
                    System.out.println("Loading fallback logo image"); // Debug log
                    Image defaultImage = new Image(getClass().getResourceAsStream("/images/logo.jpg"));
                    userImage.setImage(defaultImage);
                } catch (Exception ex) {
                    System.err.println("Error loading default image: " + ex.getMessage());
                }
            }
            
            // Update menu button text with user's name
            userMenuBtn.setText(currentUser.getName() + " " + currentUser.getLastName());
        }
    }
} 