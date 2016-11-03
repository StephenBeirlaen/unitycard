package be.nmct.unitycard.models;

public class GetTokenResponse {
    private String access_token;

    public String getAccessToken() {
        return access_token;
    }

    private String token_type;

    private int expires_in;

    private String refresh_token;

    public String getRefreshToken() {
        return refresh_token;
    }

    private String userName;

    /*@SerializedName(".issued") // Date formaat klopt niet
    private Date issued;

    @SerializedName(".expires")
    private Date expires;*/
}
