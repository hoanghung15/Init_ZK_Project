package org.example.testdemozul.dao;

import com.google.api.client.util.DateTime;
import org.example.testdemozul.model.Partner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartnerDAO {
    public void createNewPartner(Partner partner) {
        String sql = "insert  into partner(partner_id, mst, name, address, status, timestamp, description) values (?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, partner.getPartner_id());
            ps.setString(2, partner.getMst());
            ps.setString(3, partner.getName());
            ps.setString(4, partner.getAddress());
            ps.setString(5, partner.getStatus());
            ps.setTimestamp(6, new java.sql.Timestamp(partner.getTimestamp().getValue()));
            ps.setString(7, partner.getDescription());

            int rows = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Partner> getAllPartners() {
        List<Partner> partners = new ArrayList<>();
        String sql = "select * from partner";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Partner partner = new Partner();
                partner.setId(rs.getInt("id"));
                partner.setPartner_id(rs.getString("partner_id"));
                partner.setMst(rs.getString("mst"));
                partner.setName(rs.getString("name"));
                partner.setAddress(rs.getString("address"));
                partner.setStatus(rs.getString("status"));
                partner.setTimestamp(new DateTime(rs.getTimestamp("timestamp")));
                partner.setDescription(rs.getString("description"));

                partners.add(partner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return partners;
    }

    public void deletePartner(Integer id) {
        String sql = "delete from partner where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PartnerDAO partnerDAO = new PartnerDAO();
        System.out.println(partnerDAO.getAllPartners().size());
        ;
    }
}
