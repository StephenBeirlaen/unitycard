package be.nmct.unitycard.services;

import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.models.postmodels.RegisterUserBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface AuthService {
    @FormUrlEncoded
    @POST("/Token")
    Observable<Response<GetTokenResponse>> getToken(
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/Token")
    Observable<Response<GetTokenResponse>> refreshToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken
    );

    @POST("/api/Account/Register")
    Observable<Response<Void>> registerUser(
        @Body RegisterUserBody body
    );
}
