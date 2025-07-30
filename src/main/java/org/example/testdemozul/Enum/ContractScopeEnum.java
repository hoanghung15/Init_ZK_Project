package org.example.testdemozul.Enum;

public enum ContractScopeEnum {
    INTERNAL("Nội bộ"), EXTERNAL("Đối tác ngoài");
    private final String displayName;

    ContractScopeEnum(String displayName) {
        this.displayName = displayName;

    }

    public String getDisplayName() {
        return displayName;
    }
}
