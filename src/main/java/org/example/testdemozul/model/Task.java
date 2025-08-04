package org.example.testdemozul.model;

import java.util.Date;

public class Task {
    private int id;
    private String type;
    private String department;
    private String description;

    private Date startDate;
    private Date endDate;
    private Date updateAt;

    private String status;

    private int creatBy_id;
    private int staff_id;
    private int contract_id;


    public Task() {
    }

    public Task(int id, String type, String department, String description, Date startDate, Date endDate, Date updateAt, String status, int creatBy_id, int staff_id, int contract_id) {
        this.id = id;
        this.type = type;
        this.department = department;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updateAt = updateAt;
        this.status = status;
        this.creatBy_id = creatBy_id;
        this.staff_id = staff_id;
        this.contract_id = contract_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreatBy_id() {
        return creatBy_id;
    }

    public void setCreatBy_id(int creatBy_id) {
        this.creatBy_id = creatBy_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", department='" + department + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", updateAt=" + updateAt +
                ", status='" + status + '\'' +
                ", creatBy_id=" + creatBy_id +
                ", staff_id=" + staff_id +
                ", contract_id=" + contract_id +
                '}';
    }
}
