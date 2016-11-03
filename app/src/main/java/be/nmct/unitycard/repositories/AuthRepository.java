package be.nmct.unitycard.repositories;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import be.nmct.unitycard.UnityCardApplication;
import be.nmct.unitycard.models.GetTokenErrorResponse;
import be.nmct.unitycard.models.GetTokenResponse;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        Observable<Response<GetTokenResponse>> requestTokenObservable = mRestClient.getAuthService().getToken("password", username, password);
        subscribeTokenCall(requestTokenObservable, callback);
    }

    public void refreshToken(String refreshToken, TokenResponseListener callback) {
        Observable<Response<GetTokenResponse>> refreshTokenObservable = mRestClient.getAuthService().refreshToken("refresh_token", refreshToken);
        subscribeTokenCall(refreshTokenObservable, callback);
    }

    private void subscribeTokenCall(Observable<Response<GetTokenResponse>> tokenRequestObservable, final TokenResponseListener callback) {
        // Dit zit in aparte functie omdat de response voor requestToken en refreshToken hetzelfde is
        tokenRequestObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<GetTokenResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "Error requesting new access token!");
                        callback.tokenRequestError("Error requesting new access token!");
                    }

                    @Override
                    public void onNext(Response<GetTokenResponse> response) {
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
                });
    }

    public interface TokenResponseListener {
        void tokenReceived(GetTokenResponse getTokenResponse);
        void tokenRequestError(String error);
    }
}
