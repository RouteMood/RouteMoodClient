package ru.hse.routemood;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.hse.routemood.models.AuthRequest;
import ru.hse.routemood.models.AuthResponse;
import ru.hse.routemood.models.RegisterRequest;
import ru.hse.routemood.models.Route;
import ru.hse.routemood.models.User;

public class TestMain {

    Controller controller = new Controller();

    @Test
    public void registerAndLogin() {
        controller.registerUser(
            new RegisterRequest("testUser", "testUser", "passwd"), new ApiCallback<>() {
                @Override
                public void onSuccess(AuthResponse result) {
                    controller.loginUser(
                        new AuthRequest("testUser", "passwd"), new ApiCallback<>() {
                            @Override
                            public void onSuccess(AuthResponse result) {
                                System.out.println(result);
                            }

                            @Override
                            public void onError(String error) {
                                System.err.println(error);
                                fail();
                            }
                        });
                }

                @Override
                public void onError(String error) {
                    System.err.println(error);
                    fail();
                }
            });
    }

    @Test
    public void fictiveRoute() throws InterruptedException {
        controller.loginUser(
            new AuthRequest("testUser", "passwd"), new ApiCallback<>() {
                @Override
                public void onSuccess(AuthResponse authResponse) {
                    controller.getFictiveRoute(new ApiCallback<>() {
                        @Override
                        public void onSuccess(Route route) {
                            System.out.println(route);
                        }

                        @Override
                        public void onError(String error) {
                            System.err.println(error);
                            fail();
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    System.err.println(error);
                    fail();
                }
            });
        Thread.sleep(500);
    }

    @Test
    public void listUsers() throws InterruptedException {
        controller.loginUser(
            new AuthRequest("testUser", "passwd"), new ApiCallback<>() {
                @Override
                public void onSuccess(AuthResponse result) {
                    controller.listUsers(new ApiCallback<>() {
                        @Override
                        public void onSuccess(List<User> result) {
                            System.out.println(result);
                        }

                        @Override
                        public void onError(String error) {
                            System.err.println(error);
                            fail();
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    System.err.println(error);
                    fail();
                }
            });
        Thread.sleep(500);
    }
}