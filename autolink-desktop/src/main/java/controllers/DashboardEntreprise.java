package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import models.User;
import services.EntrepriseService;
import services.impl.EntrepriseServiceImpl;
import utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardEntreprise implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button blogBtn;

    @FXML
    private Button transportBtn;

    @FXML
    private Button transportBtn1;

    private final EntrepriseService entrepriseService = new EntrepriseServiceImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blogBtn.setOnAction(e -> showUserEntrepriseList());
        transportBtn.setOnAction(e -> showPartnershipRequests());
        transportBtn1.setOnAction(e -> loadContractsView());
    }

    private void showUserEntrepriseList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserEntrepriseListView.fxml"));
            Parent view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPartnershipRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PartnershipRequests.fxml"));
            Parent view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContractsView() {
        try {
            System.out.println("Loading ContractView.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ContractView.fxml"));
            Parent view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

