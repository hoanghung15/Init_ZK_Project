package org.example.testdemozul.Enum;

public enum Status {
    DONE("Hoàn thành"),
    PENDING("Đang chờ")
    ;


    private final String displayName;
    Status(String displayName) {
        this.displayName = displayName;

    }
    public String getDisplayName() {
        return displayName;
    }
}
