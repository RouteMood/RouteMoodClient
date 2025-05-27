package ru.hse.routemood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.junit.jupiter.api.Test;
import ru.hse.routemood.dto.AuthRequest;
import ru.hse.routemood.dto.AuthResponse;
import ru.hse.routemood.dto.ImageLoadResponse;
import ru.hse.routemood.dto.ImageSaveResponse;
import ru.hse.routemood.dto.RateRequest;
import ru.hse.routemood.dto.RatingRequest;
import ru.hse.routemood.dto.RatingResponse;
import ru.hse.routemood.dto.RegisterRequest;
import ru.hse.routemood.dto.UserResponse;
import ru.hse.routemood.models.Route;
import ru.hse.routemood.models.Route.RouteItem;
import ru.hse.routemood.models.User;

public class TestMain {

    private final String storagePath = "/tmp/routemood/"; // TODO load path from application.yaml
    Controller controller = new Controller();
    private UUID lastSavedImageId = UUID.fromString("392e28db-c2cb-487d-b13b-8e2fc1c4f404");

    @Test
    public void registerAndLogin() {
        controller.registerUser(
            new RegisterRequest("testUser", "testUser", "passwd"),
            (TestApiCallback<AuthResponse>) response -> controller.loginUser(
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
        loginDefaultTestUserAndRunOnSuccess(authResponse -> controller.listUsers(
            (TestApiCallback<List<User>>) System.out::println));
    }

    @Test
    public void rateRoute() throws InterruptedException {
        Double[][] array = {{1.0, 2.0}, {3.0, 4.0}};

        List<RouteItem> result = new ArrayList<>();
        for (Double[] doubles : array) {
            result.add(new RouteItem(doubles[0], doubles[1]));
        }

        Route route = Route.builder().route(result).build();

        String username = "testUser";

        loginDefaultTestUserAndRunOnSuccess((authResponse ->
            controller.saveRoute(new RatingRequest("RouteName", "SomeDescription", username, route),
                (TestApiCallback<RatingResponse>) result1 -> {
                    System.out.println(result1);

                    controller.addRate(new RateRequest(result1.getId(), username, 5),
                        (TestApiCallback<RatingResponse>) response -> {
                            System.out.println(
                                "After first rate: rating = " + response.getRating());
                            controller.getUserRate(result1.getId(), username,
                                (TestApiCallback<Integer>) rate -> {
                                    System.out.println(rate);
                                    assertEquals(5, rate);
                                });
                            controller.addRate(
                                new RateRequest(result1.getId(), username, 4),
                                (TestApiCallback<RatingResponse>) response1 -> System.out.println(
                                    "After second rate: rating = " + response1.getRating()));
                            controller.deleteRoute(result1.getId(),
                                (TestApiCallback<Void>) v -> {
                                });
                        });
                })));
    }

    @Test
    public void getRatingTable() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(authResponse ->
            controller.listRoutes((TestApiCallback<List<RatingResponse>>) System.out::println));
    }

    @Test
    public void getRatedRoutesByAuthorUsername() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(
            authResponse -> controller.getListRatedRoutesByAuthorUsername("testUser",
                (TestApiCallback<List<RatingResponse>>) System.out::println));
    }

    @Test
    public void getRatedRoutesById() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(authResponse -> controller.getRatedRouteById(
            UUID.fromString("c79e7a3b-ad6b-4f9e-bd17-edf8b7e56aae"),
            (TestApiCallback<RatingResponse>) System.out::println));
    }

    private MultipartBody.Part createFilePart() {
        File file = new File(
            storagePath + "test/images/test.jpg");

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    @Test
    public void saveAndThenDeleteImage() throws Exception {
        loginDefaultTestUserAndRunOnSuccess(authResponse ->
            controller.saveImage(
                createFilePart(),
                (TestApiCallback<ImageSaveResponse>) response -> {
                    lastSavedImageId = response.getId();
                    System.out.println(response);
                    controller.deleteImage(response.getId(),
                        (TestApiCallback<Void>) System.out::println);
                }
            )
        );

        Thread.sleep(500);
    }

    @Test
    public void loadImage() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(authResponse ->
            controller.loadImage(
                lastSavedImageId,
                (TestApiCallback<ImageLoadResponse>) response -> {
                    Path uploadPath = Paths.get(storagePath + "test/images/");
                    Path filePath = uploadPath.resolve("loadTest.jpg");

                    try {
                        Files.write(filePath, response.getFileData(), StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING);
                    } catch (Exception e) {
                        System.err.println("Can't write image: " + e.getMessage());
                        fail();
                    }
                }
            )
        );
    }

    @Test
    public void getUserInfo() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(authResponse ->
            controller.getUserInfo(
                "testUser",
                (TestApiCallback<UserResponse>) System.out::println
            )
        );
    }

    @Test
    public void updateAvatar() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(
            authResponse ->
                controller.updateAvatar(
                    createFilePart(),
                    (TestApiCallback<UUID>) System.out::println
                )
        );
    }

    @Test
    public void deleteImage() throws InterruptedException {
        loginDefaultTestUserAndRunOnSuccess(authResponse ->
            controller.deleteImage(lastSavedImageId, (TestApiCallback<Void>) System.out::println)
        );
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