package org.example.testdemozul.Enum;

public enum PaymentMethodEnum {
    CARD("Tiền mặt"),
    BANKING("Chuyển khoản"),
    DEBIT("Ghi nợ");

    private final String displayName;

    PaymentMethodEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
