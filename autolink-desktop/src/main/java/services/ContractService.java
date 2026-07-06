package services;

import models.Contract;
import models.Entreprise;
import java.sql.SQLException;
import java.util.List;

public interface ContractService {
    void addContract(Contract c) throws SQLException;
    void updateContract(Contract c) throws SQLException;
    void deleteContract(int id) throws SQLException;
    Contract getContractById(int id) throws SQLException;
    List<Contract> getAllContracts() throws SQLException;
    List<Contract> getContractsByEntreprise(Entreprise entreprise) throws SQLException;
}
