package be.nmct.unitycard.models.postmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 28/11/2016.
 */

public class PushAdvertisementNotificationBody {
    @SerializedName("Title")
    private String title;

    public PushAdvertisementNotificationBody(String title) {
        this.title = title;
    }
}
