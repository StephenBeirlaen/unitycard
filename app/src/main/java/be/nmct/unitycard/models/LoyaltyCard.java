package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class LoyaltyCard {
    @SerializedName("Id")
    private int id;

    @SerializedName("UserId")
    private String userId;

    @SerializedName("CreatedTimestamp")
    private Date createdTimestamp;

    @SerializedName("UpdatedTimestamp")
    private Date updatedTimestamp;

    public LoyaltyCard(int id, String userId, Date createdTimestamp, Date updatedTimestamp) {
        this.id = id;
        this.userId = userId;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }
    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }
    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
