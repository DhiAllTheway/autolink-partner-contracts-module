package services.impl;

import models.Entreprise;
import models.PartnershipRequest;
import services.PartnershipRequestService;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartnershipRequestServiceImpl implements PartnershipRequestService {

    private final Connection connection;

    public PartnershipRequestServiceImpl() {
        connection = MyDatabase.getInstance().getMyConnection();
    }

    @Override
    public List<PartnershipRequest> getAllRequests() throws SQLException {
        List<PartnershipRequest> list = new ArrayList<>();

        String sql = "SELECT * FROM partnership_request";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PartnershipRequest req = new PartnershipRequest();
                req.setIdRequest(rs.getInt("id_request"));

                Entreprise ent = new Entreprise();
                ent.setId(rs.getInt("entreprise_id"));
                req.setEntreprise(ent);

                req.setCompanyEmail(rs.getString("company_email"));
                req.setPhone(rs.getString("phone"));
                req.setAddress(rs.getString("address"));
                req.setTaxCode(rs.getString("tax_code"));
                req.setPartnershipType(rs.getString("partnership_type"));
                req.setDescription(rs.getString("description"));
                req.setStatus(rs.getString("status"));
                req.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());

                list.add(req);
            }
        }

        return list;
    }

    @Override
    public void addRequest(PartnershipRequest request) throws SQLException {
        String sql = "INSERT INTO partnership_request (entreprise_id, company_email, phone, address, tax_code, partnership_type, description, status, submitted_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, request.getEntreprise().getId());
            ps.setString(2, request.getCompanyEmail());
            ps.setString(3, request.getPhone());
            ps.setString(4, request.getAddress());
            ps.setString(5, request.getTaxCode());
            ps.setString(6, request.getPartnershipType());
            ps.setString(7, request.getDescription());
            ps.setString(8, request.getStatus());
            ps.setTimestamp(9, Timestamp.valueOf(request.getSubmittedAt()));

            ps.executeUpdate();
        }
    }

    // Unused methods for now (for interface compatibility)
    @Override
    public void updatePartnershipRequest(PartnershipRequest request) {
        String sql = """
        UPDATE partnership_request 
        SET company_email = ?, phone = ?, address = ?, tax_code = ?, partnership_type = ?, description = ?
        WHERE id_request = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, request.getCompanyEmail());
            ps.setString(2, request.getPhone());
            ps.setString(3, request.getAddress());
            ps.setString(4, request.getTaxCode());
            ps.setString(5, request.getPartnershipType());
            ps.setString(6, request.getDescription());
            ps.setInt(7, request.getIdRequest());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update partnership request: " + e.getMessage());
        }
    }

    @Override
    public void deletePartnershipRequest(int id) {
        String sql = "DELETE FROM partnership_request WHERE id_request = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete partnership request: " + e.getMessage());
        }
    }

    @Override public PartnershipRequest getPartnershipRequestById(int id) { return null; }
    @Override public List<PartnershipRequest> getAllPartnershipRequests() { return null; }
}
