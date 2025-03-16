package ru.hse.routemood;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.hse.routemood.models.*;
import ru.hse.routemood.models.Route.RouteItem;

import java.util.List;

public interface RouteMoodServerApi {
    String BASE_URL = "http://localhost:8080/";

    @GET("/gpt-fictive-message")
    Call<List<RouteItem>> getFictiveRoute(@Query("longitude") Double longitude,
                                          @Query("latitude") Double latitude,
                                          @Query("request") String request);

    @GET("/gpt-request")
    Call<List<RouteItem>> getRoute(@Query("longitude") Double longitude,
                                   @Query("latitude") Double latitude,
                                   @Query("request") String request);

    @POST("/gpt-request")
    Call<List<RouteItem>> getRoute(@Body GptRequest request);

    @POST("/register")
    Call<AuthResponse> registerUser(@Body RegisterRequest request);

    @POST("/login")
    Call<AuthResponse> loginUser(@Body AuthRequest request);

    @GET("/users")
    Call<List<User>> listUsers();
}
