package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Entreprise;
import models.User;
import services.EntrepriseService;
import services.impl.EntrepriseServiceImpl;
import utils.QRCodeGenerator;
import utils.SessionManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserEntrepriseListController implements Initializable {

    @FXML private FlowPane userEntrepriseContainer;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private Label pageNumberLabel;

    private final EntrepriseService entrepriseService = new EntrepriseServiceImpl();

    private List<Entreprise> allEntreprises = new ArrayList<>();
    private List<Entreprise> filteredEntreprises = new ArrayList<>();

    private int currentPage = 1;
    private final int itemsPerPage = 4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            try {
                allEntreprises = entrepriseService.getEntreprisesByUser(currentUser.getId());
                filteredEntreprises = new ArrayList<>(allEntreprises);
                setupSorting();
                refreshView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        sortComboBox.setOnAction(e -> applyFilters());
    }

    private void setupSorting() {
        ObservableList<String> options = FXCollections.observableArrayList("Company Name (A-Z)", "Company Name (Z-A)");
        sortComboBox.setItems(options);
    }

    private void applyFilters() {
        String keyword = searchField.getText().toLowerCase();

        filteredEntreprises = allEntreprises.stream()
                .filter(e -> e.getCompanyName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        String sortChoice = sortComboBox.getValue();
        if (sortChoice != null) {
            if (sortChoice.equals("Company Name (A-Z)")) {
                filteredEntreprises.sort(Comparator.comparing(Entreprise::getCompanyName));
            } else if (sortChoice.equals("Company Name (Z-A)")) {
                filteredEntreprises.sort(Comparator.comparing(Entreprise::getCompanyName).reversed());
            }
        }

        currentPage = 1;
        refreshView();
    }

    private void refreshView() {
        userEntrepriseContainer.getChildren().clear();

        int fromIndex = (currentPage - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, filteredEntreprises.size());

        if (fromIndex >= toIndex) {
            pageNumberLabel.setText("Page " + currentPage);
            return;
        }

        List<Entreprise> pageItems = filteredEntreprises.subList(fromIndex, toIndex);

        for (Entreprise entreprise : pageItems) {
            VBox card = createEntrepriseCard(entreprise);
            userEntrepriseContainer.getChildren().add(card);
        }

        pageNumberLabel.setText("Page " + currentPage);
    }

    private VBox createEntrepriseCard(Entreprise entreprise) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #ccc;");
        card.setPrefSize(300, 200);

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.jpg")));
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(entreprise.getCompanyName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label emailLabel = new Label("Email: " + entreprise.getEmail());
        Label phoneLabel = new Label("Phone: " + entreprise.getPhone());
        Label fieldLabel = new Label("Field: " + entreprise.getField());

        card.getChildren().addAll(imageView, nameLabel, emailLabel, phoneLabel, fieldLabel);

        // 🆕 QR Code on click
        card.setOnMouseClicked(event -> {
            String content = "Company: " + entreprise.getCompanyName() +
                    "\nEmail: " + entreprise.getEmail() +
                    "\nPhone: " + entreprise.getPhone() +
                    "\nField: " + entreprise.getField();
            QRCodeGenerator.generateAndShowQRCode(content);
        });

        return card;
    }

    @FXML
    public void handlePreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            refreshView();
        }
    }

    @FXML
    public void handleNextPage() {
        if ((currentPage * itemsPerPage) < filteredEntreprises.size()) {
            currentPage++;
            refreshView();
        }
    }
}
