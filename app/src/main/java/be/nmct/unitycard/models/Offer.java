package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class Offer {
    @SerializedName("Id")
    private int id;

    @SerializedName("RetailerId")
    private int retailerId;

    @SerializedName("OfferDemand")
    private String offerDemand;

    @SerializedName("OfferReceive")
    private String offerReceive;

    @SerializedName("CreatedTimestamp")
    private Date createdTimestamp;

    public Offer(int retailerId, String offerDemand, String offerReceive, Date createdTimestamp) {
        this.retailerId = retailerId;
        this.offerDemand = offerDemand;
        this.offerReceive = offerReceive;
        this.createdTimestamp = createdTimestamp;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getRetailerId() {
        return retailerId;
    }
    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public String getOfferDemand() {
        return offerDemand;
    }
    public void setOfferDemand(String offerDemand) {
        this.offerDemand = offerDemand;
    }

    public String getOfferReceive() {
        return offerReceive;
    }
    public void setOfferReceive(String offerReceive) {
        this.offerReceive = offerReceive;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }
    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}
