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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Stephen on 1/11/2016.
 */

public class RetailerRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    @Inject RestClient mRestClient;

    public RetailerRepository(Context context) {
        ((UnityCardApplication)context.getApplicationContext()).getComponent().inject(this);
    }

    // todo: rename to ApiRepository

    // ----- LoyaltyCards -----

    public void getLoyaltyCard(String accessToken, String userId, final GetLoyaltyCardListener callback) {
        mRestClient.getApiService().getLoyaltyCard(RestClient.getAuthorizationHeader(accessToken), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<LoyaltyCard>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.loyaltyCardRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<LoyaltyCard> response) {
                        if (response.isSuccessful()) {
                            LoyaltyCard loyaltyCard = response.body();

                            callback.loyaltyCardReceived(loyaltyCard);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.loyaltyCardRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.loyaltyCardRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetLoyaltyCardListener {
        void loyaltyCardReceived(LoyaltyCard loyaltyCard);
        void loyaltyCardRequestError(String error);
    }

    public void getLoyaltyCardRetailers(String accessToken, String userId, final GetLoyaltyCardRetailersListener callback) {
        mRestClient.getApiService().getLoyaltyCardRetailers(RestClient.getAuthorizationHeader(accessToken), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Retailer>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.loyaltyCardRetailersRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<List<Retailer>> response) {
                        if (response.isSuccessful()) {
                            List<Retailer> retailers = response.body();

                            callback.loyaltyCardRetailersReceived(retailers);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.loyaltyCardRetailersRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.loyaltyCardRetailersRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetLoyaltyCardRetailersListener {
        void loyaltyCardRetailersReceived(List<Retailer> retailers);
        void loyaltyCardRetailersRequestError(String error);
    }

    public void addLoyaltyCardRetailer(String accessToken, String userId, AddLoyaltyCardRetailerBody addLoyaltyCardRetailerBody, final AddLoyaltyCardRetailerListener callback) {
        mRestClient.getApiService().addLoyaltyCardRetailer(RestClient.getAuthorizationHeader(accessToken), userId, addLoyaltyCardRetailerBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.loyaltyCardRetailerRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccessful()) {
                            callback.loyaltyCardRetailerAdded();
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.loyaltyCardRetailerRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.loyaltyCardRetailerRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface AddLoyaltyCardRetailerListener {
        void loyaltyCardRetailerAdded();
        void loyaltyCardRetailerRequestError(String error);
    }

    // ----- LoyaltyPoints -----

    public void getTotalLoyaltyPoints(String accessToken, String userId, final GetTotalLoyaltyPointsListener callback) {
        mRestClient.getApiService().getTotalLoyaltyPoints(RestClient.getAuthorizationHeader(accessToken), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.totalLoyaltyPointsRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<Integer> response) {
                        if (response.isSuccessful()) {
                            Integer totalLoyaltyPoints = response.body();

                            callback.totalLoyaltyPointsReceived(totalLoyaltyPoints);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.totalLoyaltyPointsRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.totalLoyaltyPointsRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetTotalLoyaltyPointsListener {
        void totalLoyaltyPointsReceived(Integer totalLoyaltyPoints);
        void totalLoyaltyPointsRequestError(String error);
    }

    // ----- Offers -----

    public void getAllRetailerOffers(String accessToken, String userId, final GetAllRetailerOffersListener callback) {
        mRestClient.getApiService().getAllRetailerOffers(RestClient.getAuthorizationHeader(accessToken), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Offer>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.allRetailerOffersRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<List<Offer>> response) {
                        if (response.isSuccessful()) {
                            List<Offer> offers = response.body();

                            callback.allRetailerOffersReceived(offers);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.allRetailerOffersRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.allRetailerOffersRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetAllRetailerOffersListener {
        void allRetailerOffersReceived(List<Offer> offers);
        void allRetailerOffersRequestError(String error);
    }

    public void getRetailerOffers(String accessToken, String retailerId, String userId, final GetRetailerOffersListener callback) {
        mRestClient.getApiService().getRetailerOffers(RestClient.getAuthorizationHeader(accessToken), retailerId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Offer>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.retailerOffersRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<List<Offer>> response) {
                        if (response.isSuccessful()) {
                            List<Offer> offers = response.body();

                            callback.retailerOffersReceived(offers);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.retailerOffersRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.retailerOffersRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetRetailerOffersListener {
        void retailerOffersReceived(List<Offer> offers);
        void retailerOffersRequestError(String error);
    }

    // ----- Retailers -----

    public void getAllRetailers(String accessToken, final GetAllRetailersListener callback) {
        mRestClient.getApiService().getAllRetailers(RestClient.getAuthorizationHeader(accessToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Retailer>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.retailersRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<List<Retailer>> response) {
                        if (response.isSuccessful()) {
                            List<Retailer> retailers = response.body();

                            callback.retailersReceived(retailers);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.retailersRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.retailersRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetAllRetailersListener {
        void retailersReceived(List<Retailer> retailers);
        void retailersRequestError(String error);
    }

    public void getRetailer(String accessToken, String retailerId, final GetRetailerListener callback) {
        mRestClient.getApiService().getRetailer(RestClient.getAuthorizationHeader(accessToken), retailerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Retailer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.retailerRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<Retailer> response) {
                        if (response.isSuccessful()) {
                            Retailer retailer = response.body();

                            callback.retailerReceived(retailer);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.retailerRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.retailerRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetRetailerListener {
        void retailerReceived(Retailer retailer);
        void retailerRequestError(String error);
    }

    public void getRetailerLocations(String accessToken, String retailerId, final GetRetailerLocationsListener callback) {
        mRestClient.getApiService().getRetailerLocations(RestClient.getAuthorizationHeader(accessToken), retailerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<RetailerLocation>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.retailerLocationsRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<List<RetailerLocation>> response) {
                        if (response.isSuccessful()) {
                            List<RetailerLocation> retailerLocations = response.body();

                            callback.retailerLocationsReceived(retailerLocations);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.retailerLocationsRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.retailerLocationsRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetRetailerLocationsListener {
        void retailerLocationsReceived(List<RetailerLocation> retailerLocations);
        void retailerLocationsRequestError(String error);
    }

    public void getRetailerLocation(String accessToken, String retailerId, String locationId, final GetRetailerLocationListener callback) {
        mRestClient.getApiService().getRetailerLocation(RestClient.getAuthorizationHeader(accessToken), retailerId, locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<RetailerLocation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.retailerLocationRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<RetailerLocation> response) {
                        if (response.isSuccessful()) {
                            RetailerLocation retailerLocation = response.body();

                            callback.retailerLocationReceived(retailerLocation);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.retailerLocationRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.retailerLocationRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetRetailerLocationListener {
        void retailerLocationReceived(RetailerLocation retailerLocation);
        void retailerLocationRequestError(String error);
    }

    public void getAllRetailerCategories(final GetAllRetailerCategoriesListener callback) {
        mRestClient.getApiService().getAllRetailerCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<RetailerCategory>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "An error occurred");
                        callback.retailerCategoriesRequestError("An error occurred");
                    }

                    @Override
                    public void onNext(Response<List<RetailerCategory>> response) {
                        if (response.isSuccessful()) {
                            List<RetailerCategory> retailerCategories = response.body();

                            callback.retailerCategoriesReceived(retailerCategories);
                        }
                        else {
                            Converter<ResponseBody, AuthErrorResponse> converter =
                                    mRestClient.getRetrofit().responseBodyConverter(AuthErrorResponse.class, new Annotation[0]);

                            try {
                                AuthErrorResponse errorResponse = converter.convert(response.errorBody());

                                String error = "Error: " + errorResponse.getMessage();
                                callback.retailerCategoriesRequestError(error);
                                Log.e(LOG_TAG, error);
                            } catch (Exception e) {
                                callback.retailerCategoriesRequestError("An error occurred");
                            }
                        }
                    }
                });
    }

    public interface GetAllRetailerCategoriesListener {
        void retailerCategoriesReceived(List<RetailerCategory> retailerCategories);
        void retailerCategoriesRequestError(String error);
    }
}
