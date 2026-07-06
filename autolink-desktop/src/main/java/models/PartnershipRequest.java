package models;

import java.time.LocalDateTime;

public class PartnershipRequest {
    private int idRequest;
    private Entreprise entreprise;
    private String status;
    private String companyEmail;
    private String phone;
    private String address;
    private String taxCode;
    private String partnershipType;
    private String description;
    private LocalDateTime submittedAt;

    public PartnershipRequest() {}

    public PartnershipRequest(int idRequest, Entreprise entreprise, String status, String companyEmail,
                              String phone, String address, String taxCode, String partnershipType,
                              String description, LocalDateTime submittedAt) {
        this.idRequest = idRequest;
        this.entreprise = entreprise;
        this.status = status;
        this.companyEmail = companyEmail;
        this.phone = phone;
        this.address = address;
        this.taxCode = taxCode;
        this.partnershipType = partnershipType;
        this.description = description;
        this.submittedAt = submittedAt;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getPartnershipType() {
        return partnershipType;
    }

    public void setPartnershipType(String partnershipType) {
        this.partnershipType = partnershipType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
