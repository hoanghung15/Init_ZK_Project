package org.example.testdemozul.model;

import java.util.Date;

public class Contract {
    private int id;

    private String numberContract;
    private String name;
    private String emailA;
    private String emailB;
    private String phoneA;
    private String phoneB;
    private String status;
    private String file_data;

    private int staffID;

    private String contractType;
    private String contractScope;

    private Date startDate;
    private Date endDate;

    private String paymentMethod;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Contract(int id, String numberContract, String name, String emailA, String emailB, String phoneA, String phoneB, String status, int staffID, String contractType, String contractScope, Date startDate, Date endDate, String paymentMethod,String file_data) {
        this.id = id;
        this.numberContract = numberContract;
        this.name = name;
        this.emailA = emailA;
        this.emailB = emailB;
        this.phoneA = phoneA;
        this.phoneB = phoneB;
        this.status = status;
        this.staffID = staffID;
        this.contractType = contractType;
        this.contractScope = contractScope;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentMethod = paymentMethod;
        this.file_data = file_data;
    }

    public String getFile_data() {
        return file_data;
    }

    public void setFile_data(String file_data) {
        this.file_data = file_data;
    }

    public Contract() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberContract() {
        return numberContract;
    }

    public void setNumberContract(String numberContract) {
        this.numberContract = numberContract;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailA() {
        return emailA;
    }

    public void setEmailA(String emailA) {
        this.emailA = emailA;
    }

    public String getEmailB() {
        return emailB;
    }

    public void setEmailB(String emailB) {
        this.emailB = emailB;
    }

    public String getPhoneA() {
        return phoneA;
    }

    public void setPhoneA(String phoneA) {
        this.phoneA = phoneA;
    }

    public String getPhoneB() {
        return phoneB;
    }

    public void setPhoneB(String phoneB) {
        this.phoneB = phoneB;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractScope() {
        return contractScope;
    }

    public void setContractScope(String contractScope) {
        this.contractScope = contractScope;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
