package org.example.staff_module.Enum;

public enum PartnerStatus {
    ACCEPT("ACCEPT"),
    REJECT("REJECT"),
    PENDING("PENDING");


    private final String description;

    PartnerStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
