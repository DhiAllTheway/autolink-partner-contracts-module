package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Entreprise;
import models.PartnershipRequest;
import services.PartnershipRequestService;
import services.impl.PartnershipRequestServiceImpl;
import utils.SessionManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PartnershipRequestsController {

    @FXML
    private Button addRequestBtn;

    @FXML
    private FlowPane cardContainer;

    private final PartnershipRequestService service = new PartnershipRequestServiceImpl();
    private final ObservableList<PartnershipRequest> requestList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadCards();
        addRequestBtn.setOnAction(e -> showAddDialog());
    }

    private void loadCards() {
        cardContainer.getChildren().clear();

        try {
            List<PartnershipRequest> requests = service.getAllRequests();
            requestList.setAll(requests);
            for (PartnershipRequest req : requestList) {
                VBox card = createCard(req);
                cardContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load partnership requests.");
        }
    }

    private VBox createCard(PartnershipRequest req) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setPrefWidth(300);
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-background-radius: 10; -fx-border-radius: 10;");
        card.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("📩 " + req.getCompanyEmail());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label phone = new Label("📞 " + req.getPhone());
        Label address = new Label("🏠 " + req.getAddress());
        Label tax = new Label("💼 Tax: " + req.getTaxCode());
        Label type = new Label("🤝 Type: " + req.getPartnershipType());
        Label status = new Label("📌 Status: " + req.getStatus());
        Label submitted = new Label("⏰ " + req.getSubmittedAt().toString());
        Label desc = new Label("📝 " + req.getDescription());
        desc.setWrapText(true);

        // Buttons
        HBox actions = new HBox(10);
        Button editBtn = new Button("✏️ Edit");
        Button deleteBtn = new Button("🗑 Delete");

        editBtn.setOnAction(e -> showEditDialog(req));
        deleteBtn.setOnAction(e -> {
            try {
                service.deletePartnershipRequest(req.getIdRequest());
                loadCards();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to delete request.");
            }
        });

        actions.getChildren().addAll(editBtn, deleteBtn);
        card.getChildren().addAll(title, phone, address, tax, type, status, submitted, desc, actions);
        return card;
    }
    private void showEditDialog(PartnershipRequest request) {
        Dialog<PartnershipRequest> dialog = new Dialog<>();
        dialog.setTitle("Edit Partnership Request");
        dialog.setHeaderText("Update the fields below:");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField emailField = new TextField(request.getCompanyEmail());
        TextField phoneField = new TextField(request.getPhone());
        TextField addressField = new TextField(request.getAddress());
        TextField taxCodeField = new TextField(request.getTaxCode());
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Sponsoring", "Workshops", "Recycling");
        typeBox.setValue(request.getPartnershipType());

        TextArea descArea = new TextArea(request.getDescription());
        descArea.setPrefRowCount(3);
        descArea.setWrapText(true);

        grid.add(new Label("Company Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Address:"), 0, 2);
        grid.add(addressField, 1, 2);
        grid.add(new Label("Tax Code:"), 0, 3);
        grid.add(taxCodeField, 1, 3);
        grid.add(new Label("Partnership Type:"), 0, 4);
        grid.add(typeBox, 1, 4);
        grid.add(new Label("Description:"), 0, 5);
        grid.add(descArea, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Basic validation
                if (emailField.getText().isBlank() || phoneField.getText().isBlank()) {
                    showAlert("Validation Error", "Please fill in all required fields.");
                    return null;
                }

                // Update the request object
                request.setCompanyEmail(emailField.getText());
                request.setPhone(phoneField.getText());
                request.setAddress(addressField.getText());
                request.setTaxCode(taxCodeField.getText());
                request.setPartnershipType(typeBox.getValue());
                request.setDescription(descArea.getText());

                return request;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedRequest -> {
            try {
                service.updatePartnershipRequest(updatedRequest);
                loadCards(); // ✅ Refresh to see changes
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update partnership request.");
            }
        });
    }




    private void showAddDialog() {
        Dialog<PartnershipRequest> dialog = new Dialog<>();
        dialog.setTitle("Add Partnership Request");
        dialog.setHeaderText("Fill in the partnership request details");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // 1. Entreprise dropdown from current user
        ComboBox<Entreprise> entrepriseBox = new ComboBox<>();
        entrepriseBox.setPromptText("Select Entreprise");

        try {
            List<Entreprise> entreprises = SessionManager.getEntreprisesForCurrentUser();
            entrepriseBox.getItems().addAll(entreprises);

            entrepriseBox.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Entreprise item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getEmail());
                }
            });

            entrepriseBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Entreprise item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getEmail());
                }
            });

        } catch (Exception e) {
            showAlert("Error", "Could not load entreprises.");
            return;
        }

        // 2. Input fields
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        TextField taxCodeField = new TextField();

        // ❗ ComboBox for Partnership Type
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Sponsoring", "Workshops", "Recycling");
        typeBox.setPromptText("Select Partnership Type");

        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setWrapText(true);
        descField.setPrefRowCount(3);

        // 3. Layout
        grid.add(new Label("Entreprise:"), 0, 0);
        grid.add(entrepriseBox, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Tax Code:"), 0, 4);
        grid.add(taxCodeField, 1, 4);
        grid.add(new Label("Partnership Type:"), 0, 5);
        grid.add(typeBox, 1, 5);
        grid.add(new Label("Description:"), 0, 6);
        grid.add(descField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // 4. Handle Submit
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return new PartnershipRequest(
                        0,
                        entrepriseBox.getValue(),
                        "Pending",
                        emailField.getText(),
                        phoneField.getText(),
                        addressField.getText(),
                        taxCodeField.getText(),
                        typeBox.getValue(),
                        descField.getText(),
                        LocalDateTime.now()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(request -> {
            try {
                service.addRequest(request);
                loadCards();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Could not save the request.");
            }
        });
    }




    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
