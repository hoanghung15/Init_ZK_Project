package org.example.testdemozul.Enum;

public enum ContractTypeEnum {
    TEST("Kiểm thử"), PRODUCTION("Phát triển");

    private final String displayName;

    ContractTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
