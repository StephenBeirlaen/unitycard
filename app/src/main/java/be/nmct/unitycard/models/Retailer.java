package be.nmct.unitycard.models;

import java.util.List;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class Retailer {
    private int id;
    private int retailerCategoryId;
    private RetailerCategory retailerCategory;
    private String name;
    private String tagline;
    private boolean chain;
    private String logoUrl;
    private List<LoyaltyPoint> loyaltyPoints;
    private List<RetailerLocation> retailerLocations;
    private List<Offer> offers;


    public Retailer(){}

    public Retailer(int retailerCategoryId, String name, String tagline, boolean chain, String logoUrl){
        this.retailerCategoryId = retailerCategoryId;
        this.name = name;
        this.tagline = tagline;
        this.chain = chain;
        this.logoUrl = logoUrl;
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

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getRetailerCategoryId() {
        return retailerCategoryId;
    }

    public void setRetailerCategoryId(int retailerCategoryId) {
        this.retailerCategoryId = retailerCategoryId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    //geen get van boolean


    public void setChain(boolean chain) {
        this.chain = chain;
    }

    public List<LoyaltyPoint> getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(List<LoyaltyPoint> loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<RetailerLocation> getRetailerLocations() {
        return retailerLocations;
    }

    public void setRetailerLocations(List<RetailerLocation> retailerLocations) {
        this.retailerLocations = retailerLocations;
    }

    public RetailerCategory getRetailerCategory() {
        return retailerCategory;
    }

    public void setRetailerCategory(RetailerCategory retailerCategory) {
        this.retailerCategory = retailerCategory;
    }
}
