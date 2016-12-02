package be.nmct.unitycard.repositories;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.nmct.unitycard.UnityCardApplication;
import be.nmct.unitycard.models.GeoCodeResponse;
import be.nmct.unitycard.models.GetTokenErrorResponse;
import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.models.RegisterUserErrorResponse;
import be.nmct.unitycard.models.postmodels.ChangeFcmTokenBody;
import be.nmct.unitycard.models.postmodels.RegisterUserBody;
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

public class GeoCodeRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    @Inject GeoCodeClient mGeoCodeClient;

    public GeoCodeRepository(Context context) {
        ((UnityCardApplication)context.getApplicationContext()).getComponent().inject(this);
    }

    public void requestLatLong(String address, final GeoCodeResponseListener callback) {
        mGeoCodeClient.getGeoCodeService().requestLatLong(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<GeoCodeResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "Error while geocoding!");
                        callback.geoCodeRequestError("Error while geocoding!");
                    }

                    @Override
                    public void onNext(Response<GeoCodeResponse> response) {
                        if (response.isSuccessful()) {
                            GeoCodeResponse geoCodeResponse = response.body();

                            List<GeoCodeResponse.GeoCodeResult> results = geoCodeResponse.getResults();
                            if (results != null && results.size() > 0) {
                                GeoCodeResponse.GeoCodeResult result = results.get(0);

                                if (result != null) {
                                    GeoCodeResponse.GeoCodeResult.Geometry geometry = result.getGeometry();

                                    if (geometry != null) {
                                        GeoCodeResponse.GeoCodeResult.Geometry.Location location = geometry.getLocation();

                                        if (location != null) {
                                            double lat = location.getLat();
                                            double lng = location.getLng();

                                            if (lat != 0 && lng != 0) {
                                                callback.latLongReceived(lat, lng);
                                            }
                                        }
                                    }
                                }
                            }

                            callback.geoCodeRequestError("Error while geocoding!");
                        }
                        else {
                            Log.e(LOG_TAG, "Error while geocoding!");
                            callback.geoCodeRequestError("Error while geocoding!");
                        }
                    }
                });
    }

    public interface GeoCodeResponseListener {
        void latLongReceived(double lat, double lng);
        void geoCodeRequestError(String error);
    }
}
