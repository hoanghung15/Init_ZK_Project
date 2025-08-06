package org.example.testdemozul.dao;

import org.example.testdemozul.model.AssignedTask;
import org.example.testdemozul.model.Task;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AssignTaskDAO {

    /**
     * Thêm task mới vào DB
     */
    public void creatNewTask(Task task) {
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

    /**
     * Update task trong DB
     */
    public void updateTask(Task task) {
        String sql = "UPDATE task SET type = ?, department = ?, description = ?, startDate = ?, endDate = ?, " +
                "updatedAt = ?, status = ?, creatBy_id = ?, staff_id = ?, contract_id = ? " +
                "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getType());
            ps.setString(2, task.getDepartment());
            ps.setString(3, task.getDescription());

            // Convert java.util.Date -> java.sql.Date / Timestamp
            ps.setDate(4, new java.sql.Date(task.getStartDate().getTime()));        // startDate DATE
            ps.setDate(5, new java.sql.Date(task.getEndDate().getTime()));          // endDate DATE
            ps.setTimestamp(6, new java.sql.Timestamp(task.getUpdateAt().getTime())); // updateAt DATETIME/TIMESTAMP

            ps.setString(7, task.getStatus());
            ps.setInt(8, task.getCreatBy_id());
            ps.setInt(9, task.getStaff_id());
            ps.setInt(10, task.getContract_id());
            ps.setInt(11, task.getId());

            int rows = ps.executeUpdate();
            System.out.println("Update rows: " + rows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAssign(AssignedTask assignedTask) {
        String sql = "UPDATE assign_task SET staff_id = ?, task_id = ?, user_id = ?, assign_date = ?, description = ? " +
                "WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignedTask.getStaff_id());
            ps.setInt(2, assignedTask.getTask_id());
            ps.setInt(3, assignedTask.getUser_id());
            ps.setDate(4, new java.sql.Date(assignedTask.getAssignDate().getTime()));
            ps.setString(5, assignedTask.getDescription());
            ps.setInt(6, assignedTask.getId());
            int rows = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy tất cả Task
     */
    public List<Task> getAllTaskWithFilter(Integer filter) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task";

        if (filter != null) {
            if (filter == -1) {
                // -1 Chưa giao cho NV
                sql += " WHERE staff_id = 0";
            } else if (filter == 1) {
                // Đã giao cho NV
                sql += " WHERE staff_id > 0";
            }

        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
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

    /**
     * Delete task
     */
    public void deleteTask(Integer id) {
        String sql = "DELETE FROM task WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rows = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * delete assign
     */
    public void deleteAssign(Integer id) {
        String sql = "DELETE FROM assign_task WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get task by ID
     */
    public Task getTaskInfoById(Integer id) {
        Task task = new Task();
        String sql = "SELECT * FROM task WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                task.setId(rs.getInt("id"));
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return task;
    }

    /**
     * create assign
     */
    public void createNewAssign(AssignedTask assignedTask) {
        String sql = "INSERT INTO assign_task(staff_id,task_id,user_id,assign_date,description) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, assignedTask.getStaff_id());
            ps.setInt(2, assignedTask.getTask_id());
            ps.setInt(3, assignedTask.getUser_id());
            ps.setDate(4, new Date(assignedTask.getAssignDate().getTime()));
            ps.setString(5, assignedTask.getDescription());
            int rows = ps.executeUpdate();
            System.out.println("rows: " + rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all assign
     */
    public List<AssignedTask> getAllAssigned() {
        List<AssignedTask> assignedTasks = new ArrayList<>();
        String sql = "SELECT * FROM assign_task";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AssignedTask assignedTask = new AssignedTask();
                assignedTask.setId(rs.getInt("id"));
                assignedTask.setStaff_id(rs.getInt("staff_id"));
                assignedTask.setTask_id(rs.getInt("task_id"));
                assignedTask.setUser_id(rs.getInt("user_id"));
                assignedTask.setAssignDate(rs.getDate("assign_date"));
                assignedTask.setDescription(rs.getString("description"));
                assignedTasks.add(assignedTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assignedTasks;
    }

    /**
     * Get assign info by id
     */
    public AssignedTask getAssignedById(Integer id) {
        AssignedTask assignedTask = new AssignedTask();
        String sql = "SELECT * FROM assign_task WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                assignedTask.setId(rs.getInt("id"));
                assignedTask.setStaff_id(rs.getInt("staff_id"));
                assignedTask.setTask_id(rs.getInt("task_id"));
                assignedTask.setUser_id(rs.getInt("user_id"));
                assignedTask.setAssignDate(rs.getDate("assign_date"));
                assignedTask.setDescription(rs.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignedTask;
    }

    public static void main(String[] args) {
        AssignTaskDAO assignTaskDAO = new AssignTaskDAO();
        System.out.println(assignTaskDAO.getAssignedById(1).toString());
    }
}
