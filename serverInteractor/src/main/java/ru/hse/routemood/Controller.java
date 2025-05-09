package ru.hse.routemood;

import static ru.hse.routemood.RouteMoodServerApi.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.hse.routemood.dto.AuthRequest;
import ru.hse.routemood.dto.AuthResponse;
import ru.hse.routemood.dto.GptRequest;
import ru.hse.routemood.dto.RateRequest;
import ru.hse.routemood.dto.RatingRequest;
import ru.hse.routemood.dto.RatingResponse;
import ru.hse.routemood.dto.RegisterRequest;
import ru.hse.routemood.models.Route;
import ru.hse.routemood.models.User;

public class Controller {

    private Gson gson;
    private final RouteMoodServerApi routeMoodServerApi;
    private final SessionManager sessionManager;

    public Controller() {
        sessionManager = SessionManager.getInstance();
        gson = new GsonBuilder()
            .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
            .registerTypeAdapter(double.class, new DoubleTypeAdapter())
            .create();

        Retrofit retrofit = new Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        this.routeMoodServerApi = retrofit.create(RouteMoodServerApi.class);
    }

    private <T> Callback<T> createDefaulteCallback(ApiCallback<T> callback) {
        return new Callback<>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    T body = response.body();
                    String json = gson.toJson(body);
                    System.out.println("Response JSON: " + json);
                    callback.onSuccess(response.body());
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Error response JSON: " + errorBody);
                    callback.onError("Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        };
    }

    private Callback<AuthResponse> createAuthCallback(ApiCallback<AuthResponse> callback) {
        return new Callback<>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse body = response.body();
                    String json = gson.toJson(body);
                    System.out.println("Auth Response JSON: " + json);
                    sessionManager.setToken("Bearer " + body.getToken());
                    callback.onSuccess(response.body());
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Auth Error response JSON: " + errorBody);
                    callback.onError("Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        };
    }

    public void getFictiveRoute(ApiCallback<Route> callback) {
        Call<Route> call = routeMoodServerApi.getFictiveRoute(0.0, 0.0, "Default walk",
            sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }

    public void getRoute(GptRequest request, ApiCallback<Route> callback) {
        Call<Route> call = routeMoodServerApi.getRoute(request, sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }

    public void registerUser(RegisterRequest registerRequest, ApiCallback<AuthResponse> callback) {
        Call<AuthResponse> call = routeMoodServerApi.registerUser(registerRequest);
        call.enqueue(createAuthCallback(callback));
    }

    public void loginUser(AuthRequest authRequest, ApiCallback<AuthResponse> callback) {
        Call<AuthResponse> call = routeMoodServerApi.loginUser(authRequest);
        call.enqueue(createAuthCallback(callback));
    }

    public void listUsers(ApiCallback<List<User>> callback) {
        Call<List<User>> call = routeMoodServerApi.listUsers(sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }

    public void saveRoute(RatingRequest request, ApiCallback<RatingResponse> callback) {
        Call<RatingResponse> call = routeMoodServerApi.saveRoute(request,
            sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }

    public void addRate(RateRequest request, ApiCallback<RatingResponse> callback) {
        Call<RatingResponse> call = routeMoodServerApi.addRate(request, sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }

    public void getRatedRouteById(UUID id, ApiCallback<RatingResponse> callback) {
        Call<RatingResponse> call = routeMoodServerApi.getRouteById(id, sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }

    public void getListRatedRoutesByAuthorUsername(String authorUsername,
        ApiCallback<List<RatingResponse>> callback) {
        Call<List<RatingResponse>> call = routeMoodServerApi.getListRatedRoutesByAuthorUsername(
            authorUsername, sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }

    public void listRoutes(ApiCallback<List<RatingResponse>> callback) {
        Call<List<RatingResponse>> call = routeMoodServerApi.listRoutes(sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }
}
