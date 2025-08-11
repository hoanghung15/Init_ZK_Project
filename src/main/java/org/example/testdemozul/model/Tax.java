package org.example.testdemozul.model;

import java.util.Date;

public class Tax {
    private  int id;
    private Date startDate;
    private Date endDate;

    private String type;

    private int contract_id;
    private int partner_id;


    public Tax() {
    }

    public Tax(int id, Date startDate, Date endDate, String type, int contract_id, int partner_id) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.contract_id = contract_id;
        this.partner_id = partner_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    @Override
    public String toString() {
        return "Tax{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", type='" + type + '\'' +
                ", contract_id=" + contract_id +
                ", partner_id=" + partner_id +
                '}';
    }
}
