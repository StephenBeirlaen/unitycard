package be.nmct.unitycard.models.viewmodels;

import be.nmct.unitycard.models.LoyaltyPoint;
import be.nmct.unitycard.models.Retailer;

/**
 * Created by lorenzvercoutere on 12/12/16.
 */

public class RetailerLoyaltyPointVM {
    private Retailer retailer;
    private LoyaltyPoint loyaltyPoint;

    public RetailerLoyaltyPointVM(Retailer retailer, LoyaltyPoint loyaltyPoint){
        this.retailer = retailer;
        this.loyaltyPoint = loyaltyPoint;
    }

    public LoyaltyPoint getLoyaltyPoint() {
        return loyaltyPoint;
    }

    public void setLoyaltyPoint(LoyaltyPoint loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }
}
