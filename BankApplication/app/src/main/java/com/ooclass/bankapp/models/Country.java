package com.ooclass.bankapp.models;

import com.google.gson.annotations.SerializedName;

public enum Country {
    @SerializedName("Finland")
    FINLAND("Finland"),

    @SerializedName("Sweden")
    SWEDEN("Sweden"),

    @SerializedName("Norway")
    NORWAY("Norway"),

    @SerializedName("USA")
    USA("USA"),

    @SerializedName("Canada")
    CANADA("Canada"),

    @SerializedName("China")
    CHINA("China"),

    @SerializedName("Belgium")
    BELGIUM("Belgium");

    private String name;

    Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}