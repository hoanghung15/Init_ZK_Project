package org.example.testdemozul.dao;

import org.example.testdemozul.model.Task;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AssignTaskDAO {

    /** Thêm task mới vào DB */
    public void creatNewAssignTask(Task task) {
        String sql = "INSERT INTO task (type, department, description, startDate, endDate, updatedAt, status, creatBy_id, staff_id, contract_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getType());
            ps.setString(2, task.getDepartment());
            ps.setString(3, task.getDescription());

            // Convert java.util.Date -> java.sql.Date / Timestamp
            ps.setDate(4, new Date(task.getStartDate().getTime()));        // startDate DATE
            ps.setDate(5, new Date(task.getEndDate().getTime()));          // endDate DATE
            ps.setTimestamp(6, new Timestamp(task.getUpdateAt().getTime())); // updateAt DATETIME/TIMESTAMP

            ps.setString(7, task.getStatus());
            ps.setInt(8, task.getCreatBy_id());
            ps.setInt(9, task.getStaff_id());
            ps.setInt(10, task.getContract_id());

            int rows = ps.executeUpdate();
            System.out.println("Insert rows: " + rows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Lấy tất cả Task */
    public List<Task> getAllAssignTask() {
        String sql = "SELECT * FROM task";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setContract_id(rs.getInt("contract_id"));
                task.setType(rs.getString("type"));
                task.setDepartment(rs.getString("department"));
                task.setDescription(rs.getString("description"));

                // Lấy DATE và DATETIME từ DB
                task.setStartDate(rs.getDate("startDate"));
                task.setEndDate(rs.getDate("endDate"));
                task.setUpdateAt(rs.getTimestamp("updatedAt"));

                task.setStatus(rs.getString("status"));
                task.setCreatBy_id(rs.getInt("creatBy_id"));
                task.setStaff_id(rs.getInt("staff_id"));

                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }
}
