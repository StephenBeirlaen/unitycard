package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

public class AuthErrorResponse {
    @SerializedName("Message")
    private String message;

    public String getMessage() {
        return message;
    }
}
