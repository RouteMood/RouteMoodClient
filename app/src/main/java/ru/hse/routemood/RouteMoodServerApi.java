package ru.hse.routemood;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.hse.routemood.models.AuthRequest;
import ru.hse.routemood.models.AuthResponse;
import ru.hse.routemood.models.GptRequest;
import ru.hse.routemood.models.RegisterRequest;
import ru.hse.routemood.models.Route;
import ru.hse.routemood.models.User;

public interface RouteMoodServerApi {

    String BASE_URL = "http://localhost:8080/";

    @GET("/gpt-fictive-message")
    Call<Route> getFictiveRoute(@Query("longitude") Double longitude,
        @Query("latitude") Double latitude,
        @Query("request") String request,
        @Header("Authorization") String authHeader);

    @GET("/gpt-request")
    Call<Route> getRoute(@Query("longitude") Double longitude,
        @Query("latitude") Double latitude,
        @Query("request") String request,
        @Header("Authorization") String authHeader);

    @POST("/gpt-request")
    Call<Route> getRoute(@Body GptRequest request,
        @Header("Authorization") String authHeader);

    @POST("/api/register")
    Call<AuthResponse> registerUser(@Body RegisterRequest request);

    @POST("/api/login")
    Call<AuthResponse> loginUser(@Body AuthRequest request);

    @GET("/api/users")
    Call<List<User>> listUsers(@Header("Authorization") String authHeader);
}
