package org.example.testdemozul.dao;

import org.example.testdemozul.model.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {

    private static final Logger log = LoggerFactory.getLogger(ContractDAO.class);

    public void creatNewContract(Contract contract) {
        String sql = "INSERT INTO contract (" +
                "number_contract, name, email_a, email_b, phone_a, phone_b, " +
                "staff_id, contract_type, contract_scope, start_date, end_date, " +
                "payment_method, status, file_data" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql.toString())) {
            ps.setString(1, contract.getNumberContract());
            ps.setString(2, contract.getName());
            ps.setString(3, contract.getEmailA());
            ps.setString(4, contract.getEmailB());
            ps.setString(5, contract.getPhoneA());
            ps.setString(6, contract.getPhoneB());
            ps.setInt(7, contract.getStaffID());
            ps.setString(8, contract.getContractType());
            ps.setString(9, contract.getContractScope());
            ps.setDate(10, new java.sql.Date(contract.getStartDate().getTime()));
            ps.setDate(11, new java.sql.Date(contract.getEndDate().getTime()));
            ps.setString(12, contract.getPaymentMethod());
            ps.setString(13, contract.getStatus());
            ps.setString(14, contract.getFile_data());

            int rows = ps.executeUpdate();
            System.out.println("Inserted rows: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateContract(Contract contract) {
        String sql = "UPDATE contract SET " +
                "number_contract = ?, name = ?, email_a = ?, email_b = ?, phone_a = ?, phone_b = ?, " +
                "staff_id = ?, contract_type = ?, contract_scope = ?, start_date = ?, end_date = ?, " +
                "payment_method = ?, status = ?, file_data = ? " +
                "WHERE id = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, contract.getNumberContract());
            ps.setString(2, contract.getName());
            ps.setString(3, contract.getEmailA());
            ps.setString(4, contract.getEmailB());
            ps.setString(5, contract.getPhoneA());
            ps.setString(6, contract.getPhoneB());
            ps.setInt(7, contract.getStaffID());
            ps.setString(8, contract.getContractType());
            ps.setString(9, contract.getContractScope());
            ps.setDate(10, new java.sql.Date(contract.getStartDate().getTime()));
            ps.setDate(11, new java.sql.Date(contract.getEndDate().getTime()));
            ps.setString(12, contract.getPaymentMethod());
            ps.setString(13, contract.getStatus());
            ps.setString(14, contract.getFile_data()); // Nếu là BLOB, dùng setBinaryStream
            ps.setInt(15, contract.getId()); // Điều kiện WHERE theo id

            int rows = ps.executeUpdate();
            System.out.println("Updated rows: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContract(Integer id) {
        String sql = "DELETE FROM contract WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println("Deleted rows: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contract> getAllContractWithFilter(String search, String status, String approver_role, String scope, String type) {
        StringBuilder sql = new StringBuilder(
                "SELECT c.* FROM contract c " +
                        "JOIN staff s ON c.staff_id = s.id " +
                        "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>(); // Dùng để lưu tham số cho PreparedStatement

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND number_contract LIKE ?");
            params.add("%" + search + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        if (approver_role != null && !approver_role.trim().isEmpty()) {
            sql.append(" AND s.role = ?");
            params.add(approver_role);
        }
        if (scope != null && !scope.trim().isEmpty()) {
            sql.append(" AND c.contract_scope = ?");
            params.add(scope);
        }

        if (type != null && !type.trim().isEmpty()) {
            sql.append(" AND c.contract_type = ?");
            params.add(type);
        }

        List<Contract> contracts = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Contract c = new Contract();
                c.setId(rs.getInt("id"));
                c.setNumberContract(rs.getString("number_contract"));
                c.setName(rs.getString("name"));
                c.setStatus(rs.getString("status"));
                c.setContractScope(rs.getString("contract_scope"));
                c.setEmailA(rs.getString("email_a"));
                c.setPhoneA(rs.getString("phone_a"));
                c.setFile_data(rs.getString("file_data"));
                c.setStaffID(rs.getInt("staff_id"));
                c.setContractType(rs.getString("contract_type"));
                c.setPaymentMethod(rs.getString("payment_method"));
                c.setEndDate(rs.getDate("end_date"));
                c.setEmailB(rs.getString("email_b"));
                c.setPhoneB(rs.getString("phone_b"));
                c.setStartDate(rs.getDate("start_date"));
                contracts.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contracts;
    }

    public Contract getContractById(Integer id) {

        String sql = "SELECT * FROM contract WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Contract c = new Contract();
                c.setId(rs.getInt("id"));
                c.setNumberContract(rs.getString("number_contract"));
                c.setName(rs.getString("name"));
                c.setStatus(rs.getString("status"));
                c.setContractScope(rs.getString("contract_scope"));
                c.setEmailA(rs.getString("email_a"));
                c.setPhoneA(rs.getString("phone_a"));
                c.setFile_data(rs.getString("file_data"));
                c.setStaffID(rs.getInt("staff_id"));
                c.setContractType(rs.getString("contract_type"));
                c.setPaymentMethod(rs.getString("payment_method"));
                c.setEndDate(rs.getDate("end_date"));
                c.setEmailB(rs.getString("email_b"));
                c.setPhoneB(rs.getString("phone_b"));
                c.setStartDate(rs.getDate("start_date"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ContractDAO contractDAO = new ContractDAO();
        List<Contract> contracts = contractDAO.getAllContractWithFilter(null, null, null, null, null);
        for (Contract contract : contracts) {
            System.out.println(contract.toString());
        }
    }

}
