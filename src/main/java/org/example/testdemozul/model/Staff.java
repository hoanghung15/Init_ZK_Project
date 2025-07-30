package org.example.testdemozul.model;

import org.example.testdemozul.Enum.StaffRoleEnum;

public class Staff {
    private int id;
    private String name;
    private StaffRoleEnum role;

    public Staff(int id, String name, StaffRoleEnum role) {
        this.id = id;
        this.name = name;
        this.role = role;
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
}
