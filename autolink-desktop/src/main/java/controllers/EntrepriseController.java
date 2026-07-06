package controllers;

import models.Entreprise;
import services.EntrepriseService;
import services.impl.EntrepriseServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class EntrepriseController {
    private final EntrepriseService entrepriseService = new EntrepriseServiceImpl();

    public void createEntreprise(Entreprise entreprise) throws SQLException {
        entrepriseService.addEntreprise(entreprise);
    }

    public void updateEntreprise(Entreprise entreprise) throws SQLException {
        entrepriseService.updateEntreprise(entreprise);
    }

    public void deleteEntreprise(int id) throws SQLException {
        entrepriseService.deleteEntreprise(id);
    }

    public Entreprise getEntrepriseById(int id) throws SQLException {
        return entrepriseService.getEntrepriseById(id);
    }

    public List<Entreprise> getAllEntreprises() throws SQLException {
        return entrepriseService.getAllEntreprises();
    }
}
