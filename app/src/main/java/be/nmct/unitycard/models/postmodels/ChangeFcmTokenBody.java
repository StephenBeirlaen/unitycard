package be.nmct.unitycard.models.postmodels;

import com.google.gson.annotations.SerializedName;

public class ChangeFcmTokenBody {

    @SerializedName("FcmToken")
    private String fcmToken;

    public ChangeFcmTokenBody(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
