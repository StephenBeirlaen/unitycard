package be.nmct.unitycard.services;

import java.util.List;

import be.nmct.unitycard.models.Retailer;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

public interface ApiService {
    @GET("/api/retailers")
    Observable<Response<List<Retailer>>> GetAllRetailers(
            @Header("Authorization") String authorizationHeader
    );
}
