package org.example.testdemozul.model;

import com.google.api.client.util.DateTime;

public class Partner {
    private int id;
    private String partner_id;
    private String mst;
    private String name;
    private String address;
    private String status;
    private DateTime timestamp;
    private String description;

    public Partner() {
    }

    public Partner(int id, String partner_id, String mst, String name, String address, String status, DateTime timestamp,  String description) {
        this.id = id;
        this.partner_id = partner_id;
        this.mst = mst;
        this.name = name;
        this.address = address;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getMst() {
        return mst;
    }

    public void setMst(String mst) {
        this.mst = mst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "id=" + id +
                ", partner_id='" + partner_id + '\'' +
                ", mst='" + mst + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                '}';
    }
}
