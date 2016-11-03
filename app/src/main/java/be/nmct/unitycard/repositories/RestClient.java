package be.nmct.unitycard.repositories;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import be.nmct.unitycard.contracts.ApiContract;
import be.nmct.unitycard.services.ApiService;
import be.nmct.unitycard.services.AuthService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Stephen on 1/11/2016.
 */

@Singleton
// Deze klasse is een singleton (maar 1 instantie maken) en wordt via dependency injection waar nodig geinjecteerd
public class RestClient {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Retrofit mRetrofit;
    private AuthService authService;
    private ApiService apiService;

    public RestClient() {
        GsonBuilder gBuilder = new GsonBuilder();
        //gBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        Gson gSon = gBuilder.create();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // Add Stetho interceptor
        builder.addNetworkInterceptor(new StethoInterceptor());
        OkHttpClient okHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiContract.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava
                .client(okHttpClient)
                .build();

        authService = mRetrofit.create(AuthService.class);
        apiService = mRetrofit.create(ApiService.class);
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public static String getAuthorizationHeader(String accessToken) {
        return "Bearer " + accessToken;
    }
}
