package be.nmct.unitycard.models.postmodels;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import be.nmct.unitycard.repositories.ApiRepository;

/**
 * Created by Stephen on 5/11/2016.
 */

public class AddLoyaltyCardRetailerBody {
    @SerializedName("RetailerId")
    private int retailerId;

    public AddLoyaltyCardRetailerBody(int retailerId) {
        this.retailerId = retailerId;
    }



    public int getRetailerId() {
        return retailerId;
    }
}
