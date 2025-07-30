package org.example.testdemozul.Enum;

public enum StaffRoleEnum {
    MANAGER("Trưởng phòng"),
    SALES_STAFF("Nhân viên kinh doanh");

    private final String displayName;

    StaffRoleEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
