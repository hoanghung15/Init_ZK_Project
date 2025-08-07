package org.example.testdemozul.model;

import java.util.Date;

public class AssignedTask {
    int id;
    int staff_id;
    int user_id;
    int task_id;
    String description;
    Date assignDate;
    String status;

    public AssignedTask() {
    }

    public AssignedTask(int id, int staff_id, int user_id, int task_id, String description, Date assignDate, String status) {
        this.id = id;
        this.staff_id = staff_id;
        this.user_id = user_id;
        this.task_id = task_id;
        this.description = description;
        this.assignDate = assignDate;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    @Override
    public String toString() {
        return "AssignedTask{" +
                "id=" + id +
                ", staff_id=" + staff_id +
                ", user_id=" + user_id +
                ", task_id=" + task_id +
                ", description='" + description + '\'' +
                ", assignDate=" + assignDate +
                ", status='" + status + '\'' +
                '}';
    }
}
