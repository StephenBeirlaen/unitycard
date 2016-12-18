package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class Retailer {
    @SerializedName("Id")
    private int id;

    @SerializedName("RetailerCategoryId")
    private int retailerCategoryId;

    @SerializedName("Name")
    private String name;

    @SerializedName("Tagline")
    private String tagline;

    @SerializedName("Chain")
    private boolean chain;

    @SerializedName("LogoUrl")
    private String logoUrl;

    @SerializedName("UpdatedTimestamp")
    private Date updatedTimestamp;

    @SerializedName("LoyaltyPoint")
    private int loyaltyPoint;

    public Retailer(int id, int retailerCategoryId, String name, String tagline, boolean chain, String logoUrl, Date updatedTimestamp/*, int loyaltyPoint*/) {
        this.id = id;
        this.retailerCategoryId = retailerCategoryId;
        this.name = name;
        this.tagline = tagline;
        this.chain = chain;
        this.logoUrl = logoUrl;
        this.updatedTimestamp = updatedTimestamp;
        //this.loyaltyPoint = loyaltyPoint;
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

    public boolean isChain() {
        return chain;
    }
    public void setChain(boolean chain) {
        this.chain = chain;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }
    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public int getLoyaltyPoint() {
        return loyaltyPoint;
    }

    public void setLoyaltyPoint(int loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
    }

}
