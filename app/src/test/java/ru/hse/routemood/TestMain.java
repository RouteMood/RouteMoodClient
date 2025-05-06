package ru.hse.routemood;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

        controller.loginUser(
            new AuthRequest(username, "passwd"), new ApiCallback<>() {
                @Override
                public void onSuccess(AuthResponse result) {
                    controller.saveRoute(new RatingRequest(username, route), new ApiCallback<>() {
                        @Override
                        public void onSuccess(RatingItem result) {
                            System.out.println(
                                "RatingItem: id = " + result.getId() + "; rating = "
                                    + result.getRating()
                                    + "; route = " + result.getRoute() + "; authorUsername = "
                                    + result.getAuthorUsername());

                            controller.addRate(new RateRequest(result.getId(), username, 5),
                                new ApiCallback<>() {
                                    @Override
                                    public void onSuccess(RatingResponse response) {
                                        System.out.println(
                                            "After first rate: rating = " + response.getRating());
                                        controller.addRate(
                                            new RateRequest(result.getId(), username, 4),
                                            new ApiCallback<>() {
                                                @Override
                                                public void onSuccess(RatingResponse response) {
                                                    System.out.println(
                                                        "After second rate: rating = "
                                                            + response.getRating());
                                                }

                                                @Override
                                                public void onError(String error) {

                                                }
                                            });
                                    }

                                    @Override
                                    public void onError(String error) {

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

                @Override
                public void onError(String error) {
                    System.err.println(error);
                    fail();
                }
            });
        Thread.sleep(500);
    }

    @Test
    public void getRatingTable() throws InterruptedException {
        controller.loginUser(
            new AuthRequest("testUser", "passwd"), new ApiCallback<>() {
                @Override
                public void onSuccess(AuthResponse result) {
                    controller.listRoutes(new ApiCallback<>() {
                        @Override
                        public void onSuccess(List<RatingResponse> result) {
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

    @Test
    public void getRatedRoutesByAuthorUsername() throws InterruptedException {
        controller.loginUser(
            new AuthRequest("testUser", "passwd"), new ApiCallback<>() {
                @Override
                public void onSuccess(AuthResponse result) {
                    controller.getListRatedRoutesByAuthorUsername("testUser", new ApiCallback<>() {
                        @Override
                        public void onSuccess(List<RatingResponse> result) {
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

    @Test
    public void getRatedRoutesById() throws InterruptedException {
        controller.loginUser(
            new AuthRequest("testUser", "passwd"), new ApiCallback<>() {
                @Override
                public void onSuccess(AuthResponse result) {
                    controller.getRatedRouteById(
                        UUID.fromString("a87f16ac-00fd-45ce-af86-c0a48afbfb17"),
                        new ApiCallback<>() {
                            @Override
                            public void onSuccess(RatingResponse result) {
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