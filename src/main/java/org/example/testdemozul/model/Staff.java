package org.example.testdemozul.model;

import org.example.testdemozul.Enum.StaffRoleEnum;

public class Staff {
    private int id;
    private String name;
    private StaffRoleEnum role;
    private String department;

    public Staff(int id, String name, StaffRoleEnum role, String department) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.department = department;
    }

    public Staff() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StaffRoleEnum getRole() {
        return role;
    }

    public void setRole(StaffRoleEnum role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
