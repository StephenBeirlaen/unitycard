package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class AdvertisementNotification {
    @SerializedName("RetailerId")
    private int retailerId;

    @SerializedName("Title")
    private String title;

    @SerializedName("Description")
    private String description;

    public AdvertisementNotification(int retailerId, String title, String description) {
        this.retailerId = retailerId;
        this.title = title;
        this.description = description;
    }

    public int getRetailerId() {
        return retailerId;
    }
    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
