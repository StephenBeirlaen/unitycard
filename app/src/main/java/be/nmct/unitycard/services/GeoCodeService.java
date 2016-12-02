package be.nmct.unitycard.services;

import java.util.List;

import be.nmct.unitycard.models.GeoCodeResponse;
import be.nmct.unitycard.models.LoyaltyCard;
import be.nmct.unitycard.models.Offer;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.RetailerCategory;
import be.nmct.unitycard.models.RetailerLocation;
import be.nmct.unitycard.models.postmodels.AddLoyaltyCardRetailerBody;
import be.nmct.unitycard.models.postmodels.PushAdvertisementNotificationBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface GeoCodeService {
    @GET("/maps/api/geocode/json?sensor=false")
    Observable<Response<GeoCodeResponse>> requestLatLong(
            @Query("address") String address
    );
}
