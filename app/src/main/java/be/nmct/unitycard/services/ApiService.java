package be.nmct.unitycard.services;

import java.util.List;

import be.nmct.unitycard.models.LoyaltyCard;
import be.nmct.unitycard.models.Offer;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.RetailerCategory;
import be.nmct.unitycard.models.RetailerLocation;
import be.nmct.unitycard.models.postmodels.AddLoyaltyCardRetailerBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    // ----- LoyaltyCards -----

    @GET("/api/loyaltycards/{userId}")
    Observable<Response<LoyaltyCard>> getLoyaltyCard(
            @Header("Authorization") String authorizationHeader,
            @Path("userId") String userId,
            @Query("lastUpdatedTimestamp") Long lastUpdatedTimestamp
    );

    @GET("/api/loyaltycards/{userId}/retailers")
    Observable<Response<List<Retailer>>> getLoyaltyCardRetailers(
            @Header("Authorization") String authorizationHeader,
            @Path("userId") String userId,
            @Query("lastUpdatedTimestamp") Long lastUpdatedTimestamp
    );

    @POST("/api/loyaltycards/{userId}/retailers")
    Observable<Response<Void>> addLoyaltyCardRetailer(
            @Header("Authorization") String authorizationHeader,
            @Path("userId") String userId,
            @Body AddLoyaltyCardRetailerBody body
    );

    // ----- LoyaltyPoints -----

    @GET("/api/loyaltypoints/{userId}")
    Observable<Response<Integer>> getTotalLoyaltyPoints(
            @Header("Authorization") String authorizationHeader,
            @Path("userId") String userId,
            @Query("lastUpdatedTimestamp") Long lastUpdatedTimestamp
    );

    // ----- Offers -----

    @GET("/api/offers/{userId}")
    Observable<Response<List<Offer>>> getAllRetailerOffers(
            @Header("Authorization") String authorizationHeader,
            @Path("userId") String userId,
            @Query("lastUpdatedTimestamp") Long lastUpdatedTimestamp
    );

    @GET("/api/offers/{retailerId}/{userId}")
    Observable<Response<List<Offer>>> getRetailerOffers(
            @Header("Authorization") String authorizationHeader,
            @Path("retailerId") String retailerId,
            @Path("userId") String userId,
            @Query("lastUpdatedTimestamp") Long lastUpdatedTimestamp
    );

    // ----- Retailers -----

    @GET("/api/retailers")
    Observable<Response<List<Retailer>>> getAllRetailers(
            @Header("Authorization") String authorizationHeader,
            @Query("lastUpdatedTimestamp") Long lastUpdatedTimestamp
    );

    @GET("/api/retailers/{retailerId}")
    Observable<Response<Retailer>> getRetailer(
            @Header("Authorization") String authorizationHeader,
            @Path("retailerId") String retailerId
    );

    @GET("/api/retailers/{retailerId}/locations")
    Observable<Response<List<RetailerLocation>>> getRetailerLocations(
            @Header("Authorization") String authorizationHeader,
            @Path("retailerId") String retailerId,
            @Query("lastUpdatedTimestamp") Long lastUpdatedTimestamp
    );

    @GET("/api/retailers/{retailerId}/locations/{locationId}")
    Observable<Response<RetailerLocation>> getRetailerLocation(
            @Header("Authorization") String authorizationHeader,
            @Path("retailerId") String retailerId,
            @Path("locationId") String locationId
    );

    @GET("/api/retailers/categories")
    Observable<Response<List<RetailerCategory>>> getAllRetailerCategories(
            @Query("lastUpdatedTimestamp") Long lastUpdatedTimestamp
    );
}
