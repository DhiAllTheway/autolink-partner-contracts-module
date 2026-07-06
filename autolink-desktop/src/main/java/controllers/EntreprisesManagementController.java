package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.User;
import services.EntrepriseService;
import services.impl.EntrepriseServiceImpl;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EntreprisesManagementController implements Initializable {

    @FXML
    private FlowPane entreprisesFlowPane;

    @FXML
    private ImageView addEntrepriseButton;

    private final EntrepriseService entrepriseService = new EntrepriseServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (addEntrepriseButton != null) {
            addEntrepriseButton.setOnMouseClicked(e -> openAddEntrepriseWindow());
        }

        try {
            loadEntreprises();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Impossible de charger les entreprises.\n" + e.getMessage());
        }
    }

    private void loadEntreprises() throws SQLException {
        entreprisesFlowPane.getChildren().clear();
        List<User> entreprises = entrepriseService.getUserEntreprises(); // returns List<User>

        if (entreprises.isEmpty()) {
            Label emptyLabel = new Label("Aucune entreprise trouvée.");
            emptyLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            entreprisesFlowPane.getChildren().add(emptyLabel);
            return;
        }

        for (User user : entreprises) {
            VBox card = createEntrepriseCard(user);
            entreprisesFlowPane.getChildren().add(card);
        }
    }

    private VBox createEntrepriseCard(User user) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefSize(280, 200);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #ccc;");

        // Image placeholder
        ImageView imageView = new ImageView();
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        try {
            if (user.getImage_path() != null && !user.getImage_path().isEmpty()) {
                imageView.setImage(new Image("file:" + user.getImage_path()));
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.jpg")));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.jpg")));
        }

        Label nameLabel = new Label(user.getName() + " " + user.getLastName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label emailLabel = new Label(user.getEmail());
        emailLabel.setStyle("-fx-text-fill: #333;");

        Label cityLabel = new Label("Ville: " + (user.getAddress() != null ? user.getAddress().getCity() : "N/A"));
        cityLabel.setStyle("-fx-text-fill: #666;");

        Button editBtn = new Button("🖊 Modifier");
        editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        editBtn.setOnAction(e -> editEntreprise(user));

        Button deleteBtn = new Button("🗑 Supprimer");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> deleteEntreprise(user));

        HBox actions = new HBox(10, editBtn, deleteBtn);
        actions.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, nameLabel, emailLabel, cityLabel, actions);
        return card;
    }

    private void deleteEntreprise(User user) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer la suppression de cet utilisateur entreprise ?", ButtonType.YES, ButtonType.CANCEL);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    entrepriseService.deleteEntreprise(user.getId()); // assumes this deletes from entreprise user table
                    loadEntreprises();
                } catch (SQLException e) {
                    showAlert("Erreur suppression", "Impossible de supprimer : " + e.getMessage());
                }
            }
        });
    }

    private void editEntreprise(User user) {
        showAlert("Édition", "Fonctionnalité de modification non implémentée pour : " + user.getName());
    }

    private void openAddEntrepriseWindow() {
        showAlert("Ajout", "Fenêtre d'ajout non implémentée.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
