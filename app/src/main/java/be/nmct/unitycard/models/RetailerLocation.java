package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class RetailerLocation {
    @SerializedName("Id")
    private int id;

    @SerializedName("RetailerId")
    private int retailerId;

    @SerializedName("Name")
    private String name;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Street")
    private String street;

    @SerializedName("Number")
    private String number;

    @SerializedName("ZipCode")
    private int zipcode;

    @SerializedName("City")
    private String city;

    @SerializedName("Country")
    private String country;

    @SerializedName("UpdatedTimestamp")
    private Date updatedTimestamp;

    public RetailerLocation(int retailerId, String name, double latitude, double longitude,
                            String street, String number, int zipcode, String city, String country,
                            Date updatedTimestamp) {
        this.retailerId = retailerId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.number = number;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.updatedTimestamp = updatedTimestamp;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getRetailerId() {
        return retailerId;
    }
    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public int getZipcode() {
        return zipcode;
    }
    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }
    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
