package services.impl;

import models.Entreprise;
import models.User;
import services.EntrepriseService;
import utils.MyDatabase;
import org.mindrot.jbcrypt.BCrypt;
import utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntrepriseServiceImpl implements EntrepriseService {
    private final Connection connection;

    public EntrepriseServiceImpl() {
        this.connection = MyDatabase.getInstance().getMyConnection();
    }

    @Override
    public void addEntreprise(Entreprise entreprise) throws SQLException {
        String query = "INSERT INTO entreprise (role_id, company_name, email, phone, tax_code, created_at, supplier, password, field, address_street, address_city, address_postal_code, address_country) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entreprise.getRoleId());
            ps.setString(2, entreprise.getCompanyName());
            ps.setString(3, entreprise.getEmail());
            ps.setString(4, entreprise.getPhone());
            ps.setString(5, entreprise.getTaxCode());
            ps.setTimestamp(6, Timestamp.valueOf(entreprise.getCreatedAt()));
            ps.setBoolean(7, entreprise.getSupplier());
            ps.setString(8, entreprise.getPassword());
            ps.setString(9, entreprise.getField());
            ps.setString(10, entreprise.getAddressStreet());
            ps.setString(11, entreprise.getAddressCity());
            ps.setString(12, entreprise.getAddressPostalCode());
            ps.setString(13, entreprise.getAddressCountry());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entreprise.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public void updateEntreprise(Entreprise entreprise) throws SQLException {
        String query = "UPDATE entreprise SET company_name=?, email=?, phone=?, tax_code=?, supplier=?, field=?, address_street=?, address_city=?, address_postal_code=?, address_country=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, entreprise.getCompanyName());
            ps.setString(2, entreprise.getEmail());
            ps.setString(3, entreprise.getPhone());
            ps.setString(4, entreprise.getTaxCode());
            ps.setBoolean(5, entreprise.getSupplier());
            ps.setString(6, entreprise.getField());
            ps.setString(7, entreprise.getAddressStreet());
            ps.setString(8, entreprise.getAddressCity());
            ps.setString(9, entreprise.getAddressPostalCode());
            ps.setString(10, entreprise.getAddressCountry());
            ps.setInt(11, entreprise.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteEntreprise(int id) throws SQLException {
        String query = "DELETE FROM entreprise WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Entreprise getEntrepriseById(int id) throws SQLException {
        String query = "SELECT * FROM entreprise WHERE id=? AND role_id=3";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractEntrepriseFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public List<Entreprise> getAllEntreprises() throws SQLException {
        List<Entreprise> entreprises = new ArrayList<>();
        String query = "SELECT * FROM entreprise";

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                entreprises.add(extractEntrepriseFromResultSet(rs));
            }
        }
        return entreprises;
    }

    @Override
    public Entreprise authenticate(String email, String password) throws SQLException {
        String query = "SELECT * FROM entreprise WHERE email = ? AND supplier = 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (hashedPassword.startsWith("$2y$")) {
                    hashedPassword = "$2a$" + hashedPassword.substring(4);
                }

                if (BCrypt.checkpw(password, hashedPassword)) {
                    return extractEntrepriseFromResultSet(rs);
                }
            }
        }
        throw new IllegalArgumentException("Email or password incorrect, or account is not a supplier");
    }

    @Override
    public String getRoleName(int roleId) throws SQLException {
        String query = "SELECT name FROM role WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
            throw new IllegalArgumentException("Role not found");
        }
    }

    @Override
    public List<User> getUserEntreprises() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM USER WHERE role_id = 5";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        }
        return users;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setName(rs.getString("name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        return user;
    }


    @Override
    public Entreprise getEntrepriseByEmail(String email) throws SQLException {
        String query = "SELECT * FROM entreprise WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractEntrepriseFromResultSet(rs);
            }
        }
        return null;
    }
    @Override
    public List<Entreprise> getEntreprisesByUser(int userId) throws SQLException {
        List<Entreprise> list = new ArrayList<>();
        String userEmail = SessionManager.getCurrentUser().getEmail();

        String sql = "SELECT * FROM entreprise WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractEntrepriseFromResultSet(rs));
            }
        }
        return list;
    }





    private Entreprise extractEntrepriseFromResultSet(ResultSet rs) throws SQLException {
        Entreprise entreprise = new Entreprise();
        entreprise.setId(rs.getInt("id"));
        entreprise.setRoleId(rs.getInt("role_id"));
        entreprise.setCompanyName(rs.getString("company_name"));
        entreprise.setEmail(rs.getString("email"));
        entreprise.setPhone(rs.getString("phone"));
        entreprise.setTaxCode(rs.getString("tax_code"));
        entreprise.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        entreprise.setSupplier(rs.getBoolean("supplier"));
        entreprise.setPassword(rs.getString("password"));
        entreprise.setField(rs.getString("field"));
        entreprise.setAddressStreet(rs.getString("address_street"));
        entreprise.setAddressCity(rs.getString("address_city"));
        entreprise.setAddressPostalCode(rs.getString("address_postal_code"));
        entreprise.setAddressCountry(rs.getString("address_country"));
        return entreprise;
    }
}
