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
}
