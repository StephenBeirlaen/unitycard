package be.nmct.unitycard.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class RetailerLocation implements Parcelable {
    @SerializedName("Id")
    private int id;

    @SerializedName("RetailerId")
    private int retailerId;

    @SerializedName("Name")
    private String name;

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

    public RetailerLocation(int retailerId, String name,
                            String street, String number, int zipcode, String city, String country,
                            Date updatedTimestamp) {
        this.retailerId = retailerId;
        this.name = name;
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

    protected RetailerLocation(Parcel in) {
        id = in.readInt();
        retailerId = in.readInt();
        name = in.readString();
        street = in.readString();
        number = in.readString();
        zipcode = in.readInt();
        city = in.readString();
        country = in.readString();
        long tmpUpdatedTimestamp = in.readLong();
        updatedTimestamp = tmpUpdatedTimestamp != -1 ? new Date(tmpUpdatedTimestamp) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeInt(retailerId);
        dest.writeString(name);
        dest.writeString(street);
        dest.writeString(number);
        dest.writeInt(zipcode);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeLong(updatedTimestamp != null ? updatedTimestamp.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RetailerLocation> CREATOR = new Parcelable.Creator<RetailerLocation>() {
        @Override
        public RetailerLocation createFromParcel(Parcel in) {
            return new RetailerLocation(in);
        }

        @Override
        public RetailerLocation[] newArray(int size) {
            return new RetailerLocation[size];
        }
    };
}
