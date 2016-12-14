package be.nmct.unitycard.services;

import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.models.postmodels.ChangeFcmTokenBody;
import be.nmct.unitycard.models.postmodels.RegisterExternalBindingBody;
import be.nmct.unitycard.models.postmodels.RegisterUserBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
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

    @POST("/api/Account/ChangeFcmToken")
    Observable<Response<Void>> changeFcmToken(
            @Header("Authorization") String authorizationHeader,
            @Body ChangeFcmTokenBody body
    );

    @GET("/api/Account/ObtainLocalAccessToken")
    Observable<Response<GetTokenResponse>> obtainLocalAccessToken(
            @Query("provider") String provider,
            @Query("externalAccessToken") String externalAccessToken
    );

    @POST("/api/Account/RegisterExternal")
    Observable<Response<GetTokenResponse>> registerExternal(
            @Body RegisterExternalBindingBody body
    );
}
