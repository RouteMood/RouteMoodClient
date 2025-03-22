package ru.hse.routemood;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.hse.routemood.models.AuthRequest;
import ru.hse.routemood.models.RegisterRequest;
import ru.hse.routemood.models.Route;

public class TestMain {

    Controller controller = new Controller();

    @Test
    public void registerAndLogin() {
        Response<Boolean> registerResponse = controller.registerUser(
            new RegisterRequest("testUser", "testUser", "passwd"));
        assertTrue(registerResponse.isSuccessful());

        Response<Boolean> loginResponse = controller.loginUser(
            new AuthRequest("testUser", "passwd"));
        assertTrue(loginResponse.isSuccessful());
    }

    @Test
    public void fictiveRoute() {
        controller.loginUser(new AuthRequest("testUser", "passwd"));
        Response<Route> response = controller.getFictiveRoute();

        assertTrue(response.isSuccessful());
    }

    @Test
    public void listUsers() {
        controller.loginUser(new AuthRequest("testUser", "passwd"));
        assertTrue(controller.listUsers().isSuccessful());
    }
}