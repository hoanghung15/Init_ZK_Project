package org.example.testdemozul.Enum;

public enum StaffRoleEnum {
    MANAGER("Trưởng phòng"),
    SALES_STAFF("Nhân viên");

    private final String displayName;

    StaffRoleEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
