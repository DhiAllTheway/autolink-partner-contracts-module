package utils;

import models.User;
import models.Entreprise;
import services.EntrepriseService;
import services.impl.EntrepriseServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class SessionManager {
    private static User currentUser;
    private static Entreprise currentEntreprise;
    private static String currentUserType; // "USER" or "ENTREPRISE"

    public static List<Entreprise> getEntreprisesForCurrentUser() throws SQLException {
        // Replace this with a real call to EntrepriseService
        EntrepriseService entrepriseService = new EntrepriseServiceImpl();
        return entrepriseService.getEntreprisesByUser(getCurrentUser().getId());
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        currentEntreprise = null;
        currentUserType = "USER";
    }

    public static void setCurrentEntreprise(Entreprise entreprise) {
        currentEntreprise = entreprise;
        currentUser = null;
        currentUserType = "ENTREPRISE";
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Entreprise getCurrentEntreprise() {
        return currentEntreprise;
    }

    public static String getCurrentUserType() {
        return currentUserType;
    }

    public static void clearSession() {
        currentUser = null;
        currentEntreprise = null;
        currentUserType = null;
    }
}
