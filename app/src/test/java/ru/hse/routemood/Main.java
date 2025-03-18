package ru.hse.routemood;

import ru.hse.routemood.models.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();

        controller.getFictiveRoute(new ApiCallback<>() {
            @Override
            public void onSuccess(List<Route.RouteItem> result) {
                System.out.println(result);
            }

            @Override
            public void onError(String error) {
                System.err.println(error);
            }
        });
        controller.getRoute(59.92951508111041, 30.41197525476372, "Funny walk", new ApiCallback<>() {
            @Override
            public void onSuccess(List<Route.RouteItem> result) {
                System.out.println(result);
            }

            @Override
            public void onError(String error) {
                System.err.println(error);
            }
        });
        controller.getRoute(new GptRequest(0L, "Funny walk", 59.92951508111041, 30.41197525476372),
                new ApiCallback<>() {
                    @Override
                    public void onSuccess(List<Route.RouteItem> result) {
                        System.out.println(result);
                    }

                    @Override
                    public void onError(String error) {
                        System.err.println(error);
                    }
                });
        controller.registerUser(new RegisterRequest("Ivan", "quuger", "passwd"), new ApiCallback<>() {
            @Override
            public void onSuccess(AuthResponse result) {
                System.out.println(result);
            }

            @Override
            public void onError(String error) {
                System.err.println(error);
            }
        });
        controller.loginUser(new AuthRequest("quuger", "passwd"), new ApiCallback<>() {
            @Override
            public void onSuccess(AuthResponse result) {
                System.out.println(result);
            }

            @Override
            public void onError(String error) {
                System.err.println(error);
            }
        });
        controller.registerUser(new RegisterRequest("Ivan2", "quuger_twink", "passwd"), new ApiCallback<>() {
            @Override
            public void onSuccess(AuthResponse result) {
                System.out.println(result);
            }

            @Override
            public void onError(String error) {
                System.err.println(error);
            }
        });
        controller.listUsers(new ApiCallback<>() {
            @Override
            public void onSuccess(List<User> result) {
                System.out.println(result);
            }

            @Override
            public void onError(String error) {
                System.err.println(error);
            }
        });

        System.out.println("Finished");
    }
}