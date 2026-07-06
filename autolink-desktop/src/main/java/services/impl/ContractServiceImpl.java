package services.impl;

import models.Contract;
import models.Entreprise;
import services.ContractService;
import utils.MyDatabase;
import utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractServiceImpl implements ContractService {
    private final Connection cnx = MyDatabase.getInstance().getMyConnection();

    @Override
    public void addContract(Contract c) throws SQLException {
        String sql = """
                INSERT INTO contract(entreprise_id, content, receiver_id)
                VALUES (?,?,?)
                """;
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, c.getEntreprise().getId());
            ps.setString(2, c.getContent());
            ps.setInt(3, c.getReceiver().getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateContract(Contract c) throws SQLException {
        String sql = """
                UPDATE contract
                SET entreprise_id = ?, content = ?, receiver_id = ?
                WHERE id = ?
                """;
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, c.getEntreprise().getId());
            ps.setString(2, c.getContent());
            ps.setInt(3, c.getReceiver().getId());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteContract(int id) throws SQLException {
        String sql = "DELETE FROM contract WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Contract getContractById(int id) throws SQLException {
        String sql = "SELECT * FROM contract WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    @Override
    public List<Contract> getAllContracts() throws SQLException {
        List<Contract> contracts = new ArrayList<>();

        Entreprise currentEntreprise = SessionManager.getCurrentEntreprise();
        if (currentEntreprise == null) {
            throw new SQLException("No entreprise is associated with the logged-in user.");
        }

        String sql = """
        SELECT c.id, 
               c.entreprise_id, 
               c.content,
               c.receiver_id
        FROM contract c
        WHERE c.entreprise_id = ? OR c.receiver_id = ?
        """;

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, currentEntreprise.getId());
            ps.setInt(2, currentEntreprise.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                contracts.add(map(rs));
            }
        }

        return contracts;
    }

    @Override
    public List<Contract> getContractsByEntreprise(Entreprise entreprise) throws SQLException {
        List<Contract> contracts = new ArrayList<>();

        String sql = """
        SELECT c.id, 
               c.entreprise_id, 
               c.content,
               c.receiver_id
        FROM contract c
        WHERE c.entreprise_id = ? OR c.receiver_id = ?
        """;

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, entreprise.getId());
            ps.setInt(2, entreprise.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                contracts.add(map(rs));
            }
        }

        return contracts;
    }

    /* --- HELPER: map result --- */
    private Contract map(ResultSet rs) throws SQLException {
        Contract c = new Contract();
        c.setId(rs.getInt("id"));
        c.setContent(rs.getString("content"));

        Entreprise sender = new Entreprise();
        sender.setId(rs.getInt("entreprise_id"));

        Entreprise receiver = new Entreprise();
        receiver.setId(rs.getInt("receiver_id"));

        c.setEntreprise(sender);
        c.setReceiver(receiver);

        return c;
    }
}
