package be.nmct.unitycard.repositories;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import be.nmct.unitycard.contracts.ApiContract;
import be.nmct.unitycard.services.ApiService;
import be.nmct.unitycard.services.AuthService;
import be.nmct.unitycard.services.GeoCodeService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Stephen on 1/11/2016.
 */

@Singleton
// Deze klasse is een singleton (maar 1 instantie maken) en wordt via dependency injection waar nodig geinjecteerd
public class GeoCodeClient {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Retrofit mRetrofit;
    private GeoCodeService geoCodeService;

    public GeoCodeClient() {
        GsonBuilder gBuilder = new GsonBuilder();
        Gson gSon = gBuilder.create();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // Add Stetho interceptor
        builder.addNetworkInterceptor(new StethoInterceptor());
        OkHttpClient okHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiContract.GOOGLE_GEOCODE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava
                .client(okHttpClient)
                .build();

        geoCodeService = mRetrofit.create(GeoCodeService.class);
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public GeoCodeService getGeoCodeService() {
        return geoCodeService;
    }
}
