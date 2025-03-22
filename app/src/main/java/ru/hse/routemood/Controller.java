package ru.hse.routemood;

import static ru.hse.routemood.RouteMoodServerApi.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
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

    public Response<Route> getFictiveRoute() {
        Call<Route> call = routeMoodServerApi.getFictiveRoute(0.0, 0.0, "Default walk",
            "Bearer " + sessionManager.getToken());
        try {
            return call.execute();
        } catch (Exception e) {
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }

    public Response<Route> getRoute(GptRequest request) {
        Call<Route> call = routeMoodServerApi.getRoute(request,
            "Bearer " + sessionManager.getToken());
        try {
            return call.execute();
        } catch (Exception e) {
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }

    private Response<Boolean> executeCall(Call<AuthResponse> call) {
        Response<AuthResponse> response;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                sessionManager.setToken(response.body().getToken());
                return Response.success(true);
            } else {
                try (ResponseBody errorBody = response.errorBody()) {
                    return Response.error(
                        response.code(),
                        errorBody != null ? errorBody : ResponseBody.create(null, "Unknown error")
                    );
                }
            }
        } catch (Exception e) {
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }

    public Response<Boolean> registerUser(RegisterRequest registerRequest) {
        Call<AuthResponse> call = routeMoodServerApi.registerUser(registerRequest);
        return executeCall(call);
    }

    public Response<Boolean> loginUser(AuthRequest authRequest) {
        Call<AuthResponse> call = routeMoodServerApi.loginUser(authRequest);
        return executeCall(call);
    }

    public Response<List<User>> listUsers() {
        Call<List<User>> call = routeMoodServerApi.listUsers("Bearer " + sessionManager.getToken());
        try {
            return call.execute();
        } catch (Exception e) {
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }
}
