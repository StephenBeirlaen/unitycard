package be.nmct.unitycard.models;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class Offer {
    private int id;
    private int retailerId;
    private Retailer retailer;
    private String offerDemand;
    private String offerReceive;
    //datetime

    public Offer(){}

    public Offer(int retailerId, String offerDemand, String offerReceive){
        this.retailerId = retailerId;
        this.offerDemand = offerDemand;
        this.offerReceive = offerReceive;
        //datetime
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

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }
}
