package ru.hse.routemood;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import ru.hse.routemood.models.AuthRequest;
import ru.hse.routemood.models.AuthResponse;
import ru.hse.routemood.models.Route;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Controller controller = new Controller();

        CompletableFuture<AuthResponse> authResponse = new CompletableFuture<>();

        controller.loginUser(new AuthRequest("Ivan", "passwd"), new ApiCallback<>() {
            @Override
            public void onSuccess(AuthResponse result) {
                authResponse.complete(result);
            }

            @Override
            public void onError(String error) {
                System.err.println(error);
            }
        });

        controller.getFictiveRoute(new ApiCallback<>() {
            @Override
            public void onSuccess(Route result) {
                System.out.println(result);
            }

            @Override
            public void onError(String error) {
                System.err.println(error);
            }
        }, authResponse.get().getToken());

//        controller.getRoute(59.92951508111041, 30.41197525476372, "Funny walk", new ApiCallback<>() {
//            @Override
//            public void onSuccess(Route result) {
//                System.out.println(result);
//            }
//
//            @Override
//            public void onError(String error) {
//                System.err.println(error);
//            }
//        });
//        controller.getRoute(new GptRequest(0L, "Funny walk", 59.92951508111041, 30.41197525476372),
//                new ApiCallback<>() {
//                    @Override
//                    public void onSuccess(Route result) {
//                        System.out.println(result);
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        System.err.println(error);
//                    }
//                });
//        controller.loginUser(new AuthRequest("quuger", "passwd"), new ApiCallback<>() {
//            @Override
//            public void onSuccess(AuthResponse result) {
//                System.out.println(result);
//            }
//
//            @Override
//            public void onError(String error) {
//                System.err.println(error);
//            }
//        });
//        controller.registerUser(new RegisterRequest("Ivan2", "quuger_twink", "passwd"), new ApiCallback<>() {
//            @Override
//            public void onSuccess(AuthResponse result) {
//                System.out.println(result);
//            }
//
//            @Override
//            public void onError(String error) {
//                System.err.println(error);
//            }
//        });

//        controller.listUsers(new ApiCallback<>() {
//            @Override
//            public void onSuccess(List<User> result) {
//                System.out.println(result);
//            }
//
//            @Override
//            public void onError(String error) {
//                System.err.println(error);
//            }
//        }, authResponse.get().getToken());

        System.out.println("Finished");
    }
}