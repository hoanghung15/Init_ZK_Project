package org.example.testdemozul.dao;

import org.example.testdemozul.model.Contract;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {
    public List<Contract> getAllContractsWithScopeAndStatus(String scope, String status) {
        List<Contract> contracts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM contract WHERE 1=1");

        // Xây dựng câu SQL động
        if (scope != null && !scope.isEmpty()) {
            sql.append(" AND contract_scope = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (scope != null && !scope.isEmpty()) {
                ps.setString(paramIndex++, scope);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Contract contract = new Contract();
                    contract.setId(resultSet.getInt("id"));
                    contract.setNumberContract(resultSet.getString("number_contract"));
                    contract.setName(resultSet.getString("name"));
                    contract.setEmailA(resultSet.getString("email_a"));
                    contract.setEmailB(resultSet.getString("email_b"));
                    contract.setPhoneA(resultSet.getString("phone_a"));
                    contract.setPhoneB(resultSet.getString("phone_b"));
                    contract.setStaffID(resultSet.getInt("staff_id")); // cột này trong DB là INT nhưng bạn dùng String

                    contract.setContractType(resultSet.getString("contract_type"));
                    contract.setContractScope(resultSet.getString("contract_scope"));

                    contract.setStartDate(resultSet.getDate("start_date")); // java.sql.Date kế thừa java.util.Date
                    contract.setEndDate(resultSet.getDate("end_date"));

                    contract.setPaymentMethod(resultSet.getString("payment_method"));
                    contract.setStatus(resultSet.getString("status"));


                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contracts;
    }

    public List<Contract> getAllContractsWithRoleScopeStatus(String role, String scope, String status) {
        List<Contract> contracts = new ArrayList<>();

        // Xây dựng câu SQL động
        StringBuilder sql = new StringBuilder(
                "SELECT c.* FROM contract c JOIN staff s ON c.staff_id = s.id WHERE 1=1"
        );

        if (role != null && !role.isEmpty()) {
            sql.append(" AND s.role = ?");
        }
        if (scope != null && !scope.isEmpty()) {
            sql.append(" AND c.contract_scope = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND c.status = ?");
        }

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (role != null && !role.isEmpty()) {
                ps.setString(paramIndex++, role);
            }
            if (scope != null && !scope.isEmpty()) {
                ps.setString(paramIndex++, scope);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Contract contract = new Contract();
                    contract.setId(resultSet.getInt("id"));
                    contract.setNumberContract(resultSet.getString("number_contract"));
                    contract.setName(resultSet.getString("name"));
                    contract.setEmailA(resultSet.getString("email_a"));
                    contract.setEmailB(resultSet.getString("email_b"));
                    contract.setPhoneA(resultSet.getString("phone_a"));
                    contract.setPhoneB(resultSet.getString("phone_b"));
                    contract.setStaffID(resultSet.getInt("staff_id"));
                    contract.setContractType(resultSet.getString("contract_type"));
                    contract.setContractScope(resultSet.getString("contract_scope"));
                    contract.setStartDate(resultSet.getDate("start_date"));
                    contract.setEndDate(resultSet.getDate("end_date"));
                    contract.setPaymentMethod(resultSet.getString("payment_method"));
                    contract.setStatus(resultSet.getString("status"));

                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contracts;
    }

    public static void main(String[] args) {
        ContractDAO contractDAO = new ContractDAO();
        System.out.println(contractDAO.getAllContractsWithScopeAndStatus(null, "DONE").size());
        System.out.println(contractDAO.getAllContractsWithRoleScopeStatus("Trưởng phòng", "Nội bộ", "DONE").size());
    }

}
