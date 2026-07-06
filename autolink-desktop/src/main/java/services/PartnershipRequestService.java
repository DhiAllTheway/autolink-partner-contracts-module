package services;

import models.PartnershipRequest;

import java.sql.SQLException;
import java.util.List;

public interface PartnershipRequestService {
    void addRequest(PartnershipRequest request) throws SQLException;
    List<PartnershipRequest> getAllRequests() throws SQLException;

    // Optional future functionality
    void updatePartnershipRequest(PartnershipRequest request);
    void deletePartnershipRequest(int id);
    PartnershipRequest getPartnershipRequestById(int id);
    List<PartnershipRequest> getAllPartnershipRequests();
}
