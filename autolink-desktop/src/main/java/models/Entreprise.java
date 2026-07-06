package models;

import java.time.LocalDateTime;
import java.util.List;

public class Entreprise {
    private int id;
    private int roleId;
    private String companyName;
    private String email;
    private String phone;
    private String taxCode;
    private LocalDateTime createdAt;
    private boolean supplier;
    private String password;
    private String field;
    private String addressStreet;
    private String addressCity;
    private String addressPostalCode;
    private String addressCountry;

    // Relationships
    private List<Contract> contractsSent;
    private List<Contract> contractsReceived;
    private List<PartnershipRequest> partnershipRequests;

    // Constructors
    public Entreprise() {}

    public Entreprise(int id, int roleId, String companyName, String email, String phone, String taxCode,
                      LocalDateTime createdAt, boolean supplier, String password, String field,
                      String addressStreet, String addressCity, String addressPostalCode, String addressCountry) {
        this.id = id;
        this.roleId = roleId;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.taxCode = taxCode;
        this.createdAt = createdAt;
        this.supplier = supplier;
        this.password = password;
        this.field = field;
        this.addressStreet = addressStreet;
        this.addressCity = addressCity;
        this.addressPostalCode = addressPostalCode;
        this.addressCountry = addressCountry;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getSupplier() {
        return supplier;
    }

    public void setSupplier(boolean supplier) {
        this.supplier = supplier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public List<Contract> getContractsSent() {
        return contractsSent;
    }

    public void setContractsSent(List<Contract> contractsSent) {
        this.contractsSent = contractsSent;
    }

    public List<Contract> getContractsReceived() {
        return contractsReceived;
    }

    public void setContractsReceived(List<Contract> contractsReceived) {
        this.contractsReceived = contractsReceived;
    }

    public List<PartnershipRequest> getPartnershipRequests() {
        return partnershipRequests;
    }

    public void setPartnershipRequests(List<PartnershipRequest> partnershipRequests) {
        this.partnershipRequests = partnershipRequests;
    }

    @Override
    public String toString() {
        return companyName;      // shown in any ComboBox / ListView automatically
    }
}
