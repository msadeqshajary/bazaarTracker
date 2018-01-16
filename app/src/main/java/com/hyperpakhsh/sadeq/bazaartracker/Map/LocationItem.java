package com.hyperpakhsh.sadeq.bazaartracker.Map;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("shop")
    @Expose
    private String shop;
    @SerializedName("latlang")
    @Expose
    private String latlang;
    @SerializedName("location")
    @Expose
    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getLatlang() {
        return latlang;
    }

    public void setLatlang(String latlang) {
        this.latlang = latlang;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
