package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class LoyaltyPoint {
    @SerializedName("Id")
    private int id;

    @SerializedName("LoyaltyCardId")
    private int loyaltyCardId;

    @SerializedName("RetailerId")
    private int retailerId;

    @SerializedName("Points")
    private int points;

    public LoyaltyPoint(int loyaltyCardId, int retailerId, int points) {
        this.loyaltyCardId = loyaltyCardId;
        this.retailerId = retailerId;
        this.points = points;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getLoyaltyCardId() {
        return loyaltyCardId;
    }
    public void setLoyaltyCardId(int loyaltyCardId) {
        this.loyaltyCardId = loyaltyCardId;
    }

    public int getRetailerId() {
        return retailerId;
    }
    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
}
