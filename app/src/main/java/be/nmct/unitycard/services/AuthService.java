package be.nmct.unitycard.services;

import be.nmct.unitycard.models.GetTokenResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {
    @FormUrlEncoded
    @POST("/Token")
    Call<GetTokenResponse> getToken(
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/Token")
    Call<GetTokenResponse> refreshToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken
    );
}
