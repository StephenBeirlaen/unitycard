package be.nmct.unitycard.models;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class LoyaltyPoint {
    private int id;
    private int loyaltyCardId;
    private LoyaltyCard loyaltyCard;
    private int retailerId;
    private Retailer retailer;
    private int points;

    public LoyaltyPoint(){}

    public LoyaltyPoint(int loyaltyCardId, int retailerId, int points){
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

    public LoyaltyCard getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(LoyaltyCard loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }
}
