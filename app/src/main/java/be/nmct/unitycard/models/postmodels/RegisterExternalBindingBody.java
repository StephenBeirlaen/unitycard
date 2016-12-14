package be.nmct.unitycard.models.postmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 12/12/2016.
 */

public class RegisterExternalBindingBody {

    public RegisterExternalBindingBody(String email, String provider, String externalAccessToken, String firstName, String lastName, String language) {
        this.email = email;
        this.provider = provider;
        this.externalAccessToken = externalAccessToken;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
    }

    @SerializedName("Email")
    private String email;

    @SerializedName("Provider")
    private String provider;

    @SerializedName("ExternalAccessToken")
    private String externalAccessToken;

    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("Language")
    private String language;
}
