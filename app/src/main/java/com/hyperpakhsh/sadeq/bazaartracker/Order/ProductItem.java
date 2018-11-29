package com.hyperpakhsh.sadeq.bazaartracker.Order;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductItem {

    @SerializedName("id")
    @Expose
    private String localId;
    @SerializedName("softwareID")
    @Expose
    private String softwareID;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("windows")
    @Expose
    private int windows;

    private int quantity;

    private int SqliteId;

    public void setSqliteId(int SqliteId) {
        this.SqliteId = SqliteId;
    }

    public int getSqliteId() {
        return SqliteId;
    }

    public void setWindows(int windows) {
        this.windows = windows;
    }

    public int getWindows() {
        return windows;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
