package be.nmct.unitycard.services;

import java.util.List;

import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.models.Retailer;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {
    @GET("/api/retailers")
    Call<List<Retailer>> GetAllRetailers(
            @Header("Authorization") String authorizationHeader
    );
}
