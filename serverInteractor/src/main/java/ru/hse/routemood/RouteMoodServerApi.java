package ru.hse.routemood;

import java.util.List;
import java.util.UUID;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.hse.routemood.dto.AuthRequest;
import ru.hse.routemood.dto.AuthResponse;
import ru.hse.routemood.dto.GptRequest;
import ru.hse.routemood.dto.ImageLoadResponse;
import ru.hse.routemood.dto.ImageSaveResponse;
import ru.hse.routemood.dto.PageResponse;
import ru.hse.routemood.dto.RateRequest;
import ru.hse.routemood.dto.RatingRequest;
import ru.hse.routemood.dto.RatingResponse;
import ru.hse.routemood.dto.RegisterRequest;
import ru.hse.routemood.dto.UserResponse;
import ru.hse.routemood.models.Route;
import ru.hse.routemood.models.User;

public interface RouteMoodServerApi {

    String BASE_URL = "http://158.160.135.170:8080/";

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

    @GET("/user/list")
    Call<List<User>> listUsers(@Header("Authorization") String authHeader);

    @GET("/user/getInfo/{username}")
    Call<UserResponse> getUserInfo(@Path("username") String username,
        @Header("Authorization") String authHeader);

    @Multipart
    @POST("/user/update-avatar")
    Call<UUID> updateAvatar(@Part MultipartBody.Part file,
        @Header("Authorization") String authHeader);

    @POST("/rating/save")
    Call<RatingResponse> saveRoute(@Body RatingRequest request,
        @Header("Authorization") String authHeader);

    @DELETE("/rating/delete")
    Call<Void> deleteRoute(@Query("id") UUID routeId, @Header("Authorization") String authHeader);

    @PATCH("/rating/add-rate")
    Call<RatingResponse> addRate(@Body RateRequest request,
        @Header("Authorization") String authHeader);

    @GET("/rating/get-user-rate")
    Call<Integer> getUserRate(@Query("routeId") UUID routeId, @Query("username") String username,
        @Header("Authorization") String authHeader);

    @GET("/rating/get-by-id")
    Call<RatingResponse> getRouteById(@Query("id") UUID routeId,
        @Header("Authorization") String authHeader);

    @GET("/rating/get-by-author")
    Call<List<RatingResponse>> getListRatedRoutesByAuthorUsername(
        @Query("author") String authorUsername,
        @Header("Authorization") String authHeader);

    @GET("/rating/first-page")
    Call<PageResponse> getFirstPage(@Header("Authorization") String authHeader);

    @GET("/rating/next-page")
    Call<PageResponse> getNextPage(@Query("nextPageToken") String nextPageToken,
        @Header("Authorization") String authHeader);

    @GET("/rating/get-all")
    Call<List<RatingResponse>> listRoutes(@Header("Authorization") String authHeader);

    @Multipart
    @POST("images/save")
    Call<ImageSaveResponse> saveImage(@Part MultipartBody.Part file,
        @Header("Authorization") String authHeader
    );

    @GET("/images/load")
    Call<ImageLoadResponse> loadImage(@Query("id") UUID imageId,
        @Header("Authorization") String authHeader);

    @DELETE("/images/delete")
    Call<Void> deleteImage(@Query("id") UUID imageId, @Header("Authorization") String authHeader);
}
