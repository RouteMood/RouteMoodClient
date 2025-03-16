package ru.hse.routemood;

import ru.hse.routemood.models.AuthRequest;
import ru.hse.routemood.models.GptRequest;
import ru.hse.routemood.models.RegisterRequest;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();

        controller.getFictiveRoute();
        controller.getRoute(new GptRequest(0L, "Funny walk", 59.92951508111041, 30.41197525476372));
        controller.registerUser(new RegisterRequest("Ivan", "quuger", "passwd"));
        controller.loginUser(new AuthRequest("quuger", "passwd"));
        controller.registerUser(new RegisterRequest("Ivan2", "quuger_twink", "passwd"));
        controller.listUsers();

        System.out.println("Finished");
    }
}