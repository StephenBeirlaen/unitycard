package be.nmct.unitycard.models.viewmodels;

import com.google.gson.annotations.SerializedName;

import be.nmct.unitycard.models.LoyaltyPoint;
import be.nmct.unitycard.models.Retailer;

/**
 * Created by lorenzvercoutere on 12/12/16.
 */

public class RetailerLoyaltyPointVM {

    @SerializedName("Retailer")
    private Retailer retailer;

    @SerializedName("LoyaltyPoints")
    private int loyaltyPoints;

    public RetailerLoyaltyPointVM(Retailer retailer, int loyaltyPoints){
        this.retailer = retailer;
        this.loyaltyPoints = loyaltyPoints;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }
}
