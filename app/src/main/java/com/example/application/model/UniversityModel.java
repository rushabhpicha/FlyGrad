package com.example.application.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UniversityModel {
    @SerializedName("uniid")
    @Expose
    private String uniid;
    @SerializedName("name")
    @Expose
    private String name;

    public String getUniid() {
        return uniid;
    }

    public void setUniid(String uniid) {
        this.uniid = uniid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
