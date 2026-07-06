package controllers;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import models.Contract;
import models.Entreprise;
import models.User;
import services.ContractService;
import services.EntrepriseService;
import services.impl.ContractServiceImpl;
import services.impl.EntrepriseServiceImpl;
import utils.SessionManager;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ContractController {

    @FXML
    private FlowPane cardContainer;
    @FXML
    private VBox contractForm;
    @FXML
    private TextField companyNameField, receiverNameField, searchField;
    @FXML
    private TextArea contentField;
    @FXML
    private ComboBox<String> sortComboBox;

    private final ContractService contractService = new ContractServiceImpl();
    private final EntrepriseService entrepriseService = new EntrepriseServiceImpl();
    private Contract selectedContract = null;
    private List<Contract> allContracts;

    // Pagination
    private static final int ITEMS_PER_PAGE = 5;
    private int currentPage = 0;

    @FXML
    public void initialize() {
        try {
            loadContracts();
            setupSortOptions();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load contracts.");
        }

        contractForm.setVisible(false);
    }

    private void setupSortOptions() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Sender Name A-Z", "Sender Name Z-A"
        );
        sortComboBox.setItems(options);
        sortComboBox.setOnAction(e -> applySearchSortPagination());
    }

    private void loadContracts() throws SQLException {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) return;

        Entreprise currentEntreprise = entrepriseService.getEntrepriseByEmail(currentUser.getEmail());
        if (currentEntreprise == null) return;

        allContracts = contractService.getContractsByEntreprise(currentEntreprise);
        applySearchSortPagination();
    }

    private void applySearchSortPagination() {
        List<Contract> filtered = allContracts;

        String searchText = searchField.getText() != null ? searchField.getText().toLowerCase() : "";

        if (!searchText.isEmpty()) {
            filtered = filtered.stream()
                    .filter(c -> c.getContent().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
        }

        String selectedSort = sortComboBox.getSelectionModel().getSelectedItem();
        if (selectedSort != null) {
            if (selectedSort.equals("Sender Name A-Z")) {
                filtered = filtered.stream()
                        .sorted(Comparator.comparing(c -> c.getEntreprise().getId()))
                        .collect(Collectors.toList());
            } else if (selectedSort.equals("Sender Name Z-A")) {
                filtered = filtered.stream()
                        .sorted(Comparator.comparing((Contract c) -> c.getEntreprise().getId()).reversed())
                        .collect(Collectors.toList());
            }
        }

        displayContractsPaginated(filtered);
    }

    private void displayContractsPaginated(List<Contract> filteredContracts) {
        cardContainer.getChildren().clear();
        int fromIndex = currentPage * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, filteredContracts.size());

        if (fromIndex >= filteredContracts.size()) {
            currentPage = 0;
            fromIndex = 0;
            toIndex = Math.min(ITEMS_PER_PAGE, filteredContracts.size());
        }

        List<Contract> currentContracts = filteredContracts.subList(fromIndex, toIndex);

        for (Contract contract : currentContracts) {
            VBox card = createContractCard(contract);
            cardContainer.getChildren().add(card);
        }
    }

    private VBox createContractCard(Contract contract) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10));
        card.setPrefWidth(300);
        card.setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-radius: 8;");

        Label sender = new Label("Sender ID: " + contract.getEntreprise().getId());
        Label receiver = new Label("Receiver ID: " + contract.getReceiver().getId());
        Label content = new Label("Content: " + contract.getContent());

        Button editBtn = new Button("✏️ Edit");
        editBtn.setOnAction(e -> handleEditContract(contract));

        Button deleteBtn = new Button("🗑️ Delete");
        deleteBtn.setOnAction(e -> handleDeleteContract(contract));

        Button pdfBtn = new Button("📄 Generate PDF");
        pdfBtn.setOnAction(e -> generateContractPDF(contract));

        VBox buttons = new VBox(5, editBtn, deleteBtn, pdfBtn);
        buttons.setAlignment(Pos.CENTER);

        card.getChildren().addAll(sender, receiver, content, buttons);
        return card;
    }

    private void generateContractPDF(Contract contract) {
        try {
            // Create document and page
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(100, 750);
            contentStream.showText("CONTRACT DETAILS");
            contentStream.endText();

            // Draw horizontal line under title
            contentStream.setLineWidth(1f);
            contentStream.moveTo(100, 740);
            contentStream.lineTo(500, 740);
            contentStream.stroke();

            // Column headers
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 710);
            contentStream.showText("Contract ID");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Sender");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Receiver");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Content");
            contentStream.endText();

            // Contract values
            float startY = 690;
            float currentY = startY;

            // First three columns (single line)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, currentY);
            contentStream.showText(String.valueOf(contract.getId()));
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(String.valueOf(contract.getEntreprise().getId()));
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(String.valueOf(contract.getReceiver().getId()));
            contentStream.endText();

            // Content column with word wrap
            String content = contract.getContent();
            float contentX = 400; // X position for content column
            float maxWidth = 150;  // Max width for content column

            String[] words = content.split(" ");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                if (line.length() == 0) {
                    line.append(word);
                } else {
                    String testLine = line + " " + word;
                    float testWidth = PDType1Font.HELVETICA.getStringWidth(testLine) / 1000 * 12;

                    if (testWidth < maxWidth) {
                        line.append(" ").append(word);
                    } else {
                        // Draw current line
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.newLineAtOffset(contentX, currentY);
                        contentStream.showText(line.toString());
                        contentStream.endText();

                        // Move to next line
                        currentY -= 15;
                        line = new StringBuilder(word);
                    }
                }
            }

            // Draw remaining text
            if (line.length() > 0) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(contentX, currentY);
                contentStream.showText(line.toString());
                contentStream.endText();
            }

            contentStream.close();

            // Save document
            String filename = "Contract_" + contract.getId() + ".pdf";
            document.save(filename);
            document.close();

            showAlert("Success", "PDF generated: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDF: " + e.getMessage());
        }
    }



    @FXML
    public void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            applySearchSortPagination();
        }
    }

    @FXML
    public void handleNextPage() {
        int maxPage = (int) Math.ceil((double) allContracts.size() / ITEMS_PER_PAGE) - 1;
        if (currentPage < maxPage) {
            currentPage++;
            applySearchSortPagination();
        }
    }

    @FXML
    public void handleSearch() {
        applySearchSortPagination();
    }

    @FXML
    public void handleAddContract() {
        companyNameField.clear();
        receiverNameField.clear();
        contentField.clear();
        contractForm.setVisible(true);
        selectedContract = null;
    }

    @FXML
    public void handleSaveContract() {
        try {
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser == null) return;

            Entreprise currentEntreprise = entrepriseService.getEntrepriseByEmail(currentUser.getEmail());
            if (currentEntreprise == null) return;

            if (selectedContract == null) {
                Contract newContract = new Contract();
                newContract.setEntreprise(currentEntreprise);
                Entreprise receiver = entrepriseService.getEntrepriseByEmail(receiverNameField.getText());
                if (receiver == null) {
                    showAlert("Error", "Receiver not found!");
                    return;
                }
                newContract.setReceiver(receiver);
                newContract.setContent(contentField.getText());

                contractService.addContract(newContract);
            } else {
                selectedContract.setContent(contentField.getText());
                contractService.updateContract(selectedContract);
            }

            loadContracts();
            contractForm.setVisible(false);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save contract.");
        }
    }

    @FXML
    public void handleCancel() {
        contractForm.setVisible(false);
    }

    private void handleEditContract(Contract contract) {
        companyNameField.setText(String.valueOf(contract.getEntreprise().getId()));
        receiverNameField.setText(String.valueOf(contract.getReceiver().getId()));
        contentField.setText(contract.getContent());
        selectedContract = contract;
        contractForm.setVisible(true);
    }

    private void handleDeleteContract(Contract contract) {
        try {
            contractService.deleteContract(contract.getId());
            loadContracts();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete contract.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
