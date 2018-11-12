package com.hyperpakhsh.sadeq.bazaartracker.Customers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerListItem {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("softwareId")
    @Expose
    private String softwareId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("windows")
    @Expose
    private int windows;

    public void setWindows(int windows) {
        this.windows = windows;
    }

    public int getWindows() {
        return windows;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(String softwareId) {
        this.softwareId = softwareId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
