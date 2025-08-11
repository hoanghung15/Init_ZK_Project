package org.example.testdemozul.dao;

import org.example.testdemozul.model.Tax;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaxDAO {
    public void createNewTax(Tax tax) {
        String sql = "insert into tax(contract_id, partner_id, type, start_date, end_date) values(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tax.getContract_id());
            ps.setInt(2, tax.getPartner_id());
            ps.setString(3, tax.getType());
            ps.setDate(4, new java.sql.Date(tax.getStartDate().getTime()));
            ps.setDate(5, new java.sql.Date(tax.getEndDate().getTime()));

            int rows = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Tax> getAllTaxes() {
        String sql = "select * from tax";
        List<Tax> taxes = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tax tax = new Tax();
                tax.setId(rs.getInt("id"));
                tax.setContract_id(rs.getInt("contract_id"));
                tax.setPartner_id(rs.getInt("partner_id"));
                tax.setType(rs.getString("type"));
                tax.setStartDate(rs.getDate("start_date"));
                tax.setEndDate(rs.getDate("end_date"));
                taxes.add(tax);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return taxes;
    }

    public static void main(String[] args) {
        TaxDAO dao = new TaxDAO();
        for (Tax tax : dao.getAllTaxes()) {
            System.out.println(tax.toString());
        }
    }
}
