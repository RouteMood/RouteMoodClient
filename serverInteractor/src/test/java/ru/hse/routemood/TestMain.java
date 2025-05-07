package ru.hse.routemood;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import ru.hse.routemood.dto.AuthRequest;
import ru.hse.routemood.dto.AuthResponse;
import ru.hse.routemood.dto.RateRequest;
import ru.hse.routemood.dto.RatingRequest;
import ru.hse.routemood.dto.RatingResponse;
import ru.hse.routemood.dto.RegisterRequest;
import ru.hse.routemood.models.RatingItem;
import ru.hse.routemood.models.Route;
import ru.hse.routemood.models.Route.RouteItem;
import ru.hse.routemood.models.User;

public class TestMain {

    Controller controller = new Controller();

    @Test
    public void registerAndLogin() {
        controller.registerUser(
            new RegisterRequest("testUser", "testUser", "passwd"),
            (TestApiCallback<AuthResponse>) result -> controller.loginUser(
                new AuthRequest("testUser", "passwd"),
                (TestApiCallback<AuthResponse>) System.out::println));
    }

    @Test
    public void fictiveRoute() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(authResponse -> controller.getFictiveRoute(
            (TestApiCallback<Route>) System.out::println));
    }

    @Test
    public void listUsers() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(response -> controller.listUsers(
            (TestApiCallback<List<User>>) System.out::println));
    }

    @Test
    public void rateRoute() throws InterruptedException {
        Double[][] array = {{1.0, 2.0},
            {3.0, 4.0}};

        List<RouteItem> result = new ArrayList<>();
        for (Double[] doubles : array) {
            result.add(new RouteItem(doubles[0], doubles[1]));
        }

        Route route = Route.builder().route(result).build();

        String username = "testUser";

        loginDefaultTestUserAndRunOnSuccess((authResponse ->
            controller.saveRoute(new RatingRequest(username, route),
                (TestApiCallback<RatingItem>) result1 -> {
                    System.out.println(
                        "RatingItem: id = " + result1.getId() + "; rating = " + result1.getRating()
                            + "; route = " + result1.getRoute() + "; authorUsername = "
                            + result1.getAuthorUsername());

                    controller.addRate(new RateRequest(result1.getId(), username, 5),
                        (TestApiCallback<RatingResponse>) response -> {
                            System.out.println(
                                "After first rate: rating = " + response.getRating());
                            controller.addRate(
                                new RateRequest(result1.getId(), username, 4),
                                (TestApiCallback<RatingResponse>) response1 -> System.out.println(
                                    "After second rate: rating = " + response1.getRating()));
                        });
                })));
    }

    @Test
    public void getRatingTable() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess((authResponse) ->
            controller.listRoutes((TestApiCallback<List<RatingResponse>>) System.out::println));
    }

    @Test
    public void getRatedRoutesByAuthorUsername() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(
            (authResponse) -> controller.getListRatedRoutesByAuthorUsername("testUser",
                (TestApiCallback<List<RatingResponse>>) System.out::println));
    }

    @Test
    public void getRatedRoutesById() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess((authResponse) -> controller.getRatedRouteById(
            UUID.fromString("a87f16ac-00fd-45ce-af86-c0a48afbfb17"),
            (TestApiCallback<RatingResponse>) System.out::println));
    }

    private void loginDefaultTestUserAndRunOnSuccess(Consumer<AuthResponse> task)
        throws InterruptedException {
        loginUserAndRunOnSuccess("testUser", "passwd", task);
    }

    private void loginUserAndRunOnSuccess(String username, String password,
        Consumer<AuthResponse> task)
        throws InterruptedException {
        controller.loginUser(new AuthRequest(username, password),
            (TestApiCallback<AuthResponse>) task::accept);
        Thread.sleep(500);
    }

    private interface TestApiCallback<T> extends ApiCallback<T> {

        @Override
        void onSuccess(T result);

        @Override
        default void onError(String error) {
            System.err.println(error);
            fail();
        }
    }

}