package be.nmct.unitycard.models;

import android.database.Observable;

import java.util.Date;
import java.util.List;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class LoyaltyCard {
    private int id;
    private String userId;
    //private Date createdTimeStamp;
    private List<LoyaltyPoint> loyaltyPoints;


    public LoyaltyCard(){}

    public LoyaltyCard(String userId){
        this.userId = userId;
        //datetime nog zoeken
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

    //datetime nog get en set


    public List<LoyaltyPoint> getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(List<LoyaltyPoint> loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

}
