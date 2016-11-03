package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

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

    public LoyaltyCard(String userId, Date createdTimestamp) {
        this.userId = userId;
        this.createdTimestamp = createdTimestamp;
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
}
