package be.nmct.unitycard.services;

import be.nmct.unitycard.models.GeoCodeResponse;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GeoCodeService {
    @GET("/maps/api/geocode/json?sensor=false")
    Observable<Response<GeoCodeResponse>> requestLatLong(
            @Query("address") String address
    );
}
