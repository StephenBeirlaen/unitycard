package be.nmct.unitycard.repositories;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import be.nmct.unitycard.UnityCardApplication;
import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.models.GetTokenErrorResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Stephen on 1/11/2016.
 */

public class AuthRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    @Inject RestClient mRestClient;

    public AuthRepository(Context context) {
        ((UnityCardApplication)context.getApplicationContext()).getComponent().inject(this);
    }

    public void requestToken(String username, String password, TokenResponseListener callback) {
        Call<GetTokenResponse> requestTokenCall = mRestClient.getAuthService().getToken("password", username, password);
        enqueueTokenCall(requestTokenCall, callback);
    }

    public void refreshToken(String refreshToken, TokenResponseListener callback) {
        Call<GetTokenResponse> refreshTokenCall = mRestClient.getAuthService().refreshToken("refresh_token", refreshToken);
        enqueueTokenCall(refreshTokenCall, callback);
    }

    private void enqueueTokenCall(Call<GetTokenResponse> tokenCall, final TokenResponseListener callback) {
        // Dit zit in aparte functie omdat de response voor requestToken en refreshToken hetzelfde is
        tokenCall.enqueue(new Callback<GetTokenResponse>() {
            @Override
            public void onResponse(Call<GetTokenResponse> call, Response<GetTokenResponse> response) {
                if (response.isSuccessful()) {
                    GetTokenResponse getTokenResponse = response.body();

                    if (TextUtils.isEmpty(getTokenResponse.getAccessToken()) || TextUtils.isEmpty(getTokenResponse.getRefreshToken())) { // Login invalid
                        callback.tokenRequestError("Invalid login");
                    }
                    else { // OK
                        Log.d(LOG_TAG, "Received new access token: " + getTokenResponse.getAccessToken());
                        callback.tokenReceived(getTokenResponse);
                    }
                }
                else {
                    Converter<ResponseBody, GetTokenErrorResponse> converter =
                            mRestClient.getRetrofit().responseBodyConverter(GetTokenErrorResponse.class, new Annotation[0]);

                    try {
                        GetTokenErrorResponse errorResponse = converter.convert(response.errorBody());

                        String error = "Error " + errorResponse.getError()
                                + " occurred while requesting access token: "
                                + errorResponse.getErrorDescription();
                        callback.tokenRequestError(error);
                        Log.e(LOG_TAG, error);

                    } catch (Exception e) {
                        callback.tokenRequestError("Error requesting new access token and couldn't parse error!");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetTokenResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Error requesting new access token!");
                callback.tokenRequestError("Error requesting new access token!");
            }
        });
    }

    public interface TokenResponseListener {
        void tokenReceived(GetTokenResponse getTokenResponse);
        void tokenRequestError(String error);
    }
}
