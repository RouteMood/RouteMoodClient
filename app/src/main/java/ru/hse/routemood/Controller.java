package ru.hse.routemood;

import static ru.hse.routemood.RouteMoodServerApi.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.hse.routemood.models.AuthRequest;
import ru.hse.routemood.models.AuthResponse;
import ru.hse.routemood.models.GptRequest;
import ru.hse.routemood.models.RegisterRequest;
import ru.hse.routemood.models.Route;
import ru.hse.routemood.models.User;

public class Controller {

    private final RouteMoodServerApi routeMoodServerApi;
    private final SessionManager sessionManager;

    public Controller() {
        sessionManager = SessionManager.getInstance();
        Gson gson = new GsonBuilder()
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
                    callback.onSuccess(response.body());
                } else {
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
                    sessionManager.setToken(response.body().getToken());
                    callback.onSuccess(response.body());
                } else {
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
            "Bearer " + sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }

    public void getRoute(GptRequest request, ApiCallback<Route> callback) {
        Call<Route> call = routeMoodServerApi.getRoute(request,
            "Bearer " + sessionManager.getToken());
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
        Call<List<User>> call = routeMoodServerApi.listUsers("Bearer " + sessionManager.getToken());
        call.enqueue(createDefaulteCallback(callback));
    }
}
