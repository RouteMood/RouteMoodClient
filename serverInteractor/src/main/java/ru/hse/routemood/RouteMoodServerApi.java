package ru.hse.routemood;

import java.util.List;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.hse.routemood.dto*
import ru.hse.routemood.models*

public interface RouteMoodServerApi {

    String BASE_URL = "http://localhost:8080/";

    @GET("/gpt-fictive-message")
    Call<Route> getFictiveRoute(@Query("latitude") Double latitude,
        @Query("longitude") Double longitude,
        @Query("request") String request,
        @Header("Authorization") String authHeader);

    @GET("/gpt-request")
    Call<Route> getRoute(@Query("latitude") Double latitude,
        @Query("longitude") Double longitude,
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

    @POST("/rating/save")
    Call<RatingResponse> saveRoute(@Body RatingRequest request,
        @Header("Authorization") String authHeader);

    @DELETE("/rating/delete")
    Call<Void> deleteRoute(@Query("id") UUID routeId, @Header("Authorization") String authHeader);

    @PATCH("/rating/add-rate")
    Call<RatingResponse> addRate(@Body RateRequest request,
        @Header("Authorization") String authHeader);

    @GET("/rating/get-by-id")
    Call<RatingResponse> getRouteById(@Query("id") UUID routeId,
        @Header("Authorization") String authHeader);

    @GET("/rating/get-by-author")
    Call<List<RatingResponse>> getListRatedRoutesByAuthorUsername(
        @Query("author") String authorUsername,
        @Header("Authorization") String authHeader);

    @GET("/rating/get-all")
    Call<List<RatingResponse>> listRoutes(@Header("Authorization") String authHeader);

    @POST("/images/save")
    Call<ImageSaveResponse> saveImage(@Body ImageSaveRequest request,
        @Header("Authorization") String authHeader);

    @GET("/image/load")
    Call<ImageLoadResponse> loadImage(@Query("id") UUID imageId,
        @Header("Authorization") String authHeader);

    Call<Void> deleteImage(@Query("id") UUID imageId, @Header("Authorization") String authHeader);
}
