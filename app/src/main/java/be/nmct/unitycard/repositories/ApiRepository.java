package be.nmct.unitycard.repositories;

import android.content.Context;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.inject.Inject;

import be.nmct.unitycard.UnityCardApplication;
import be.nmct.unitycard.models.AuthErrorResponse;
import be.nmct.unitycard.models.LoyaltyCard;
import be.nmct.unitycard.models.Offer;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.RetailerCategory;
import be.nmct.unitycard.models.RetailerLocation;
import be.nmct.unitycard.models.postmodels.AddLoyaltyCardRetailerBody;
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

public class ApiRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    @Inject RestClient mRestClient;

    public ApiRepository(Context context) {
        ((UnityCardApplication)context.getApplicationContext()).getComponent().inject(this);
    }

    // ----- LoyaltyCards -----

    public void getLoyaltyCard(String accessToken, String userId, final GetResultListener<LoyaltyCard> callback) {
        subscribeApiCall(mRestClient.getApiService().getLoyaltyCard(RestClient.getAuthorizationHeader(accessToken), userId), callback);
    }

    public void getLoyaltyCardRetailers(String accessToken, String userId, final GetResultListener<List<Retailer>> callback) {
        subscribeApiCall(mRestClient.getApiService().getLoyaltyCardRetailers(RestClient.getAuthorizationHeader(accessToken), userId), callback);
    }

    public void addLoyaltyCardRetailer(String accessToken, String userId, AddLoyaltyCardRetailerBody addLoyaltyCardRetailerBody, final GetResultListener callback) {
        subscribeApiCall(mRestClient.getApiService().addLoyaltyCardRetailer(RestClient.getAuthorizationHeader(accessToken), userId, addLoyaltyCardRetailerBody), callback);
    }

    // ----- LoyaltyPoints -----

    public void getTotalLoyaltyPoints(String accessToken, String userId, final GetResultListener<Integer> callback) {
        subscribeApiCall(mRestClient.getApiService().getTotalLoyaltyPoints(RestClient.getAuthorizationHeader(accessToken), userId), callback);
    }

    // ----- Offers -----

    public void getAllRetailerOffers(String accessToken, String userId, final GetResultListener<List<Offer>> callback) {
        subscribeApiCall(mRestClient.getApiService().getAllRetailerOffers(RestClient.getAuthorizationHeader(accessToken), userId), callback);
    }

    public void getRetailerOffers(String accessToken, String retailerId, String userId, final GetResultListener<List<Offer>> callback) {
        subscribeApiCall(mRestClient.getApiService().getRetailerOffers(RestClient.getAuthorizationHeader(accessToken), retailerId, userId), callback);
    }

    // ----- Retailers -----

    public void getAllRetailers(String accessToken, final GetResultListener<List<Retailer>> callback) {
        subscribeApiCall(mRestClient.getApiService().getAllRetailers(RestClient.getAuthorizationHeader(accessToken)), callback);
    }

    public void getRetailer(String accessToken, String retailerId, final GetResultListener<Retailer> callback) {
        subscribeApiCall(mRestClient.getApiService().getRetailer(RestClient.getAuthorizationHeader(accessToken), retailerId), callback);
    }

    public void getRetailerLocations(String accessToken, String retailerId, final GetResultListener<List<RetailerLocation>> callback) {
        subscribeApiCall(mRestClient.getApiService().getRetailerLocations(RestClient.getAuthorizationHeader(accessToken), retailerId), callback);
    }

    public void getRetailerLocation(String accessToken, String retailerId, String locationId, final GetResultListener<RetailerLocation> callback) {
        subscribeApiCall(mRestClient.getApiService().getRetailerLocation(RestClient.getAuthorizationHeader(accessToken), retailerId, locationId), callback);
    }

    public void getAllRetailerCategories(final GetResultListener<List<RetailerCategory>> callback) {
        subscribeApiCall(mRestClient.getApiService().getAllRetailerCategories(), callback);
    }

    // Generic code
    private <T> void subscribeApiCall(Observable<Response<T>> apiRequestObservable, final GetResultListener<T> callback) {
        apiRequestObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<T>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.requestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<T> response) {
                        if (response.isSuccessful()) {
                            T result = response.body();

                            callback.resultReceived(result);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.requestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.requestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetResultListener<T> {
        void resultReceived(T result);
        void requestError(String error);
    }
}
