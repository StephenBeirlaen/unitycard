package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class RetailerCategory {
    @SerializedName("Id")
    private int id;

    @SerializedName("Name")
    private String name;

    @SerializedName("UpdatedTimestamp")
    private Date updatedTimestamp;

    public RetailerCategory(String name, Date updatedTimestamp) {
        this.name = name;
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }
    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
