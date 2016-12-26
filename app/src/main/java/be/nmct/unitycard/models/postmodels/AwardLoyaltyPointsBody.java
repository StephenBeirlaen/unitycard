package be.nmct.unitycard.models.postmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lorenzvercoutere on 26/12/16.
 */

public class AwardLoyaltyPointsBody {

    @SerializedName("LoyaltyPointsIncrementAmount")
    private int loyaltyPointsIncrementAmount;

    public AwardLoyaltyPointsBody(int loyaltyPointsIncrementAmount){
        this.loyaltyPointsIncrementAmount = loyaltyPointsIncrementAmount;
    }

    public int getLoyaltyPointsIncrementAmount() {
        return loyaltyPointsIncrementAmount;
    }
}
