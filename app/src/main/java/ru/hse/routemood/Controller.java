package ru.hse.routemood;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.hse.routemood.models.*;
import ru.hse.routemood.models.Route.RouteItem;

import java.util.List;

import static ru.hse.routemood.RouteMoodServerApi.BASE_URL;

public class Controller {
    private final RouteMoodServerApi routeMoodServerApi;

    public Controller() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.routeMoodServerApi = retrofit.create(RouteMoodServerApi.class);
    }

    public void getFictiveRoute() {
        Call<List<RouteItem>> call = routeMoodServerApi.getFictiveRoute(0.0, 0.0, "Default walk");
        call.enqueue(getCallbackClass());
    }

    public void getRoute(Double longitude, Double latitude, String request) {
        Call<List<RouteItem>> call = routeMoodServerApi.getRoute(longitude, latitude, request);
        call.enqueue(getCallbackClass());
    }

    public void getRoute(GptRequest request) {
        Call<List<RouteItem>> call = routeMoodServerApi.getRoute(request);
        call.enqueue(getCallbackClass());
    }

    public void registerUser(RegisterRequest registerRequest) {
        Call<AuthResponse> call = routeMoodServerApi.registerUser(registerRequest);
        call.enqueue(getCallbackClass());
    }

    public void loginUser(AuthRequest authRequest) {
        Call<AuthResponse> call = routeMoodServerApi.loginUser(authRequest);
        call.enqueue(getCallbackClass());
    }

    public void listUsers() {
        Call<List<User>> call = routeMoodServerApi.listUsers();
        call.enqueue(getCallbackClass());
    }

    private <T> Callback<T> getCallbackClass() {
        return new Callback<>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    T body = response.body();
                    System.out.println(body);
                    // TODO give the data out.
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                t.printStackTrace();
            }
        };
    }
}