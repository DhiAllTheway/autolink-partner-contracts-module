package services;

import models.Entreprise;
import models.User;

import java.sql.SQLException;
import java.util.List;

public interface EntrepriseService {

    void addEntreprise(Entreprise entreprise) throws SQLException;

    void updateEntreprise(Entreprise entreprise) throws SQLException;

    void deleteEntreprise(int id) throws SQLException;

    Entreprise getEntrepriseById(int id) throws SQLException;

    List<Entreprise> getAllEntreprises() throws SQLException;

    Entreprise authenticate(String email, String password) throws SQLException;

    String getRoleName(int roleId) throws SQLException;

    List<User> getUserEntreprises() throws SQLException;

    Entreprise getEntrepriseByEmail(String email) throws SQLException;

    /**
     * Get all entreprises that belong to the given user.
     * Used for selecting entreprise while adding a partnership request.
     */
    List<Entreprise> getEntreprisesByUser(int userId) throws SQLException;
}
