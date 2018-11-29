package com.hyperpakhsh.sadeq.bazaartracker.Order;


public class FactorItem {
    private int userId,SaleMaliId,CustomerId,total, DiscountAmount,Charge,id;
    private String date,RegTime,Des,customer;
    private int sentStatus,mysqlId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }

    public void setMysqlId(int mysqlId) {
        this.mysqlId = mysqlId;
    }

    public int getMysqlId() {
        return mysqlId;
    }

    public void setSentStatus(int sentStatus) {
        this.sentStatus = sentStatus;
    }

    public int getSentStatus() {
        return sentStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setCharge(int charge) {
        Charge = charge;
    }

    public int getCharge() {
        return Charge;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSaleMaliId() {
        return SaleMaliId;
    }

    public void setSaleMaliId(int saleMaliId) {
        SaleMaliId = saleMaliId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRegTime() {
        return RegTime;
    }

    public void setRegTime(String regTime) {
        RegTime = regTime;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }
}
