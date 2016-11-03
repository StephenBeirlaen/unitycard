package be.nmct.unitycard.repositories;

import android.content.Context;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.inject.Inject;

import be.nmct.unitycard.UnityCardApplication;
import be.nmct.unitycard.models.AuthErrorResponse;
import be.nmct.unitycard.models.GetTokenErrorResponse;
import be.nmct.unitycard.models.Retailer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Stephen on 1/11/2016.
 */

public class RetailerRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    @Inject RestClient mRestClient;

    public RetailerRepository(Context context) {
        ((UnityCardApplication)context.getApplicationContext()).getComponent().inject(this);
    }

    public void getAllRetailers(String accessToken, final GetAllRetailersListener callback) {
        Call<List<Retailer>> retailersCall = mRestClient.getApiService().GetAllRetailers(RestClient.getAuthorizationHeader(accessToken));
        retailersCall.enqueue(new Callback<List<Retailer>>() {
            @Override
            public void onResponse(Call<List<Retailer>> call, Response<List<Retailer>> response) {
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

            @Override
            public void onFailure(Call<List<Retailer>> call, Throwable t) {
                Log.e(LOG_TAG, "An error occurred");
                callback.retailersRequestError("An error occurred");
            }
        });
    }

    public interface GetAllRetailersListener {
        void retailersReceived(List<Retailer> retailers);
        void retailersRequestError(String error);
    }
}
