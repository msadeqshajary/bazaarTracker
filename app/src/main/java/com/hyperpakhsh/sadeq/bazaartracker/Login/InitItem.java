package com.hyperpakhsh.sadeq.bazaartracker.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * item for create initial values
 */

public class InitItem {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("saleMaliID")
    @Expose
    private String saleMaliID;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSaleMaliID() {
        return saleMaliID;
    }

    public void setSaleMaliID(String saleMaliID) {
        this.saleMaliID = saleMaliID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
