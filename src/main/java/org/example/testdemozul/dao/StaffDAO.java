package org.example.testdemozul.dao;

import org.example.testdemozul.model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "select * from staff";
        try (Statement statement = DBConnection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Staff staff = new Staff();
                staff.setId(resultSet.getInt("id"));
                staff.setName(resultSet.getString("name"));
                staffList.add(staff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public List<Staff> getAllStaffByDepartment(String department) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "select * from staff where department LIKE ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, department);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setId(rs.getInt("id"));
                staff.setName(rs.getString("name"));
                staffList.add(staff);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public Staff getStaffById(int id) {
        Staff staff = new Staff();

        String sql = "select * from staff where id = ?";

        try(PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                staff.setId(rs.getInt("id"));
                staff.setName(rs.getString("name"));
                staff.setDepartment(rs.getString("department"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return staff;
    }

    public static void main(String[] args) {
        StaffDAO staffDAO = new StaffDAO();
        System.out.println(staffDAO.getStaffById(2).toString());
    }

}
