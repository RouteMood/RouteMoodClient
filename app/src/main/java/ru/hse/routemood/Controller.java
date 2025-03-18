package ru.hse.routemood;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.hse.routemood.models.*;

import java.util.List;

import static ru.hse.routemood.RouteMoodServerApi.BASE_URL;

public class Controller {
    private final RouteMoodServerApi routeMoodServerApi;

    public Controller() {
        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.routeMoodServerApi = retrofit.create(RouteMoodServerApi.class);
    }

    public void getFictiveRoute(ApiCallback<Route> callback) {
        Call<Route> call = routeMoodServerApi.getFictiveRoute(0.0, 0.0, "Default walk");
        call.enqueue(createCallback(callback));
    }

    public void getRoute(Double longitude, Double latitude, String request, ApiCallback<Route> callback) {
        Call<Route> call = routeMoodServerApi.getRoute(longitude, latitude, request);
        call.enqueue(createCallback(callback));
    }

    public void getRoute(GptRequest request, ApiCallback<Route> callback) {
        Call<Route> call = routeMoodServerApi.getRoute(request);
        call.enqueue(createCallback(callback));
    }

    public void registerUser(RegisterRequest registerRequest, ApiCallback<AuthResponse> callback) {
        Call<AuthResponse> call = routeMoodServerApi.registerUser(registerRequest);
        call.enqueue(createCallback(callback));
    }

    public void loginUser(AuthRequest authRequest, ApiCallback<AuthResponse> callback) {
        Call<AuthResponse> call = routeMoodServerApi.loginUser(authRequest);
        call.enqueue(createCallback(callback));
    }

    public void listUsers(ApiCallback<List<User>> callback) {
        Call<List<User>> call = routeMoodServerApi.listUsers();
        call.enqueue(createCallback(callback));
    }

    private <T> Callback<T> createCallback(ApiCallback<T> callback) {
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
}
